package net.explorviz.extension.heatmap.persistence;

import java.util.Optional;
import net.explorviz.landscape.model.store.Timestamp;

/**
 * Represents a repository to store LandscapeMetric objects persistently.
 *
 * @author Tim-Niklas Reck
 *
 * @param <T> the return type of landscape metrics.
 */
public interface HeatmapRepository<T> {

  /**
   * Saves a model component of the respective type into the database
   *
   * @param Id the id of the component.
   * @param timestamp The timestamp of the source landscape.
   * @param component the component to be saved.
   */
  void save(String Id, long timestamp, T component);

  /**
   * Retrieves a model component with a specific timestamp from the repository.
   *
   * @param timestamp the timestamp of the source landscape
   * @return the component
   */
  Optional<T> getByTimestamp(final long timestamp);

  /**
   * Retrieves a model component with a specific timestamp from the repository.
   *
   * @param timestamp the timestamp of the source landscape
   * @return the component
   */
  Optional<T> getByTimestamp(final Timestamp timestamp);

  /**
   * Retrieves a model component with a specific, unique identifier.
   *
   * @param id the id of the model component
   * @return the model component
   */
  Optional<T> getById(final String id);

  /**
   * Removes all model components that have exceeded their lifespan.
   *
   * @param from the reference timestamp to use for lifetime calculation.
   */
  void cleanup(final long from);

  /**
   * Removes allmodel components that have exceeded their life span. Equivalent to
   * {@code cleanup(System.currentTimeMillis())}
   *
   */
  default void cleanup() {
    this.cleanup(System.currentTimeMillis());
  }

  /**
   * Removes all landscape metrics in the repository.
   */
  void clear();
}
