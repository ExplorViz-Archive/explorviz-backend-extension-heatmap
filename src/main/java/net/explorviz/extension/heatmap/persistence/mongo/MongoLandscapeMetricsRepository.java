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
import net.explorviz.extension.heatmap.model.heatmap.LandscapeMetric;
import net.explorviz.extension.heatmap.persistence.HeatmapRepository;
import net.explorviz.landscape.model.store.Timestamp;
import net.explorviz.shared.config.annotations.Config;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoLandscapeMetricsRepository implements HeatmapRepository<LandscapeMetric> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(MongoLandscapeMetricsRepository.class);

  @Config("repository.persistence.intervalInMinutes")
  private int intervalInMinutes;

  private final MongoHelper mongoHelper;

  private final LandscapeMetricsSerializationHelper serializationHelper;

  @Inject
  public MongoLandscapeMetricsRepository(final MongoHelper mongoHelper,
      final LandscapeMetricsSerializationHelper helper) {
    this.mongoHelper = mongoHelper;
    this.serializationHelper = helper;
  }

  @Override
  public void save(final String id, final long timestamp, final LandscapeMetric component) {

    final String landscapeMetricsJsonApi;
    try {
      landscapeMetricsJsonApi = this.serializationHelper.serialize(component);
    } catch (final DocumentSerializationException e) {
      throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
    }

    final MongoCollection<Document> metricsCollection =
        this.mongoHelper.getLandscapeMetricCollection();

    final Document metricsDocument = new Document();
    metricsDocument.append(MongoHelper.FIELD_ID, id);
    metricsDocument.append(MongoHelper.FIELD_TIMESTAMP, timestamp);
    metricsDocument.append(MongoHelper.FIELD_LANDSCAPEMETRICS, landscapeMetricsJsonApi);

    try {
      metricsCollection.insertOne(metricsDocument);
    } catch (final MongoException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("No document saved: " + e.getMessage());
      }
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Saved metrics {timestamp: %d, id: %s}", timestamp, id);
    }
  }

  @Override
  public Optional<LandscapeMetric> getByTimestamp(final long timestamp) {
    final MongoCollection<Document> metricsCollection =
        this.mongoHelper.getLandscapeMetricCollection();

    final Document metricsDocument = new Document();
    metricsDocument.append(MongoHelper.FIELD_TIMESTAMP, timestamp);

    final FindIterable<Document> result = metricsCollection.find(metricsDocument);

    if (result.first() == null) {
      return Optional.empty();
    } else {
      LandscapeMetric metrics;
      try {
        metrics = this.serializationHelper
            .deserialize(result.first().getString(MongoHelper.FIELD_LANDSCAPEMETRICS));
        return Optional.of(metrics);
      } catch (final DocumentSerializationException e) {
        throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public Optional<LandscapeMetric> getByTimestamp(final Timestamp timestamp) {
    return this.getByTimestamp(timestamp.getTimestamp());
  }

  @Override
  public Optional<LandscapeMetric> getById(final String id) {
    final MongoCollection<Document> metricsCollection =
        this.mongoHelper.getLandscapeMetricCollection();

    final Document metricsDocument = new Document();
    metricsDocument.append(MongoHelper.FIELD_ID, id);

    final FindIterable<Document> result = metricsCollection.find(metricsDocument);

    if (result.first() == null) {
      return Optional.empty();
    } else {
      LandscapeMetric metrics;
      try {
        metrics = this.serializationHelper
            .deserialize(result.first().getString(MongoHelper.FIELD_LANDSCAPEMETRICS));
        return Optional.of(metrics);
      } catch (final DocumentSerializationException e) {
        throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public Optional<LandscapeMetric> getNthLastRecord(final int n) {
    final MongoCollection<Document> metricsCollection =
        this.mongoHelper.getLandscapeMetricCollection();

    // Check if there are at least n documents in the database
    if (metricsCollection.countDocuments() < n) {
      return Optional.empty();
    }

    final FindIterable<Document> result =
        metricsCollection.find().limit(1).skip(n - 1)
            .sort(com.mongodb.client.model.Sorts.descending(MongoHelper.FIELD_TIMESTAMP));

    if (result.first() == null) {
      return Optional.empty();
    } else {
      LandscapeMetric metrics;
      try {
        metrics = this.serializationHelper
            .deserialize(result.first().getString(MongoHelper.FIELD_LANDSCAPEMETRICS));
        return Optional.of(metrics);
      } catch (final DocumentSerializationException e) {
        throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public void cleanup(final long from) {
    final long enddate = from - TimeUnit.MINUTES.toMillis(this.intervalInMinutes);

    final MongoCollection<Document> metricCollection =
        this.mongoHelper.getLandscapeMetricCollection();

    final Document landscapeDocument = new Document();
    landscapeDocument.append(MongoHelper.FIELD_TIMESTAMP, new BasicDBObject("$lt", enddate));

    final DeleteResult landsapeResult = metricCollection.deleteMany(landscapeDocument);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(String.format("Cleaned %d metrics.",
          landsapeResult.getDeletedCount()));
    }

  }

  @Override
  public void clear() {
    final MongoCollection<Document> metricCollection =
        this.mongoHelper.getLandscapeMetricCollection();
    metricCollection.deleteMany(new Document());
  }
}
