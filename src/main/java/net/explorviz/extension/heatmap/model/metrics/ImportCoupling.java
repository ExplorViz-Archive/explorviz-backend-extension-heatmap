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
 * Metric for the total number of messages sent by all methods in all objects of a class.
 * 
 * @author Tim-Niklas Reck
 *
 */
@Type("importCoupling")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ImportCoupling extends Metric {

  @JsonCreator
  public ImportCoupling(@JsonProperty("id") final String id) {
    super(id, "importCoupling", "import coupling",
        "Total number of messages sent by all methods in all objects of a class.");
  }

  @Override
  public double computeMetric(final Clazz clazz, final Application application) {

    final List<AggregatedClazzCommunication> aggregatedClazzCommunications =
        application.getAggregatedClazzCommunications();

    double totalClazzCommunications = 0;

    for (final AggregatedClazzCommunication aggCom : aggregatedClazzCommunications) {
      if (clazz.equals(aggCom.getSourceClazz())) {
        totalClazzCommunications += aggCom.getTotalRequests();
      }
    }

    return totalClazzCommunications;
  }

}
