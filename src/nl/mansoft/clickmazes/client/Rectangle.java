/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.clickmazes.client;

/**
 *
 * @author manson
 */
public class Rectangle {
    /**
     * Compatible with JDK 1.0+.
     */
    /**
     * The X coordinate of the top-left corner of the rectangle.
     *
     * @see #setLocation(int, int)
     * @see #getLocation()
     * @serial the x coordinate
     */
    public int x;
    /**
     * The Y coordinate of the top-left corner of the rectangle.
     *
     * @see #setLocation(int, int)
     * @see #getLocation()
     * @serial the y coordinate
     */
    public int y;
    /**
     * The width of the rectangle.
     *
     * @see #setSize(int, int)
     * @see #getSize()
     * @serial
     */
    public int width;
    /**
     * The height of the rectangle.
     *
     * @see #setSize(int, int)
     * @see #getSize()
     * @serial
     */
    public int height;

    /**
     * Initializes a new instance of <code>Rectangle</code> with a top
     * left corner at (0,0) and a width and height of 0.
     */
    public Rectangle() {
    }

    /**
     * Initializes a new instance of <code>Rectangle</code> from the
     * coordinates of the specified rectangle.
     *
     * @param r the rectangle to copy from
     * @since 1.1
     */
    public Rectangle(Rectangle r) {
        x = r.x;
        y = r.y;
        width = r.width;
        height = r.height;
    }

    /**
     * Initializes a new instance of <code>Rectangle</code> from the specified
     * inputs.
     *
     * @param x the X coordinate of the top left corner
     * @param y the Y coordinate of the top left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
  /**
   * Tests whether or not the specified point is inside this rectangle.
   * According to the contract of Shape, a point on the border is in only if
   * it has an adjacent point inside the rectangle in either the increasing
   * x or y direction.
   *
   * @param p the point to test
   * @return true if the point is inside the rectangle
   * @throws NullPointerException if p is null
   * @see #contains(int, int)
   * @since 1.1
   */
  public boolean contains(Point p)
  {
    return contains (p.x, p.y);
  }

  /**
   * Tests whether or not the specified point is inside this rectangle.
   * According to the contract of Shape, a point on the border is in only if
   * it has an adjacent point inside the rectangle in either the increasing
   * x or y direction.
   *
   * @param x the X coordinate of the point to test
   * @param y the Y coordinate of the point to test
   * @return true if the point is inside the rectangle
   * @since 1.1
   */
  public boolean contains(int x, int y)
  {
    return inside (x, y);
  }

  /**
   * Checks whether all points in the given rectangle are contained in this
   * rectangle.
   *
   * @param r the rectangle to check
   * @return true if r is contained in this rectangle
   * @throws NullPointerException if r is null
   * @see #contains(int, int, int, int)
   * @since 1.1
   */
  public boolean contains(Rectangle r)
  {
    return contains (r.x, r.y, r.width, r.height);
  }

  /**
   * Checks whether all points in the given rectangle are contained in this
   * rectangle.
   *
   * @param x the x coordinate of the rectangle to check
   * @param y the y coordinate of the rectangle to check
   * @param w the width of the rectangle to check
   * @param h the height of the rectangle to check
   * @return true if the parameters are contained in this rectangle
   * @since 1.1
   */
  public boolean contains(int x, int y, int w, int h)
  {
    return width > 0 && height > 0 && w > 0 && h > 0
      && x >= this.x && x + w <= this.x + this.width
      && y >= this.y && y + h <= this.y + this.height;
  }
  /**
   * Tests whether or not the specified point is inside this rectangle.
   *
   * @param x the X coordinate of the point to test
   * @param y the Y coordinate of the point to test
   * @return true if the point is inside the rectangle
   * @deprecated use {@link #contains(int, int)} instead
   */
  public boolean inside(int x, int y)
  {
    return width > 0 && height > 0
      && x >= this.x && x < this.x + width
      && y >= this.y && y < this.y + height;
  }
}
