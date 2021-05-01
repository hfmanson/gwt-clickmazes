package nl.mansoft.clickmazes.client.jolly;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class Jolly extends Composite {

	private static JollyUiBinder uiBinder = GWT.create(JollyUiBinder.class);

	interface JollyUiBinder extends UiBinder<Widget, Jolly> {
	}
	private JollyCanvas canvas;
	
	public Jolly() {
		initWidget(uiBinder.createAndBindUi(this));
		canvas = new JollyCanvas(svgContainer.getElement());
		canvas.onModuleLoad();
	}
		
	@UiField
	HTML svgContainer;
	
	@UiField
	Button restartButton;

	public static native void requestFullscreen(Element element) /*-{
		if (element.requestFullscreen) {
			element.requestFullscreen();
		} else if (element.mozRequestFullScreen) {
			element.mozRequestFullScreen();
		} else if (element.webkitRequestFullScreen) {
			element.webkitRequestFullScreen();
		}
	}-*/;

	@UiHandler("restartButton")
	void onClickRestart(ClickEvent e) {
		canvas.restart();
		requestFullscreen(svgContainer.getElement());
	}
	@UiField
	Button previousButton;

	@UiHandler("previousButton")
	void onClickPrevious(ClickEvent e) {
		canvas.prevLevel();
	}
	
	@UiField
	Button nextButton;

	@UiHandler("nextButton")
	void onClickNext(ClickEvent e) {
		canvas.nextLevel();
	}
}
