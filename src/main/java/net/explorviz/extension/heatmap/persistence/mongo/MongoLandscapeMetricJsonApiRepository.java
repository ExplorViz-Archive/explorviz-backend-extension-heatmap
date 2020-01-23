package net.explorviz.extension.heatmap.persistence.mongo;

import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;
import net.explorviz.extension.heatmap.persistence.LandscapeMetricRepository;
import net.explorviz.landscape.model.store.Timestamp;
import net.explorviz.shared.config.annotations.Config;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoLandscapeMetricJsonApiRepository implements LandscapeMetricRepository<String> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(MongoLandscapeMetricJsonApiRepository.class);

  private final MongoHelper mongoHelper;

  private final LandscapeMetricSerializationHelper serializationHelper;

  @Config("repository.persistence.intervalInMinutes")
  private int intervalInMinutes;

  @Inject
  public MongoLandscapeMetricJsonApiRepository(final MongoHelper mongoHelper,
      final LandscapeMetricSerializationHelper serializationHelper) {
    this.mongoHelper = mongoHelper;
    this.serializationHelper = serializationHelper;
  }

  @Override
  public void save(final LandscapeMetrics lmetrics) {

    if (lmetrics.getApplications().isEmpty() | lmetrics.getAplicationMetrics().isEmpty()) {
      LOGGER.info("No applications to serialize");
      // return;
      // } else {
      // // TODO: remove
      // for (final ApplicationMetricCollection m : lmetrics.getAplicationMetrics()) {
      // LOGGER.info(m.getAppName() + ":" + m.getAppID());
      //
      // for (final String key : m.getMetricValues().keySet()) {
      // LOGGER.info(key);
      // final Map<String, Integer> map = m.getMetricValues().get(key).getClassMetricValues();
      // map.forEach((k, v) -> LOGGER.info("fqn: " + k + ", value: " + v));
      // }
      // }

    }
    final String landscapeMetricJsonApi;
    try {
      landscapeMetricJsonApi = this.serializationHelper.serialize(lmetrics);
    } catch (final DocumentSerializationException e) {
      throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
    }

    final MongoCollection<Document> landscapeMetricCollection =
        this.mongoHelper.getHeatmapCollection();

    final Document landscapeMetricDocument =
        new Document();
    landscapeMetricDocument.append(MongoHelper.FIELD_ID, lmetrics.getId());
    landscapeMetricDocument.append(MongoHelper.FIELD_TIMESTAMP, lmetrics.getTimestamp());
    landscapeMetricDocument.append(MongoHelper.FIELD_HEATMAP, landscapeMetricJsonApi);

    try {
      landscapeMetricCollection.insertOne(landscapeMetricDocument);
    } catch (final MongoException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("No document saved.");
        return;
      }
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(String.format("Saved landscape metrics {timestamp: %d, id: %s}",
          lmetrics.getTimestamp(),
          lmetrics.getId()));
    }
  }

  @Override
  public Optional<String> getByTimestamp(final long timestamp) {

    final MongoCollection<Document> landscapeMetriCollection =
        this.mongoHelper.getHeatmapCollection();

    final Document landscapeMetricsDocument = new Document();
    landscapeMetricsDocument.append(MongoHelper.FIELD_TIMESTAMP, timestamp);

    final FindIterable<Document> result = landscapeMetriCollection.find(landscapeMetricsDocument);

    if (result.first() == null) {
      return Optional.empty();
    } else {
      return Optional.of((String) result.first().get(MongoHelper.FIELD_HEATMAP));
    }
  }

  @Override
  public Optional<String> getByTimestamp(final Timestamp timestamp) {
    return this.getByTimestamp(timestamp.getTimestamp());
  }

  @Override
  public Optional<String> getById(final String id) {

    final MongoCollection<Document> landscapeMetricsCollection =
        this.mongoHelper.getHeatmapCollection();

    final Document landscapeMetricsDocument = new Document();

    final FindIterable<Document> result = landscapeMetricsCollection.find(landscapeMetricsDocument);

    if (result.first() == null) {
      return Optional.empty();
    } else {
      return Optional.of(result.first().getString(MongoHelper.FIELD_HEATMAP));
    }
  }

  @Override
  public void cleanup(final long from) {
    final long enddate = from - TimeUnit.MINUTES.toMillis(this.intervalInMinutes);

    final MongoCollection<Document> landscapeCollection = this.mongoHelper.getHeatmapCollection();

    final Document landscapeMetricsDocument = new Document();
    landscapeMetricsDocument.append(MongoHelper.FIELD_TIMESTAMP, new BasicDBObject("$lt", enddate));

    final DeleteResult landsapeResult = landscapeCollection.deleteMany(landscapeMetricsDocument);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(String.format("Cleaned %d landscape metrics objects",
          landsapeResult.getDeletedCount()));
    }
  }

  @Override
  public void clear() {
    final MongoCollection<Document> landscapeMetricsCollection =
        this.mongoHelper.getHeatmapCollection();
    landscapeMetricsCollection.deleteMany(new Document());
  }

}
