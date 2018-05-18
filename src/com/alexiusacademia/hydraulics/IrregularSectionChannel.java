package com.alexiusacademia.hydraulics;

import java.util.ArrayList;
import java.util.List;

/**
 * Irregular section channel
 * An extension of the class OpenChannel
 * Used for irregular section channel, usually river channels.
 */
public class IrregularSectionChannel extends OpenChannel {
  /* **********************************
   * Properties
   ***********************************/
  // Unknown class
  public enum Unknown {
    DISCHARGE,
    BED_SLOPE
  }

  // Unknown
  private Unknown unknown;

  // List of points for the channel profile
  private List<Point> points;
  private float maxWaterElevation;
  private float waterElevation;

  /* **********************************
   * Setters
   ***********************************/

  public void setUnknown(Unknown unknown) {
    this.unknown = unknown;
  }

  public void setPoints(List<Point> points) {
    this.points = points;
  }

  public void setWaterElevation(float waterElevation) {
    this.waterElevation = waterElevation;
  }

  /* **********************************
   * Getters
   ***********************************/

  public List<Point> getPoints() {
    return points;
  }

  public float getMaxWaterElevation() {
    return maxWaterElevation;
  }

  public float getWaterElevation() {
    return waterElevation;
  }

  /**
   * Create an empty {@code IrregularSectionChannel}
   */
  public IrregularSectionChannel() {
    this.unknown = Unknown.DISCHARGE;
  }

  /**
   * Creates an {@code IrregularOpenChannel} with given unknown
   * @param unknown Unknown
   */
  public IrregularSectionChannel(Unknown unknown) {
    this.unknown = unknown;
  }

  /**
   * Creates an {@code IrregularSectionChannel} with gicen unknown and points
   * @param unknown Unknown
   * @param pts List of Points
   */
  public IrregularSectionChannel(Unknown unknown, List<Point> pts) {
    this.points = pts;
  }

  /* **********************************
   * Methods
   ***********************************/

  public boolean analyze() {
    if (isValidInputs()) {
      switch (this.unknown) {
        case DISCHARGE: solveForDischarge();
          break;
          default:
            try {
              throw new InvalidValueException("Invalid unknown.");
            } catch (InvalidValueException e) {
              this.isCalculationSuccessful = false;
              this.errMessage = e.getMessage();
            }
      }
    }
    return false;
  }

  private void solveForDischarge() {
    // Count the points
    int numberOfPoints = this.points.size();

    // Elevation of left and right bank
    float leftBankElevation = this.points.get(0).getY();
    float rightBankElevation = this.points.get(numberOfPoints - 1).getY();

    // Get the lower of the 2 banks
    if (leftBankElevation > rightBankElevation) {
      this.maxWaterElevation = rightBankElevation;
    } else {
      this.maxWaterElevation = leftBankElevation;
    }

    // Number of waterline intersections
    int leftIntersections = 0, rightIntersections = 0;

    // Remove points above the waterline intersection at the banks
    List<Point> newPoints = new ArrayList<>();

    for (int i = 0; i < this.points.size(); i++) {
      float x = this.points.get(i).getX();
      float y = this.points.get(i).getY();

      // Look for the intersection at the left side of the channel
      if (leftIntersections == 0) {
        if (y <= this.waterElevation && i > 0) {
          leftIntersections++;
          // Solve for the intersection point using interpolation
          float x1 = this.points.get(i - 1).getX();
          float y1 = this.points.get(i - 1).getY();
          float x2 = this.points.get(i).getX();
          float y2 = this.points.get(i).getY();
          float x3 = (this.waterElevation - y1) * (x2 - x1) / (y2 - y1) + x1;
          newPoints.add(new Point(x3, this.waterElevation));
        }
      }
    }

  }

  /**
   * Get the lowest point elevation from the list of poits.
   * @return Double Lowest point
   */
  private float calculateLowestPoint() {
    List<Float> elevations = new ArrayList<Float>();
    float lowest = 0;

    for (Point p : this.points) {
      elevations.add(p.getY());
    }

    for (float el : elevations) {
      if (lowest > el) {
        lowest = el;
      }
    }

    return lowest;
  }

  /**
   * Check for invalid inputs
   * @return Boolean True if all inputs are valid.
   */
  private boolean isValidInputs() {
    try {
      if (this.waterElevation > this.maxWaterElevation) {
        throw new InvalidValueException("Water elevation is above the lowest bank. Overflow!");
      }

      if (this.waterElevation < calculateLowestPoint()) {
        throw new DimensionException("Water surface was set below the lowest ground.");
      }

    } catch (Exception e) {
      this.isCalculationSuccessful = false;
      this.errMessage = e.getMessage();
      return false;
    }
    return true;
  }
}
