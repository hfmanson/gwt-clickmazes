/*
 * BoxupCanvas.java
 *
 * Created on 2 juli 2005, 17:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package nl.mansoft.clickmazes.client.boxup;
import java.util.Vector;

import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.itf.ISVGTransformable;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import nl.mansoft.clickmazes.client.Point;


/**
 *
 * @author Manson
 */
public class BoxupCanvas implements ClickHandler {    
    private int rows;
    private int cols;
    private boolean haveWon;
    final String strAbout = "BoxUp version 1.02";
    private String titleStr;
    private String encStr;
    private int puzz[][];
    Point mover;
    Point moverMarker;
    box redBox;
    box blueBox;
    Vector sBoxes;
    Vector lBoxes;
    Element div;
    private String levels[] =
    {
        "330121---;nMW;---",
        "332101---;NMw;---",
        "330211M-W;-N-;n--",
        "330121-S-;eMS;-N-",
        "442130--MS;-Xe-;-wX-;N---",
        "441102-S-M;-wnW;Sne-;--N-",
        "441301--M-;En-W;E-nW;En-W",
        "440220-WS-;SMwn;enNW;-Nn-",
        "550033s-E-X;-X---;---X-;--XE-;MX--W",
        "551202-----;EnX-X;Nw---;MXSXN;-----",
        "554043----s;EnNn-;-XXXM;-sSsW;n----",
        "554134---s-;-Xn-n;E--X-;-WX-S;E--NM",
        "422120--E-;-nnM",
        "524111---S-;-NWMn",
        "623121-Mw---;--NwN-",
        "622051--eE--;Ne--MN",
        "622131e---M-;N-eN--",
    };
     private String titles[] =
     {
         "3x3 No.1",
         "3x3 No.2",
         "3x3 No.3",
         "3x3 No.4",
         "4x4 No.1",
         "4x4 No.2",
         "4x4 No.3",
         "4x4 No.4",
         "5x5 No.1",
         "5x5 No.2",
         "5x5 No.3",
         "5x5 No.4",
         "4x2 (47 moves)",
         "5x2 (68 moves)",
         "6x2 (82 moves)",
         "6x2 (97 moves)",
         "6x2 (108 moves)"
     };
    int currentLevel = 0;
    private OMSVGSVGElement svg;
    static private Point coords[][] =
    {
    	{
    		new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(0, -1)
    	},
    	{
    		new Point(1, 0), new Point(-1, 0), new Point(0, 1), new Point(1, 0)
    	},
    	{
    		new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(0, 1)
    	},
    	{
    		new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(-1, 0)
    	}
    };
    private OMSVGRectElement moverRect;
    private OMSVGTextElement solved;

    class box extends Point {
        boolean openTo(int i) {
            return i == mouth;
        }
        
        void set(Point point) {
            super.x = point.x;
            super.y = point.y;
    		setTranslate(path, x, y);
        }
        
        void setMark() {
            mark.x = super.x;
            mark.y = super.y;
        }
        
        void restoreMark() {
            super.x = mark.x;
            super.y = mark.y;
        }
        
        int sz;
        int mouth;
        boolean flag;
        Point mark;
        OMSVGPathElement path;
        
        box(char c, int i, int j) {
            super(i, j);
            flag = false;
            mouth = 0;
            sz = 0;
            if(c == 'N') {
                mouth = 0;
                sz = 2;
            }
            if(c == 'S') {
                mouth = 2;
                sz = 2;
            }
            if(c == 'E') {
                mouth = 1;
                sz = 2;
            }
            if(c == 'W') {
                mouth = 3;
                sz = 2;
            }
            if(c == 'n') {
                mouth = 0;
                sz = 1;
            }
            if(c == 's') {
                mouth = 2;
                sz = 1;
            }
            if(c == 'e') {
                mouth = 1;
                sz = 1;
            }
            if(c == 'w') {
                mouth = 3;
                sz = 1;
            }
            mark = new Point(i, j);
        }
    }
    public BoxupCanvas(Element div) {
    	this.div = div;
	}
    
    private void setLevel() {
        encStr = levels[currentLevel];
        Document.get().setTitle("BoxUp " + titles[currentLevel]);
    }
    
    private boolean validSQ(Point point) {
        if(point.x < 0 || point.x >= puzz[0].length)
            return false;
        if(point.y < 0 || point.y >= puzz.length)
            return false;
        return puzz[point.y][point.x] != 1;
    }
    
    public void navigate(int i) {
        byte byte0 = 0;
        byte byte1 = 0;
        boolean flag = false;
        byte byte2 = 0;
        if(i == 0) {
            byte1 = -1;
            byte2 = 2;
        }
        if(i == 2) {
            byte1 = 1;
            byte2 = 0;
        }
        if(i == 3) {
            byte0 = -1;
            byte2 = 1;
        }
        if(i == 1) {
            byte0 = 1;
            byte2 = 3;
        }
        boolean flag1 = false;
        Point point = new Point(mover.x + byte0, mover.y + byte1);
        box box1 = null;
        box box2 = null;
        box box3 = null;
        box box4 = null;
        box box5 = null;
        box box6 = null;
        if (validSQ(point)) {
            flag1 = false;
            for(int j = 0; j < sBoxes.size(); j++) {
                box box7 = (box)sBoxes.elementAt(j);
                if(box7.equals(mover))
                    box1 = box7;
                if(box7.equals(point))
                    box3 = box7;
            }
            
            for(int k = 0; k < lBoxes.size(); k++) {
                box box8 = (box)lBoxes.elementAt(k);
                if(box8.equals(mover))
                    box2 = box8;
                if(box8.equals(point))
                    box4 = box8;
            }
            
            if(box2 != null && !box2.openTo(i)) {
                box6 = box2;
                box5 = box1;
            }
            if(box1 != null && !box1.openTo(i))
                box5 = box1;
            if(box3 != null) {
                if(!box3.openTo(byte2))
                    flag1 = true;
                if(box5 != null || box6 != null)
                    flag1 = true;
            }
            if(box4 != null) {
                if(!box4.openTo(byte2))
                    flag1 = true;
                if(box6 != null)
                    flag1 = true;
            }
        } else {
            flag1 = true;
        }
        if(flag1)
            return;
        mover = point;
		setTranslate(moverRect, mover.x, mover.y);
        if(box5 != null)
            box5.set(point);
        if(box6 != null)
            box6.set(point);
        checkForWin();
    }
    
    public boolean checkForWin() {
        if(!mover.equals(redBox))
            return false;
        if(!mover.equals(blueBox)) {
            return false;
        } else {
            haveWon = true;
        	svg.appendChild(solved);
            return true;
        }
    }
    
    private boolean loadPuzz() {
        lBoxes = new Vector();
        sBoxes = new Vector();
        if(!Character.isDigit(encStr.charAt(0)) || !Character.isDigit(encStr.charAt(1)) || !Character.isDigit(encStr.charAt(2)) || !Character.isDigit(encStr.charAt(3)) || !Character.isDigit(encStr.charAt(4)) || !Character.isDigit(encStr.charAt(5)))
            return false;
        cols = Character.digit(encStr.charAt(0), 16);
        rows = Character.digit(encStr.charAt(1), 16);
        int i = Character.digit(encStr.charAt(2), 16);
        int j = Character.digit(encStr.charAt(3), 16);
        int k = Character.digit(encStr.charAt(4), 16);
        int l = Character.digit(encStr.charAt(5), 16);
        if(rows > 16 || cols > 16)
            return false;
        if(rows < 2 || cols < 2)
            return false;
        if(encStr.length() < 6 + rows * cols)
            return false;
        puzz = new int[rows][cols];
        int i1 = -1;
        int j1 = 0;
        int k1 = 6;
        redBox = null;
        blueBox = null;
        while(k1 < encStr.length() && j1 < rows) {
            char c = encStr.charAt(k1++);
            if(i1 == cols || c == ';' || c == ',') {
                i1 = -1;
                j1++;
            } else {
                puzz[j1][++i1] = 0;
                if(c == 'X')
                    puzz[j1][i1] = 1;
                else
                    if(c == 'M') {
                    mover = new Point(i1, j1);
                    } else {
                    box box1 = new box(c, i1, j1);
                    if(box1.sz == 0) {
                        box1 = null;
                    } else {
                        if(box1.sz == 1)
                            sBoxes.addElement(box1);
                        if(box1.sz == 2)
                            lBoxes.addElement(box1);
                        if(i1 == i && j1 == j)
                            redBox = box1;
                        if(i1 == k && j1 == l)
                            blueBox = box1;
                    }
                    }
            }
        }
        if(redBox == null || blueBox == null) {
            return false;
        } else {
            moverMarker = new Point(mover);
            return true;
        }
    }

    public void restart() {
        haveWon = false;
        setLevel();
        loadPuzz();
        if (svg != null) {
        	Element svgElement = svg.getElement();
        	svgElement.getParentNode().removeChild(svgElement);
        }
		svg = new OMSVGSVGElement();
    	float xsize = 0.2f + cols;
    	float ysize = 0.2f + rows;

    	svg.setAttribute("width", "100%");
    	svg.setAttribute("height", "100%");
    	svg.setViewBox(-0.1f, -0.1f, xsize, ysize);

//    	RootPanel.get().getElement().appendChild(svg.getElement());        
    	div.appendChild(svg.getElement());        
        drawBoard();
    	solved = new OMSVGTextElement(xsize / 8.0f, ysize / 2.0f, OMSVGLength.SVG_LENGTHTYPE_NUMBER, "solved");
    	solved.setAttribute("font-size", "0.8");
    	solved.setAttribute("stroke-width", "0.01");
    	solved.setAttribute("fill", "yellow");
    	solved.setAttribute("stroke", "black");
    }

	public void addObjects(box box1, box objectBox, float size1, String objectColor) {
		String color = box1 == objectBox ? objectColor : "black";
		float size = 1.0f - 2 * size1;
		Point[] coord = coords[box1.mouth];
		OMSVGPathElement path = new OMSVGPathElement();
		appendTransform(path);
		OMSVGPathSegList segs = path.getPathSegList();
		path.setAttribute("fill", "none");
		path.setAttribute("stroke", color);
		path.setAttribute("stroke-width", "0.075");
		segs.appendItem(path.createSVGPathSegMovetoAbs(size1 + coord[0].x * size, size1 + coord[0].y * size));
		for (int j = 1; j < 4; j++) {
			segs.appendItem(path.createSVGPathSegLinetoRel(size * coord[j].x, size * coord[j].y));
		}
		setTranslate(path, box1.x, box1.y);
		box1.path = path;
		svg.appendChild(path);
	}
    
    public void drawBoard()
    {
        for(int k = 0; k < rows; k++)
        {
            for(int l = 0; l < cols; l++)
            {
    			OMSVGRectElement rect = new OMSVGRectElement(l, k, 1f, 1f, 0f, 0f);
    			rect.addClickHandler(this);
            	rect.setAttribute("stroke", "grey");
            	rect.setAttribute("stroke-width", "0.02");
            	rect.setAttribute("fill", puzz[k][l] == 1 ? "black" : "white");
                svg.appendChild(rect);
            }
        }
        for(int i4 = 0; i4 < lBoxes.size(); i4++) {
            addObjects((box) lBoxes.elementAt(i4), blueBox, 0.1f, "blue");
        }
        for(int j4 = 0; j4 < sBoxes.size(); j4++) {
            addObjects((box) sBoxes.elementAt(j4), redBox, 0.225f, "red");        	
        }
		moverRect = new OMSVGRectElement(0.375f, 0.375f, 0.25f, 0.25f, 0f, 0f);
		appendTransform(moverRect);
		setTranslate(moverRect, mover.x, mover.y);
		svg.appendChild(moverRect);
    }

    public int numLevels() {
        return levels.length;
    }

    public String getCurrentLevel() {
        return Integer.toString(currentLevel);
    }

    public void nextLevel() {
        if (currentLevel < numLevels() - 1) {
            currentLevel++;
            setLevel();
            restart();
        }
    }

    public void prevLevel() {
        if (currentLevel > 0) {
            currentLevel--;
            setLevel();
            restart();
        }
    }
    public void onModuleLoad() {
		currentLevel = 0;
		restart();
	}
	
	public void onClick(ClickEvent event) {
		OMSVGRectElement rect = (OMSVGRectElement) event.getSource();
		int x = (int) (rect.getX().getBaseVal().getValue());
		int y = (int) (rect.getY().getBaseVal().getValue());
		
//		System.out.println("click: " + x + "," + y);
		int diffX = x - mover.x;
		int diffY = y - mover.y;
		if (diffX < 0 && diffY == 0)
			navigate(3);
		else if (diffX > 0 && diffY == 0)
			navigate(1);
		else if (diffX == 0 && diffY > 0)
			navigate(2);
		else if (diffX == 0 && diffY < 0)
			navigate(0);
	}
	
    private void appendTransform(ISVGTransformable transformable) {
    	transformable.getTransform().getBaseVal().appendItem(svg.createSVGTransform());
    }
    
    private void setTranslate(ISVGTransformable transformable, float x, float y) {
    	transformable.getTransform().getBaseVal().getItem(0).setTranslate(x, y);
    }
    
}
