# IrrigFlo
IrrigFlo is an open source java library for solving hydraulics flow calculations in hydraulics engineering.
The aim of the library is to help other developers in creating their own desktop applications for hydraulics engineering
that has the capability of commercial softwares.

### Installation:
To install the library, download it from the [releases](https://github.com/alexiusacademia/IrrigFlo/releases) page. It is advisable to download the latest release for updated features.

### Usage
Solving trapezoidal shaped channel
```java
import com.alexiusacademia.hydraulics.TrapezoidalOpenChannel;

public class TrapezoidalChannel {
  public static void main(String[] args) {
    // Create an instance of the class with a given unknown, Discharge
    TrapezoidalOpenChannel channel = new TrapezoidalOpenChannel(TrapezoidalOpenChannel.Unknown.DISCHARGE);

    // Setting the properties required
    // If some required properties are not set, getErrMessage will return a null message.
    toc.setBedSlope(bedSlope);
    toc.setBaseWidth(baseWidth);
    toc.setWaterDepth(waterDepth);
    toc.setSideSlope(sideSlope);
    toc.setManningRoughness(manning);

    if (toc.analyze()) {
      printLine("Analysis");
      printLine("Discharge = " + toc.getDischarge());
      printLine("Average velocity = " + toc.getAverageVelocity());
      printLine("Wetted area = " + toc.getWettedArea());
      printDash();
      printLine("Critical depth = " + toc.getCriticalDepth());
      printLine("Froude number = " + toc.getFroudeNumber());
      printLine("Flow Type = " + toc.getFlowType());
      printLine("Critical Slope = " + toc.getCriticalSlope());
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

// Start of output
Circular open channel
= = = = = = = = = = = = = =
Discharge = 0.687263130787826
Water depth = 0.7
Wetted perimeter = 2.085893287217503
Critical depth = 0.44559999999996724
Froude number = 0.42106818161340415
Critical slope = 0.004793932865164748
// End of output
```

Below describes the proposed content of the project:

### Todos:

#### Libraries:
**Open Channel Flow**
---
- [x]  Rectangular Channel
- [x]  Trapezoidal Channel
- [x]  Circular Channel
- [ ]  Irregular Section Channel

**Weirs**
---
- [ ]  Rectangular Weir
- [ ]  Triangular Weir
- [ ]  Sharp-crested Weir
- [ ]  Broad-crested Weir
- [ ]  Ogee Weir

**Dams**
---
- [ ]  Dam Reservoir Operation Study

**Hydrology**
---
- [ ]  Regional Flood Frequency Analysis

#### GUI
**Open Channel Flow**
---
- [ ]  Rectangular Channel
- [ ]  Trapezoidal Channel
- [ ]  Circular Channel
- [ ]  Irregular Section Channel

**Weirs/Dams**
---
- [ ]  Rectangular Weir
- [ ]  Triangular Weir
- [ ]  Sharp-crested Weir
- [ ]  Broad-crested Weir
- [ ]  Ogee Weir
