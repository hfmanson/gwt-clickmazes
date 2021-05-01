package javax.microedition.lcdui;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class Graphics {
    /**
     * Constant for centering text and images horizontally
     * around the anchor point
     *
     * <P>Value <code>1</code> is assigned to <code>HCENTER</code>.</P>
     */
    public static final int HCENTER = 1;
  
    /**
     * Constant for centering images vertically
     * around the anchor point.
     *
     * <P>Value <code>2</code> is assigned to <code>VCENTER</code>.</P>
     */
    public static final int VCENTER = 2;
  
    /**
     * Constant for positioning the anchor point of text and images
     * to the left of the text or image.
     *
     * <P>Value <code>4</code> is assigned to <code>LEFT</code>.</P>
     */
    public static final int LEFT = 4;
  
    /**
     * Constant for positioning the anchor point of text and images
     * to the right of the text or image.
     *
     * <P>Value <code>8</code> is assigned to <code>RIGHT</code>.</P>
     */
    public static final int RIGHT = 8;
  
    /**
     * Constant for positioning the anchor point of text and images
     * above the text or image.
     *
     * <P>Value <code>16</code> is assigned to <code>TOP</code>.</P>
     */
    public static final int TOP = 16;

    /**
     * Constant for positioning the anchor point of text and images
     * below the text or image.
     *
     * <P>Value <code>32</code> is assigned to <code>BOTTOM</code>.</P>
     */
    public static final int BOTTOM = 32;
  
    /**
     * Constant for positioning the anchor point at the baseline of text.
     *
     * <P>Value <code>64</code> is assigned to <code>BASELINE</code>.</P>
     */
    public static final int BASELINE = 64;

    /**
     * Constant for the <code>SOLID</code> stroke style.
     *
     * <P>Value <code>0</code> is assigned to <code>SOLID</code>.</P>
     */
    public static final int SOLID = 0;

    /**
     * Constant for the <code>DOTTED</code> stroke style.
     *
     * <P>Value <code>1</code> is assigned to <code>DOTTED</code>.</P>
     */
    public static final int DOTTED = 1;

	private Context2d context;

	public void setContext2d(Context2d context) {
		this.context = context;
	}

	// public void setColor(int red, int green, int blue) {
	// // TODO
	// }
	public void setColor(int RGB) {
		int r = (RGB >> 16) & 0xff;
		int g = (RGB >> 8) & 0xff;
		int b = RGB & 0xff;
		CssColor fillStrokeStyle = CssColor.make(r, g, b);
		context.setFillStyle(fillStrokeStyle);
		context.setStrokeStyle(fillStrokeStyle);		
	}

	public void drawRect(int x, int y, int width, int height) {
		context.strokeRect(x, y, width, height);
	}

	public void fillRect(int x, int y, int width, int height) {
		context.fillRect(x, y, width, height);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		double w2 = width / 2.0;
		double h2 = height / 2.0;
		context.beginPath();
		context.arc(x + w2, y + h2, w2, startAngle, arcAngle);
		context.fill();
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		context.beginPath();
		context.moveTo(x1, y1);
		context.lineTo(x2, y2);
		context.stroke();
	}

	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		double w2 = width / 2.0;
		double h2 = height / 2.0;
		context.beginPath();
		context.arc(x + w2, y + h2, w2, startAngle, arcAngle);
		context.stroke();
	}
	
	public Font getFont() {
		return new Font(context);
	}
	
	public void drawString(String str, int x, int y, int anchor) {
		context.strokeText(str, x, y);
	}
}
