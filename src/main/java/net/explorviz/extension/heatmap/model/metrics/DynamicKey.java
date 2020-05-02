package net.explorviz.extension.heatmap.model.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.List;
import net.explorviz.landscape.model.application.AggregatedClazzCommunication;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.application.Clazz;

/**
 * DKC(Dynamic Key Class) “The percentage of sum of calls sent out from the class and calls received
 * by the class at runtime taken over the total number of static calls sent and received by all the
 * classes.”
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("dynamicKey")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class DynamicKey extends Metric {

  @JsonCreator
  public DynamicKey(@JsonProperty("id") final String id) {
    super(id, "dynamicKey", "dynamic Key",
        "The percentage of sum of calls sent out from the class and calls received by the "
            + "class at runtime taken over the total number of static calls sent and received "
            + "by all the classes.");
  }

  @Override
  public double computeMetric(final Clazz clazz, final Application application) {
    final List<AggregatedClazzCommunication> aggregatedClazzCommunications =
        application.getAggregatedClazzCommunications();

    double totalCommunications = 0;
    double totalClazzCommunications = 0;

    for (final AggregatedClazzCommunication aggCom : aggregatedClazzCommunications) {
      if (clazz.equals(aggCom.getSourceClazz()) || clazz.equals(aggCom.getTargetClazz())) {
        totalClazzCommunications += aggCom.getTotalRequests();
      }
      totalCommunications += aggCom.getTotalRequests();
    }

    if (totalCommunications != 0) {
      totalClazzCommunications = totalClazzCommunications / totalCommunications;
    }

    return totalClazzCommunications;
  }

}
