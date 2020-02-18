package net.explorviz.extension.heatmap.model.metrics;

import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("ExportCoupling")
public class ExportCoupling extends Metric {

  public ExportCoupling(final String name, final String desc) {
    super("export coupling",
        "Number of messages received by all methods of all object of a class.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    // TODO Auto-generated method stub
    return 0;
  }

}
