package com.alexiusacademia.tester;

import com.alexiusacademia.hydraulics.TrapezoidalOpenChannel;

public class TrapezoidalChannel {
  public static void main(String[] args) {
    printLine("Trapezoidal open channel");
    printDash();

    double bedSlope = 0.001;
    double baseWidth = 1.0;
    double waterDepth = 0.989;
    double sideSlope = 1.0;
    double manning = 0.015;

    TrapezoidalOpenChannel toc = new TrapezoidalOpenChannel(TrapezoidalOpenChannel.Unknown.DISCHARGE);
    toc.setBedSlope(bedSlope);
    toc.setBaseWidth(baseWidth);
    toc.setWaterDepth(waterDepth);
    toc.setSideSlope(sideSlope);
    toc.setManningRoughness(manning);

    printLine("Given");
    printLine("Bed Slope = " + bedSlope);
    printLine("Base Width = " + baseWidth);
    printLine("Water depth = " + waterDepth);
    printLine("Side slope = " + sideSlope);
    printLine("Manning's roughness coeff. = " + manning);
    printDash();

    if (toc.analyze()) {
      printLine("Analysis");
      printLine("Discharge = " + toc.getDischarge());
      printLine("Average velocity = " + toc.getAverageVelocity());
      printLine("Wetted area = " + toc.getWettedArea());
      printDash();
      printLine("Critical depth = " + toc.getCriticalDepth());
      printLine("Froude number = " + toc.getFroudeNumber());
      printLine("Flow Type = " + toc.getFlowType());
    } else {
      printLine("An error has occurred: " + toc.getErrMessage());
    }
  }

  private static void printLine(String s) {
    System.out.println(s);
  }


  private static void printDash() {
    printLine("= = = = = = = = = = =");
  }
}
