package com.alexiusacademia.hydraulics;

/**
 * Trapezoidal open channel
 * An extension of OpenChannel class
 * For trapezoidal shaped channels
 */
public class TrapezoidalOpenChannel extends OpenChannel {

  /* **********************************
   * Properties
   ***********************************/
  // Unknown class
  public enum Unknown {
    DISCHARGE, BED_SLOPE, WATER_DEPTH, BASE_WIDTH, SIDE_SLOPE
  }

  // Width at the bottom (smallest width) of the section
  private double baseWidth;

  // Unknown from the enum Unknown
  private Unknown unknown;

  // Sideslope, assumed equal on both sides
  private double sideSlope;

  /**
   * Creates an empty {@code TrapezoidalOpenChannel}
   */
  public TrapezoidalOpenChannel() {
    // Set the default unknown if not set
    this.unknown = Unknown.DISCHARGE;
  }

  /**
   * Creates a {@code TrapezoidalOpenChannel} with given parameters.
   * @param unknown The unknown from the enum Unknown
   * @param bedSlope The channel slope.
   * @param baseWidth The channel width at the bottom.
   * @param waterDepth The depth of the water.
   * @param sideSlope The side slope on both sides
   * @param manningRoughness The Manning's roughness coefficient.
   */
  public TrapezoidalOpenChannel(Unknown unknown, double bedSlope, double baseWidth, double waterDepth,
                                double sideSlope, double manningRoughness) {
    this.unknown = unknown;
    this.bedSlope = bedSlope;
    this.baseWidth = baseWidth;
    this.waterDepth = waterDepth;
    this.sideSlope = sideSlope;
    this.manningRoughness = manningRoughness;
  }

  /* **********************************
   * Setters
   ***********************************/

  /**
   * Sets the unknown.
   * @param unknown The unknown from the enum Unknown.
   */
  public void setUnknown(Unknown unknown) {
    this.unknown = unknown;
  }

  /**
   * Sets the bottom width of the trapezoidal channel.
   * @param baseWidth The channel width at the bottom.
   */
  public void setBaseWidth(double baseWidth) {
    this.baseWidth = baseWidth;
  }

  /**
   * Sets the sideslope on both sides.
   * @param sideSlope The side slope on both sides
   */
  public void setSideSlope(double sideSlope) {
    this.sideSlope = sideSlope;
  }

  /* **********************************
   * Getters
   ***********************************/

  public double getBaseWidth() {
    return baseWidth;
  }

  public Unknown getUnknown() {
    return unknown;
  }

  public double getSideSlope() {
    return sideSlope;
  }

  /* ********************************
   * Methods
   **********************************/

}
