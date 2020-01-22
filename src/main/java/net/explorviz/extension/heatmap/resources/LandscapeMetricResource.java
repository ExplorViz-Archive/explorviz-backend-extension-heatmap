package net.explorviz.extension.heatmap.resources;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.sse.SseEventSink;
import net.explorviz.extension.heatmap.services.HeatmapService;

/**
 * TODO: Implement communication with frontend.
 *
 * @author Tim-Niklas Reck
 *
 */
@Path("landscapemetric")
// @RolesAllowed({"admin"})
@PermitAll
public class LandscapeMetricResource {

  // Access annotations can also be applied at method level

  private static final String MEDIA_TYPE = "application/vnd.api+json";

  private final HeatmapService heatmapService;

  @Inject
  public LandscapeMetricResource(final HeatmapService heatmapService) {
    this.heatmapService = heatmapService;
  }

  @GET
  @Produces(MEDIA_TYPE)
  // @Operation(description = "", summary = "")
  public void listenToBroadcast(@Context final SseEventSink eventSink,
      @Context final HttpServletResponse response) {

    this.heatmapService.register(eventSink);
  }

  // TODO: Further methods
}
