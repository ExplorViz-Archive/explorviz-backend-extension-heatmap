package net.explorviz.extension.heatmap.model.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("exportCoupling")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ExportCoupling extends Metric {

  @JsonCreator
  public ExportCoupling(@JsonProperty("id") final String id) {
    super(id, "export coupling",
        "Number of messages received by all methods of all object of a class.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    // TODO Auto-generated method stub
    return 0;
  }

}
