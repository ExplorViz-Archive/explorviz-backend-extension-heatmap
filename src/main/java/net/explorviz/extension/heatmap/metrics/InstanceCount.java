package net.explorviz.extension.heatmap.metrics;

import net.explorviz.landscape.model.application.Clazz;

public class InstanceCount extends Metric {

  public InstanceCount() {
    super("instance count", "The number of instances of a class.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    return clazz.getInstanceCount();
  }

}
