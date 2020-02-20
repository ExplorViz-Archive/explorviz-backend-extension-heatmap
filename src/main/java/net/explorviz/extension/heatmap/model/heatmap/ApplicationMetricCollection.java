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
import net.explorviz.landscape.model.application.Application;

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
  private final List<ApplicationMetric> metricValues = new ArrayList<>();

  public ApplicationMetricCollection(final String id, final Application application,
      final List<Metric> metrics) {
    super(id);
    this.appName = application.getName();
    this.appId = application.getId();
    this.metricValues.addAll(this.computeMetrics(application, metrics));
  }

  @JsonCreator
  public ApplicationMetricCollection(@JsonProperty("id") final String id,
      @JsonProperty("appName") final String appName, @JsonProperty("appId") final String appId) {
    super(id);
    this.appName = appName;
    this.appId = appId;
  }

  /**
   * Compute the metric values for all clazzes of the application and each metric.
   *
   * @param application the application holding the clazzes
   * @param metrics the list of metrics to be applied
   * @return
   */
  private List<ApplicationMetric> computeMetrics(final Application application,
      final List<Metric> metrics) {
    final List<ApplicationMetric> list = new ArrayList<>();

    for (final Metric metric : metrics) {
      final ApplicationMetric appMetrics = new ApplicationMetric(metric, application);
      list.add(appMetrics);
    }
    return list;
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
      if (appMetric.getMetric().getName().contentEquals(metricName)) {
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
}
