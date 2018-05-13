package com.alexiusacademia.tester;

import com.alexiusacademia.hydraulics.CircularOpenChannel;

public class CircularChannel {

  public static void main(String[] args) {
    double slope = 0.001;
    double diameter = 1.0;
    double waterDepth = 0.70;
    double manning = 0.015;

    CircularOpenChannel coc = new CircularOpenChannel();
    coc.setDiameter(diameter);
    coc.setBedSlope(slope);
    coc.setWaterDepth(waterDepth);
    coc.setManningRoughness(manning);

    printLine("Circular open channel");
    printDash();

    if (coc.analyze()) {
      printLine("Discharge = " + coc.getDischarge());
    } else {
      printLine("An error has occured: " + coc.getErrMessage());
    }
  }

  private static void printLine(String s) {
    System.out.println(s);
  }

  private static void printDash() {
    printLine("= = = = = = = = = = = = = =");
  }
}
