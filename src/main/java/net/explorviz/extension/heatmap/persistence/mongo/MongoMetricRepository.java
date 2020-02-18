package net.explorviz.extension.heatmap.persistence.mongo;

import java.util.Optional;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;
import net.explorviz.extension.heatmap.persistence.HeatmapRepository;
import net.explorviz.landscape.model.store.Timestamp;

public class MongoMetricRepository implements HeatmapRepository<LandscapeMetrics> {

  @Override
  public void save(final String Id, final long timestamp, final LandscapeMetrics component) {
    // TODO Auto-generated method stub

  }

  @Override
  public Optional<LandscapeMetrics> getByTimestamp(final long timestamp) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<LandscapeMetrics> getByTimestamp(final Timestamp timestamp) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<LandscapeMetrics> getById(final String id) {
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
