package net.explorviz.extension.heatmap.model.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.application.Clazz;

@Type("dummyMetric")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class DummyMetric extends Metric {

  /**
   * "Dummy metric for testing and demonstration purposes."
   *
   * @param id
   */
  @JsonCreator
  public DummyMetric(@JsonProperty("id") final String id) {
    super(id, "dummyMetric", "dummy metric",
        "Dummy metric for testing and demonstration purposes.");
  }

  @Override
  public double computeMetric(final Clazz clazz, final Application application) {
    return Math.round(Math.random() * 100);
  }

}
