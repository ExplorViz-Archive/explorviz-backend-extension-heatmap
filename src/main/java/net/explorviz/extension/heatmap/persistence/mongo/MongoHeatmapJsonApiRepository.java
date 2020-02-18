package net.explorviz.extension.heatmap.persistence.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import net.explorviz.extension.heatmap.persistence.HeatmapRepository;
import net.explorviz.landscape.model.store.Timestamp;
import net.explorviz.shared.config.annotations.Config;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoHeatmapJsonApiRepository implements HeatmapRepository<String> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(MongoHeatmapJsonApiRepository.class);

  private final MongoHelper mongoHelper;

  @Config("repository.persistence.intervalInMinutes")
  private int intervalInMinutes;

  @Inject
  public MongoHeatmapJsonApiRepository(final MongoHelper mongoHelper,
      final HeatmapSerializationHelper serializationHelper) {
    this.mongoHelper = mongoHelper;
  }

  @Override
  public void save(final String Id, final long timestamp, final String landscapeMetricJsonApi) {

    final MongoCollection<Document> landscapeMetricCollection =
        this.mongoHelper.getHeatmapCollection();

    final Document landscapeMetricDocument =
        new Document();
    landscapeMetricDocument.append(MongoHelper.FIELD_ID, Id);
    landscapeMetricDocument.append(MongoHelper.FIELD_TIMESTAMP, timestamp);
    landscapeMetricDocument.append(MongoHelper.FIELD_HEATMAP, landscapeMetricJsonApi);

    try {
      landscapeMetricCollection.insertOne(landscapeMetricDocument);
    } catch (final MongoException e) {
      if (LOGGER.isErrorEnabled()) {
        // LOGGER.error("No document saved.");
        return;
      }
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(String.format("Saved landscape metrics {timestamp: %d, id: %s}",
          timestamp, Id));
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
