package net.explorviz.extension.heatmap.model;

import com.github.jasminb.jsonapi.annotations.Type;

/**
 * Contains the name, value pair of a class and the metric value.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("ClazzMetric")
public class ClazzMetric extends BaseEntity {

  private final String clazzName;
  private double value;
  private final String metricName;

  public ClazzMetric(final String clazzName, final double value, final String metricName) {
    this.clazzName = clazzName;
    this.value = value;
    this.metricName = metricName;
  }

  /**
   * Add the value to the existing one.
   *
   * @param value
   */
  public void addValue(final double value) {
    this.value += value;
  }

  public String getClazzName() {
    return this.clazzName;
  }

  public double getValue() {
    return this.value;
  }

  public String getMetricName() {
    return this.metricName;
  }
}
