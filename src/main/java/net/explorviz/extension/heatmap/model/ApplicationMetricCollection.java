package net.explorviz.extension.heatmap.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.explorviz.extension.heatmap.metrics.Metric;
import net.explorviz.landscape.model.application.Application;

public class ApplicationMetricCollection {

  private final String appName;
  private final String appID;
  private final Map<String, Map<String, Integer>> metricValues;

  public ApplicationMetricCollection(final Application application, final List<Metric> metrics) {
    this.appName = application.getName();
    this.appID = application.getId();
    this.metricValues = this.computeMetrics(application, metrics);
  }

  private Map<String, Map<String, Integer>> computeMetrics(final Application application,
      final List<Metric> metrics) {
    final Map<String, Map<String, Integer>> map = new HashMap<>();

    for (final Metric metric : metrics) {
      final ApplicationMetric appMetrics = new ApplicationMetric(metric, application);
      map.put(metric.getName(), appMetrics.getClassMetricValues());
    }
    return map;
  }

  public String getAppName() {
    return this.appName;
  }

  public String getAppID() {
    return this.appID;
  }

  public Map<String, Map<String, Integer>> getMetricValues() {
    return this.metricValues;
  }
}
