package net.explorviz.extension.heatmap.main;

import net.explorviz.extension.heatmap.model.ApplicationMetric;
import net.explorviz.extension.heatmap.model.ApplicationMetricCollection;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;
import net.explorviz.extension.heatmap.model.metrics.ClassActivity;
import net.explorviz.extension.heatmap.model.metrics.ExportCoupling;
import net.explorviz.extension.heatmap.model.metrics.ImportCoupling;
import net.explorviz.extension.heatmap.model.metrics.InstanceCount;
import net.explorviz.extension.heatmap.model.metrics.Metric;
import net.explorviz.extension.heatmap.resources.HeatmapBroadcastResource;
import net.explorviz.landscape.model.helper.TypeProvider;
import net.explorviz.shared.common.provider.GenericTypeFinder;
import net.explorviz.shared.common.provider.JsonApiListProvider;
import net.explorviz.shared.common.provider.JsonApiProvider;
import net.explorviz.shared.security.filters.AuthenticationFilter;
import net.explorviz.shared.security.filters.AuthorizationFilter;
import net.explorviz.shared.security.filters.CorsResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;

public class HeatmapApplication extends ResourceConfig {

  public HeatmapApplication() {

    super();

    GenericTypeFinder.getTypeMap().putIfAbsent("Metric", Metric.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ApplicationMetric", ApplicationMetric.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ApplicationMetricCollection",
        ApplicationMetricCollection.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("LandscapeMetrics", LandscapeMetrics.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("InstanceCountMetric", InstanceCount.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ImportCouplingMetric", ImportCoupling.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ExportCouplingMetric", ExportCoupling.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ClassActivityMetric", ClassActivity.class);


    // register Landscape Model classes, since we want to use them
    TypeProvider.getExplorVizCoreTypesAsMap().forEach((classname, classRef) -> {
      GenericTypeFinder.getTypeMap().put(classname, classRef);
    });

    // register DI
    this.register(new DependencyInjectionBinder());

    // Security
    this.register(AuthenticationFilter.class);
    this.register(AuthorizationFilter.class);
    this.register(CorsResponseFilter.class);

    // register providers
    this.register(JsonApiProvider.class);
    this.register(JsonApiListProvider.class);

    // resources
    this.register(HeatmapBroadcastResource.class);

    // register the model classes
    // this.register(LandscapeMetricResource.class);
    // this.register(ApplicationMetricCollection.class);
    // this.register(ApplicationMetric.class);
    // this.register(Metric.class);

    // Starting point for your DI-based extension
    this.register(SetupApplicationListener.class);
  }
}
