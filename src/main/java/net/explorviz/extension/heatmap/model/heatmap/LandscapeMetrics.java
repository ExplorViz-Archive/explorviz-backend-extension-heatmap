package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;
import net.explorviz.extension.heatmap.model.metrics.Metric;

/**
 * Model representing the raw metric values of all applications in a software landscape for the
 * given timestamp.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("LandscapeMetrics")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class LandscapeMetrics extends BaseEntity {

  private final Long timestamp;

  @Relationship("metrics")
  private List<Metric> metrics = new ArrayList<>();

  @Relationship("applicationMetricCollections")
  private List<ApplicationMetricCollection> applicationMetricCollections = new ArrayList<>();

  @JsonCreator
  public LandscapeMetrics(@JsonProperty("id") final String id,
      @JsonProperty("timestamp") final long timestamp) {
    super(id);
    this.timestamp = timestamp;
  }

  public Long getTimestamp() {
    return this.timestamp;
  }

  /**
   * Return the ApplicationMetricCollection for a given application name.
   *
   * @param applicationName
   * @return
   */
  public ApplicationMetricCollection getApplicationMetricCollectionByName(
      final String applicationName) {
    ApplicationMetricCollection appCollection = null;
    for (final ApplicationMetricCollection tmpCollection : this.applicationMetricCollections) {
      if (tmpCollection.getAppName().equals(applicationName)) {
        appCollection = tmpCollection;
        break;
      }
    }
    return appCollection;
  }

  public List<Metric> getMetrics() {
    return this.metrics;
  }

  public List<ApplicationMetricCollection> getApplicationMetricCollections() {
    return this.applicationMetricCollections;
  }

  public void setMetrics(final List<Metric> metrics) {
    this.metrics = metrics;
  }

  public void setAplicationMetricCollections(
      final List<ApplicationMetricCollection> applicationMetricCollections) {
    this.applicationMetricCollections = applicationMetricCollections;
  }

}
