package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;

/**
 * Contains the name, value pair of a class and the metric value.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("ClazzMetric")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ClazzMetric extends BaseEntity {

  private final String clazzName;
  private double value;

  @JsonCreator
  public ClazzMetric(@JsonProperty("id") final String id,
      @JsonProperty("clazzName") final String clazzName,
      @JsonProperty("value") final double value) {
    super(id);
    this.clazzName = clazzName;
    this.value = value;
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
}
