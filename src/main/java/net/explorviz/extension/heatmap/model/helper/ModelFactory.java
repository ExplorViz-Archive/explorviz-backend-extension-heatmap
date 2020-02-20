package net.explorviz.extension.heatmap.model.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.explorviz.extension.heatmap.model.heatmap.ApplicationMetric;
import net.explorviz.extension.heatmap.model.heatmap.ApplicationMetricCollection;
import net.explorviz.extension.heatmap.model.heatmap.ClazzMetric;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;
import net.explorviz.extension.heatmap.model.heatmap.LandscapeMetrics;
import net.explorviz.extension.heatmap.model.metrics.ClassActivity;
import net.explorviz.extension.heatmap.model.metrics.InstanceCount;
import net.explorviz.extension.heatmap.model.metrics.Metric;
import net.explorviz.extension.heatmap.persistence.mongo.MongoHeatmapRepository;
import net.explorviz.extension.heatmap.persistence.mongo.MongoLandscapeMetricsRepository;
import net.explorviz.landscape.model.application.Application;
import net.explorviz.landscape.model.application.Clazz;
import net.explorviz.landscape.model.application.Component;
import net.explorviz.landscape.model.helper.ModelHelper;
import net.explorviz.landscape.model.landscape.Landscape;
import net.explorviz.shared.common.idgen.IdGenerator;
import net.explorviz.shared.config.helper.PropertyHelper;

/**
 * Factory to generate and add ids to model classes.
 *
 * @author Tim-Niklas Reck
 *
 */
@Singleton
public class ModelFactory {

  private final IdGenerator idGen;
  private final MongoHeatmapRepository mongoHeatmapRepo;
  private final MongoLandscapeMetricsRepository mongoMetricRepo;

  private final List<Metric> metrics = new ArrayList<>();

  @Inject
  public ModelFactory(final MongoHeatmapRepository mongoHeatmapRepo,
      final MongoLandscapeMetricsRepository mongoMetricRepo, final IdGenerator idGen) {
    this.idGen = idGen;
    this.mongoHeatmapRepo = mongoHeatmapRepo;
    this.mongoMetricRepo = mongoMetricRepo;

    this.metrics.add(new InstanceCount(this.idGen.generateId()));
    this.metrics.add(new ClassActivity(this.idGen.generateId()));
  }

  public LandscapeMetrics createLandscapeMetrics(final Landscape l) {
    final LandscapeMetrics lmetrics =
        new LandscapeMetrics(this.idGen.generateId(), l.getTimestamp().getTimestamp());
    lmetrics.setMetrics(this.metrics);
    final List<ApplicationMetricCollection> appMetricCollection =
        this.computeApplicationMetricsCollection(l);
    lmetrics.setAplicationMetricCollections(appMetricCollection);
    return lmetrics;
  }

  public ApplicationMetric createApplicationMetric(final Application application,
      final Metric metric) {
    final ApplicationMetric appMetric =
        new ApplicationMetric(this.idGen.generateId(), metric.getName());
    appMetric.setClassMetricValues(this.computeApplicationMetrics(application, metric));
    return appMetric;
  }

  /**
   * Create a new metric object with a list of metrics and a new ExplorViz landscape.
   *
   * @param metrics
   * @param landscape
   */
  public ApplicationMetricCollection createApplicationMetricCollection(
      final Application application) {
    final ApplicationMetricCollection appCollection = new ApplicationMetricCollection(
        this.idGen.generateId(), application.getName(), application.getId());
    appCollection.setMetricValues(this.computeMetrics(application, this.metrics));
    return appCollection;
  }

  public ClazzMetric createClazzMetric(final String clazzName, final double value) {
    return new ClazzMetric(this.idGen.generateId(), clazzName, value);
  }

  /**
   * Create a new heatmap based on the given landscape metrics. The windowsize defined in
   * "explorviz.properties" should be at least 2. Otherwise it will just show the current metric.
   *
   * @param lmetrics the new landscape metrics to be considered
   * @param previousTimestamp the timestamp of the previous heatmap
   */
  public Heatmap createHeatmap(
      final LandscapeMetrics lmetrics,
      final long previousTimestamp) {

    final int windowsize = PropertyHelper.getIntegerProperty("heatmap.window.size");
    final Heatmap heatmap =
        new Heatmap(this.idGen.generateId(), windowsize, lmetrics.getTimestamp());

    final LandscapeMetrics aggregatedHeatmap = this.computeAggregatedHeatmap(lmetrics,
        this.mongoHeatmapRepo.getByTimestamp(previousTimestamp));
    heatmap.setAggregatedHeatmap(aggregatedHeatmap);

    final LandscapeMetrics windowedHeatmap = this.computeWindowHeatmap(lmetrics,
        this.mongoMetricRepo.getNthLastRecord(windowsize), windowsize);
    heatmap.setWindowedHeatmap(windowedHeatmap);
    return heatmap;
  }

  /**
   * Compute the metric values for all clazzes of the application and each metric.
   *
   * @param application the application holding the clazzes
   * @param metrics the list of metrics to be applied
   * @return
   */
  private List<ApplicationMetric> computeMetrics(final Application application,
      final List<Metric> metrics) {
    final List<ApplicationMetric> list = new ArrayList<>();

    for (final Metric metric : metrics) {
      final ApplicationMetric appMetrics = this.createApplicationMetric(application, metric);
      list.add(appMetrics);
    }
    return list;
  }

  /**
   * Computes the metric values of the metric for a given application.
   *
   * @param application
   * @return
   */
  private List<ClazzMetric> computeApplicationMetrics(final Application application,
      final Metric metric) {
    final List<ClazzMetric> clazzMetrics = new ArrayList<>();

    final List<Clazz> applicationClazzes = new ArrayList<>();
    for (final Component component : application.getComponents()) {
      applicationClazzes.addAll(ModelHelper.getChildrenComponentClazzes(component));
    }

    for (final Clazz clazz : applicationClazzes) {
      clazzMetrics.add(this.createClazzMetric(clazz.getFullQualifiedName(),
          metric.computeMetric(clazz)));
    }

    return clazzMetrics;
  }

  /**
   * Compute the values for all metrics for all applications.
   *
   * @return An ApplicationMetricCollection object that holds the name and id of the application and
   *         a map that contains class value pairs for each metric.
   */
  private List<ApplicationMetricCollection> computeApplicationMetricsCollection(
      final Landscape landscape) {
    final List<ApplicationMetricCollection> appMetrics = new ArrayList<>();
    final List<Application> applications = HeatmapHelper.findLandscapeApplications(landscape);

    for (final Application application : applications) {
      appMetrics.add(this.createApplicationMetricCollection(application));
    }

    return appMetrics;
  }

  /**
   * Add the half of the metrics of the aggregatedMap to this metric map.
   *
   * @param aggregatedMap
   * @return
   */
  public LandscapeMetrics aggregateHeatmaps(final LandscapeMetrics newMap,
      final LandscapeMetrics aggregatedMap) {
    // First loop: For all applications ...
    for (final ApplicationMetricCollection appCollection : newMap
        .getAplicationMetricCollections()) {
      final ApplicationMetricCollection aggregatedCollection =
          aggregatedMap.getApplicationMetricCollectionByName(appCollection.getAppName());
      if (aggregatedCollection == null) {
        continue;
      }
      // Second loop: ... for all metrics ...
      for (final ApplicationMetric appMetric : appCollection.getMetricValues()) {
        final ApplicationMetric aggregatedMetric =
            aggregatedCollection.getByMetricName(appMetric.getMetricName());
        // Third loop: ... for all clazzes ...
        for (final ClazzMetric clazzMetric : appMetric.getClassMetricValues()) {
          final ClazzMetric aggregatedClazz =
              aggregatedMetric.getClazzMetricByName(clazzMetric.getClazzName());
          if (aggregatedClazz != null) {
            // ... set value to half of the old value and add the new value if an old value exists.
            aggregatedClazz.decayAndAddValue(clazzMetric.getValue());
          } else {
            // ... or to the new value if there was no old value.
            aggregatedMetric.getClassMetricValues()
                .add(this.createClazzMetric(clazzMetric.getClazzName(),
                    clazzMetric.getValue()));
          }
        }
      }
    }
    return aggregatedMap;
  }

  /**
   * Compute the difference between the same classes of each metric object and return an object
   * containing the difference. If a class is not present in the old object it is added and treated
   * as if the old value was 0.
   *
   * @param oldMetrics
   * @return
   */
  public LandscapeMetrics computeDifference(final LandscapeMetrics newMetrics,
      final LandscapeMetrics oldMetrics) {
    // First loop: For all applications ...
    for (final ApplicationMetricCollection appCollection : newMetrics
        .getAplicationMetricCollections()) {
      final ApplicationMetricCollection comparedCollection =
          oldMetrics.getApplicationMetricCollectionByName(appCollection.getAppName());
      if (comparedCollection == null) {
        continue;
      }
      // Second loop: ... for all metrics ...
      for (final ApplicationMetric appMetric : appCollection.getMetricValues()) {
        final ApplicationMetric comparedMetric =
            comparedCollection.getByMetricName(appMetric.getMetricName());
        // Third loop: ... for all clazzes ...
        for (final ClazzMetric clazzMetric : appMetric.getClassMetricValues()) {
          final ClazzMetric comparedClazz =
              comparedMetric.getClazzMetricByName(clazzMetric.getClazzName());
          if (comparedClazz != null) {
            // ... set value to half of the old value and add the new value if an old value exists.
            comparedClazz.subtractValue(clazzMetric.getValue());
          } else {
            // ... or to the new value if there was no old value.
            comparedMetric.getClassMetricValues()
                .add(this.createClazzMetric(clazzMetric.getClazzName(),
                    clazzMetric.getValue()));
          }

        }
      }
    }
    return oldMetrics;
  }

  /**
   * Compute the aggregated heatmap by adding the (decaying) old values to the new metrics. If there
   * is no old heatmap, this just takes the current lmetrics.
   *
   * @param lmetrics
   * @param previousHeatmap the previous heatmap or nothing if there is no heatmap in the database.
   */
  private LandscapeMetrics computeAggregatedHeatmap(final LandscapeMetrics lmetrics,
      final Optional<Heatmap> previousHeatmap) {
    // 1. Check if there is a previous heatmap in the database.
    if (previousHeatmap.isPresent()) {
      final Heatmap previousMap = previousHeatmap.get();
      // 2. decay and add lmetrics
      return this.aggregateHeatmaps(lmetrics, previousMap.getAggregatedHeatmap());
    } else {
      return lmetrics;
    }
  }

  /**
   * Compute the heatmap for the given landscape metrics and the set windowsize.
   *
   * @param metrics
   */
  private LandscapeMetrics computeWindowHeatmap(final LandscapeMetrics lmetrics,
      final Optional<LandscapeMetrics> previousMetrics,
      final int windowsize) {
    // 1. get the heatmap of the n-1 timesteps before landscape metrics
    if (windowsize == 0 || previousMetrics.isEmpty()) {
      return lmetrics;
    } else {
      // 2. compute difference
      return this.computeDifference(lmetrics, previousMetrics.get());
    }
  }


}
