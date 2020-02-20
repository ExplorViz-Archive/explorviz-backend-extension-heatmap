package net.explorviz.extension.heatmap.model;

import java.util.List;
import javax.inject.Singleton;
import net.explorviz.extension.heatmap.model.heatmap.LandscapeMetrics;
import net.explorviz.extension.heatmap.model.metrics.Metric;
import net.explorviz.landscape.model.landscape.Landscape;
import net.explorviz.shared.common.idgen.IdGenerator;

@Singleton
public class LandscapeMetricFactory {

  private final IdGenerator idGen;


  public LandscapeMetricFactory(final IdGenerator idGen) {
    this.idGen = idGen;
  }

  public LandscapeMetrics createLandscapeMetrics(final List<Metric> metrics, final Landscape l) {
    return new LandscapeMetrics(this.idGen.generateId(), metrics, l);
  }
}
