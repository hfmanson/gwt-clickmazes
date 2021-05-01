package nl.mansoft.clickmazes.client.jolly;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class JollyEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new Jolly());
	}

}
