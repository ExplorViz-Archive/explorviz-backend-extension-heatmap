package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the values for all metrics and clazzes of a single application.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("ApplicationMetricCollection")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ApplicationMetricCollection extends BaseEntity {

  private final String appName;
  private final String appId;

  @Relationship("metricValues")
  private List<ApplicationMetric> metricValues = new ArrayList<>();

  @JsonCreator
  public ApplicationMetricCollection(@JsonProperty("id") final String id,
      @JsonProperty("appName") final String appName, @JsonProperty("appId") final String appId) {
    super(id);
    this.appName = appName;
    this.appId = appId;
  }

  /**
   * Return the metric clazz values of the application for one specific metric.
   *
   * @param metricName the name of the metric
   * @return the application metric object or null if there is no object for the given metric.
   */
  public ApplicationMetric getByMetricName(final String metricName) {
    ApplicationMetric tmpMetric = null;
    for (final ApplicationMetric appMetric : this.metricValues) {
      if (appMetric.getMetricName().equals(metricName)) {
        tmpMetric = appMetric;
      }
    }
    return tmpMetric;
  }

  public String getAppName() {
    return this.appName;
  }

  public String getAppID() {
    return this.appId;
  }

  public List<ApplicationMetric> getMetricValues() {
    return this.metricValues;
  }

  public String getAppId() {
    return this.appId;
  }

  public void setMetricValues(final List<ApplicationMetric> metricValues) {
    this.metricValues = metricValues;
  }
}
