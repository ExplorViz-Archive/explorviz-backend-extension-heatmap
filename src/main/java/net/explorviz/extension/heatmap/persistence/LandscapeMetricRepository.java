package net.explorviz.extension.heatmap.persistence;

import java.util.Optional;
import javax.ws.rs.ClientErrorException;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;
import net.explorviz.landscape.model.store.Timestamp;

/**
 * Represents a repository to store LandscapeMetric objects persistently.
 *
 * @author Tim-Niklas Reck
 *
 * @param <T> the return type of landscape metrics.
 */
public interface LandscapeMetricRepository<T> {

  /**
   * Saves a heatmap for a landscape in the repository.
   *
   * @param timestamp
   * @param lmetrics
   */
  void save(LandscapeMetrics lmetrics);

  /**
   * Retrieves a LandscapeMetric object with a specific timestamp from the repository.
   *
   * @param timestamp the timestamp of the landscape metric object
   * @return the landscape metric object
   */
  Optional<T> getByTimestamp(final long timestamp);

  /**
   * Retrieves a LandscapeMetric object with a specific timestamp from the repository.
   *
   * @param timestamp the timestamp of the landscape metric object
   * @return the landscape metric object
   */
  Optional<T> getByTimestamp(final Timestamp timestamp);

  /**
   * Retrieves a landscape metric object with a specific, unique identifier.
   *
   * @param id the id of the landscape metric object
   *
   * @return the landscape object
   *
   * @throws ClientErrorException if the landscape could not be found.
   */
  Optional<T> getById(final String id);

  /**
   * Removes all landscape metrics that have exceeded their lifespan.
   *
   * @param from the reference timestamp to use for lifetime calculation.
   */
  void cleanup(final long from);

  /**
   * Removes all landscape metrics that have exceeded their life span. Equivalent to
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
