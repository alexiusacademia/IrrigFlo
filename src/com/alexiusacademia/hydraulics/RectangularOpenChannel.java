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
  protected double baseWidth;

  /**
   * The unknown for the flow calculation.
   * Type is derived from the enum Unknown.
   */
  protected Unknown unknown;

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
   * @param baseWidth Bottom width or channel width for rectangular sections.
   */
  public void setBaseWidth(double baseWidth) {
    this.baseWidth = baseWidth;
  }

  /**
   * Set the unknown for the channel.
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
   * @return baseWidth
   */
  public double getBaseWidth() {
    return baseWidth;
  }

  /**
   * Check if an error has occurred.
   * @return isError
   */
  public boolean isCalculationSuccessful() {
    return isCalculationSuccessful;
  }

  /**
   * Gets the error message.
   * @return errMessage
   */
  public String getErrMessage() {
    return errMessage;
  }

  /** *********************************
   * Methods
   ********************************** */
  public boolean analyze() {
    // Get the unknown
    switch (this.unknown) {
      case DISCHARGE:
        solveForDischarge();
      case WATER_DEPTH:
        solveForWaterDepth();
      case BASE_WIDTH:
        solveForBaseWidth();
      case BED_SLOPE:
        solveForBedSlope();
    }

    return this.isCalculationSuccessful;
  }

  /**
   * Solve for the unknown bed slope
   */
  private void solveForBedSlope() {
    double calculatedDischarge = 0.0;
    double trialSlope = 0.0;

    try {
      if (this.baseWidth <= 0.0) {
        throw new DimensionException("Base width must be greater than zero!");
      }
      if (this.manningRoughness <= 0.0) {
        throw new InvalidValueException("Roughness coefficient must be a positive value.");
      }

      while (calculatedDischarge < this.discharge) {
        trialSlope += 0.00000001;
        this.wettedArea = this.baseWidth * this.waterDepth;
        this.wettedPerimeter = this.baseWidth + 2 * this.waterDepth;
        this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
        this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(trialSlope) * Math.pow(this.hydraulicRadius, (2.0/3.0));
        calculatedDischarge = this.averageVelocity * this.wettedArea;
      }

      this.bedSlope = trialSlope;

      this.isCalculationSuccessful = true;

    } catch (DimensionException ex) {
      this.isCalculationSuccessful = false;
      this.errMessage = ex.getMessage();
    } catch (InvalidValueException ex) {
      this.isCalculationSuccessful = false;
      this.errMessage = ex.getMessage();
    }
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
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) * Math.pow(this.hydraulicRadius, (2.0/3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

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
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) * Math.pow(this.hydraulicRadius, (2.0/3.0));
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
    this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) * Math.pow(this.hydraulicRadius, (2.0/3.0));
    this.discharge = this.averageVelocity * this.wettedArea;

  }

}
