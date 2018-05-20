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

    float x1, x2, x3, y1, y2;

    for (int i = 0; i < this.points.size(); i++) {
      // float x = this.points.get(i).getX();
      float y = this.points.get(i).getY();

      // Look for the intersection at the left side of the channel
      if (leftIntersections == 0) {
        if (y <= this.waterElevation && i > 0) {
          leftIntersections++;
          // Solve for the intersection point using interpolation
          x1 = this.points.get(i - 1).getX();
          y1 = this.points.get(i - 1).getY();
          x2 = this.points.get(i).getX();
          y2 = this.points.get(i).getY();
          x3 = (this.waterElevation - y1) * (x2 - x1) / (y2 - y1) + x1;
          newPoints.add(new Point(x3, this.waterElevation));
        }
      }

      // Look for the intersection at the right side of the channel
      if (rightIntersections == 0) {
        if (y >= this.waterElevation) {
          rightIntersections++;
          x1 = this.points.get(i - 1).getX();
          y1 = this.points.get(i - 1).getY();
          x2 = this.points.get(i).getX();
          y2 = this.points.get(i).getY();
          x3 = (this.waterElevation - y1) * (x2 - x1) / (y2 - y1) + x1;
          newPoints.add(new Point(x3, this.waterElevation));
        }
      }

      if (leftIntersections == 1) {
        if (rightIntersections == 0) {
          newPoints.add(this.points.get(i));
        }
      }
    }

    // Hydraulic elements


  }

  /**
   * Implementation of the shoelace formula in computing area of a polygon with
   * given vertices.
   * @param points The vertices covered by the cross sectional area.
   * @return Double Polygon area
   */
  private double polygonArea(List<Point> points) {
    // Number of vertices of the polygon
    int n = points.size();

    // Initialize area
    double area = 0;
    int j;

    for (int i = 0; i < n; i++) {
      j = (i + 1) % n;
      area += points.get(i).getX() * points.get(j).getY();
      area -= points.get(j).getX() * points.get(i).getY();
    }

    area = Math.abs(area) / 2;

    return area;
  }

  /**
   * Get the total distance covered by multiple points
   * @param points The vertices covered by the cross sectional area.
   * @return Double Polygon perimeter
   */
  private double polygonPerimeter(List<Point> points) {
    // Initialize perimeter
    double perimeter = 0;

    // Number of vertices of the polygon
    int n = points.size();

    Point p1, p2;

    for (int i = 0; i < (n-1); i++) {
      p1 = points.get(i);
      p2 = points.get(i + 1);
      perimeter += distanceBetweenTwoPoints(p1, p2);
    }

    return perimeter;
  }

  /**
   * Calculate the distance between two given points.
   * @param p1 First point
   * @param p2 Second point
   * @return Double The distance between the 2 points
   */
  private double distanceBetweenTwoPoints(Point p1, Point p2) {
    float x1, y1, x2, y2;
    x1 = p1.getX();
    y1 = p1.getY();
    x2 = p2.getX();
    y2 = p2.getY();
    return Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
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
