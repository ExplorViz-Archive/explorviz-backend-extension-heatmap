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
      final LandscapeMetricsSerializationHelper serializationHelper) {
    this.mongoHelper = mongoHelper;
  }

  @Override
  public void save(final String Id, final long timestamp, final String heatmapJsonApi) {

    final MongoCollection<Document> heatmapCollection =
        this.mongoHelper.getHeatmapCollection();

    final Document heatmapDocument =
        new Document();
    heatmapDocument.append(MongoHelper.FIELD_ID, Id);
    heatmapDocument.append(MongoHelper.FIELD_TIMESTAMP, timestamp);
    heatmapDocument.append(MongoHelper.FIELD_HEATMAP, heatmapJsonApi);

    try {
      heatmapCollection.insertOne(heatmapDocument);
    } catch (final MongoException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("No document saved.");
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

    final MongoCollection<Document> heatmapCollection =
        this.mongoHelper.getHeatmapCollection();

    final Document heatmapDocument = new Document();
    heatmapDocument.append(MongoHelper.FIELD_TIMESTAMP, timestamp);

    final FindIterable<Document> result = heatmapCollection.find(heatmapDocument);

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

    final MongoCollection<Document> heatmapCollection =
        this.mongoHelper.getHeatmapCollection();

    final Document heatmapDocument = new Document();

    final FindIterable<Document> result = heatmapCollection.find(heatmapDocument);

    if (result.first() == null) {
      return Optional.empty();
    } else {
      return Optional.of(result.first().getString(MongoHelper.FIELD_HEATMAP));
    }
  }

  @Override
  public Optional<String> getNthLastRecord(final int n) {
    final MongoCollection<Document> metricsCollection =
        this.mongoHelper.getLandscapeMetricCollection();

    // Check if there are atleast n documents in the database
    if (metricsCollection.countDocuments() < n) {
      return Optional.empty();
    }

    final FindIterable<Document> result =
        metricsCollection.find().limit(1).skip(n - 1)
            .sort(com.mongodb.client.model.Sorts.descending(MongoHelper.FIELD_HEATMAP));

    if (result.first() == null) {
      return Optional.empty();
    } else {
      return Optional.of(result.first().getString(MongoHelper.FIELD_HEATMAP));
    }
  }

  @Override
  public void cleanup(final long from) {
    final long enddate = from - TimeUnit.MINUTES.toMillis(this.intervalInMinutes);

    final MongoCollection<Document> heatmapCollection = this.mongoHelper.getHeatmapCollection();

    final Document heatmapDocument = new Document();
    heatmapDocument.append(MongoHelper.FIELD_TIMESTAMP, new BasicDBObject("$lt", enddate));

    final DeleteResult landsapeResult = heatmapCollection.deleteMany(heatmapDocument);

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
