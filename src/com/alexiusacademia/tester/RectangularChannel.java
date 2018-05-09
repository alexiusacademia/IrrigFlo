package com.alexiusacademia.tester;

import com.alexiusacademia.hydraulics.RectangularOpenChannel;

public class RectangularChannel {
  public static void main(String[] args) {
    /*
     * Demonstration of using RectangularOpenChannel
     */

    // Given
    double discharge = 1.2;
    double bedSlope = 0.001;
    double baseWidth = 2.0;
    double waterDepth = 0.989;
    double manningRoughness = 0.015;

    // Initialization
    RectangularOpenChannel roc = new RectangularOpenChannel(RectangularOpenChannel.Unknown.WATER_DEPTH);
    roc.setDischarge(discharge);
    roc.setBedSlope(bedSlope);
    roc.setBaseWidth(baseWidth);
    roc.setManningRoughness(manningRoughness);

    if (roc.analyze()) {
      System.out.println("Water Depth = " + roc.getWaterDepth());
    } else {
      System.out.println("Calculation unsuccessful.");
      System.out.println(roc.getErrMessage());
    }

    RectangularOpenChannel roc2 = new RectangularOpenChannel(RectangularOpenChannel.Unknown.BASE_WIDTH);
    roc2.setDischarge(discharge);
    roc2.setBedSlope(bedSlope);
    roc2.setWaterDepth(waterDepth);
    roc2.setManningRoughness(manningRoughness);
    if (roc2.analyze()) {
      System.out.println("Base Width = " + Math.round(roc2.getBaseWidth()));
    } else {
      System.out.println("Calculation unsuccessful.");
      System.out.println(roc2.getErrMessage());
    }
  }

  private static void println(String s) {
    System.out.println(s);
  }
}
