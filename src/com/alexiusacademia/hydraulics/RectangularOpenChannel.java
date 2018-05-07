package com.alexiusacademia.hydraulics;

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

  /** *********************************
   * Methods
   ********************************** */
  public void analyze() {
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
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(trialSlope) * Math.pow(this.hydraulicRadius, (2.0/3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.bedSlope = trialSlope;
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
