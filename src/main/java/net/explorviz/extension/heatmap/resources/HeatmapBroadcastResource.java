package net.explorviz.extension.heatmap.resources;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSink;
import net.explorviz.extension.heatmap.services.HeatmapService;
import net.explorviz.extension.heatmap.services.KafkaLandscapeExchangeService;
import net.explorviz.shared.security.filters.Secure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This resource provides an interface for the frontend to register for heatmap broadcast updated.
 *
 * @author Tim-Niklas Reck
 *
 */
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "token", scheme = "bearer",
    bearerFormat = "JWT")
@SecurityRequirement(name = "token")
@Tag(name = "Heatmap Broadcast")
@Path("v1/heatmap/broadcast")
@Secure
@PermitAll
public class HeatmapBroadcastResource {

  // Access annotations can also be applied at method level

  // private static final String MEDIA_TYPE = "application/vnd.api+json";
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLandscapeExchangeService.class);
  private final HeatmapService heatmapService;

  @Inject
  public HeatmapBroadcastResource(final HeatmapService heatmapService) {
    this.heatmapService = heatmapService;
  }

  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS)
  // @Operation(description = "Endpoint that clients can use to register for metric updates.",
  // summary="Register for metric updates")
  public void listenToBroadcast(@Context final SseEventSink eventSink,
      @Context final HttpServletResponse response) {

    // https://serverfault.com/a/801629
    response.addHeader("Cache-Control", "no-cache");
    response.addHeader("X-Accel-Buffering", "no");

    this.heatmapService.register(eventSink);
    LOGGER.debug("Registering: {}", eventSink.toString());
  }

}
