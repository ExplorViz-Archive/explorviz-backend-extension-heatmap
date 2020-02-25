package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import java.util.List;

/**
 * Contains the model for the heatmap respresentations of a landscape.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("heatmap")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class Heatmap extends BaseEntity {

  private final long windowsize;
  private final long timestamp;

  private List<String> metricTypes;

  // The id of the corresponding landscape.
  private final String landscapeId;

  @Relationship("aggregatedHeatmap")
  private LandscapeMetrics aggregatedHeatmap;
  @Relationship("windowedHeatmap")
  private LandscapeMetrics windowedHeatmap;

  @JsonCreator
  public Heatmap(@JsonProperty("id") final String id,
      @JsonProperty("windowsize") final int windowsize,
      @JsonProperty("timestamp") final long timestamp,
      @JsonProperty("landscapeId") final String landscapeId) {
    super(id);
    this.windowsize = windowsize;
    this.timestamp = timestamp;
    this.landscapeId = landscapeId;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public LandscapeMetrics getAggregatedHeatmap() {
    return this.aggregatedHeatmap;
  }

  public void setAggregatedHeatmap(final LandscapeMetrics aggregatedHeatmap) {
    this.aggregatedHeatmap = aggregatedHeatmap;
  }

  public LandscapeMetrics getWindowedHeatmap() {
    return this.windowedHeatmap;
  }

  public long getWindowsize() {
    return this.windowsize;
  }

  public String getLandscapeId() {
    return this.landscapeId;
  }

  public List<String> getMetricTypes() {
    return this.metricTypes;
  }

  public void setMetricTypes(final List<String> metricNames) {
    this.metricTypes = metricNames;
  }

  public void setWindowedHeatmap(final LandscapeMetrics windowedHeatmap) {
    this.windowedHeatmap = windowedHeatmap;
  }

}
