package com.alexiusacademia.hydraulics;

public class OpenChannel {

  // Unknowns
  public enum OpenChannelUnknown {
    DISCHARGE
  }
  protected double discharge;
  protected double bedSlope;
  protected float waterDepth;
  protected float manningRoughness;
  protected float wettedPerimeter;
  protected float wettedArea;
  protected float hydraulicRadius;
  protected float froudeNumber;
  protected String flowType;
  protected OpenChannelUnknown unknown;

  public OpenChannel(OpenChannelUnknown unknown) {
    this.unknown = unknown;
  }

  /** ****************************************
   * Getters
   ***************************************** */
  public String getUnknown() {
    switch (this.unknown) {
      case DISCHARGE:
        return "Discharge";
    }
    return "";
  }

  public double getDischarge() {
    return discharge;
  }

  public double getBedSlope() {
    return bedSlope;
  }

  public float getWaterDepth() {
    return waterDepth;
  }

  public float getWettedPerimeter() {
    return wettedPerimeter;
  }

  public float getWettedArea() {
    return wettedArea;
  }

  public float getHydraulicRadius() {
    return hydraulicRadius;
  }

  public float getFroudeNumber() {
    return froudeNumber;
  }

  public String getFlowType() {
    return flowType;
  }

  /** ***************************************
   * Setters
   **************************************** */
  public void setUnknown(OpenChannelUnknown unknown) {
    this.unknown = unknown;
  }

  public void setBedSlope(double bedSlope) {
    this.bedSlope = bedSlope;
  }

  public void setDischarge(double discharge) {
    this.discharge = discharge;
  }

  public void setWaterDepth(float waterDepth) {
    this.waterDepth = waterDepth;
  }

  public void setManningRoughness(float manningRoughness) {
    this.manningRoughness = manningRoughness;
  }
}