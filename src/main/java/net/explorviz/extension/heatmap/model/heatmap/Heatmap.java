package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

/**
 * Contains the model for the heatmap respresentations of a landscape.
 *
 * @author Tim-Niklas Reck
 *
 */
@Type("Heatmap")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "super.id")
public class Heatmap extends BaseEntity {

  private final int windowsize;
  private final long timestamp;

  @Relationship("aggregatedHeatmap")
  private LandscapeMetrics aggregatedHeatmap;
  @Relationship("windowedHeatmap")
  private LandscapeMetrics windowedHeatmap;

  @JsonCreator
  public Heatmap(@JsonProperty("id") final String id,
      @JsonProperty("windowsize") final int windowsize,
      @JsonProperty("timestamp") final long timestamp) {
    super(id);
    this.windowsize = windowsize;
    this.timestamp = timestamp;
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

  public int getWindowsize() {
    return this.windowsize;
  }

  public void setWindowedHeatmap(final LandscapeMetrics windowedHeatmap) {
    this.windowedHeatmap = windowedHeatmap;
  }

}
