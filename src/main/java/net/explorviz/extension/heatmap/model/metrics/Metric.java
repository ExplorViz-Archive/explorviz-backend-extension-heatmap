package net.explorviz.extension.heatmap.model.metrics;

import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.extension.heatmap.model.BaseEntity;
import net.explorviz.landscape.model.application.Clazz;

/**
 * Base class used for inheritance of metrics.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("Metric")
public abstract class Metric extends BaseEntity {

  private final String name;

  private final String description;

  public Metric(final String name, final String desc) {
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
