package nl.mansoft.clickmazes.client.planks;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

import nl.mansoft.clickmazes.client.StringTokenizer;

public class PlanksMIDlet implements EntryPoint {
	Element div;
	String m_strPositions[];
	String m_strNodes[];
	String m_strNames[];
	int m_iPuzzle;
	Swamp swamp;

	public PlanksMIDlet(Element div) {
		this.div = div;
	}

	public void updateSize() {
		swamp.updateSize(div);
		swamp.repaint();		
	}
	@Override
	public void onModuleLoad() {
		String positions = "750163142401214144,860174011142441040,970284102002225053,A80295636402222427,B903A5363760620333";
		String nodes = "3125402453235313641245,31454013441246335631473125,512357314630274125652347841346512567,51247832385025674136841246534579315842356,41367414793456403894126752478A3359313641469";
		m_strPositions = extractStrings(positions);
		m_strNodes = extractStrings(nodes);
		m_iPuzzle = 1;
		swamp = new Swamp(false, m_strPositions[m_iPuzzle],
				m_strNodes[m_iPuzzle], "", false);
		div.appendChild(swamp.getCanvasElement());
//		addAttachHandler(new AttachEvent.Handler() {
//			
//			@Override
//			public void onAttachOrDetach(AttachEvent event) {
//				updateSize();
//				
//			}
//		});
		
		Window.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				updateSize();
			}
		});
	}

	public void onClick(int x, int y) {
		swamp.onClick(x, y);
	}

	public String[] extractStrings(String s) {
		if (s == null)
			return null;
		StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
		int i = stringtokenizer.countTokens();
		String as[] = new String[i + 1];
		as[0] = null;
		for (int j = 0; j < i; j++)
			as[j + 1] = stringtokenizer.nextToken().trim();

		return as;
	}

	public void restart() {
		setPuzzle();
	}

	public void nextLevel() {
		if (m_iPuzzle + 1 < m_strNodes.length) {
			m_iPuzzle++;
			setPuzzle();
		}
	}

	public void prevLevel() {
		if (m_iPuzzle > 1)
			m_iPuzzle--;
		else
			m_iPuzzle = 1;
		setPuzzle();
	}

	public void undo() {
		swamp.UndoAction();
	}

	public void redo() {
		swamp.RedoAction();
	}

	public void setPuzzle() {
		// TODO m_lab.setText(createTitle(m_iPuzzle));
		swamp.RestartAction(m_strPositions[m_iPuzzle], m_strNodes[m_iPuzzle],
				"");
	}

	public static String toExtendedHex(int i) {
		char c = (char) (i >= 10 ? (65 + i) - 10 : 48 + i);
		String s = "" + c;
		return s;
	}
}
