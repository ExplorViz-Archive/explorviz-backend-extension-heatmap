package net.explorviz.extension.heatmap.model;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.explorviz.extension.heatmap.metrics.Metric;
import net.explorviz.landscape.model.application.Application;

@Type("ApplicationMetricCollection")
public class ApplicationMetricCollection extends BaseEntity {

  // @Relationship("appName")
  private final String appName;

  // @Relationship("appID")
  private final String appID;

  @Relationship("metricValues")
  private final Map<String, ApplicationMetric> metricValues;


  public ApplicationMetricCollection(final Application application, final List<Metric> metrics) {
    this.appName = application.getName();
    this.appID = application.getId();
    this.metricValues = this.computeMetrics(application, metrics);
  }

  /**
   * Compute the metric values for all clazzes of the application and each metric.
   *
   * @param application the application holding the clazzes
   * @param metrics the list of metrics to be applied
   * @return
   */
  private Map<String, ApplicationMetric> computeMetrics(final Application application,
      final List<Metric> metrics) {
    final Map<String, ApplicationMetric> map = new HashMap<>();

    for (final Metric metric : metrics) {
      final ApplicationMetric appMetrics = new ApplicationMetric(metric, application);
      map.put(metric.getName(), appMetrics);
    }
    return map;
  }

  /**
   * Return the metric clazz values of the application for one specific metric.
   *
   * @param metricName the name of the metric
   * @return the application metric object
   */
  public ApplicationMetric getByMetricName(final String metricName) {
    return this.metricValues.get(metricName);
  }

  public String getAppName() {
    return this.appName;
  }

  public String getAppID() {
    return this.appID;
  }

  public Map<String, ApplicationMetric> getMetricValues() {
    return this.metricValues;
  }
}
