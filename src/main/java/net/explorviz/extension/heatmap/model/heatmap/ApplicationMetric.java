package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;
import net.explorviz.extension.heatmap.model.metrics.Metric;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ApplicationMetric extends BaseEntity {

  private final String metricName;

  @Relationship("classMetricValues")
  private final List<ClazzMetric> classMetricValues = new ArrayList<>();

  public ApplicationMetric(final String id, final Metric metric, final Application application) {
    super(id);
    this.metricName = metric.getName();
    this.classMetricValues.addAll(this.computeApplicationMetrics(application, metric));
  }

  @JsonCreator
  public ApplicationMetric(@JsonProperty("id") final String id,
      @JsonProperty("metricName") final String metricName) {
    super(id);
    this.metricName = metricName;
  }

  /**
   * Computes the metric values of the metric for a given application.
   *
   * @param application
   * @return
   */
  private List<ClazzMetric> computeApplicationMetrics(final Application application,
      final Metric metric) {
    final List<ClazzMetric> clazzMetrics = new ArrayList<>();

    final List<Clazz> applicationClazzes = new ArrayList<>();
    for (final Component component : application.getComponents()) {
      applicationClazzes.addAll(ModelHelper.getChildrenComponentClazzes(component));
    }

    for (final Clazz clazz : applicationClazzes) {
      clazzMetrics.add(new ClazzMetric(clazz.getFullQualifiedName(),
          metric.computeMetric(clazz));
    }

    return clazzMetrics;
  }

  public String getMetricName() {
    return this.metricName;
  }

  public List<ClazzMetric> getClassMetricValues() {
    return this.classMetricValues;
  }

  /**
   * Return the ClazzMetric with the given clazzName
   *
   * @param clazzName the full qualified name of the target clazz
   * @return
   */
  public ClazzMetric getClazzMetricByName(final String clazzName) {
    ClazzMetric clazzMetric = null;
    for (final ClazzMetric tmpMetric : this.classMetricValues) {
      if (tmpMetric.getClazzName().equals(clazzName)) {
        clazzMetric = tmpMetric;
        break;
      }
    }
    return clazzMetric;
  }

}
