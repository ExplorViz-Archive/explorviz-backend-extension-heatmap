package net.explorviz.extension.heatmap.model;

import com.github.jasminb.jsonapi.annotations.Type;
import java.util.Map;

/**
 * The representation of one heatmap with a given metric in the backend. The metric values are
 * mapped to their respective classes. Generation of the two dimensional grid is done in the
 * frontend.
 * 
 * @author FRooG
 *
 */
@Type("heatmap")
public class HeatmapModel extends BaseModel {

  private String metricName;

  public String getMetricName() {
    return this.metricName;
  }

  public void setMetricName(final String metricName) {
    this.metricName = metricName;
  }

  public String getMetricDescription() {
    return this.metricDescription;
  }

  public void setMetricDescription(final String metricDescription) {
    this.metricDescription = metricDescription;
  }

  public Map<String, Integer> getClassMetricValues() {
    return this.ClassMetricValues;
  }

  public void setClassMetricValues(final Map<String, Integer> classMetricValues) {
    this.ClassMetricValues = classMetricValues;
  }

  private String metricDescription;
  private Map<String, Integer> ClassMetricValues;

  public HeatmapModel() {
    // default constructor for JSON API parsing
  }

  public HeatmapModel(final String metricName, final String metricDescription,
      final Map<String, Integer> classMetricValues) {
    this.metricDescription = metricDescription;
    this.metricName = metricName;
    this.ClassMetricValues = classMetricValues;
  }

}
