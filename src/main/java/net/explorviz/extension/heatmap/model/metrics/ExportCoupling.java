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
 * Metric for the number of messages received by all methods of all object of a class.
 * 
 * @author Tim-Niklas Reck
 *
 */
@Type("exportCoupling")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ExportCoupling extends Metric {

  @JsonCreator
  public ExportCoupling(@JsonProperty("id") final String id) {
    super(id, "exportCoupling", "export coupling",
        "Number of messages received by all methods of all object of a class.");
  }

  @Override
  public double computeMetric(final Clazz clazz, final Application application) {
    final List<AggregatedClazzCommunication> aggregatedClazzCommunications =
        application.getAggregatedClazzCommunications();

    double totalClazzCommunications = 0;

    for (final AggregatedClazzCommunication aggCom : aggregatedClazzCommunications) {
      if (clazz.equals(aggCom.getTargetClazz())) {
        totalClazzCommunications += aggCom.getTotalRequests();
      }
    }

    return totalClazzCommunications;
  }

}
