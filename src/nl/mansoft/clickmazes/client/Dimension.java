/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.clickmazes.client;

/**
 *
 * @author manson
 */
public class Dimension {
  /**
   * Compatible with JDK 1.0+.
   */
  private static final long serialVersionUID = 4723952579491349524L;

  /**
   * The width of this object.
   *
   * @see #getSize()
   * @see #setSize(double, double)
   * @serial the width
   */
  public int width;

  /**
   * The height of this object.
   *
   * @see #getSize()
   * @see #setSize(double, double)
   * @serial the height
   */
  public int height;

  /**
   * Create a new Dimension with a width and height of zero.
   */
  public Dimension()
  {
  }

  /**
   * Create a new Dimension with width and height identical to that of the
   * specified dimension.
   *
   * @param d the Dimension to copy
   * @throws NullPointerException if d is null
   */
  public Dimension(Dimension d)
  {
    width = d.width;
    height = d.height;
  }

  /**
   * Create a new Dimension with the specified width and height.
   *
   * @param w the width of this object
   * @param h the height of this object
   */
  public Dimension(int w, int h)
  {
    width = w;
    height = h;
  }

  /**
   * Gets the width of this dimension.
   *
   * @return the width, as a double
   */
  public double getWidth()
  {
    return width;
  }

  /**
   * Gets the height of this dimension.
   *
   * @return the height, as a double
   */
  public double getHeight()
  {
    return height;
  }

  /**
   * Sets the size of this dimension. The values are rounded to int.
   *
   * @param w the new width
   * @param h the new height
   * @since 1.2
   */
  public void setSize(double w, double h)
  {
    width = (int) w;
    height = (int) h;
  }

  /**
   * Returns the size of this dimension. A pretty useless method, as this is
   * already a dimension.
   *
   * @return a copy of this dimension
   * @see #setSize(Dimension)
   * @since 1.1
   */
  public Dimension getSize()
  {
    return new Dimension(width, height);
  }

  /**
   * Sets the width and height of this object to match that of the
   * specified object.
   *
   * @param d the Dimension to get the new width and height from
   * @throws NullPointerException if d is null
   * @see #getSize()
   * @since 1.1
   */
  public void setSize(Dimension d)
  {
    width = d.width;
    height = d.height;
  }

  /**
   * Sets the width and height of this object to the specified values.
   *
   * @param w the new width value
   * @param h the new height value
   */
  public void setSize(int w, int h)
  {
    width = w;
    height = h;
  }

  /**
   * Tests this object for equality against the specified object.  This will
   * be true if and only if the specified object is an instance of
   * Dimension2D, and has the same width and height.
   *
   * @param obj the object to test against
   * @return true if the object is equal to this
   */
  public boolean equals(Object obj)
  {
    if (! (obj instanceof Dimension))
      return false;
    Dimension dim = (Dimension) obj;
    return height == dim.height && width == dim.width;
  }

  /**
   * Return the hashcode for this object. It is not documented, but appears
   * to be <code>((width + height) * (width + height + 1) / 2) + width</code>.
   *
   * @return the hashcode
   */
  public int hashCode()
  {
    // Reverse engineering this was fun!
    return (width + height) * (width + height + 1) / 2 + width;
  }

  /**
   * Returns a string representation of this object. The format is:
   * <code>getClass().getName() + "[width=" + width + ",height=" + height
   * + ']'</code>.
   *
   * @return a string representation of this object
   */
  public String toString()
  {
    return getClass().getName()
      + "[width=" + width + ",height=" + height + ']';
  }
    
}
