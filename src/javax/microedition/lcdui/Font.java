package javax.microedition.lcdui;

import com.google.gwt.canvas.dom.client.Context2d;

public class Font {
	private Context2d context;

	Font(Context2d context) {
		context.setFont("14px sans-serif");
		this.context = context;		
	}
	
	public int getHeight() {
		return 20;
	}
	
	public int getBaselinePosition() {
		return 2;
	}
	
	public int stringWidth(String str) {
		return (int) context.measureText(str).getWidth();
	}
}
