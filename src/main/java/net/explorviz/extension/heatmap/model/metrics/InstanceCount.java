package net.explorviz.extension.heatmap.model.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("instanceCount")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class InstanceCount extends Metric {

  @JsonCreator
  public InstanceCount(@JsonProperty("id") final String id) {
    super(id, "instance count", "The number of instances of a class.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    return clazz.getInstanceCount();
  }

}
