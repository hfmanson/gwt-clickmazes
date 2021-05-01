package nl.mansoft.clickmazes.client.jolly;

import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.storage.client.Storage;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.itf.ISVGTransformable;

import nl.mansoft.clickmazes.client.Point;

public class JollyCanvas implements EntryPoint {
	
    private int selidx;
    private String levels[] = {
        "5742WWWWXB20B40B01B21B41B13B33B43B04B14B05B15B25B35b450",
        "5713ENNESENXB10B40B11B12B33B43B04B14B05B15B25B35b450",
        "5721NEESSSWWNXB01B11B31B32B03B13B34B05B15B25B35b450",
        "5711WSEESSENNNWXB00B10B41B42B43B04B14B05B15B25B35b450",
        "5734WWNX44NNWWNNEXB10B40B11B31B41B03B23B33B04B05B15B25B35b450",
        "5701SEEX40SSSSXB11B31B32B03B13B33B04B14B34B05B15B25B35b450",
        "5721WWSSEX43NWWSSEXB00B10B20B30B40B31B41B12B33B04B14B05B15B25B35b450",
        "5731NWWWSX23NWSSEENNXB40B11B21B41B42B43B05B15B25B35b450",
        "5701SSX20SX22SSXB00B10B30B40B11B31B41B12B32B13B33B05B15B25B35b450",
        "5734WNNNNX10WX42SSXB11B31B41B02B12B32B33B14B05B15B25B35b450",
        "5730WWWX32ENNX22WWNXB11B21B31B13B33B43B14B05B15B25B35b450",
        "5742NWX02SSEEX34ENXB00B10B20B30B40B01B12B22B32B33B05B15B25B35b450",
        "5710WSSSX12ENNXB30B40B11B32B13B23B33B24B34B05B15B25B35b450",
        "5731WWSX13SEENNXB00B20B30B40B01B22B42B03B23B43B04B05B15B25B35b450",
        "5732WWWX03SEEEENNXB00B10B20B30B01B31B13B23B33B05B15B25B35b450",
        "5703ENNX20SEX42WSXB00B10B30B40B01B41B02B22B23B43B04B05B15B25B35b450",
        "5740WWX10WSSSSX34ENNNXB11B31B13B23B33B14B24B05B15B25B35b450",
        "5731WWSEESWWWXB30B40B01B02B42B04B34B05B15B25B35b450",
        "5700EEEEX12WNX41SWWXB11B31B03B13B23B33B04B14B05B15B25B35b450",
        "5712WSSEX22ENESSSXB00B30B40B01B11B13B24B34B05B15B25B35b450",
        "5742WWX44WWX14WX12WXB10B30B11B31B03B13B23B33B05B15B25B35b450",
        "5742NWWSX23SEENXB00B30B40B01B11B02B32B03B13B33B05B15B25B35b450",
        "5731EX23NNNX33EXB30B40B01B11B32B42B03B13B04B14B24B34B05B15B25B35b450",
        "5714NNEENWWNWSXB41B02B33B34B05B15B25B35b450",
        "5740WX42WWX21WWSX23WWXB00B10B31B41B12B33B43B04B14B05B15B25B35b450",
        "5700EEEEX02EEEEX34WWWXB11B31B41B03B23B33B05B15B25B35b450",
        "5731WWSSX14EENNXB00B10B30B40B41B02B22B03B23B43B05B15B25B35b450",
        "5702NX24WX44NNXB00B10B20B30B40B31B41B12B13B33B04B34B05B15B25B35b450",
        "5723NNNWSSSWSEEEENNXB01B41B33B05B15B25B35b450",
        "5730EX00EESSEENX13WNNXB11B31B12B33B43B04B14B05B15B25B35b450",
        "5712SX14EENNNWWXB20B30B01B22B42B03B23B43B04B05B15B25B35b450",
        "5732ESSWNWNNXB00B20B01B31B12B04B14B05B15B25B35b450",
        "5740SX44NX01NX13WSXB10B20B30B02B22B32B42B14B24B34B05B15B25B35b450",
        "5712EEX44NNNX34WXB00B20B01B21B31B13B23B33B14B05B15B25B35b450",
        "5731SEX11ENX13NWX33WSXB00B10B30B01B03B43B04B14B05B15B25B35b450",
        "5722EENWWNWWSSESWSEEX43WWXB30B40B34B05B15B25B35b450",
        "5732WWX44WNX13SWXB00B10B20B30B40B01B21B41B02B42B03B23B43B05B15B25B35b450",
        "5730WWX41WWX32WWX23WWX44WWXB40B42B33B43B04B14B05B15B25B35b450",
        "5720WWSEEX13NWSSEEX44NNNNWSXB22B32B05B15B25B35b450",
        "5702SSX22NWWX33WWXB00B10B40B41B12B32B42B14B24B05B15B25B35b450",
        "5704EX24NNNNX44NNNXB11B31B32B03B13B33B34B05B15B25B35b450",
        "5712SSWX44NNWWNXB00B10B20B30B40B01B11B31B03B23B33B05B15B25B35b450",
        "5700ESWX41NWSX02EEESWWWSEEEENNXB05B15B25B35b450",
        "5723NNX04EEEX20WWSSSXB30B11B31B12B13B33B43B05B15B25B35b450",
        "5714EX20WWSX42NNX32WXB30B11B31B13B23B33B34B05B15B25B35b450",
        "5704NNX14EEENNWWWXB10B20B30B11B31B13B23B33B05B15B25B35b450",
        "5722WSX23ENNWX34WWXB00B10B20B30B40B11B41B42B03B43B04B05B15B25B35b450",
        "5701SSX22NX20ESSEX12NNWXB41B13B33B43B04B14B05B15B25B35b450",
        "5710EX24WWNNEEX33WWXB30B40B11B21B31B32B34B05B15B25B35b450",
        "5721NWSSX23WWNX24ENNNXB30B40B01B22B43B04B14B05B15B25B35b450",
        "5742NNX10SSX30WX22EXB00B21B31B02B03B23B33B43B04B05B15B25B35b450",
        "5713WX04EEEEX02EEEEXB00B10B20B30B40B01B11B21B31B41B23B05B15B25B35b450",
        "5702NNX24NNNNX31SSSX10SSSXB30B40B41B03B04B14B05B15B25B35b450",
        "5704ENNX20SWWNX30ESXB10B31B02B22B03B43B24B05B15B25B35b450",
        "5713ENNNX24WWNNNNX44WXB10B30B40B11B31B43B05B15B25B35b450",
        "5730EX34ENNNX10ESSSSXB01B11B31B02B12B32B33B14B05B15B25B35b450",
        "5741NX32SSWWNX30WWX21SWXB11B31B42B03B23B43B05B15B25B35b450",
        "5742NNWX24ENNX20WWSX02ESSXB11B31B03B23B43B04B05B15B25B35b450",
        "5721WWSX34NNNX12ESSXB00B10B30B40B42B03B13B43B05B15B25B35b450",
        "5732SWWWNNNX44NNNWWWSXB30B40B22B04B14B05B15B25B35b450",
        "5720EX01SSX44NNX24WXB10B40B31B41B12B13B33B04B34B05B15B25B35b450",
        "5734ENNWX14ENNWX41NWWWWXB11B21B03B13B33B04B05B15B25B35b450",
        "5740WWWSSX04EEEENNNX00SSSEEEXB31B05B15B25B35b450",
        "5732NNWSWNWSSESSX24NNX42NXB03B33B43B04B05B15B25B35b450",
        "5720WWSSEEX11EESSWWX44NXB40B41B42B14B24B34B05B15B25B35b450",
        "5741NX03NNX11EEX13EEXB00B10B30B12B22B42B04B14B34B05B15B25B35b450",
        "5741NWWWWSX13NNEESSX34WWXB02B42B03B23B43B05B15B25B35b450",
        "5723NWWSX30WWSX24WWX31SXB40B01B21B41B13B33B43B05B15B25B35b450",
        "5720WWX22WSSEEX32ENWWXB30B01B02B03B23B43B05B15B25B35b450",
        "5711NWX12WSX24WWX43NNX23ESX31NEXB01B21B22B32B13B05B15B25B35b450",
        "5742NNX22SSEEX00SSSX21NXB11B31B12B33B43B04B14B05B15B25B35b450",
        "5701SSSX24NNX41SSSXB30B40B11B32B13B33B14B34B05B15B25B35b450",
        "5724NX14WNNENEESX34ENNNNWXB20B01B13B33B05B15B25B35b450",
        "5724NWX01NEX02SSX30WX40SWX32WWNXB21B42B14B34B05B15B25B35b450",
        "5731NX12SSX21NWWSSX22SESEXB11B32B03B43B24B05B15B25B35b450",
        "5724WWX13NNWX40WX42NWWX34NNXB20B02B22B23B43B05B15B25B35b450",
        "5730ESX32ESSWWNX10WSSEEX14WXB11B31B13B33B05B15B25B35b450",
        "5710WSSX32ENNWX33SWWNNEXB11B31B03B23B43B05B15B25B35b450",
        "5723WWX10WX21WWX22WX31EX42SSXB20B02B32B33B24B05B15B25B35b450",
        "5702NNEX42SSX22NEENX12SSEXB30B11B32B03B23B33B05B15B25B35b450",
    };
    int currentLevel;
    private String titleStr;
    private String encStr;
    private String oTitleStr;
    private String oEncStr;
    private String dEncStr;
    private int puzz[][];
    private int sqsz;
    private int rows;
    private int cols;
    private int ex;
    private int ey;
    private int maxLen;
    private boolean haveWon;
    int wormCols[];
    int halfCols[] = {
        0x9999ff, 0xff9999, 0x99ff99, 0xffff99, 0xff9999, 0x99ff99, 0xffff99
    };
    Point curEnd;
    Point flagP;
    worm tmpW;
    worm flagW;
    worm curWorm;
    worm editWorm;
    Vector wormSet;
    private OMSVGSVGElement svg;
    private OMSVGTextElement solved;
    private OMSVGGElement firstSquare;
    private Storage localStorage = null;
    Element div;
    
    public JollyCanvas(Element div) {
    	this.div = div;
		localStorage = Storage.getLocalStorageIfSupported();
	}
    
    class worm implements MouseMoveHandler, MouseDownHandler, MouseUpHandler, TouchStartHandler, TouchEndHandler, TouchMoveHandler 
    {
    	public void moveWorm(int clientX, int clientY)
    	{
    		OMSVGPoint p = svg.createSVGPoint();
    		p.setX(clientX);
    		p.setY(clientY);
    		OMSVGMatrix m = firstSquare.getScreenCTM();
    		p = p.matrixTransform(m.inverse());
    		int i1 = (int) Math.floor(p.getX());
    		int j1 = (int) Math.floor(p.getY());
    		
//    		System.out.println("move:" + i1 + "," + j1);
            Point point = new Point(i1, j1);
            
            if (point.x < 0 || point.x >= cols || point.y < 0 || point.y >= rows)
            	return;
            if(curEnd.equals(point))
                return;
            if(puzz[j1][i1] == 15)
                return;
            if(puzz[j1][i1] >= 0 && curWorm.index != puzz[j1][i1])
                return;
            if(!curWorm.validate(curEnd, point))
                return;
            worm worm1 = null;
            for(int k1 = 0; k1 < wormSet.size(); k1++)
            {
                worm1 = (worm)wormSet.elementAt(k1);
                if(worm1.contains(point))
                    break;
                worm1 = null;
            }

            if(worm1 != null)
                return;
            curWorm.drag(curEnd, point);
            curEnd = curWorm.getEnd(point);
//    		System.out.println("move1:" + curEnd.x + "," + curEnd.y);
            checkForWin();    	
    	}
    	
    	public void onMouseMove(MouseMoveEvent event) {
    		moveWorm(event.getClientX(), event.getClientY());
    	}

    	public void onMouseDown(MouseDownEvent event) {
    		if (svgEnd == null)
    		{
	    		svgEnd = (OMSVGGElement) event.getSource();
//	    		svgEnd.setAttribute("fill", "red");
	    		mouseMoveRegistration = svg.addMouseMoveHandler(this);
	    		curEnd = body[svgEnd == svgHead ? 0 : bLen - 1];
//	    		System.out.println("down: " + curEnd.x + "," + curEnd.y);
	    		curWorm = this;
    		}
    	}

		public void onMouseUp(MouseUpEvent event) {
	        curWorm = null;
	        curEnd = null;
			if (svgEnd != null)
			{
				mouseMoveRegistration.removeHandler();
//	    		svgEnd.setAttribute("fill", toRGB(wColor));
				svgEnd = null;
			}
		}
		
        String getEnc()
        {
            StringBuffer stringbuffer = new StringBuffer("" + body[0].x + body[0].y);
            for(int i = 1; i < bLen; i++)
            {
                int j = body[i - 1].x;
                int k = body[i - 1].y;
                if(body[i].x == j - 1)
                    stringbuffer.append("W");
                else
                if(body[i].x == j + 1)
                    stringbuffer.append("E");
                else
                if(body[i].y == k - 1)
                    stringbuffer.append("N");
                else
                if(body[i].y == k + 1)
                    stringbuffer.append("S");
            }

            stringbuffer.append("X");
            return stringbuffer.toString();
        }

        void addUnit(int i, int j)
        {
            body[bLen++] = new Point(i, j);
        }

        void extendTail(Point point)
        {
            body[bLen++] = new Point(point);
        }

        void removeTail()
        {
            bLen--;
        }

        void extendHead(Point point)
        {
            for(int i = bLen; i > 0; i--)
                body[i] = body[i - 1];

            body[0] = new Point(point);
            bLen++;
        }

        void removeHead()
        {
            for(int i = 1; i < bLen; i++)
                body[i - 1] = body[i];

            bLen--;
        }

        void drag(Point point, Point point1)
        {
            if(isHead(point))
            {
                for(int i = bLen - 1; i > 0; i--)
                    body[i] = body[i - 1];

                body[0] = point1;
            } else
            if(isTail(point))
            {
                for(int j = 0; j < bLen - 1; j++)
                    body[j] = body[j + 1];

                body[bLen - 1] = point1;
            }
            updateWorm();
        }

        boolean isHead(Point point)
        {
            return body[0].equals(point);
        }

        boolean isTail(Point point)
        {
            return body[bLen - 1].equals(point);
        }

        Point getEnd(Point point)
        {
            if(body[0].equals(point))
                return body[0];
            if(body[bLen - 1].equals(point))
                return body[bLen - 1];
            else
                return null;
        }

        Point getOtherEnd(Point point)
        {
            if(body[0].equals(point))
                return body[bLen - 1];
            if(body[bLen - 1].equals(point))
                return body[0];
            else
                return null;
        }

        boolean validate(Point point, Point point1)
        {
            int i = point.x - point1.x;
            int j = point.y - point1.y;
            if(i == 0 && (j == -1 || j == 1))
                return true;
            return j == 0 && (i == -1 || i == 1);
        }

        boolean onMainGrid()
        {
            for(int i = 0; i < bLen; i++)
                if(body[i].y >= rows - 2)
                    return false;

            return true;
        }
        
        private OMSVGCircleElement createCircleWithColor(float cx, float cy, float r, String color)
        {
        	OMSVGCircleElement circle = new OMSVGCircleElement(cx, cy, r);
            circle.setAttribute("fill", color);
    		return circle;
        }
        
        private void updateCircle(OMSVGCircleElement circle, float cx, float cy)
        {
        	circle.setAttribute("cx", Float.toString(cx));
        	circle.setAttribute("cy", Float.toString(cy));
        }
        
        private OMSVGGElement createEye()
        {
        	OMSVGGElement eye = createGElementWithTransform();
            eye.appendChild(createCircleWithColor(0.0f, 0.0f, 0.125f, "white"));
            eye.appendChild(createCircleWithColor(0.0f, 0.0f, 0.08f, "black"));
            return eye;
        }
        
        
        void initWorm()
        {
    		svgHead = createGElementWithTransform();
    		svgHead.appendChild(new OMSVGCircleElement(0.0f, 0.0f, 0.4f));
        	oog1 = createEye();
        	oog2 = createEye();
        	svgHead.appendChild(oog1);
        	svgHead.appendChild(oog2);
//    		svgHead.setStrokeWidth(0.5);
        	svgHead.addMouseDownHandler(this);
        	svgHead.addTouchStartHandler(this);
        	svgHead.getElement().setAttribute("ontouchstart", "event.preventDefault()");
        	svgHead.getElement().setAttribute("ontouchmove", "event.preventDefault()");
        	svgHead.getElement().setAttribute("ontouchend", "event.preventDefault()");
    		svgTail = createGElementWithTransform();
    		svgTail.appendChild(new OMSVGCircleElement(0.0f, 0.0f, 0.4f));
//    		svgTail.setStrokeWidth(0.5);
        	svgTail.addMouseDownHandler(this);
        	svgTail.addTouchStartHandler(this);
        	svgTail.getElement().setAttribute("ontouchstart", "event.preventDefault()");
        	svgTail.getElement().setAttribute("ontouchmove", "event.preventDefault()");
        	svgTail.getElement().setAttribute("ontouchend", "event.preventDefault()");
        	svgLen = 1 + (bLen - 2) * 2;
        	svgCenter = new OMSVGCircleElement[svgLen];
        	for (int i = 0; i < svgLen; i++)
        		svgCenter[i] = new OMSVGCircleElement(0.0f, 0.0f, 0.4f);
        	updateWorm();
        	svgWorm = new OMSVGGElement();
        	svgWorm.setAttribute("fill", toRGB(wColor));
        	for (int i = 0; i < svgLen; i++)
        		svgWorm.appendChild(svgCenter[i]);
        	svgWorm.appendChild(svgHead);
        	svgWorm.appendChild(svgTail);
        	svg.addMouseUpHandler(this);
        	svg.addTouchEndHandler(this);
        	svg.addTouchMoveHandler(this);
        	svg.appendChild(svgWorm);        	
        }
        
        void updateWorm()
        {
        	float sqsz = 1.0f;
            int j1 = 0;
            int k1 = 0;
            float i2 = sqsz / 4.0f;
            int svgIndex = 0;
            for(int j2 = 0; j2 < bLen; j2++)
            {
                int l = body[j2].x;
                int i1 = body[j2].y;
                if(j2 > 0)
                {
                	OMSVGCircleElement circle = svgCenter[svgIndex++]; 
                    if(j1 < l)
                		updateCircle(circle, l, 0.5f + i1);
//                        myDrawOval(g, wColor, i, sqsz * l - l1, sqsz * i1, sqsz, sqsz, flag);
                    if(j1 > l)
                		updateCircle(circle, 1.0f + l, 0.5f + i1);
//                        myDrawOval(g, wColor, i, sqsz * l + l1, sqsz * i1, sqsz, sqsz, flag);
                    if(k1 < i1)
                		updateCircle(circle, 0.5f + l, i1);
//                        myDrawOval(g, wColor, i, sqsz * l, sqsz * i1 - l1, sqsz, sqsz, flag);
                    if(k1 > i1)
                		updateCircle(circle, 0.5f + l, 1.0f + i1);
//                        myDrawOval(g, wColor, i, sqsz * l, sqsz * i1 + l1, sqsz, sqsz, flag);
                }
                if (j2 == 0)
                {
                	setTranslate(svgHead, 0.5f + l, 0.5f + i1);
                	
                    float k2 = 0.0f;
                    float l2 = 0.0f;
                    float i3 = 0.0f;
                    float j3 = 0.0f;
                    float k3 = body[j2 + 1].x;
                    float l3 = body[j2 + 1].y;
                    if(k3 < l)
                    {
                        k2 = i3 = i2;
                        l2 = -i2;
                        j3 = i2;
                    }
                    if(k3 > l)
                    {
                        k2 = i3 = -i2;
                        l2 = -i2;
                        j3 = i2;
                    }
                    if(l3 < i1)
                    {
                        k2 = -i2;
                        i3 = i2;
                        l2 = j3 = i2;
                    }
                    if(l3 > i1)
                    {
                        k2 = -i2;
                        i3 = i2;
                        l2 = j3 = -i2;
                    }
                    setTranslate(oog1, k2, l2);
                    setTranslate(oog2, i3, j3);
                    
                }
                else if (j2 == bLen - 1)
                	setTranslate(svgTail, 0.5f + l, 0.5f + i1);
                else
                	updateCircle(svgCenter[svgIndex++], 0.5f + l, 0.5f + i1);
                j1 = l;
                k1 = i1;
            }
        }        
        
        boolean contains(Point point)
        {
            for(int i = 0; i < bLen; i++)
                if(body[i].equals(point))
                    return true;

            return false;
        }

        public Point body[];
        int wColor;
        int bLen;
        public int index;
        public OMSVGGElement svgWorm;
        private OMSVGGElement oog1;
        private OMSVGGElement oog2;
        private OMSVGGElement svgHead;
        private OMSVGGElement svgTail;
        private OMSVGGElement svgEnd;
        private OMSVGCircleElement[] svgCenter;
        private int svgLen;
        private HandlerRegistration mouseMoveRegistration;
//        private HandlerRegistration touchMoveRegistration;
        
        worm(int i, int j, int k)
        {
            body = null;
            bLen = 0;
            index = i;
            body = new Point[maxLen];
            wColor = wormCols[i];
            bLen = 0;
            addUnit(j, k);
        }


		public void onTouchStart(TouchStartEvent event) {
			event.preventDefault();
    		if (svgEnd == null)
    		{
	    		svgEnd = (OMSVGGElement) event.getSource();
//	    		svgEnd.setFillColor("green");
//	    		touchMoveRegistration = svg.addTouchMoveHandler(this);
	    		curEnd = body[svgEnd == svgHead ? 0 : bLen - 1];
//	    		System.out.println("down: " + curEnd.x + "," + curEnd.y);
	    		curWorm = this;
    		}
		}


		public void onTouchEnd(TouchEndEvent event) {
	        curWorm = null;
	        curEnd = null;
			if (svgEnd != null)
			{
//				touchMoveRegistration.removeHandler();
//				svgEnd.setFillColor(toRGB(wColor));
				svgEnd = null;
			}
		}


		public void onTouchMove(TouchMoveEvent event) {
			JsArray<Touch> touches = event.getTouches();
			if (touches.length() == 1)
			{
				event.preventDefault();
				Touch touch = touches.get(0);
				if (svgEnd != null)
				{
					moveWorm(touch.getClientX(), touch.getClientY());
				}
			}
		}
    }

    private void setLevel() {
        encStr = levels[currentLevel];
        if (localStorage != null) {
        	localStorage.setItem("level", Integer.toString(currentLevel));
        }
    }
    
    public void restart() {
        curEnd = null;
        flagP = null;
        tmpW = null;
        flagW = null;
        curWorm = null;
        editWorm = null;
        haveWon = false;
        setLevel();
        loadPuzz();
        selidx = 0;
        if (svg != null) {
        	Element svgElement = svg.getElement();
        	svgElement.getParentNode().removeChild(svgElement);
        }
		svg = new OMSVGSVGElement();
    	svg.setAttribute("width", "100%");
    	svg.setAttribute("height", "100%");
//		svg.setAttribute("zoomAndPan", "disable");
    	float xsize = 0.2f + cols;
    	float ysize = 0.2f + rows;
    	svg.setViewBox(-0.1f, -0.1f, xsize, ysize);
//    	RootPanel.get().getElement().appendChild(svg.getElement());        
    	div.appendChild(svg.getElement());        
        drawBoard();
    	solved = new OMSVGTextElement(xsize / 3.0f, ysize / 2.0f, OMSVGLength.SVG_LENGTHTYPE_NUMBER, "solved");
    	solved.setAttribute("font-size", "0.8");
    	solved.setAttribute("stroke-width", "0.01");
    	solved.setAttribute("fill", "yellow");
    	solved.setAttribute("stroke", "black");
    }
    
    public boolean checkForWin() {
        if(haveWon)
            return true;
        for(int i = 0; i < wormSet.size(); i++)
        {
            worm worm1 = (worm)wormSet.elementAt(i);
            if(!worm1.contains(new Point(0, rows - 1)))
                continue;
            haveWon = true;
            break;
        }

        if(!haveWon)
        {
            return false;
        } else
        {
//        	alert("Solved!");
        	svg.appendChild(solved);
            return true;
        }
    }

    private boolean loadPuzz() {
        if(encStr == null)
            return false;
        char c = encStr.charAt(0);
        char c1 = encStr.charAt(1);
        if(!Character.isDigit(c) || !Character.isDigit(c1))
            return false;
        cols = Character.digit(c, 16);
        rows = Character.digit(c1, 16);
        
        maxLen = 0;
        if(rows > 16 || cols > 16)
            return false;
        if(rows < 2 || cols < 2)
            return false;
        puzz = new int[rows][cols];
        puzz = new int[rows][cols];
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
                puzz[i][j] = -1;

        }

        int k = 0;
        int l = 0;
        int i1 = 0;
        for(int j1 = 2; j1 < encStr.length(); j1++)
        {
            char c2 = encStr.charAt(j1);
            if(c2 == 'B' || c2 == 'b')
                break;
            if(c2 == 'X')
            {
                l++;
                if(k > maxLen)
                    maxLen = k;
                k = 0;
            } else
            if(Character.isDigit(c2))
            {
                char c4 = encStr.charAt(j1 + 1);
                if(!Character.isDigit(c4))
                    return false;
                l++;
                j1++;
                if(k > maxLen)
                    maxLen = k;
                k = 0;
            } else
            {
                k++;
            }
        }

        maxLen++;
        maxLen = 32;
        if((l & 1) != 0)
            return false;
        worm worm1 = null;
        wormSet = new Vector();
        int k1 = 0;
        int l1 = 0;
        for(int i2 = 2; i2 < encStr.length(); i2++)
        {
            char c3 = encStr.charAt(i2);
            if(c3 == 'X')
            {
                wormSet.addElement(worm1);
                worm1 = null;
            } else
            if(Character.isDigit(c3))
            {
                char c5 = encStr.charAt(i2 + 1);
                k1 = Character.digit(c3, 16);
                l1 = Character.digit(c5, 16);
                if(worm1 == null)
                    worm1 = new worm(i1++, k1, l1);
                i2++;
            } else
            if(c3 == 'B')
            {
                char c6 = encStr.charAt(i2 + 1);
                char c8 = encStr.charAt(i2 + 2);
                k1 = Character.digit(c6, 16);
                l1 = Character.digit(c8, 16);
                puzz[l1][k1] = 15;
                i2 += 2;
            } else
            if(c3 == 'b')
            {
                char c7 = encStr.charAt(i2 + 1);
                char c9 = encStr.charAt(i2 + 2);
                char c10 = encStr.charAt(i2 + 3);
                k1 = Character.digit(c7, 16);
                l1 = Character.digit(c9, 16);
                int j2 = Character.digit(c10, 16);
                puzz[l1][k1] = j2;
                i2 += 3;
            } else
            if(worm1 != null)
                if(c3 == 'W')
                    worm1.addUnit(--k1, l1);
                else
                if(c3 == 'E')
                    worm1.addUnit(++k1, l1);
                else
                if(c3 == 'S')
                    worm1.addUnit(k1, ++l1);
                else
                if(c3 == 'N')
                    worm1.addUnit(k1, --l1);
        }

        if(worm1 != null)
        {
            wormSet.addElement(worm1);
            i1++;
        }
        return true;
    }

    private void appendTransform(ISVGTransformable transformable) {
    	transformable.getTransform().getBaseVal().appendItem(svg.createSVGTransform());
    }
    
    private void setTranslate(ISVGTransformable transformable, float x, float y) {
    	transformable.getTransform().getBaseVal().getItem(0).setTranslate(x, y);
    }
    
    private OMSVGGElement createGElementWithTransform() {
    	OMSVGGElement g = new OMSVGGElement();
    	appendTransform(g);
    	return g;
    }
    
    public void drawBoard()
    {
        for(int k = 0; k < rows; k++)
        {
            for(int l = 0; l < cols; l++)
            {
            	OMSVGGElement square = createGElementWithTransform();
            	if (k == 0 && l == 0)
            		firstSquare = square;
            	setTranslate(square, l, k);
            	OMSVGRectElement rect = new OMSVGRectElement(0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f);
            	OMSVGRectElement rect1 = null;
    			rect.setAttribute("stroke", "black");
    			rect.setAttribute("stroke-width", "0.02");
                if(puzz[k][l] == 15)
                {
        			rect.setAttribute("fill", "grey");                	
                }
                else if(puzz[k][l] >= 0)
                {
        			rect.setAttribute("fill", "white");                	
                	rect1 = new OMSVGRectElement(0.333f, 0.333f, 0.333f, 0.333f, 0.0f, 0.0f);
                	rect1.setAttribute("fill", toRGB(halfCols[puzz[k][l]]));
//                    g.setColor(halfCols[puzz[k][l]]);
//                    myDrawRect(g, -1, i, sqsz * l, sqsz * k, sqsz, sqsz);
                }
                else
        			rect.setAttribute("fill", "white");                	
    			square.appendChild(rect);
    			if (rect1 != null)
        			square.appendChild(rect1);
                svg.appendChild(square);
            }
        }    	
        for(int k1 = 0; k1 < wormSet.size(); k1++)
        {
            worm worm1 = (worm)wormSet.elementAt(k1);

            worm1.initWorm();
        }
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

    public void changeLevel(String txtLevel) {
        if (!txtLevel.equals("")) {
            int level = Integer.parseInt(txtLevel);
            if (level >= 0 && level < numLevels()) {
                currentLevel = level;
                setLevel();
                restart();
            } else {
//TODO                midlet.showAlert(this, "Could not load level");
            }
        }
    }

    public static String toRGB(int rgb)
    {
    	int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        return "rgb(" + r + "," + g + "," + b + ")";
    }
    
	public static native void alert(String msg) /*-{
		$wnd.alert(msg);
	}-*/;


	public void onModuleLoad() {
		currentLevel = 0;
		if (localStorage != null) {
			String stockLevel = localStorage.getItem("level");
			if (stockLevel != null) {
		        currentLevel = Integer.parseInt(stockLevel);				
			}
		}
        wormCols = new int[] {
//            Color.blue, Color.red, Color.green, Color.yellow, Color.magenta, Color.cyan, Color.orange, Color.pink, Color.red, Color.green,
//            Color.yellow, Color.magenta, Color.cyan, Color.orange, Color.pink
            0x0000FF, 0xFF0000, 0x00FF00, 0xFFFF00, 0xFF00FF, 0x00FFFF, 0xFFC800, 0xFFAFAF, 0xFF00000, 0x00FF00,
            0xFFFF00, 0xFF00FF, 0x00FFFF, 0xFFC800, 0xFFAFAF
        };
        restart();
	}
}
