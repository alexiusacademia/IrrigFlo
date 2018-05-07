package com.alexiusacademia.hydraulics;

public class OpenChannel {

  protected double discharge;
  protected double bedSlope;
  protected float waterDepth;
  protected float manningRoughness;
  protected float wettedPerimeter;
  protected float wettedArea;
  protected float hydraulicRadius;
  protected double averageVelocity;
  protected float froudeNumber;
  protected String flowType;

  public OpenChannel() {

  }

  /** ****************************************
   * Getters
   ***************************************** */

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