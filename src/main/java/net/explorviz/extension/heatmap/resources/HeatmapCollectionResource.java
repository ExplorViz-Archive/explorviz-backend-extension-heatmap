package net.explorviz.extension.heatmap.resources;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import net.explorviz.extension.heatmap.services.HeatmapService;

/**
 * TODO: Implement communication with frontend.
 * 
 * @author Tim-Niklas Reck
 *
 */
@Path("heatmap")
// @RolesAllowed({"admin"})
@PermitAll
public class HeatmapCollectionResource {

  // Access annotations can also be applied at method level

  private static final String MEDIA_TYPE = "application/vnd.api+json";
  @Inject
  private HeatmapService heatmapService;

  @GET
  @Produces(MEDIA_TYPE)
  public void getModel() {


  }
}
