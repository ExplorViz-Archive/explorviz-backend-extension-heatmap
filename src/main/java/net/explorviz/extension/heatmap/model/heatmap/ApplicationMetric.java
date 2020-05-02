package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The representation of the metric values for all classes of an application in the backend. The
 * final visualization is done in the frontend.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("applicationMetric")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class ApplicationMetric extends BaseEntity {

  private final String metricType;
  private long largestValue = 0;

  @Relationship("classMetricValues")
  private List<ClazzMetric> classMetricValues = new ArrayList<>();

  @JsonCreator
  public ApplicationMetric(@JsonProperty("id") final String id,
      @JsonProperty("metricType") final String metricType) {
    super(id);
    this.metricType = metricType;
  }

  public String getMetricType() {
    return this.metricType;
  }

  public List<ClazzMetric> getClassMetricValues() {
    return this.classMetricValues;
  }

  public long getLargestValue() {
    return this.largestValue;
  }

  public void setLargestValue(final long largestValue) {
    this.largestValue = largestValue;
  }

  public void setClassMetricValues(final List<ClazzMetric> classMetricValues) {
    this.classMetricValues = classMetricValues;
  }


  /**
   * Return the {@link ClazzMetric} with the given clazzName
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

  /**
   * Compute the largest value of all ClazzMetrics.
   */
  public void computeLargestValue(final double scalar) {
    this.largestValue = Long.MIN_VALUE;
    for (final ClazzMetric clazzMetric : this.classMetricValues) {
      final double v = Math.abs(clazzMetric.getValue());
      if (v > this.largestValue) {
        this.largestValue = Math.round(v);
      }
    }
    this.largestValue *= scalar;
  }
}
