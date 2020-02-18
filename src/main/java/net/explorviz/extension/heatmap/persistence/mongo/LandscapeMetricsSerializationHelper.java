package net.explorviz.extension.heatmap.persistence.mongo;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import javax.inject.Inject;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;

/**
 * Helper class for de-/serializing landscapes from/to json api.
 *
 * @author Tim-Niklas Reck
 *
 */
public class LandscapeMetricsSerializationHelper {

  private final ResourceConverter jsonApiConverter;
  // private static final Logger LOGGER =
  // LoggerFactory.getLogger(LandscapeMetricSerializationHelper.class);

  @Inject
  public LandscapeMetricsSerializationHelper(final ResourceConverter jsonApiConverter) {
    this.jsonApiConverter = jsonApiConverter;
  }

  /**
   * Serializes a landscape metrics object to a json api string.
   *
   * @throws DocumentSerializationException if the landscape metrics could not be parsed.
   */
  public String serialize(final LandscapeMetrics l) throws DocumentSerializationException {
    final JSONAPIDocument<LandscapeMetrics> landscapeDoc = new JSONAPIDocument<>(l);
    final byte[] landscapeBytes = this.jsonApiConverter.writeDocument(landscapeDoc);
    return new String(landscapeBytes);
  }

  /**
   * Deserializes a json-api string to a {@link LandscapeMetrics} object.
   *
   * @param jsonApi the json api string representing a landscape metrics object
   * @return the landscape metrics
   * @throws DocumentSerializationException if the given string can't be deserialized to a landscape
   *         metrics object
   */
  public LandscapeMetrics deserialize(final String jsonApi) throws DocumentSerializationException {

    final byte[] b = jsonApi.getBytes();
    final JSONAPIDocument<LandscapeMetrics> landscapeDoc =
        this.jsonApiConverter.readDocument(b, LandscapeMetrics.class);

    return landscapeDoc.get();
  }
}
