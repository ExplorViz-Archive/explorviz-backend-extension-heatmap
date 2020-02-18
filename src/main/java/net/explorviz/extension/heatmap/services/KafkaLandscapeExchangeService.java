package net.explorviz.extension.heatmap.services;

import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;
import net.explorviz.extension.heatmap.model.metrics.ClassActivity;
import net.explorviz.extension.heatmap.model.metrics.InstanceCount;
import net.explorviz.extension.heatmap.model.metrics.Metric;
import net.explorviz.extension.heatmap.persistence.mongo.LandscapeMetricsSerializationHelper;
import net.explorviz.extension.heatmap.persistence.mongo.MongoHeatmapJsonApiRepository;
import net.explorviz.extension.heatmap.persistence.mongo.MongoHeatmapRepository;
import net.explorviz.extension.heatmap.persistence.mongo.MongoLandscapeMetricsRepository;
import net.explorviz.landscape.model.landscape.Landscape;
import net.explorviz.shared.config.annotations.Config;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exchange service for consuming landscape objects via Kafka topics. Started by @see
 * SetupApplicationListener on application startup.
 *
 */
@Service
public class KafkaLandscapeExchangeService implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLandscapeExchangeService.class);

  private final KafkaConsumer<String, String> kafkaConsumer;

  private final LandscapeSerializationHelper serializationHelper;
  private final LandscapeMetricsSerializationHelper heatmapSerializationHelper;

  private final MongoHeatmapJsonApiRepository mongoHeatmapJsonApiRepo;
  private final MongoHeatmapRepository mongoHeatmapRepo;
  private final MongoLandscapeMetricsRepository mongoMetricRepo;

  private final HeatmapService heatmapService;

  private final List<Metric> metrics = new ArrayList<>();
  private long previousTimestamp = 0;

  private final String kafkaTopic;

  /**
   * Exchange service for consuming landscape objects via Kafka topics. Started by @see
   * SetupApplicationListener on application startup.
   *
   */
  @Inject
  public KafkaLandscapeExchangeService(final LandscapeSerializationHelper serializationHelper,
      final LandscapeMetricsSerializationHelper heatmapSerializationHelper,
      final MongoHeatmapJsonApiRepository mongoHeatmapJsonApiRepo,
      final MongoHeatmapRepository mongoHeatmapRepo,
      final MongoLandscapeMetricsRepository mongoMetricRepo,
      final HeatmapService heatmapService,
      @Config("exchange.kafka.topic.name") final String kafkaTopic,
      @Config("exchange.kafka.group.id") final String kafkaGroupId,
      @Config("exchange.kafka.bootstrap.servers") final String kafkaBootStrapServerList) {

    this.serializationHelper = serializationHelper;
    this.heatmapSerializationHelper = heatmapSerializationHelper;
    this.mongoHeatmapJsonApiRepo = mongoHeatmapJsonApiRepo;
    this.mongoHeatmapRepo = mongoHeatmapRepo;
    this.mongoMetricRepo = mongoMetricRepo;
    this.heatmapService = heatmapService;
    this.kafkaTopic = kafkaTopic;

    this.metrics.add(new InstanceCount());
    this.metrics.add(new ClassActivity());

    final Properties properties = new Properties();
    properties.put("bootstrap.servers", kafkaBootStrapServerList);
    properties.put("group.id", kafkaGroupId);
    properties.put("enable.auto.commit", "true");
    properties.put("auto.commit.interval.ms", "1000");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");// NOCS
    properties.put("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");

    this.kafkaConsumer = new KafkaConsumer<>(properties);
  }

  @Override
  public void run() {
    LOGGER.info("Starting Kafka Exchange \n");

    this.kafkaConsumer.subscribe(Arrays.asList(this.kafkaTopic));

    String ls = null;
    try {
      ls = new String(Files.readAllBytes(Paths.get("G:/Masterarbeit/landscape0.txt")));
    } catch (final IOException e) {
    }

    while (true) {

      final ConsumerRecords<String, String> records =
          this.kafkaConsumer.poll(Duration.ofMillis(100));

      for (final ConsumerRecord<String, String> record : records) {

        // LOGGER.debug("Recevied landscape Kafka record: {}", record.value());

        String serializedLandscape = record.value();
        if (ls != null) {
          serializedLandscape = ls;
        }

        Landscape l;
        try {
          l = this.serializationHelper.deserialize(serializedLandscape);
        } catch (final DocumentSerializationException e) {
          // LOGGER.error("Could not deserialize landscape with value {}", serializedLandscape, e);
          continue;
        }

        LOGGER.info("Deserialized landscape with id {}:", l.getId());

        // -- 1. compute metrics for landscape
        final LandscapeMetrics lmetrics = new LandscapeMetrics(this.metrics, l);

        // -- 2. compute heatmap with the new lmetrics
        final Heatmap heatmap = new Heatmap(lmetrics, this.previousTimestamp, this.mongoHeatmapRepo,
            this.mongoMetricRepo);

        // -- 3. serialize heatmap for transmission and persistence
        String serializedHeatmap = null;
        try {
          LOGGER.info("Trying to serialize {}", lmetrics.getId());
          serializedHeatmap = this.heatmapSerializationHelper.serialize(lmetrics);
        } catch (final DocumentSerializationException e) {
          if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Could not serialize document. No document broadcasted.");
          }
        }
        // LOGGER.info("Computed metrics for landscape with id: {}", l.getId());

        if (serializedHeatmap != null) {
          // -- 4. persist heatmap and lmetrics into db
          this.mongoHeatmapJsonApiRepo.save(heatmap.getId(), heatmap.getTimestamp(),
              serializedHeatmap);
          this.mongoHeatmapRepo.save(heatmap.getId(), heatmap.getTimestamp(), heatmap);
          this.mongoMetricRepo.save(lmetrics.getId(), lmetrics.getTimestamp(), lmetrics);

          // 6. broadcast serialized heatmap to clients (similar to broadcast service)
          LOGGER.debug("Broadcasting landscape with id: {}", l.getId());
          this.heatmapService.broadcastLandscapeMetric(serializedHeatmap);
        }

        // Save this timestamp for following loop executions.
        this.previousTimestamp = l.getTimestamp().getTimestamp();
      }
    }
  }
}
