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

    RectangularOpenChannel roc = new RectangularOpenChannel();
    roc.setUnknown(RectangularOpenChannel.Unknown.DISCHARGE);
    roc.setBedSlope(0.001);
    roc.setBaseWidth((float)1.0);
    roc.setWaterDepth((float)0.989);
    roc.setManningRoughness((float)0.015);

    roc.analyze();

    System.out.println("Discharge = " + roc.getDischarge());
  }
}
