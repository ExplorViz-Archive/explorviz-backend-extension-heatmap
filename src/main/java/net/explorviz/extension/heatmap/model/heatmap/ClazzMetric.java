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
@Type("clazzMetric")
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
   * Add the new value to the old one.
   *
   * @param value
   */
  public void addValue(final double value) {
    this.value += value;
  }

  /**
   * Subtract the new value from the old one.
   *
   * @param value the value to substract
   */
  public void subtractValue(final double value) {
    this.value -= value;
  }

  public String getClazzName() {
    return this.clazzName;
  }

  public double getValue() {
    return this.value;
  }
}
