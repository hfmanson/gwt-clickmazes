package nl.mansoft.clickmazes.client.planks;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class PlanksEntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new Planks());
	}
}
