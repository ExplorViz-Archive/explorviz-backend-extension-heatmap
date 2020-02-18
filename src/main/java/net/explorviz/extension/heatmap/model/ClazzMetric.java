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
   * Decay this value by 0.5 and add new value.
   *
   * @param value
   */
  public void decayAndAddValue(final double value) {
    this.value = this.value * 0.5 + value;
  }

  /**
   * Subtract the existing value from the new one.
   * 
   * @param value
   */
  public void subtractValue(final double value) {
    this.value = value - this.value;
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
