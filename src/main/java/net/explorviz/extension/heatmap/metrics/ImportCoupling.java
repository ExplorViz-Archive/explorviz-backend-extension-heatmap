package net.explorviz.extension.heatmap.metrics;

import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("Metric")
public class ImportCoupling extends Metric {

  public ImportCoupling() {
    super("import coupling",
        "Total number of messages sent by all methods in all objects of a class.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    // TODO Auto-generated method stub
    return 0;
  }

}
