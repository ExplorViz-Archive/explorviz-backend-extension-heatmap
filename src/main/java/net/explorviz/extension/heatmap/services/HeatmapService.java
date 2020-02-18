package net.explorviz.extension.heatmap.services;

import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Singleton
public class HeatmapService {

  private static final Logger LOGGER = LoggerFactory.getLogger(HeatmapService.class);
  private static final MediaType APPLICATION_JSON_API_TYPE =
      new MediaType("application", "vnd.api+json");

  private static final String SSE_EVENT_NAME = "message";

  private final Sse sse;
  private final SseBroadcaster broadcaster;

  /**
   * Creates a new service.
   *
   * @param sse - Sse entry point
   */
  public HeatmapService(@Context final Sse sse) {
    this.sse = sse;
    this.broadcaster = sse.newBroadcaster();

    this.broadcaster.onClose(this::onCloseOperation);
    this.broadcaster.onError(this::onErrorOperation);
  }

  /**
   * Broadcast new JSON-API compliant stringified landscape metrics landscape to all registered
   * clients.
   *
   * @param jsonApiLandscapeMetrics
   */
  public void broadcastLandscapeMetric(final String jsonApiLandscapeMetrics) {
    final OutboundSseEvent event = this.sse.newEventBuilder()
        .name(SSE_EVENT_NAME)
        .mediaType(APPLICATION_JSON_API_TYPE)
        .data(jsonApiLandscapeMetrics)
        .build();

    this.broadcaster.broadcast(event);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Broadcast new landscape metrics to clients");
    }
  }

  public void register(final SseEventSink eventSink) {
    this.broadcaster.register(eventSink);
  }

  private void onCloseOperation(final SseEventSink sink) { // NOPMD
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("SseEventSink closed");
    }
  }

  private void onErrorOperation(final SseEventSink sink, final Throwable e) { // NOPMD
    if (LOGGER.isErrorEnabled()) {
      LOGGER.error("Broadcasting to a SseEventSink failed. "
          + "This may not be a problem, since there is no way to unregister.");
    }
  }

}
