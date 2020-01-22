package net.explorviz.extension.heatmap.main;

import net.explorviz.extension.heatmap.metrics.Metric;
import net.explorviz.extension.heatmap.model.ApplicationMetric;
import net.explorviz.extension.heatmap.model.ApplicationMetricCollection;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;
import net.explorviz.extension.heatmap.resources.LandscapeMetricResource;
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

    GenericTypeFinder.getTypeMap().put("ApplicationMetric", ApplicationMetric.class);
    GenericTypeFinder.getTypeMap().put("ApplicationMetricCollection",
        ApplicationMetricCollection.class);
    GenericTypeFinder.getTypeMap().put("LandscapeMetrics", LandscapeMetrics.class);
    GenericTypeFinder.getTypeMap().put("Metric", Metric.class);


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

    // register the HeatmapCollectionResource
    this.register(LandscapeMetricResource.class);

    // Starting point for your DI-based extension
    this.register(SetupApplicationListener.class);
  }
}
