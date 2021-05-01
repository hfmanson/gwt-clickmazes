package nl.mansoft.clickmazes.client.boxup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;

public class Boxup extends Composite {

	private static BoxupUiBinder uiBinder = GWT.create(BoxupUiBinder.class);

	interface BoxupUiBinder extends UiBinder<Widget, Boxup> {
	}
	private BoxupCanvas canvas;
	
	public Boxup() {
		initWidget(uiBinder.createAndBindUi(this));
		canvas = new BoxupCanvas(svgContainer.getElement());
		Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				NativeEvent ne = event.getNativeEvent();
				if (ne.getType().equals("keydown")) {
					switch (ne.getKeyCode()) {
					case 38: // N
						canvas.navigate(0);
						break;
					case 40: // S
						canvas.navigate(2);
						break;
					case 37: // W
						canvas.navigate(3);
						break;
					case 39: // E
						canvas.navigate(1);
						break;
					}					
				}
			}
		});
		canvas.onModuleLoad();		
	}
		
	@UiField
	HTML svgContainer;
	
	@UiField
	Button restartButton;

	@UiHandler("restartButton")
	void onClickRestart(ClickEvent e) {
		canvas.restart();
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
