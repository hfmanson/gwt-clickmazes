package javax.microedition.lcdui;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Widget;

public abstract class Canvas {
    Context2d context;
    com.google.gwt.canvas.client.Canvas canvas;
    
	public Canvas() {
		canvas = com.google.gwt.canvas.client.Canvas.createIfSupported();
		if (canvas != null) {
			canvas.setCoordinateSpaceWidth(500);
			canvas.setCoordinateSpaceHeight(500);
			context = canvas.getContext2d();
		}
	}
	
	public void updateSize(Element e) {
		canvas.setCoordinateSpaceWidth(e.getOffsetWidth());
		canvas.setCoordinateSpaceHeight(e.getOffsetHeight());		
	}
	
	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();		
	}
	
	public com.google.gwt.canvas.client.Canvas getCanvas() {
		return canvas;		
	}
	
	protected abstract void paint(Graphics g);
	
	public final void repaint() { 
		Graphics g = new Graphics();
		g.setContext2d(context);
		paint(g);
	}
	
	protected void pointerPressed(int x, int y) {		
	}
	
	public void onClick(int x, int y) {
		pointerPressed(x, y);
	}
	
	// should go to Displayable
	public int getWidth() {
		return canvas.getCanvasElement().getWidth();
	}
	
	public int getHeight() {
		return canvas.getCanvasElement().getHeight();
	}
	
}
