package com.alexiusacademia.hydraulics;

import java.io.Serializable;

public class OpenChannel implements Serializable {

  /** ****************************************
   * Constants
   ***************************************** */
  protected final double GRAVITY_METRIC = 9.81;
  protected final double DEPTH_TRIAL_INCREMENT = 0.00001;
  protected final double SLOPE_TRIAL_INCREMENT = 0.0000001;
  protected final double METER_TO_FOOT = 3.28;

  /** ****************************************
   * Properties
   ***************************************** */
  public enum FlowType {
    CRITICAL,
    SUBCRITICAL,
    SUPERCRITICAL
  }

  public enum Unit {
    METRIC,
    ENGLISH
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

  /**
   * Unit to be used in setting and retrieving values.
   * Default is set to metric system.
   */
  protected Unit unit = Unit.METRIC;

  /** Creates a parameterless instance of OpenChannel. */
  public OpenChannel() {

  }

  /** ****************************************
   * Getters
   ***************************************** */

  public double getDischarge() {
    if (this.unit == Unit.ENGLISH) {
      return discharge * Math.pow(METER_TO_FOOT, 3);
    }
    return discharge;
  }

  public double getAverageVelocity() {
    if (this.unit == Unit.ENGLISH) {
      return averageVelocity * METER_TO_FOOT;
    }
    return averageVelocity;
  }

  public double getBedSlope() {
    return bedSlope;
  }

  public double getWaterDepth() {
    if (this.unit == Unit.ENGLISH) {
      return waterDepth * METER_TO_FOOT;
    }
    return waterDepth;
  }

  public double getWettedPerimeter() {
    if (this.unit == Unit.ENGLISH) {
      return wettedPerimeter * METER_TO_FOOT;
    }
    return wettedPerimeter;
  }

  public double getWettedArea() {
    if (this.unit == Unit.ENGLISH) {
      return wettedArea * Math.pow(METER_TO_FOOT, 2);
    }
    return wettedArea;
  }

  public double getHydraulicRadius() {
    if (this.unit == Unit.ENGLISH) {
      return hydraulicRadius * METER_TO_FOOT;
    }
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
    if (this.unit == Unit.ENGLISH) {
      return hydraulicDepth * METER_TO_FOOT;
    }
    return hydraulicDepth;
  }

  public double getDischargeIntensity() {
    if (this.unit == Unit.ENGLISH) {
      return dischargeIntensity * Math.pow(METER_TO_FOOT, 2);
    }
    return dischargeIntensity;
  }

  public double getCriticalDepth() {
    if (this.unit == Unit.ENGLISH) {
      return criticalDepth * METER_TO_FOOT;
    }
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

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
  public void setBedSlope(double bedSlope) {
    this.bedSlope = bedSlope;
  }

  public void setDischarge(double discharge) {
    if (this.unit == Unit.ENGLISH) {
      this.discharge = discharge / Math.pow(METER_TO_FOOT, 3);
    } else {
      this.discharge = discharge;
    }
  }

  public void setWaterDepth(double waterDepth) {
    if (this.unit == Unit.ENGLISH) {
      this.waterDepth = waterDepth / METER_TO_FOOT;
    } else {
      this.waterDepth = waterDepth;
    }
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