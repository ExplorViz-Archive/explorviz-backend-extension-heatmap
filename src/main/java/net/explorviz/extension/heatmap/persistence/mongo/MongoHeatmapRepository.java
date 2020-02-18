package net.explorviz.extension.heatmap.persistence.mongo;

import java.util.Optional;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;
import net.explorviz.extension.heatmap.persistence.HeatmapRepository;
import net.explorviz.landscape.model.store.Timestamp;

public class MongoHeatmapRepository implements HeatmapRepository<Heatmap> {

  @Override
  public void save(final String Id, final long timestamp, final Heatmap component) {
    // TODO Auto-generated method stub

  }

  @Override
  public Optional<Heatmap> getByTimestamp(final long timestamp) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<Heatmap> getByTimestamp(final Timestamp timestamp) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<Heatmap> getById(final String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void cleanup(final long from) {
    // TODO Auto-generated method stub

  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub

  }

}
