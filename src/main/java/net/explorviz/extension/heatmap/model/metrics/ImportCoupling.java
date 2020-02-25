package net.explorviz.extension.heatmap.model.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("importCoupling")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ImportCoupling extends Metric {

  @JsonCreator
  public ImportCoupling(@JsonProperty("id") final String id) {
    super(id, "importCoupling", "import coupling",
        "Total number of messages sent by all methods in all objects of a class.");
  }

  @Override
  public double computeMetric(final Clazz clazz) {
    // TODO Auto-generated method stub
    return Math.round(Math.random() * 100 - 50);
  }

}
