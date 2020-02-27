package net.explorviz.extension.heatmap.model.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {

  // https://www.baeldung.com/java-round-decimal-number
  public static double round(final double value, final int places) {
    if (places < 0) {
      throw new IllegalArgumentException();
    }

    BigDecimal bd = new BigDecimal(Double.toString(value));
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

}
