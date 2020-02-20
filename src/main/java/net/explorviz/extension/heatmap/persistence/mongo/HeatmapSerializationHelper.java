package net.explorviz.extension.heatmap.persistence.mongo;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import javax.inject.Inject;
import net.explorviz.extension.heatmap.model.heatmap.Heatmap;

public class HeatmapSerializationHelper {

  private final ResourceConverter jsonApiConverter;

  @Inject
  public HeatmapSerializationHelper(final ResourceConverter jsonApiConverter) {
    this.jsonApiConverter = jsonApiConverter;
  }

  /**
   * Serializes a {@link Heatmap} object to a json api string.
   *
   * @throws DocumentSerializationException if the landscape metrics could not be parsed.
   */
  public String serialize(final Heatmap h) throws DocumentSerializationException {
    final JSONAPIDocument<Heatmap> heatmapDoc = new JSONAPIDocument<>(h);
    final byte[] landscapeBytes = this.jsonApiConverter.writeDocument(heatmapDoc);
    return new String(landscapeBytes);
  }

  /**
   * Deserializes a json-api string to a {@link Heatmap} object.
   *
   * @param jsonApi the json api string representing a landscape metrics object
   * @return the landscape metrics
   * @throws DocumentSerializationException if the given string can't be deserialized to a landscape
   *         metrics object
   */
  public Heatmap deserialize(final String jsonApi) throws DocumentSerializationException {

    final byte[] b = jsonApi.getBytes();
    final JSONAPIDocument<Heatmap> heatmapDoc =
        this.jsonApiConverter.readDocument(b, Heatmap.class);

    return heatmapDoc.get();
  }
}
