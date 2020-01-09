package net.explorviz.extension.heatmap.metrics;

import net.explorviz.landscape.model.application.Clazz;

public class ClassActivity extends Metric {

  public ClassActivity(final String name, final String desc) {
    super("class activity", "Combined metric for messages and number of instances.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    // TODO Auto-generated method stub
    return 0;
  }

}
