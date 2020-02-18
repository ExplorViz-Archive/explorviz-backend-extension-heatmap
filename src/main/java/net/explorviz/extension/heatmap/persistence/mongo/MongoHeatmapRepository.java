package net.explorviz.extension.heatmap.persistence.mongo;

import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;
import net.explorviz.extension.heatmap.persistence.HeatmapRepository;
import net.explorviz.landscape.model.store.Timestamp;

public class MongoHeatmapRepository implements HeatmapRepository<Heatmap> {

  @Inject
  private MongoHeatmapJsonApiRepository repo;

  @Inject
  private HeatmapSerializationHelper serializationHelper;

  @Override
  public void save(final String Id, final long timestamp, final Heatmap component) {
    String heatmapJsonApi;
    try {
      heatmapJsonApi = this.serializationHelper.serialize(component);
    } catch (final DocumentSerializationException e) {
      throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
    }
    this.repo.save(Id, timestamp, heatmapJsonApi);
  }

  @Override
  public Optional<Heatmap> getByTimestamp(final long timestamp) {
    final Optional<String> jsonHeatmap = this.repo.getByTimestamp(timestamp);

    if (!jsonHeatmap.isPresent()) {
      return Optional.empty();
    }

    try {
      return Optional.of(this.serializationHelper.deserialize(jsonHeatmap.get()));
    } catch (final DocumentSerializationException e) {
      throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
    }
  }

  @Override
  public Optional<Heatmap> getByTimestamp(final Timestamp timestamp) {
    return this.getByTimestamp(timestamp.getTimestamp());
  }

  @Override
  public Optional<Heatmap> getById(final String id) {
    final Optional<String> jsonHeatmap = this.repo.getById(id);

    if (!jsonHeatmap.isPresent()) {
      return Optional.empty();
    }

    try {
      return Optional.of(this.serializationHelper.deserialize(jsonHeatmap.get()));
    } catch (final DocumentSerializationException e) {
      throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
    }
  }

  @Override
  public Optional<Heatmap> getNthLastRecord(final int n) {
    final Optional<String> jsonHeatmap = this.repo.getNthLastRecord(n);

    if (!jsonHeatmap.isPresent()) {
      return Optional.empty();
    }

    try {
      return Optional.of(this.serializationHelper.deserialize(jsonHeatmap.get()));
    } catch (final DocumentSerializationException e) {
      throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
    }
  }

  @Override
  public void cleanup(final long from) {
    this.repo.cleanup();

  }

  @Override
  public void clear() {
    this.repo.clear();
  }
}
