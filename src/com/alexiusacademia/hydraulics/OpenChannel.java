package com.alexiusacademia.hydraulics;

import java.io.Serializable;

public class OpenChannel implements Serializable {

  /** ****************************************
   * Constants
   ***************************************** */
  protected final double GRAVITY_METRIC = 9.81;
  protected final double DEPTH_TRIAL_INCREMENT = 0.00001;
  protected final double SLOPE_TRIAL_INCREMENT = 0.0000001;

  /** ****************************************
   * Properties
   ***************************************** */
  public enum FlowType {
    CRITICAL,
    SUBCRITICAL,
    SUPERCRITICAL
  }

  /** Channel flow rate */
  protected double discharge;

  /** Channel bed slope */
  protected double bedSlope;

  /** Channel water depth */
  protected double waterDepth;

  /** Manning's roughness coefficient */
  protected double manningRoughness;

  /** Length of wet in the section */
  protected double wettedPerimeter;

  /** Cross sectional area of water */
  protected double wettedArea;

  /** Higher hydraulic radius means higher flow velocity */
  protected double hydraulicRadius;

  /** The average velocity of a channel */
  protected double averageVelocity;

  /** Froude number */
  protected double froudeNumber;

  /** Flow type defined in enum {@code FlowType} */
  protected FlowType flowType;

  /** Properties for critical flow calculation */
  protected double hydraulicDepth;
  protected double dischargeIntensity;
  protected double criticalDepth;
  protected double criticalSlope;

  /** Handle if calculation error or exception occurs */
  protected boolean isCalculationSuccessful;

  /** Describe message about an error. */
  protected String errMessage;

  /** Creates a parameterless instance of OpenChannel. */
  public OpenChannel() {

  }

  /** ****************************************
   * Getters
   ***************************************** */

  public double getDischarge() {
    return discharge;
  }

  public double getAverageVelocity() {
    return averageVelocity;
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

  public double getManningRoughness() {
    return manningRoughness;
  }

  public FlowType getFlowType() {
    return flowType;
  }

  public double getHydraulicDepth() {
    return hydraulicDepth;
  }

  public double getDischargeIntensity() {
    return dischargeIntensity;
  }

  public double getCriticalDepth() {
    return criticalDepth;
  }

  public double getCriticalSlope() {
    return criticalSlope;
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

  /**
   * Methods
   */
  protected void flowType() {
    // Flow type
    if (this.froudeNumber == 1) {
      this.flowType = FlowType.CRITICAL;
    } else if (this.froudeNumber < 1) {
      this.flowType = FlowType.SUBCRITICAL;
    } else {
      this.flowType = FlowType.SUPERCRITICAL;
    }
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
  public InvalidValueException() {}

  /**
   * Construct a {@code InvalidValueException} with a message parameter.
   * @param message A string description of the exception.
   */
  public InvalidValueException(String message){
    super(message);
  }
}