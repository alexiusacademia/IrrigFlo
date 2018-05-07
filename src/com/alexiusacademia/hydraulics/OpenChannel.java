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

  public void setWaterDepth(double waterDepth) {
    this.waterDepth = waterDepth;
  }

  public void setManningRoughness(double manningRoughness) {
    this.manningRoughness = manningRoughness;
  }
}

/**
 * A custom exception for invalid dimensions.
 */
class DimensionException extends Exception {
  /**
   * Constructs a {@code DimensionException} with no parameter.
   */
  public DimensionException() {

  }

  /**
   * Construct a {@code DimensionException} with a message parameter.
   * @param message A string description of the exception.
   */
  public DimensionException(String message) {
    super(message);
  }
}

/**
 * A custom exception for handling illegal or invalid values or constants.
 */
class InvalidValueException extends Exception {
  /**
   * Construct a {@code InvalidValueException} with no parameter.
   */
  public InvalidValueException() {};

  /**
   * Construct a {@code InvalidValueException} with a message parameter.
   * @param message A string description of the exception.
   */
  public InvalidValueException(String message){
    super(message);
  };
}