package net.explorviz.extension.heatmap.metrics;

import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("Metric")
public class ClassActivity extends Metric {

  public ClassActivity() {
    super("class activity", "Combined metric for messages and number of instances.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    // TODO Auto-generated method stub
    return 0;
  }

}
