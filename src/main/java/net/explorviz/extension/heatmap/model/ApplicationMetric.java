package net.explorviz.extension.heatmap.model;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.explorviz.extension.heatmap.metrics.Metric;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.application.Clazz;
import net.explorviz.landscape.model.application.Component;
import net.explorviz.landscape.model.helper.ModelHelper;

/**
 * The representation of the metric values for all classes of an application in the backend. The
 * final visualization is done in the frontend.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("ApplicationMetric")
public class ApplicationMetric extends BaseEntity {

  @Relationship("metric")
  private final Metric metric;

  @Relationship("classMetricValues")
  private final Map<String, Integer> classMetricValues;

  public Metric getMetric() {
    return this.metric;
  }

  public Map<String, Integer> getClassMetricValues() {
    return this.classMetricValues;
  }


  public ApplicationMetric(final Metric metric, final String applicationId,
      final Map<String, Integer> classMetricValues) {
    this.metric = metric;
    this.classMetricValues = classMetricValues;
  }

  public ApplicationMetric(final Metric metric, final Application application) {
    this.metric = metric;
    this.classMetricValues = this.computeApplicationMetrics(application);
  }

  /**
   * Computes the metric values of the metric for a given application.
   *
   * @param application
   * @return
   */
  private Map<String, Integer> computeApplicationMetrics(final Application application) {
    final Map<String, Integer> clazzMetrics = new HashMap<>();

    final List<Clazz> applicationClazzes = new ArrayList<>();
    for (final Component component : application.getComponents()) {
      applicationClazzes.addAll(ModelHelper.getChildrenComponentClazzes(component));
    }

    for (final Clazz clazz : applicationClazzes) {
      clazzMetrics.put(clazz.getFullQualifiedName(), this.metric.computeMetric(clazz));
    }

    return clazzMetrics;
  }

}
