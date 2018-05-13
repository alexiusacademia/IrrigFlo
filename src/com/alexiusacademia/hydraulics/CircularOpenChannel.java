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
    DIAMETER
  }

  // Pipe internal diameter
  private double diameter;

  // Pipe unknown
  private Unknown unknown;

  // Calculated discharge, must always be reset
  private double calculatedDischarge;

  // Almost full (More than half full)
  private boolean almostFull;

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
      }
      solveForCriticalFlow();
      return this.isCalculationSuccessful;
    }

    return false;
  }

  /**
   * Solve for the unknown bed slope
   */
  private void solveForBedSlope() {
    // Shorten the variables
    double h = this.waterDepth;
    double d = this.diameter;
    double tetha;

    // Make sure bed slope starts at zero
    this.bedSlope = 0;

    calculatedDischarge = 0.0;

    while (calculatedDischarge < this.discharge) {
      this.bedSlope += 0.0000001;
      this.almostFull = (h >= (d / 2));
      // Calculate tetha
      if (this.almostFull) {
        tetha = 2 * Math.acos((2 * h - d)/d) * 180 / Math.PI;
      } else {
        tetha = 2 * Math.acos((d - 2 * h)/d) * 180 / Math.PI;
      }

      // Calculate area of triangle
      double aTri;
      aTri = Math.pow(d, 2) * Math.sin(tetha * Math.PI / 180) / 8;

      // Calculate rea of sector
      double aSec;
      if (this.almostFull) {
        aSec = Math.PI * Math.pow(d, 2) * (360 - tetha) / 1440;
        this.wettedArea = aSec + aTri;
        this.wettedPerimeter = Math.PI * d * (360 - tetha) / 360;
      } else {
        aSec = tetha * Math.PI * Math.pow(d, 2) / 1440;
        this.wettedArea = aSec - aTri;
        this.wettedPerimeter = Math.PI * d * tetha / 360;
      }

      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
              Math.pow(this.hydraulicRadius, (2.0/ 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.isCalculationSuccessful = true;
  }

  /**
   * Solve for the unknown diameter
   */
  private void solveForDiameter() {
    // Shorten the variables
    double h = this.waterDepth;
    double d = h;
    double tetha;

    calculatedDischarge = 0.0;

    while (calculatedDischarge < this.discharge) {
      d += 0.00001;
      this.almostFull = (h >= (d / 2));
      // Calculate tetha
      if (this.almostFull) {
        tetha = 2 * Math.acos((2 * h - d)/d) * 180 / Math.PI;
      } else {
        tetha = 2 * Math.acos((d - 2 * h)/d) * 180 / Math.PI;
      }

      // Calculate area of triangle
      double aTri;
      aTri = Math.pow(d, 2) * Math.sin(tetha * Math.PI / 180) / 8;

      // Calculate rea of sector
      double aSec;
      if (this.almostFull) {
        aSec = Math.PI * Math.pow(d, 2) * (360 - tetha) / 1440;
        this.wettedArea = aSec + aTri;
        this.wettedPerimeter = Math.PI * d * (360 - tetha) / 360;
      } else {
        aSec = tetha * Math.PI * Math.pow(d, 2) / 1440;
        this.wettedArea = aSec - aTri;
        this.wettedPerimeter = Math.PI * d * tetha / 360;
      }

      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
              Math.pow(this.hydraulicRadius, (2.0/ 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }
    this.diameter = d;

    this.isCalculationSuccessful = true;
  }

  /**
   * Solve for discharge
   */
  private void solveForDischarge() {
    // Shorten the variables
    double h = this.waterDepth;
    double d = this.diameter;
    double tetha;

    this.almostFull = (h >= (d / 2));

    // Calculate tetha
    if (this.almostFull) {
      tetha = 2 * Math.acos((2 * h - d)/d) * 180 / Math.PI;
    } else {
      tetha = 2 * Math.acos((d - 2 * h)/d) * 180 / Math.PI;
    }

    // Calculate area of triangle
    double aTri;
    aTri = Math.pow(d, 2) * Math.sin(tetha * Math.PI / 180) / 8;

    // Calculate rea of sector
    double aSec;
    if (this.almostFull) {
      aSec = Math.PI * Math.pow(d, 2) * (360 - tetha) / 1440;
      this.wettedArea = aSec + aTri;
      this.wettedPerimeter = Math.PI * d * (360 - tetha) / 360;
    } else {
      aSec = tetha * Math.PI * Math.pow(d, 2) / 1440;
      this.wettedArea = aSec - aTri;
      this.wettedPerimeter = Math.PI * d * tetha / 360;
    }

    this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;

    this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
            Math.pow(this.hydraulicRadius, (2.0/ 3.0));
    this.discharge = this.averageVelocity * this.wettedArea;

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

  }
}
