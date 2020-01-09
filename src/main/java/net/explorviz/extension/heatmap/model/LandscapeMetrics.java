package net.explorviz.extension.heatmap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.explorviz.extension.heatmap.metrics.Metric;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.landscape.Landscape;
import net.explorviz.landscape.model.store.Timestamp;

/**
 *
 * @author Tim-Niklas Reck
 *
 */
public class LandscapeMetrics extends BaseModel {

  private final Timestamp timestamp;
  private final List<Application> applications;
  private final Map<Application, ApplicationMetrics> aplicationMetrics;
  private final Metric metric;

  public LandscapeMetrics(final Metric metric, final Landscape landscape) {
    this.metric = metric;
    this.timestamp = landscape.getTimestamp();
    this.applications = this.findLandscapeApplications(landscape);
    this.aplicationMetrics = this.computeApplicationMetrics(this.applications);
  }

  public Timestamp getTimestamp() {
    return this.timestamp;
  }

  public List<Application> getApplications() {
    return this.applications;
  }

  public Map<Application, ApplicationMetrics> getAplicationMetrics() {
    return this.aplicationMetrics;
  }

  public Metric getMetric() {
    return this.metric;
  }

  /**
   * List all applications contained in a landscape.
   *
   * @param landscape
   * @return
   */
  private List<Application> findLandscapeApplications(final Landscape landscape) {
    final List<Application> applications = new ArrayList<>();
    // TODO: create List of applications;
    return applications;
  }

  /**
   * Compute the values for the given metric for all applications.
   *
   * @param applications
   * @return
   */
  private Map<Application, ApplicationMetrics> computeApplicationMetrics(
      final List<Application> applications) {
    final Map<Application, ApplicationMetrics> appMetrics = new HashMap<>();

    for (final Application app : applications) {
      appMetrics.put(app, new ApplicationMetrics(this.metric, app));
    }
    return appMetrics;
  }

}
