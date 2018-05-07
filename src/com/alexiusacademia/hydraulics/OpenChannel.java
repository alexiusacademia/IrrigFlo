package com.alexiusacademia.hydraulics;

public class OpenChannel {

  protected double discharge;
  protected double bedSlope;
  protected double waterDepth;
  protected double manningRoughness;
  protected double wettedPerimeter;
  protected double wettedArea;
  protected double hydraulicRadius;
  protected double averageVelocity;
  protected double froudeNumber;
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

  public double getWaterDepth() {
    return waterDepth;
  }

  public double getWettedPerimeter() {
    return wettedPerimeter;
  }

  public double getWettedArea() {
    return wettedArea;
  }

  public double getHydraulicRadius() {
    return hydraulicRadius;
  }

  public double getFroudeNumber() {
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