package com.alexiusacademia.hydraulics;

import com.alexiusacademia.hydraulics.OpenChannel;

public class RectangularOpenChannel extends OpenChannel {
  public enum Unknown {
    DISCHARGE,
    BED_SLOPE,
    WATER_DEPTH,
    BASE_WIDTH
  }
  /** *********************************
   * Properties
   ********************************** */
  protected float baseWidth;
  protected Unknown unknown;

  /** *********************************
   * Setters
   ********************************** */
  public void setBaseWidth(float baseWidth) {
    this.baseWidth = baseWidth;
  }

  public void setUnknown(Unknown unknown) {
    this.unknown = unknown;
  }

  /** *********************************
   * Getters
   ********************************** */
  public float getBaseWidth() {
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
    }
  }

  private void solveForDischarge() {
    float wettedArea = this.baseWidth * this.waterDepth;
    float wettedPerimeter = this.baseWidth + 2 * this.waterDepth;
    float hydraulicRadius = wettedArea / wettedPerimeter;
    double averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) * Math.pow(hydraulicRadius, (2.0/3.0));
    double discharge = averageVelocity * wettedArea;

    this.discharge = discharge;
    this.wettedArea = wettedArea;
    this.wettedPerimeter = wettedPerimeter;
    this.averageVelocity = averageVelocity;
    this.hydraulicRadius = hydraulicRadius;
  }

}
