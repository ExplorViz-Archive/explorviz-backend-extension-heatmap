package net.explorviz.extension.heatmap.main;

import net.explorviz.extension.heatmap.model.heatmap.ApplicationMetric;
import net.explorviz.extension.heatmap.model.heatmap.ApplicationMetricCollection;
import net.explorviz.extension.heatmap.model.heatmap.ClazzMetric;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;
import net.explorviz.extension.heatmap.model.heatmap.LandscapeMetric;
import net.explorviz.extension.heatmap.model.metrics.DummyMetric;
import net.explorviz.extension.heatmap.model.metrics.DynamicKey;
import net.explorviz.extension.heatmap.model.metrics.ExportCoupling;
import net.explorviz.extension.heatmap.model.metrics.ImportCoupling;
import net.explorviz.extension.heatmap.model.metrics.InstanceCount;
import net.explorviz.extension.heatmap.model.metrics.Metric;
import net.explorviz.extension.heatmap.resources.HeatmapBroadcastResource;
import net.explorviz.extension.heatmap.resources.HeatmapReloadResource;
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

    GenericTypeFinder.getTypeMap().putIfAbsent("ClazzMetric", ClazzMetric.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ApplicationMetric", ApplicationMetric.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ApplicationMetricCollection",
        ApplicationMetricCollection.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("LandscapeMetric", LandscapeMetric.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("Heatmap", Heatmap.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("Metric", Metric.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("InstanceCountMetric", InstanceCount.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ImportCouplingMetric", ImportCoupling.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("ExportCouplingMetric", ExportCoupling.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("DynamicKeyMetric", DynamicKey.class);
    GenericTypeFinder.getTypeMap().putIfAbsent("DummyMetric", DummyMetric.class);


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
    this.register(HeatmapReloadResource.class);

    // Starting point for your DI-based extension
    this.register(SetupApplicationListener.class);
  }
}
