package net.explorviz.extension.heatmap.model.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Clazz;

@Type("ClassActivity")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ClassActivity extends Metric {

  @JsonCreator
  public ClassActivity(@JsonProperty("id") final String id) {
    super(id, "class activity", "Combined metric for messages and number of instances.");
  }

  @Override
  public int computeMetric(final Clazz clazz) {
    // TODO Auto-generated method stub
    return 0;
  }

}
