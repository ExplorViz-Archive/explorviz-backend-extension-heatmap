package net.explorviz.extension.heatmap.model;

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
@Type("applicationMetrics")
public class ApplicationMetrics extends BaseModel {

  private final Metric metric;
  private final String applicationId;
  private final Map<String, Integer> classMetricValues;

  public Metric getMetric() {
    return this.metric;
  }

  public Map<String, Integer> getClassMetricValues() {
    return this.classMetricValues;
  }

  public String getApplicationId() {
    return this.applicationId;
  }

  public ApplicationMetrics(final Metric metric, final String applicationId,
      final Map<String, Integer> classMetricValues) {
    this.metric = metric;
    this.applicationId = applicationId;
    this.classMetricValues = classMetricValues;
  }

  public ApplicationMetrics(final Metric metric, final Application application) {
    this.applicationId = application.getId();
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
