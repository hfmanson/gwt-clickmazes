/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.clickmazes.client;

/**
 *
 * @author manson
 */
public class Polygon {
  /**
   * Compatible with JDK 1.0+.
   */
  private static final long serialVersionUID = -6460061437900069969L;

  /**
   * This total number of endpoints.
   *
   * @serial the number of endpoints, possibly less than the array sizes
   */
  public int npoints;

  /**
   * The array of X coordinates of endpoints. This should not be null.
   *
   * @see #addPoint(int, int)
   * @serial the x coordinates
   */
  public int[] xpoints;

  /**
   * The array of Y coordinates of endpoints. This should not be null.
   *
   * @see #addPoint(int, int)
   * @serial the y coordinates
   */
  public int[] ypoints;

  /**
   * The bounding box of this polygon. This is lazily created and cached, so
   * it must be invalidated after changing points.
   *
   * @see #getBounds()
   * @serial the bounding box, or null
   */
  protected Rectangle bounds;

  /** A big number, but not so big it can't survive a few float operations */
  private static final double BIG_VALUE = java.lang.Double.MAX_VALUE / 10.0;

  /**
   * Initializes an empty polygon.
   */
  public Polygon()
  {
    // Leave room for growth.
    xpoints = new int[4];
    ypoints = new int[4];
  }

  /**
   * Create a new polygon with the specified endpoints. The arrays are copied,
   * so that future modifications to the parameters do not affect the polygon.
   *
   * @param xpoints the array of X coordinates for this polygon
   * @param ypoints the array of Y coordinates for this polygon
   * @param npoints the total number of endpoints in this polygon
   * @throws NegativeArraySizeException if npoints is negative
   * @throws IndexOutOfBoundsException if npoints exceeds either array
   * @throws NullPointerException if xpoints or ypoints is null
   */
  public Polygon(int[] xpoints, int[] ypoints, int npoints)
  {
    this.xpoints = new int[npoints];
    this.ypoints = new int[npoints];
    System.arraycopy(xpoints, 0, this.xpoints, 0, npoints);
    System.arraycopy(ypoints, 0, this.ypoints, 0, npoints);
    this.npoints = npoints;
  }
 /**
   * Returns the bounding box of this polygon. This is the smallest
   * rectangle with sides parallel to the X axis that will contain this
   * polygon.
   *
   * @return the bounding box for this polygon
   * @see #getBounds2D()
   * @deprecated use {@link #getBounds()} instead
   */
  public Rectangle getBoundingBox()
  {
    if (bounds == null)
      {
        if (npoints == 0)
          return bounds = new Rectangle();
        int i = npoints - 1;
        int minx = xpoints[i];
        int maxx = minx;
        int miny = ypoints[i];
        int maxy = miny;
        while (--i >= 0)
          {
            int x = xpoints[i];
            int y = ypoints[i];
            if (x < minx)
              minx = x;
            else if (x > maxx)
              maxx = x;
            if (y < miny)
              miny = y;
            else if (y > maxy)
              maxy = y;
          }
        bounds = new Rectangle(minx, miny, maxx - minx, maxy - miny);
      }
    return bounds;
  }
  /**
   * Tests whether or not the specified point is inside this polygon.
   *
   * @param x the X coordinate of the point to test
   * @param y the Y coordinate of the point to test
   * @return true if the point is inside this polygon
   * @since 1.2
   */
  public boolean contains(double x, double y)
  {
    return ((evaluateCrossings(x, y, false, BIG_VALUE) & 1) != 0);
  }

    /**
     * Determines whether the specified {@link Point} is inside this
     * <code>Polygon</code>.
     * @param p the specified <code>Point</code> to be tested
     * @return <code>true</code> if the <code>Polygon</code> contains the
     *                  <code>Point</code>; <code>false</code> otherwise.
     * @see #contains(double, double)
     * @since 1.0
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Determines whether the specified coordinates are inside this
     * <code>Polygon</code>.
     * <p>
     * @param x the specified X coordinate to be tested
     * @param y the specified Y coordinate to be tested
     * @return {@code true} if this {@code Polygon} contains
     *         the specified coordinates {@code (x,y)};
     *         {@code false} otherwise.
     * @see #contains(double, double)
     * @since 1.1
     */
    public boolean contains(int x, int y) {
        return contains((double) x, (double) y);
    }

    /**
     * Determines whether the specified coordinates are contained in this
     * <code>Polygon</code>.
     * @param x the specified X coordinate to be tested
     * @param y the specified Y coordinate to be tested
     * @return {@code true} if this {@code Polygon} contains
     *         the specified coordinates {@code (x,y)};
     *         {@code false} otherwise.
     * @see #contains(double, double)
     * @deprecated As of JDK version 1.1,
     * replaced by <code>contains(int, int)</code>.
     * @since 1.0
     */
    public boolean inside(int x, int y) {
        return contains((double) x, (double) y);
    }

  /**
   * Helper for contains, intersects, calculates the number of intersections
   * between the polygon and a line extending from the point (x, y) along
   * the positive X, or Y axis, within a given interval.
   *
   * @return the winding number.
   * @see #contains(double, double)
   */
  private int evaluateCrossings(double x, double y, boolean useYaxis,
                                double distance)
  {
    double x0;
    double x1;
    double y0;
    double y1;
    double epsilon = 0.0;
    int crossings = 0;
    int[] xp;
    int[] yp;

    if (useYaxis)
      {
        xp = ypoints;
        yp = xpoints;
        double swap;
        swap = y;
        y = x;
        x = swap;
      }
    else
      {
        xp = xpoints;
        yp = ypoints;
      }

    /* Get a value which is small but not insignificant relative the path. */
    epsilon = 1E-7;

    x0 = xp[0] - x;
    y0 = yp[0] - y;
    for (int i = 1; i < npoints; i++)
      {
        x1 = xp[i] - x;
        y1 = yp[i] - y;

        if (y0 == 0.0)
          y0 -= epsilon;
        if (y1 == 0.0)
          y1 -= epsilon;
        if (y0 * y1 < 0)
          if (Line2D.linesIntersect(x0, y0, x1, y1, epsilon, 0.0, distance, 0.0))
            ++crossings;

        x0 = xp[i] - x;
        y0 = yp[i] - y;
      }

    // end segment
    x1 = xp[0] - x;
    y1 = yp[0] - y;
    if (y0 == 0.0)
      y0 -= epsilon;
    if (y1 == 0.0)
      y1 -= epsilon;
    if (y0 * y1 < 0)
      if (Line2D.linesIntersect(x0, y0, x1, y1, epsilon, 0.0, distance, 0.0))
        ++crossings;

    return crossings;
  }     
}
