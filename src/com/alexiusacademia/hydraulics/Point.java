package com.alexiusacademia.hydraulics;

public class Point {
  /* **********************************
   * Properties
   ***********************************/
  // abscissa
  private float x;

  // ordinate
  private float y;

  /* **********************************
   * Setters
   ***********************************/

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  /* **********************************
   * Getters
   ***********************************/

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  /**
   * Creates an empty point
   */
  public Point() { }

  /**
   * Creates a {@code Point} with predefined coordinate
   * @param x Abscissa
   * @param y Ordinate
   */
  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }
}
