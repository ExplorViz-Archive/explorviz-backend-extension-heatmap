package net.explorviz.extension.heatmap.persistence.mongo;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import javax.inject.Inject;
import net.explorviz.extension.heatmap.model.heatmap.LandscapeMetric;

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
  public String serialize(final LandscapeMetric l) throws DocumentSerializationException {
    final JSONAPIDocument<LandscapeMetric> landscapeDoc = new JSONAPIDocument<>(l);
    final byte[] landscapeBytes = this.jsonApiConverter.writeDocument(landscapeDoc);
    return new String(landscapeBytes);
  }

  /**
   * Deserializes a json-api string to a {@link LandscapeMetric} object.
   *
   * @param jsonApi the json api string representing a landscape metrics object
   * @return the landscape metrics
   * @throws DocumentSerializationException if the given string can't be deserialized to a landscape
   *         metrics object
   */
  public LandscapeMetric deserialize(final String jsonApi) throws DocumentSerializationException {

    final byte[] b = jsonApi.getBytes();
    final JSONAPIDocument<LandscapeMetric> landscapeDoc =
        this.jsonApiConverter.readDocument(b, LandscapeMetric.class);

    return landscapeDoc.get();
  }
}
