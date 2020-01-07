package net.explorviz.extension.heatmap.model;

import com.github.jasminb.jsonapi.annotations.Type;
import java.util.Map;

/**
 *
 * @author FRooG
 *
 */
@Type("heatmapcollection")
public class HeatmapCollectionModel extends BaseModel {

  private Map<String, HeatmapModel> heatmaps;

  public HeatmapCollectionModel() {
    // default constructor for JSON API parsing
  }

  public HeatmapCollectionModel(final Map<String, HeatmapModel> heatmaps) {
    this.heatmaps = heatmaps;
  }

  public Map<String, HeatmapModel> getHeatmaps() {
    return this.heatmaps;
  }

  public void setHeatmaps(final Map<String, HeatmapModel> heatmaps) {
    this.heatmaps = heatmaps;
  }
}
