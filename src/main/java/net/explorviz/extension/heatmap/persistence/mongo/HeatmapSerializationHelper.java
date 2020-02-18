package net.explorviz.extension.heatmap.persistence.mongo;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import java.util.List;
import javax.inject.Inject;
import net.explorviz.extension.heatmap.model.LandscapeMetrics;

/**
 * Helper class for de-/serializing landscapes from/to json api.
 *
 * @author Tim-Niklas Reck
 *
 */
public class HeatmapSerializationHelper {

  private final ResourceConverter jsonApiConverter;
  // private static final Logger LOGGER =
  // LoggerFactory.getLogger(LandscapeMetricSerializationHelper.class);

  @Inject
  public HeatmapSerializationHelper(final ResourceConverter jsonApiConverter) {
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
   * Serializes a list of landscape metrics to a json api string.
   *
   * @throws DocumentSerializationException if the landscape list could not be parsed.
   */
  public String serializeToList(final List<LandscapeMetrics> l)
      throws DocumentSerializationException {
    final JSONAPIDocument<List<LandscapeMetrics>> landscapeDoc = new JSONAPIDocument<>(l);
    final byte[] landscapeBytes = this.jsonApiConverter.writeDocumentCollection(landscapeDoc);
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

  /**
   * Deserializes a json-api string to a list of {@link LandscapeMetrics} objects.
   *
   * @param jsonApi the json api string representing a landscape metrics object
   * @return the landscape metrics list
   * @throws DocumentSerializationException if the given string can't be deserialized to a landscape
   *         metrics object
   */
  public List<LandscapeMetrics> deserializeToList(final String jsonApi)
      throws DocumentSerializationException {

    final byte[] b = jsonApi.getBytes();
    final JSONAPIDocument<List<LandscapeMetrics>> landscapeDoc =
        this.jsonApiConverter.readDocumentCollection(b, LandscapeMetrics.class);

    return landscapeDoc.get();
  }

}
