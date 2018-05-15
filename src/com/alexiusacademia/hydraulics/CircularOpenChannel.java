/**
 * Deals with circular sections that are not flowing with its full diameter.
 */
package com.alexiusacademia.hydraulics;

public class CircularOpenChannel extends OpenChannel {
  /* **********************************
   * Properties
   ***********************************/
  // Unknown class
  public enum Unknown {
    DISCHARGE,
    BED_SLOPE,
    DIAMETER,
    WATER_DEPTH
  }

  // Pipe internal diameter
  private double diameter;

  // Pipe unknown
  private Unknown unknown;

  // Calculated discharge, must always be reset
  private double calculatedDischarge;

  // Almost full (More than half full)
  private boolean almostFull;

  // Area of triangle in the section of circular channel
  private double triangleArea;

  /**
   * Creates a {@code CircularOpenChannel} with default unknown as discharge.
   */
  public CircularOpenChannel() {
    this.unknown = Unknown.DISCHARGE;
  }

  /**
   * Creates a {@code CircularOpenChannel} with a given unknown from the user.
   * @param unknown The unknown from the enum Unknown
   */
  public CircularOpenChannel(Unknown unknown) {
    this.unknown = unknown;
  }

  /* **********************************
   * Setters
   ***********************************/

  /**
   * Sets the pipe internal diameter
   * @param diameter Pipe internal diameter
   */
  public void setDiameter(double diameter) {
    this.diameter = diameter;
  }

  public void setUnknown(Unknown unknown) {
    this.unknown = unknown;
  }

  /* **********************************
   * Getters
   ***********************************/

  /**
   * Returns the pipe diameter
   * @return Pipe diameter
   */
  public double getDiameter() {
    return diameter;
  }

  /**
   * Returns the unknown element
   * @return Unknown
   */
  public Unknown getUnknown() {
    return unknown;
  }

  /**
   * Returns true if almost full
   * @return Boolean almostFull
   */
  public boolean isAlmostFull() {
    return almostFull;
  }

  /* ********************************
   * Methods
   **********************************/
  public boolean analyze() {

    if (isValidInputs()) {
      switch (this.unknown) {
        case DISCHARGE:
          solveForDischarge();
          break;
        case DIAMETER:
          solveForDiameter();
          break;
        case BED_SLOPE:
          solveForBedSlope();
          break;
        case WATER_DEPTH:
          solveForWaterDepth();
          break;
      }
      solveForCriticalFlow();
      return this.isCalculationSuccessful;
    }

    return false;
  }

  /**
   * Solve for the unknown water depth
   */
  private void solveForWaterDepth() {
    // Shorten the variables
    double h = 0;       // Water depth
    double d = this.diameter;
    double theta;
    double aTri = 0;    // Area of triangle
    double aSec;        // Area of sector

    calculatedDischarge = 0.0;

    while (calculatedDischarge < this.discharge) {
      h += 0.0001;
      this.almostFull = (h >= (d / 2));
      // Calculate theta
      if (this.almostFull) {
        theta = 2 * Math.acos((2 * h - d)/d) * 180 / Math.PI;
      } else {
        theta = 2 * Math.acos((d - 2 * h)/d) * 180 / Math.PI;
      }

      // Calculate area of triangle
      aTri = Math.pow(d, 2) * Math.sin(theta * Math.PI / 180) / 8;

      // Calculate rea of sector
      if (this.almostFull) {
        aSec = Math.PI * Math.pow(d, 2) * (360 - theta) / 1440;
        this.wettedArea = aSec + aTri;
        this.wettedPerimeter = Math.PI * d * (360 - theta) / 360;
      } else {
        aSec = theta * Math.PI * Math.pow(d, 2) / 1440;
        this.wettedArea = aSec - aTri;
        this.wettedPerimeter = Math.PI * d * theta / 360;
      }

      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
              Math.pow(this.hydraulicRadius, (2.0/ 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.triangleArea = aTri;
    this.waterDepth = h;
    this.isCalculationSuccessful = true;
  }

  /**
   * Solve for the unknown bed slope
   */
  private void solveForBedSlope() {
    // Shorten the variables
    double h = this.waterDepth;
    double d = this.diameter;
    double theta;
    double aTri = 0;    // Area of triangle
    double aSec;        // Area of sector

    // Make sure bed slope starts at zero
    this.bedSlope = 0;

    calculatedDischarge = 0.0;

    while (calculatedDischarge < this.discharge) {
      this.bedSlope += 0.0000001;
      this.almostFull = (h >= (d / 2));
      // Calculate theta
      if (this.almostFull) {
        theta = 2 * Math.acos((2 * h - d)/d) * 180 / Math.PI;
      } else {
        theta = 2 * Math.acos((d - 2 * h)/d) * 180 / Math.PI;
      }

      // Calculate area of triangle
      aTri = Math.pow(d, 2) * Math.sin(theta * Math.PI / 180) / 8;

      // Calculate rea of sector
      if (this.almostFull) {
        aSec = Math.PI * Math.pow(d, 2) * (360 - theta) / 1440;
        this.wettedArea = aSec + aTri;
        this.wettedPerimeter = Math.PI * d * (360 - theta) / 360;
      } else {
        aSec = theta * Math.PI * Math.pow(d, 2) / 1440;
        this.wettedArea = aSec - aTri;
        this.wettedPerimeter = Math.PI * d * theta / 360;
      }

      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
              Math.pow(this.hydraulicRadius, (2.0/ 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }
    this.triangleArea = aTri;
    this.isCalculationSuccessful = true;
  }

  /**
   * Solve for the unknown diameter
   */
  private void solveForDiameter() {
    // Shorten the variables
    double h = this.waterDepth;
    double d = h;
    double theta;
    double aTri = 0;    // Area of triangle
    double aSec;        // Area of sector

    calculatedDischarge = 0.0;

    while (calculatedDischarge < this.discharge) {
      d += 0.00001;
      this.almostFull = (h >= (d / 2));
      // Calculate theta
      if (this.almostFull) {
        theta = 2 * Math.acos((2 * h - d)/d) * 180 / Math.PI;
      } else {
        theta = 2 * Math.acos((d - 2 * h)/d) * 180 / Math.PI;
      }

      // Calculate area of triangle
      aTri = Math.pow(d, 2) * Math.sin(theta * Math.PI / 180) / 8;

      // Calculate rea of sector
      if (this.almostFull) {
        aSec = Math.PI * Math.pow(d, 2) * (360 - theta) / 1440;
        this.wettedArea = aSec + aTri;
        this.wettedPerimeter = Math.PI * d * (360 - theta) / 360;
      } else {
        aSec = theta * Math.PI * Math.pow(d, 2) / 1440;
        this.wettedArea = aSec - aTri;
        this.wettedPerimeter = Math.PI * d * theta / 360;
      }

      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
              Math.pow(this.hydraulicRadius, (2.0/ 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }
    this.diameter = d;
    this.triangleArea = aTri;
    this.isCalculationSuccessful = true;
  }

  /**
   * Solve for discharge
   */
  private void solveForDischarge() {
    // Shorten the variables
    double h = this.waterDepth;
    double d = this.diameter;
    double theta;
    double aTri = 0;    // Area of triangle
    double aSec;        // Area of sector

    this.almostFull = (h >= (d / 2));

    // Calculate theta
    if (this.almostFull) {
      theta = 2 * Math.acos((2 * h - d)/d) * 180 / Math.PI;
    } else {
      theta = 2 * Math.acos((d - 2 * h)/d) * 180 / Math.PI;
    }

    // Calculate area of triangle
    aTri = Math.pow(d, 2) * Math.sin(theta * Math.PI / 180) / 8;

    // Calculate rea of sector
    if (this.almostFull) {
      aSec = Math.PI * Math.pow(d, 2) * (360 - theta) / 1440;
      this.wettedArea = aSec + aTri;
      this.wettedPerimeter = Math.PI * d * (360 - theta) / 360;
    } else {
      aSec = theta * Math.PI * Math.pow(d, 2) / 1440;
      this.wettedArea = aSec - aTri;
      this.wettedPerimeter = Math.PI * d * theta / 360;
    }

    this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
    this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
            Math.pow(this.hydraulicRadius, (2.0/ 3.0));
    this.discharge = this.averageVelocity * this.wettedArea;
    this.triangleArea = aTri;
    this.isCalculationSuccessful = true;
  }

  /**
   * Checks whether all inputs are valid.
   * @return Boolean Returns true if all the inputs are valid, false otherwise
   */
  private boolean isValidInputs() {

    try {
      if (this.unknown != Unknown.DIAMETER) {
        if (this.waterDepth >= this.diameter) {
          throw new DimensionException("Water depth must be less than the pipe diameter.");
        }
      }
    } catch (Exception ex) {
      this.isCalculationSuccessful = false;
      this.errMessage = ex.getMessage();
      return false;
    }
    return true;
  }

  /**
   * Solve for critical flow properties (e.g. critical depth, froude number, flow type ...)
   */
  private void solveForCriticalFlow() {
    // Q^2 / g
    double Q2g = Math.pow(this.discharge, 2) / this.GRAVITY_METRIC;

    // Other side of equation
    double tester = 0;

    // Critical depth
    double yc = 0;

    // Critical area, perimeter, hydraulic radius, critical slope
    double Ac = 0, Pc = 0, Rc, Sc;

    // Top width
    double T;

    // Angle of water edges from the center
    double theta = 0;

    // Triangle at critical flow
    double aTriC;

    // Sector at critical flow
    double aSecC;

    while (tester < Q2g) {
      yc += 0.00001;

      T = solveForTopWidth(yc);

      // Calculate theta
      if (yc > (this.diameter/2)) {
        // Almost full
        theta = 2 * Math.acos((2 * yc - this.diameter) / this.diameter) * 180 / Math.PI;
      } else {
        // Less than half full
        theta = 2 * Math.acos((this.diameter - 2 * yc) / this.diameter) * 180 / Math.PI;
      }

      // Calculate area of triangle
      aTriC = Math.pow(this.diameter, 2) * Math.sin(theta * Math.PI / 180) / 8;

      // Calculate area of sector
      if (yc > (this.diameter/2)) {
        aSecC = Math.PI * Math.pow(this.diameter, 2) * (360 - theta) / 1440;
        Ac = aSecC + aTriC;
        Pc = Math.PI * this.diameter * (360 - theta) / 360;
      } else {
        aSecC = theta * Math.PI * Math.pow(this.diameter, 2) / 1440;
        Ac = aSecC - aTriC;
        Pc = Math.PI * this.diameter * theta / 360;
      }
      // Compare the equation
      tester = Math.pow(Ac, 3) / T;
    }
    
    // Pass to global variable
    this.criticalDepth = yc;

    // Hydraulic radius at critical flow
    Rc = Ac / Pc;

    Sc = Math.pow(this.discharge / (Ac * Math.pow(Rc, (2.0/3.0))) * this.manningRoughness, 2);
    this.criticalSlope = Sc;

    // Solve for froude number
    this.hydraulicDepth = this.wettedArea / solveForTopWidth(this.waterDepth);
    this.froudeNumber = this.averageVelocity / Math.sqrt(this.GRAVITY_METRIC * this.hydraulicDepth);

    // Select the flow type
    this.flowType();
  }

  /**
   * Solves for water top width in circular channels.
   * @param y Water depth
   * @return Double Top width
   */
  private double solveForTopWidth(double y) {
    double topWidth;
    double triangleHeight;

    if (this.almostFull) {
      triangleHeight = y - this.diameter/2;
    } else {
      triangleHeight = this.diameter/2 - y;
    }
    topWidth = 2 * this.triangleArea / triangleHeight;

    return topWidth;
  }
}
