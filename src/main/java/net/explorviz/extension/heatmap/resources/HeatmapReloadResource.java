package net.explorviz.extension.heatmap.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;
import net.explorviz.extension.heatmap.persistence.HeatmapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Resource providing persisted {@link Heatmap} data for the frontend.
 *
 * @author Tim-Niklas Reck
 *
 */
@Path("v1/heatmaps/")
@Tag(name = "Heatmaps")
// @RolesAllowed({"admin", "user"})
// @SecurityScheme(type = SecuritySchemeType.HTTP, name = "token", scheme = "bearer",
// bearerFormat = "JWT")
// @SecurityRequirement(name = "token")
// @Secure
@PermitAll
public class HeatmapReloadResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(HeatmapReloadResource.class);

  private static final String MEDIA_TYPE = "application/vnd.api+json";
  private static final long QUERY_PARAM_DEFAULT_VALUE_LONG = 0L;
  // private static final String QUERY_PARAM_EMPTY_STRING = "";

  private final HeatmapRepository<String> heatmapRepository;

  @Inject
  public HeatmapReloadResource(final HeatmapRepository<String> heatmapRepository) {
    this.heatmapRepository = heatmapRepository;
  }


  /**
   * Returns {@link Heatmap} with the passed query parameter.
   *
   * @param timestamp - query parameter
   * @return the requested timestamp
   */
  @GET
  @Produces(MEDIA_TYPE)
  // @Operation(summary = "Find a heatmap by its timestamp")
  // @ApiResponse(responseCode = "200",
  // description = "Response contains the first heatmap with the given timestamp.",
  // content = @Content(schema = @Schema(implementation = Heatmap.class)))
  // @ApiResponse(responseCode = "404", description = "No heatmap with the given timestamp.")
  public String getHeatmapByTimestamp(@Parameter(description = "The timestamp to filter by.",
      required = true) @QueryParam("timestamp") final long timestamp) {

    if (timestamp == QUERY_PARAM_DEFAULT_VALUE_LONG) {
      throw new BadRequestException("Query parameter 'timestamp' is mandatory");
    }

    LOGGER.info("Sending requested heatmaps with timestamp {} to server.", timestamp);
    return Stream
        .of(this.heatmapRepository.getByTimestamp(timestamp))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElseThrow(
            () -> new NotFoundException("Heatmap with timestamp " + timestamp + " not found."));
  }

}

