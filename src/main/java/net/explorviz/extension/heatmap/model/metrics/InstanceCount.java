package net.explorviz.extension.heatmap.model.metrics;

import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("InstanceCount")
public class InstanceCount extends Metric {

  public InstanceCount() {
    super("instance count", "The number of instances of a class.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    return clazz.getInstanceCount();
  }

}
