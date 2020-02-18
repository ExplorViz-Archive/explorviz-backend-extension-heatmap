package net.explorviz.extension.heatmap.model;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;
import net.explorviz.extension.heatmap.model.metrics.Metric;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.landscape.Landscape;
import net.explorviz.landscape.model.landscape.Node;
import net.explorviz.landscape.model.landscape.NodeGroup;
import net.explorviz.landscape.model.landscape.System;

/**
 * Model representing the raw metric values of all applications in a software landscape for the
 * given timestamp.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("LandscapeMetrics")
public class LandscapeMetrics extends BaseEntity {

  // private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeMetrics.class);

  private final Long timestamp;

  private final List<String> applicationIds = new ArrayList<>();

  @Relationship("metrics")
  private final List<Metric> metrics;

  @Relationship("applicationMetricCollection")
  private final List<ApplicationMetricCollection> aplicationMetricCollections;

  /**
   * Create a new metric object with a list of metrics and a new ExplorViz landscape.
   *
   * @param metrics
   * @param landscape
   */
  public LandscapeMetrics(final List<Metric> metrics, final Landscape landscape) {
    this.metrics = metrics;
    this.timestamp = landscape.getTimestamp().getTimestamp();
    this.aplicationMetricCollections = this.computeApplicationMetrics(landscape);
  }

  public List<String> getApplicationIds() {
    return this.applicationIds;
  }

  public Long getTimestamp() {
    return this.timestamp;
  }

  public List<ApplicationMetricCollection> getAplicationMetrics() {
    return this.aplicationMetricCollections;
  }

  public List<Metric> getMetric() {
    return this.metrics;
  }

  /**
   * List all applications contained in a landscape.
   *
   * @param landscape
   * @return
   */
  private List<Application> findLandscapeApplications(final Landscape landscape) {
    final List<Application> applications = new ArrayList<>();

    for (final System system : landscape.getSystems()) {
      for (final NodeGroup nodegroup : system.getNodeGroups()) {
        for (final Node node : nodegroup.getNodes()) {
          applications.addAll(node.getApplications());
        }
      }
    }
    return applications;
  }


  /**
   * Compute the values for all metrics for all applications.
   *
   * @return An ApplicationMetricCollection object that holds the name and id of the application and
   *         a map that contains class value pairs for each metric.
   */
  private List<ApplicationMetricCollection> computeApplicationMetrics(final Landscape landscape) {
    final List<ApplicationMetricCollection> appMetrics = new ArrayList<>();
    final List<Application> applications = this.findLandscapeApplications(landscape);

    for (final Application application : applications) {
      this.applicationIds.add(application.getId());
      appMetrics.add(new ApplicationMetricCollection(application, this.metrics));
    }

    return appMetrics;
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
    for (final ApplicationMetricCollection tmpCollection : this.aplicationMetricCollections) {
      if (tmpCollection.getAppName().equals(applicationName)) {
        appCollection = tmpCollection;
        break;
      }
    }
    return appCollection;
  }

  /**
   * Add the half of the metrics of the aggregatedMap to this.
   *
   * @param aggregatedMap
   * @return
   */
  public LandscapeMetrics aggregateHeatmaps(final LandscapeMetrics aggregatedMap) {
    final LandscapeMetrics newHeatmap = this;

    // First loop: For all applications ...
    for (final ApplicationMetricCollection appCollection : newHeatmap.aplicationMetricCollections) {
      final ApplicationMetricCollection aggregatedCollection =
          aggregatedMap.getApplicationMetricCollectionByName(appCollection.getAppName());
      if (aggregatedCollection == null) {
        continue;
      }
      // Second loop: ... for all metrics ...
      for (final ApplicationMetric appMetric : appCollection.getMetricValues()) {
        final ApplicationMetric aggregatedMetric =
            aggregatedCollection.getByMetricName(appMetric.getMetric().getName());
        if (aggregatedMetric == null) {
          continue;
        }
        // Third loop: ... for all clazzes ...
        for (final ClazzMetric clazzMetric : appMetric.getClassMetricValues()) {
          if (aggregatedMetric != null) {
            final ClazzMetric aggregatedClazz =
                aggregatedMetric.getClazzMetricByName(clazzMetric.getClazzName());
            // ... add half of the value of the aggregated (old) map.
            clazzMetric.addValue(0.5 * aggregatedClazz.getValue());
          }

        }
      }
    }
    return newHeatmap;
  }

}
