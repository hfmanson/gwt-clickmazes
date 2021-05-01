package nl.mansoft.clickmazes.client.boxup;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BoxupEntrypoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new Boxup());
	}
}
