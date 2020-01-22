package net.explorviz.extension.heatmap.main;

import javax.inject.Singleton;
import net.explorviz.extension.heatmap.persistence.mongo.LandscapeMetricSerializationHelper;
import net.explorviz.extension.heatmap.persistence.mongo.MongoHelper;
import net.explorviz.extension.heatmap.persistence.mongo.MongoLandscapeMetricJsonApiRepository;
import net.explorviz.extension.heatmap.services.HeatmapService;
import net.explorviz.extension.heatmap.services.KafkaLandscapeExchangeService;
import net.explorviz.extension.heatmap.services.LandscapeSerializationHelper;
import net.explorviz.shared.common.injection.CommonDependencyInjectionBinder;

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

    this.bind(LandscapeMetricSerializationHelper.class).to(LandscapeMetricSerializationHelper.class)
        .in(Singleton.class);
    this.bind(MongoHelper.class).to(MongoHelper.class).in(Singleton.class);
    this.bind(MongoLandscapeMetricJsonApiRepository.class)
        .to(MongoLandscapeMetricJsonApiRepository.class).in(Singleton.class);

  }
}
