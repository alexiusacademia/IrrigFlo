package com.alexiusacademia.hydraulics;

/**
 * Trapezoidal open channel
 * An extension of OpenChannel class
 * For trapezoidal shaped channels
 */
public class TrapezoidalOpenChannel extends OpenChannel {

  /* **********************************
   * Properties
   ***********************************/
  // Unknown class
  public enum Unknown {
    DISCHARGE, BED_SLOPE, WATER_DEPTH, BASE_WIDTH
  }

  // Width at the bottom (smallest width) of the section
  private double baseWidth;

  // Unknown from the enum Unknown
  private Unknown unknown;

  // Sideslope, assumed equal on both sides
  private double sideSlope;

  // Temporary discharge, calculated, must be reset always
  private double calculatedDischarge;

  /**
   * Creates an empty {@code TrapezoidalOpenChannel}
   */
  public TrapezoidalOpenChannel() {
    // Set the default unknown if not set
    this.unknown = Unknown.DISCHARGE;
  }

  /**
   * Creates a {@code TrapezoidalOpenChannel} with given parameters.
   * @param unknown The unknown from the enum Unknown
   * @param bedSlope The channel slope.
   * @param baseWidth The channel width at the bottom.
   * @param waterDepth The depth of the water.
   * @param sideSlope The side slope on both sides
   * @param manningRoughness The Manning's roughness coefficient.
   */
  public TrapezoidalOpenChannel(Unknown unknown, double bedSlope, double baseWidth, double waterDepth,
                                double sideSlope, double manningRoughness) {
    this.unknown = unknown;
    this.bedSlope = bedSlope;
    this.baseWidth = baseWidth;
    this.waterDepth = waterDepth;
    this.sideSlope = sideSlope;
    this.manningRoughness = manningRoughness;
  }

  /* **********************************
   * Setters
   ***********************************/

  /**
   * Sets the unknown.
   * @param unknown The unknown from the enum Unknown.
   */
  public void setUnknown(Unknown unknown) {
    this.unknown = unknown;
  }

  /**
   * Sets the bottom width of the trapezoidal channel.
   * @param baseWidth The channel width at the bottom.
   */
  public void setBaseWidth(double baseWidth) {
    this.baseWidth = baseWidth;
  }

  /**
   * Sets the sideslope on both sides.
   * @param sideSlope The side slope on both sides
   */
  public void setSideSlope(double sideSlope) {
    this.sideSlope = sideSlope;
  }

  /* **********************************
   * Getters
   ***********************************/

  public double getBaseWidth() {
    return baseWidth;
  }

  public Unknown getUnknown() {
    return unknown;
  }

  public double getSideSlope() {
    return sideSlope;
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
        case BED_SLOPE:
          solveForBedSlope();
          break;
        case WATER_DEPTH:
          solveForWaterDepth();
          break;
        case BASE_WIDTH:
          solveForBaseWidth();
          break;
      }
    }
    return false;
  }

  /**
   * Solve for the unknown base width
   */
  private void solveForBaseWidth() {
    calculatedDischarge = 0.0;
    double trialBaseWidth = 0.0;

    while (calculatedDischarge < this.discharge) {
      trialBaseWidth += 0.0001;
      this.wettedArea = (trialBaseWidth + this.waterDepth * this.sideSlope) * this.waterDepth;
      this.wettedPerimeter = 2 * this.waterDepth * Math.sqrt(Math.pow(this.sideSlope,2) + 1) + trialBaseWidth;
      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
              Math.pow(this.hydraulicRadius, (2.0/3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.baseWidth = trialBaseWidth;
  }

  /**
   * Solve for the unknown water depth
   */
  private void solveForWaterDepth() {
    calculatedDischarge = 0.0;
    double trialWaterDepth = 0.0;

    while (calculatedDischarge < this.discharge) {
      trialWaterDepth += 0.0001;
      this.wettedArea = (this.baseWidth + trialWaterDepth * this.sideSlope) * trialWaterDepth;
      this.wettedPerimeter = 2 * trialWaterDepth * Math.sqrt(Math.pow(this.sideSlope,2) + 1) + this.baseWidth;
      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
              Math.pow(this.hydraulicRadius, (2.0/3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.waterDepth = trialWaterDepth;
  }


  /**
   * Solve for the unknown bed slope
   */
  private void solveForBedSlope() {
    calculatedDischarge = 0.0;
    double trialSlope = 0.0;

    while (calculatedDischarge < this.discharge) {
      trialSlope += 0.00000001;
      this.wettedArea = (this.baseWidth + this.waterDepth * this.sideSlope) * this.waterDepth;
      this.wettedPerimeter = 2 * this.waterDepth * Math.sqrt(Math.pow(this.sideSlope,2) + 1) + this.baseWidth;
      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(trialSlope) *
              Math.pow(this.hydraulicRadius, (2.0/3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.bedSlope = trialSlope;
  }

  /**
   * Solve for the unknown discharge
   */
  private void solveForDischarge() {
    this.wettedArea = (this.baseWidth + this.waterDepth * this.sideSlope) * this.waterDepth;
    this.wettedPerimeter = 2 * this.waterDepth * Math.sqrt(Math.pow(this.sideSlope,2) + 1) + this.baseWidth;
    this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
    this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
            Math.pow(this.hydraulicRadius, (2.0/3.0));
    this.discharge = this.averageVelocity * this.wettedArea;
  }

  /**
   * Checks whether all inputs are valid.
   * @return Boolean Returns true if all the inputs are valid, false otherwise
   */
  private boolean isValidInputs() {
    try {
      if (this.manningRoughness <= 0) {
        throw new InvalidValueException("Manning's roughness must be greater than zero.");
      }
      if (this.unknown != Unknown.DISCHARGE) {
        if (this.discharge <= 0) {
          throw new InvalidValueException("Discharge must be greater than zero.");
        }
      }
      if (this.unknown != Unknown.BED_SLOPE) {
        if (this.bedSlope <= 0) {
          throw new InvalidValueException("Bed slope must not be flat or less than zero.");
        }
      }
      if (this.unknown != Unknown.BASE_WIDTH) {
        if (this.baseWidth == 0) {
          throw new DimensionException("Base width must be greater than zero.");
        } else if (this.baseWidth < 0) {
          throw new DimensionException("Invalid base width dimension.");
        }
      }
      if (this.unknown != Unknown.WATER_DEPTH) {
        if (this.waterDepth == 0) {
          throw new DimensionException("Water depth must be greater than zero.");
        } else if (this.waterDepth < 0) {
          throw new DimensionException("Invalid depth of water.");
        }
      }
      if (this.sideSlope < 0) {
        throw new DimensionException("Side slope value should be positive.");
      }

    } catch (InvalidValueException | DimensionException e) {
      this.isCalculationSuccessful = false;
      this.errMessage = e.getMessage();
      return false;
    }

    // Return true if all exceptions are passed
    return true;
  }
}
