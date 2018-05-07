package com.alexiusacademia.tester;

import com.alexiusacademia.hydraulics.*;

public class Main {
  public static void main(String[] args) {
    // Given for rectangular channel
    /*
    S = 0.001
    b = 1.0
    d = 0.989
    n = 0.015
     */
    System.out.println("Sample Output:");
    System.out.println("= = = = = = = = = = = = =");
    /**
     * Unknown is discharge
     */
    RectangularOpenChannel roc = new RectangularOpenChannel();
    /*
    System.out.println("Unknown = Discharge");
    roc.setUnknown(RectangularOpenChannel.Unknown.DISCHARGE);
    roc.setBedSlope(0.001);
    roc.setBaseWidth(1.0);
    roc.setWaterDepth(0.989);
    roc.setManningRoughness(0.015);
    roc.analyze();

    System.out.println("Discharge = " + roc.getDischarge());
    System.out.println("");
    System.out.println("Unknown = WaterDepth");
    roc.setUnknown(RectangularOpenChannel.Unknown.WATER_DEPTH);
    roc.setDischarge(1.0);
    roc.setBedSlope(0.001);
    roc.setBaseWidth((float)1.0);
    roc.setManningRoughness((float)0.015);
    roc.analyze();

    System.out.println("Water Depth = " + roc.getWaterDepth());

    System.out.println("");
    System.out.println("Unknown = BaseWidth");

    roc.setUnknown(RectangularOpenChannel.Unknown.BASE_WIDTH);
    roc.setDischarge(1.0);
    roc.setBedSlope(0.001);
    roc.setWaterDepth(0.989);
    roc.setManningRoughness((float)0.015);
    roc.analyze();

    System.out.println("Base width = " + roc.getBaseWidth());
    System.out.println("");
    */
    System.out.println("Unknown = Bed Slope");

    roc.setUnknown(RectangularOpenChannel.Unknown.BED_SLOPE);
    roc.setDischarge(1.0);
    roc.setWaterDepth(0.989);
    roc.setBaseWidth(1.0);
    roc.setManningRoughness(0.01);

    if (roc.analyze()) {
      System.out.println("Bed slope = " + roc.getBedSlope());
    } else {
      System.out.println("An error has occurred: " + roc.getErrMessage());
    }
  }
}
