package net.explorviz.extension.heatmap.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.explorviz.extension.heatmap.metrics.Metric;
import net.explorviz.landscape.model.landscape.Landscape;

/**
 * Computes and holds the metrics for a given landscape.
 *
 * @author Tim-Niklas Reck
 *
 */
public class LandscapeMetricsCollection extends BaseModel {

  private final long timestamp;
  private final List<Metric> metrics;
  private final Map<String, LandscapeMetrics> landscapeMetrics;

  /**
   * Representation of landscape metric values. To be used upon receiving a new landscape from the
   * landscape service.
   *
   * @param timestamp
   * @param metrics
   */
  public LandscapeMetricsCollection(final long timestamp, final Landscape landscape,
      final List<Metric> metrics) {
    this.timestamp = timestamp;
    this.metrics = metrics;
    this.landscapeMetrics = this.applyMetrics(landscape);
  }

  /**
   * Representation of landscape metric values. To be used by this extensions history service.
   *
   * @param timestamp
   * @param metrics
   * @param heatmaps
   */
  public LandscapeMetricsCollection(final long timestamp, final List<Metric> metrics,
      final Map<String, LandscapeMetrics> landscapeMetrics) {
    this.timestamp = timestamp;
    this.metrics = metrics;
    this.landscapeMetrics = landscapeMetrics;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public List<Metric> getMetrics() {
    return this.metrics;
  }

  public Map<String, LandscapeMetrics> getLandscapeMetrics() {
    return this.landscapeMetrics;
  }


  /**
   * Apply all metrics on a given Landscape.
   *
   * @param landscape
   * @return
   */
  private Map<String, LandscapeMetrics> applyMetrics(final Landscape landscape) {
    final Map<String, LandscapeMetrics> maps = new HashMap<>();
    for (final Metric metric : this.metrics) {
      maps.put(metric.getName(), new LandscapeMetrics(metric, landscape));
    }

    return maps;
  }

}
