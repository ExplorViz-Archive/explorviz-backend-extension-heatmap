package net.explorviz.extension.heatmap.main;

import javax.inject.Singleton;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;
import net.explorviz.extension.heatmap.persistence.HeatmapRepository;
import net.explorviz.extension.heatmap.persistence.mongo.HeatmapSerializationHelper;
import net.explorviz.extension.heatmap.persistence.mongo.LandscapeMetricsSerializationHelper;
import net.explorviz.extension.heatmap.persistence.mongo.MongoHeatmapJsonApiRepository;
import net.explorviz.extension.heatmap.persistence.mongo.MongoHeatmapRepository;
import net.explorviz.extension.heatmap.persistence.mongo.MongoHelper;
import net.explorviz.extension.heatmap.persistence.mongo.MongoLandscapeMetricsRepository;
import net.explorviz.extension.heatmap.services.HeatmapService;
import net.explorviz.extension.heatmap.services.KafkaLandscapeExchangeService;
import net.explorviz.extension.heatmap.services.LandscapeSerializationHelper;
import net.explorviz.shared.common.injection.CommonDependencyInjectionBinder;
import org.glassfish.hk2.api.TypeLiteral;

/**
 * The DependencyInjectionBinder is used to register Contexts and Dependency Injection (CDI) aspects
 * for this application.
 */
public class DependencyInjectionBinder extends CommonDependencyInjectionBinder {

  @Override
  public void configure() {

    // Common DI
    super.configure();

    // Service-specific DI

    this.bind(HeatmapService.class).to(HeatmapService.class).in(Singleton.class);

    this.bind(KafkaLandscapeExchangeService.class).to(KafkaLandscapeExchangeService.class)
        .in(Singleton.class);

    this.bind(LandscapeSerializationHelper.class).to(LandscapeSerializationHelper.class)
        .in(Singleton.class);

    // Mongo

    this.bind(LandscapeMetricsSerializationHelper.class)
        .to(LandscapeMetricsSerializationHelper.class)
        .in(Singleton.class);
    this.bind(HeatmapSerializationHelper.class).to(HeatmapSerializationHelper.class)
        .in(Singleton.class);
    this.bind(MongoHelper.class).to(MongoHelper.class).in(Singleton.class);

    this.bind(MongoHeatmapJsonApiRepository.class)
        .to(MongoHeatmapJsonApiRepository.class).in(Singleton.class);
    this.bind(MongoHeatmapRepository.class).to(MongoHeatmapRepository.class).in(Singleton.class);
    this.bind(MongoLandscapeMetricsRepository.class).to(MongoLandscapeMetricsRepository.class)
        .in(Singleton.class);
    this.bind(MongoHeatmapJsonApiRepository.class)
        .to(new TypeLiteral<HeatmapRepository<String>>() {}).in(Singleton.class);
    this.bind(MongoHeatmapRepository.class).to(new TypeLiteral<HeatmapRepository<Heatmap>>() {})
        .in(Singleton.class);
    this.bind(MongoLandscapeMetricsRepository.class)
        .to(new TypeLiteral<HeatmapRepository<LandscapeMetrics>>() {})
        .in(Singleton.class);

  }
}
