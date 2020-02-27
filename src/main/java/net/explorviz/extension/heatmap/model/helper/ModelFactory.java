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
import net.explorviz.extension.heatmap.model.heatmap.LandscapeMetric;
import net.explorviz.extension.heatmap.model.metrics.ClassActivity;
import net.explorviz.extension.heatmap.model.metrics.ExportCoupling;
import net.explorviz.extension.heatmap.model.metrics.ImportCoupling;
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

    this.metrics.add(new ClassActivity(this.idGen.generateId()));
    this.metrics.add(new ExportCoupling(this.idGen.generateId()));
    this.metrics.add(new ImportCoupling(this.idGen.generateId()));
    this.metrics.add(new InstanceCount(this.idGen.generateId()));
  }

  /**
   * Create and initialize a new {@link LandscapeMetric} and generate a new id for it.
   *
   * @param landscape
   * @return
   */
  public LandscapeMetric createLandscapeMetrics(final Landscape landscape) {
    final LandscapeMetric lmetrics =
        new LandscapeMetric(this.idGen.generateId(), landscape.getTimestamp().getTimestamp(),
            landscape.getId());
    lmetrics.setMetrics(this.metrics);
    final List<ApplicationMetricCollection> appMetricCollections =
        this.computeApplicationMetricsCollection(landscape);
    lmetrics.setApplicationMetricCollections(appMetricCollections);
    return lmetrics;
  }


  /**
   * Create and initialize a new {@link ApplicationMetricCollection} and generate a new id for it.
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
   * Create and initialize a new {@link ApplicationMetric} and generate a new id for it.
   *
   * @param application
   * @param metric
   * @return
   */
  public ApplicationMetric createApplicationMetric(final Application application,
      final Metric metric) {
    final ApplicationMetric appMetric =
        new ApplicationMetric(this.idGen.generateId(), metric.getTypeName());
    appMetric.setClassMetricValues(this.computeApplicationMetrics(application, metric));
    return appMetric;
  }


  /**
   * Create a new heatmap based on the given landscape metrics. The windowsize defined in
   * "explorviz.properties" should be at least 2. Otherwise it will just show the current metric.
   *
   * @param lmetrics the new landscape metrics to be considered
   * @param previousTimestamp the timestamp of the previous heatmap
   */
  public Heatmap createHeatmap(
      final LandscapeMetric lmetrics,
      final long previousTimestamp) {

    final int windowsize = PropertyHelper.getIntegerProperty("heatmap.window.size");
    final Heatmap heatmap =
        new Heatmap(this.idGen.generateId(), windowsize, lmetrics.getTimestamp(),
            lmetrics.getLandscapeId());

    final LandscapeMetric aggregatedHeatmap =
        this.computeAggregatedHeatmap(this.deepCopy(lmetrics),
            this.mongoHeatmapRepo.getByTimestamp(previousTimestamp));
    final LandscapeMetric windowedHeatmap = this.computeWindowHeatmap(this.deepCopy(lmetrics),
        this.mongoMetricRepo.getNthLastRecord(windowsize), windowsize);

    heatmap.setAggregatedHeatmap(aggregatedHeatmap);
    heatmap.setWindowedHeatmap(windowedHeatmap);

    final List<String> metricTypes = new ArrayList<>();
    for (final Metric metric : this.metrics) {
      metricTypes.add(metric.getTypeName());
    }
    heatmap.setMetricTypes(metricTypes);

    return heatmap;
  }


  /**
   * Creates a deep copy of a {@link LandscapeMetric} object.
   *
   * @param landscapeMetrics
   * @return
   */
  public LandscapeMetric deepCopy(final LandscapeMetric landscapeMetrics) {
    final LandscapeMetric lmetrics =
        new LandscapeMetric(this.idGen.generateId(), landscapeMetrics.getTimestamp(),
            landscapeMetrics.getLandscapeId());
    lmetrics.setMetrics(this.metrics);
    final List<ApplicationMetricCollection> appMetricCollections = new ArrayList<>();
    for (final ApplicationMetricCollection collection : landscapeMetrics
        .getApplicationMetricCollections()) {
      appMetricCollections.add(this.deepCopy(collection));
    }
    lmetrics.setApplicationMetricCollections(appMetricCollections);
    return lmetrics;
  }

  /**
   * Creates a deep copy of a {@link ApplicationMetricCollection} object.
   *
   * @param applicationMetricCollection
   * @return
   */
  public ApplicationMetricCollection deepCopy(
      final ApplicationMetricCollection applicationMetricCollection) {
    final ApplicationMetricCollection appCollection =
        new ApplicationMetricCollection(this.idGen.generateId(),
            applicationMetricCollection.getAppName(), applicationMetricCollection.getAppId());
    final List<ApplicationMetric> appMetrics = new ArrayList<>();
    for (final ApplicationMetric appMetric : applicationMetricCollection.getMetricValues()) {
      appMetrics.add(this.deepCopy(appMetric));
    }
    appCollection.setMetricValues(appMetrics);
    return appCollection;
  }


  /**
   * Creates a deep copy of a {@link ApplicationMetric} object.
   *
   * @param applicationMetricCollection
   * @return
   */
  public ApplicationMetric deepCopy(
      final ApplicationMetric applicationMetric) {
    final ApplicationMetric appMetric =
        new ApplicationMetric(this.idGen.generateId(), applicationMetric.getMetricType());

    final List<ClazzMetric> clazzMetrics = new ArrayList<>();
    for (final ClazzMetric clazzMetric : applicationMetric.getClassMetricValues()) {
      clazzMetrics.add(this.createClazzMetric(clazzMetric.getClazzName(), clazzMetric.getValue()));
    }
    appMetric.setClassMetricValues(clazzMetrics);
    return appMetric;
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
  private LandscapeMetric aggregateHeatmaps(final LandscapeMetric lmetrics,
      final LandscapeMetric aggregatedMap) {
    // First loop: For all applications ...
    for (final ApplicationMetricCollection appCollection : lmetrics
        .getApplicationMetricCollections()) {
      final ApplicationMetricCollection aggregatedCollection =
          aggregatedMap.getApplicationMetricCollectionByName(appCollection.getAppName());
      if (aggregatedCollection == null) {
        continue;
      }
      // Second loop: ... for all metrics ...
      for (final ApplicationMetric appMetric : appCollection.getMetricValues()) {
        final ApplicationMetric aggregatedMetric =
            aggregatedCollection.getByMetricType(appMetric.getMetricType());
        // Third loop: ... for all clazzes ...
        for (final ClazzMetric clazzMetric : appMetric.getClassMetricValues()) {
          final ClazzMetric aggregatedClazz =
              aggregatedMetric.getClazzMetricByName(clazzMetric.getClazzName());
          if (aggregatedClazz != null) {
            // ... add half of the old value to the new one.
            clazzMetric.addValue(0.5 * aggregatedClazz.getValue());
          }
        }
      }
    }
    return lmetrics;
  }

  /**
   * Compute the difference between the same classes of each metric object and return an object
   * containing the difference.
   *
   * @param oldMetrics
   * @return
   */
  private LandscapeMetric computeDifference(final LandscapeMetric lmetrics,
      final LandscapeMetric oldMetrics) {
    // First loop: For all applications ...
    for (final ApplicationMetricCollection appCollection : lmetrics
        .getApplicationMetricCollections()) {
      final ApplicationMetricCollection comparedCollection =
          oldMetrics.getApplicationMetricCollectionByName(appCollection.getAppName());
      if (comparedCollection == null) {
        continue;
      }
      // Second loop: ... for all metrics ...
      for (final ApplicationMetric appMetric : appCollection.getMetricValues()) {
        final ApplicationMetric comparedMetric =
            comparedCollection.getByMetricType(appMetric.getMetricType());
        // Third loop: ... for all clazzes ...
        for (final ClazzMetric clazzMetric : appMetric.getClassMetricValues()) {
          final ClazzMetric comparedClazz =
              comparedMetric.getClazzMetricByName(clazzMetric.getClazzName());
          if (comparedClazz != null) {
            // ... substract old value from new value to get the difference.
            clazzMetric.subtractValue(comparedClazz.getValue());
          }
        }
      }
    }
    return lmetrics;
  }

  /**
   * Compute the aggregated heatmap by adding the (decaying) old values to the new metrics. If there
   * is no old heatmap, this just takes the current lmetrics.
   *
   * @param lmetrics
   * @param previousHeatmap the previous heatmap or nothing if there is no heatmap in the database.
   */
  private LandscapeMetric computeAggregatedHeatmap(final LandscapeMetric lmetrics,
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
  private LandscapeMetric computeWindowHeatmap(final LandscapeMetric lmetrics,
      final Optional<LandscapeMetric> previousMetrics,
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
