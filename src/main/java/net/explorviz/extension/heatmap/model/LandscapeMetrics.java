package net.explorviz.extension.heatmap.model;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;
import net.explorviz.extension.heatmap.metrics.Metric;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.landscape.Landscape;
import net.explorviz.landscape.model.landscape.Node;
import net.explorviz.landscape.model.landscape.NodeGroup;
import net.explorviz.landscape.model.landscape.System;

/**
 * Model representing the metric values of all applications in a software landscape.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("LandscapeMetrics")
public class LandscapeMetrics extends BaseEntity {

  // private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeMetrics.class);

  private final Long timestamp;

  @Relationship("applications")
  private final List<Application> applications;

  @Relationship("applicationMetricCollection")
  private final List<ApplicationMetricCollection> aplicationMetrics;

  @Relationship("metrics")
  private final List<Metric> metrics;

  public LandscapeMetrics(final List<Metric> metrics, final Landscape landscape) {
    this.metrics = metrics;
    this.timestamp = landscape.getTimestamp().getTimestamp();
    this.applications = this.findLandscapeApplications(landscape);
    this.aplicationMetrics = this.computeApplicationMetrics();
  }

  public Long getTimestamp() {
    return this.timestamp;
  }

  public List<Application> getApplications() {
    return this.applications;
  }

  public List<ApplicationMetricCollection> getAplicationMetrics() {
    return this.aplicationMetrics;
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
  private List<ApplicationMetricCollection> computeApplicationMetrics() {
    final List<ApplicationMetricCollection> appMetrics = new ArrayList<>();

    for (final Application application : this.applications) {
      appMetrics.add(new ApplicationMetricCollection(application, this.metrics));
    }

    return appMetrics;
  }

}
