package net.explorviz.extension.heatmap.model.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.extension.heatmap.model.heatmap.BaseEntity;
import net.explorviz.landscape.model.application.Clazz;

/**
 * Base class used for inheritance of metrics.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("Metric")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public abstract class Metric extends BaseEntity {

  private final String name;

  private final String description;

  @JsonCreator
  public Metric(@JsonProperty("id") final String id, @JsonProperty("name") final String name,
      @JsonProperty("desc") final String desc) {
    super(id);
    this.name = name;
    this.description = desc;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }


  /**
   * The logic which is used to compute the specified metric for a given class;
   *
   * @return The value of the metric for the given clazz.
   */
  public abstract int computeMetric(Clazz clazz);
}
