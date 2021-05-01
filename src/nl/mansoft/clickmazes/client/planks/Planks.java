package nl.mansoft.clickmazes.client.planks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class Planks extends Composite {

	private static PlanksUiBinder uiBinder = GWT.create(PlanksUiBinder.class);

	interface PlanksUiBinder extends UiBinder<Widget, Planks> {
	}
	private PlanksMIDlet midlet;
	private Element element;
	
	public Planks() {
		initWidget(uiBinder.createAndBindUi(this));
		element = midletContainer.getElement();
		midlet = new PlanksMIDlet(element);
		midlet.onModuleLoad();
	}
	
	public static native void requestFullscreen(Element element) /*-{
		if (element.requestFullscreen) {
			element.requestFullscreen();
		} else if (element.mozRequestFullScreen) {
			element.mozRequestFullScreen();
		} else if (element.webkitRequestFullScreen) {
			element.webkitRequestFullScreen();
		}
	}-*/;
		
	@UiField
	HTML midletContainer;

	@UiHandler("midletContainer")
	void onClickMidlet(ClickEvent e) {
		midlet.onClick(e.getRelativeX(element), e.getRelativeY(element));
	}
	
	@UiField
	Button restartButton;

	@UiHandler("restartButton")
	void onClickRestart(ClickEvent e) {
		requestFullscreen(midletContainer.getElement());
		midlet.restart();
	}
	@UiField
	Button previousButton;

	@UiHandler("previousButton")
	void onClickPrevious(ClickEvent e) {
		midlet.prevLevel();
	}
	
	@UiField
	Button nextButton;

	@UiHandler("nextButton")
	void onClickNext(ClickEvent e) {
		midlet.nextLevel();
	}
	@UiField
	Button undoButton;

	@UiHandler("undoButton")
	void onClickUndo(ClickEvent e) {
		midlet.undo();
	}
	@UiField
	Button redoButton;

	@UiHandler("redoButton")
	void onClickRedo(ClickEvent e) {
		midlet.redo();
	}
}
