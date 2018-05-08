package com.alexiusacademia.hydraulics;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

public class RectangularOpenChannel extends OpenChannel {
  /**
   * Unknowns
   */
  public enum Unknown {
    DISCHARGE,
    BED_SLOPE,
    WATER_DEPTH,
    BASE_WIDTH
  }

  /** *********************************
   * Properties
   ********************************** */

  /**
   * Base width or the channel width for rectangular sections.
   */
  private double baseWidth;

  /**
   * The unknown for the flow calculation.
   * Type is derived from the enum Unknown.
   */
  private Unknown unknown;

  /**
   * Handle if calculation error or exception occurs
   */
  private boolean isCalculationSuccessful;

  /**
   * Describe message about an error.
   */
  private String errMessage;


  /** *********************************
   * Setters
   ********************************** */

  /**
   * Set the bottom width of the channel.
   *
   * @param baseWidth Bottom width or channel width for rectangular sections.
   */
  public void setBaseWidth(double baseWidth) {
    this.baseWidth = baseWidth;
  }

  /**
   * Set the unknown for the channel.
   *
   * @param unknown
   */
  public void setUnknown(Unknown unknown) {
    this.unknown = unknown;
  }

  /** *********************************
   * Getters
   ********************************** */

  /**
   * Gets the bottom width or channel width.
   *
   * @return baseWidth
   */
  public double getBaseWidth() {
    return baseWidth;
  }

  /**
   * Check if an error has occurred.
   *
   * @return isError
   */
  public boolean isCalculationSuccessful() {
    return isCalculationSuccessful;
  }

  /**
   * Gets the error message.
   *
   * @return errMessage
   */
  public String getErrMessage() {
    return errMessage;
  }

  public Unknown getUnknown() {
    return unknown;
  }

  /**
   * ********************************
   * Methods
   * *********************************
   */
  public boolean analyze() {
    if (isValidInputs()) {
      // Get the unknown
      switch (this.unknown) {
        case DISCHARGE:
          solveForDischarge();
          break;
        case WATER_DEPTH:
          solveForWaterDepth();
          break;
        case BASE_WIDTH:
          solveForBaseWidth();
          break;
        case BED_SLOPE:
          solveForBedSlope();
          break;
      }
      return this.isCalculationSuccessful;
    }
    return false;
  }

  /**
   * Solve for the unknown bed slope
   */
  private void solveForBedSlope() {
    double calculatedDischarge = 0.0;
    double trialSlope = 0.0;

    while (calculatedDischarge < this.discharge) {
      trialSlope += 0.00000001;
      this.wettedArea = this.baseWidth * this.waterDepth;
      this.wettedPerimeter = this.baseWidth + 2 * this.waterDepth;
      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(trialSlope) * Math.pow(this.hydraulicRadius, (2.0 / 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.bedSlope = trialSlope;

    this.isCalculationSuccessful = true;

  }

  /**
   * Solve for the unknown base width
   */
  private void solveForBaseWidth() {
    double calculatedDischarge = 0.0;
    double trialBaseWidth = 0.0;

    while (calculatedDischarge < this.discharge) {
      trialBaseWidth += 0.0001;
      this.wettedArea = trialBaseWidth * this.waterDepth;
      this.wettedPerimeter = trialBaseWidth + 2 * this.waterDepth;
      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) * Math.pow(this.hydraulicRadius, (2.0 / 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.isCalculationSuccessful = true;

    this.baseWidth = trialBaseWidth;
  }

  /**
   * Solve for the unknown water depth
   */
  private void solveForWaterDepth() {
    double calculatedDischarge = 0.0;
    double trialWaterDepth = 0.0;

    while (calculatedDischarge < this.discharge) {
      trialWaterDepth += 0.0001;
      this.wettedArea = this.baseWidth * trialWaterDepth;
      this.wettedPerimeter = this.baseWidth + 2 * trialWaterDepth;
      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) * Math.pow(this.hydraulicRadius, (2.0 / 3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.waterDepth = trialWaterDepth;
  }

  /**
   * Solve for the unknown discharge
   */
  private void solveForDischarge() {

    this.wettedArea = this.baseWidth * this.waterDepth;
    this.wettedPerimeter = this.baseWidth + 2 * this.waterDepth;
    this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
    this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) * Math.pow(this.hydraulicRadius, (2.0 / 3.0));
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

    } catch (InvalidValueException | DimensionException e) {
      this.isCalculationSuccessful = false;
      this.errMessage = e.getMessage();
      return false;
    }

    // Return true if all exceptions are passed
    return true;
  }

}