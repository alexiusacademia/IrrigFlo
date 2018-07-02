package com.alexiusacademia.tester;

import com.alexiusacademia.hydraulics.IrregularSectionChannel;
import com.alexiusacademia.hydraulics.Point;

import java.util.ArrayList;
import java.util.List;

public class IrregularChannel {
  public static void main(String[] args) {
    IrregularSectionChannel isc = new IrregularSectionChannel();

    List<Point> pts = new ArrayList<>();
    /*
    pts.add(new Point(0, 0));
    pts.add(new Point(1.524f, -0.64f));
    pts.add(new Point(3.049f, -1.036f));
    pts.add(new Point(4.573f, -1.707f));
    pts.add(new Point(6.097f, -1.433f));
    pts.add(new Point(7.622f, -1.067f));
    pts.add(new Point(9.146f, -1.341f));
    pts.add(new Point(10.67f, -1.646f));
    pts.add(new Point(12.195f, -1.859f));
    pts.add(new Point(13.719f, -1.768f));
    pts.add(new Point(15.244f, -1.738f));
    pts.add(new Point(16.768f, -1.555f));
    pts.add(new Point(18.293f, -1.829f));
    pts.add(new Point(19.817f, -1.982f));
    pts.add(new Point(21.341f, -2.195f));
    pts.add(new Point(22.866f, -2.195f));
    pts.add(new Point(24.39f, -2.5f));
    pts.add(new Point(25.915f, -1.677f));
    pts.add(new Point(27.439f, -1.096f));
    pts.add(new Point(28.963f, -0.975f));
    pts.add(new Point(30.488f, 0));
    */
    pts.add(new Point(0, 100));
    pts.add(new Point(5, 99));
    pts.add(new Point(10, 96));
    pts.add(new Point(15, 95.5f));
    pts.add(new Point(20, 98));
    pts.add(new Point(25, 100));

    isc.setPoints(pts);
    isc.setUnknown(IrregularSectionChannel.Unknown.DISCHARGE);
    isc.setDischarge(1);
    isc.setBedSlope(0.002);
    isc.setWaterElevation(99.5f);
    isc.setManningRoughness(0.03);

    if (isc.analyze()) {
      printLine("Discharge = " + isc.getDischarge());
      printLine("Wetted area = " + isc.getWettedArea());
      printLine("Wetted perimeter = " + isc.getWettedPerimeter());
      printLine("Hydraulic Radius = " + isc.getHydraulicRadius());
      printLine("Water elevation = " + isc.getWaterElevation());
      printLine("Froude number = " + isc.getFroudeNumber());
      printLine("Flow type = " + isc.getFlowType());
      printLine("Bed Slope = " + isc.getBedSlope());
      printLine("Hydraulic depth = " + isc.getHydraulicDepth());
    } else {
      printLine("An error has occurred: " + isc.getErrMessage());
      printLine("Lowest bank = " + isc.getMaxWaterElevation());
      printLine("Water elev. = " + isc.getWaterElevation());
    }

  }

  private static void printLine(String s) {
    System.out.println(s);
  }

  private static void printDash() {
    printLine("= = = = = = = = = = = = = =");
  }
}
