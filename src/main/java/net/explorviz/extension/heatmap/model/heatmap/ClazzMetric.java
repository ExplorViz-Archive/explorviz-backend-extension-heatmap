package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.extension.heatmap.model.helper.MathHelper;

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
   * Add the new value to the old one. The result is rounded to 4 decimal places.
   *
   * @param value
   */
  public void addValue(final double value) {
    final double tmp = this.value + value;
    this.value = MathHelper.round(tmp, 4);
    System.out.println(this.value);
  }

  /**
   * Subtract the new value from the old one. The result is rounded to 4 decimal places.
   *
   * @param value the value to substract
   */
  public void subtractValue(final double value) {
    final double tmp = this.value - value;
    this.value = MathHelper.round(tmp, 4);
  }

  public String getClazzName() {
    return this.clazzName;
  }

  public double getValue() {
    return this.value;
  }
}
