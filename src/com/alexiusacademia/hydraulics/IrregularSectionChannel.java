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

  public enum Unknown {                 // Unknown class
    DISCHARGE,
    BED_SLOPE
  }


  private Unknown unknown;              // Unknown
  private List<Point> points;           // List of points for the channel profile
  private float maxWaterElevation;
  private float waterElevation;
  private double calculatedDischarge;
  private double criticalWaterElevation;

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

  public double getCriticalWaterElevation() {
    return criticalWaterElevation;
  }

  public Unknown getUnknown() {
    return unknown;
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
        case DISCHARGE:
          solveForDischarge();
          break;
        case BED_SLOPE:
          solveForBedSlope();
          break;
          default:
            try {
              throw new InvalidValueException("Invalid unknown.");
            } catch (InvalidValueException e) {
              this.isCalculationSuccessful = false;
              this.errMessage = e.getMessage();
            }
      }
      solveForCriticalFlow();
      return isCalculationSuccessful;
    }
    return false;
  }

  /**
   * Solve for the unknown bed slope
   */
  private void solveForBedSlope() {
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
        if (y >= this.waterElevation && i > 0) {
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

    double trialSlope = 0;
    calculatedDischarge = 0;

    while (calculatedDischarge < this.discharge) {
      trialSlope += SLOPE_TRIAL_INCREMENT;
      this.wettedArea = polygonArea(newPoints);
      this.wettedPerimeter = polygonPerimeter(newPoints);
      this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
      this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(trialSlope) *
              Math.pow(this.hydraulicRadius, (2.0/3.0));
      calculatedDischarge = this.averageVelocity * this.wettedArea;
    }

    this.bedSlope = trialSlope;
    this.isCalculationSuccessful = true;
  }

  /**
   * Solve for the unknown discharge
   */
  private void solveForDischarge() {
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
        if (y >= this.waterElevation && i > 0) {
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
    this.wettedArea = polygonArea(newPoints);
    this.wettedPerimeter = polygonPerimeter(newPoints);
    this.hydraulicRadius = this.wettedArea / this.wettedPerimeter;
    this.averageVelocity = (1 / this.manningRoughness) * Math.sqrt(this.bedSlope) *
            Math.pow(this.hydraulicRadius, (2.0/3.0));
    this.discharge = this.averageVelocity * this.wettedArea;

    this.isCalculationSuccessful = true;
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
    // First, solve for the lowest bank
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

    try {
      if (this.waterElevation > this.maxWaterElevation) {
        throw new InvalidValueException("Water elevation is above the lowest bank. Overflow!");
      }

      if (this.waterElevation < calculateLowestPoint()) {
        throw new DimensionException("Water surface was set below the lowest ground.");
      }

      if (this.points.size() < 3) {
        throw new DimensionException("Invalid number of points. Minimum is three (3) points.");
      }

      if (this.manningRoughness <= 0) {
        throw new InvalidValueException("Manning's roughness must be greater than zero.");
      }

      if (this.unknown != Unknown.DISCHARGE) {
        if (this.discharge <= 0) {
          throw new InvalidValueException("Discharge must be greater than zero.");
        }
      }

      if (this.unknown != Unknown.BED_SLOPE) {
        if (this.bedSlope <= 0) {
          throw new InvalidValueException("Bed slope must not be flat or less than zero.");
        }
      }

    } catch (Exception e) {
      this.isCalculationSuccessful = false;
      this.errMessage = e.getMessage();
      return false;
    }
    return true;
  }

  /**
   * Solve for critical flow properties (e.g. critical depth, froude number, flow type ...)
   */
  private void solveForCriticalFlow() {
    double Q2g = Math.pow(this.discharge, 2) / this.GRAVITY_METRIC;

    double tester = 0;

    // Critical depth elevation
    // Initially at the lowest elevation
    double yc = this.calculateLowestPoint();

    // Critical area, perimeter, hydraulic radius, critical slope
    double Ac = 0, Pc, Rc, Sc;

    // Top width
    double T = 0;

    // Remove points above the waterline intersection at the banks
    List<Point> newPoints = new ArrayList<>();

    while (tester < Q2g) {
      yc += this.DEPTH_TRIAL_INCREMENT;

      // Get the new points
      // Number of waterline intersections
      int leftIntersections = 0, rightIntersections = 0;

      float x1, x2, x3, y1, y2;

      for (int i = 0; i < this.points.size(); i++) {
        // float x = this.points.get(i).getX();
        float y = this.points.get(i).getY();

        // Look for the intersection at the left side of the channel
        if (leftIntersections == 0) {
          if (y <= yc && i > 0) {
            leftIntersections++;
            // Solve for the intersection point using interpolation
            x1 = this.points.get(i - 1).getX();
            y1 = this.points.get(i - 1).getY();
            x2 = this.points.get(i).getX();
            y2 = this.points.get(i).getY();
            x3 = (float) ((yc - y1) * (x2 - x1) / (y2 - y1) + x1);
            newPoints.add(new Point(x3, (float) yc));
          }
        }

        // Look for the intersection at the right side of the channel
        if (rightIntersections == 0) {
          if (y >= yc && i > 0) {
            rightIntersections++;
            x1 = this.points.get(i - 1).getX();
            y1 = this.points.get(i - 1).getY();
            x2 = this.points.get(i).getX();
            y2 = this.points.get(i).getY();
            x3 = (float) ((yc - y1) * (x2 - x1) / (y2 - y1) + x1);
            newPoints.add(new Point(x3, (float) yc));
          }
        }

        if (leftIntersections == 1) {
          if (rightIntersections == 0) {
            newPoints.add(this.points.get(i));
          }
        }
      }

      tester = Math.pow(Ac, 3) / T;
    }

    this.criticalWaterElevation = yc;
    // Calculate the area covered
    Ac = polygonArea(newPoints);
    Pc = polygonPerimeter(newPoints);
    Rc = Ac / Pc;
    Sc = Math.pow(this.discharge / (Ac * Math.pow(Rc, (2.0/3.0))) * this.manningRoughness, 2);
    this.criticalSlope = Sc;
    T = distanceBetweenTwoPoints(newPoints.get(0), newPoints.get(newPoints.size() - 1));
    this.hydraulicDepth = this.wettedArea / T;
    this.froudeNumber = this.averageVelocity / Math.sqrt(this.GRAVITY_METRIC * this.hydraulicDepth);

    // Select the flow type
    this.flowType();
  }
}
