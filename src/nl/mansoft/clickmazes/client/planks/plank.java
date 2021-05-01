// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   planks.java

package nl.mansoft.clickmazes.client.planks;

import javax.microedition.lcdui.Graphics;
import nl.mansoft.clickmazes.client.Point;
import nl.mansoft.clickmazes.client.Polygon;


class plank
{

    plank(int i, int j, int k, int l)
    {
        clrPlank = 0xffff00; //Color.yellow;
        clrPlankEdge = 0; //Color.black;
        m_bCarried = false;
        m_bTouching = false;
        m_pt1 = new Point(i, j);
        m_pt2 = new Point(k, l);
        m_pt1Init = new Point(i, j);
        m_pt2Init = new Point(k, l);
        m_iLen = i != k ? k - i : l - j;
        if(m_iLen < 0)
            m_iLen = -m_iLen;
    }

    public boolean PointIsInside(Point point, ViewAttr viewattr)
    {
        int ai[] = new int[4];
        int ai1[] = new int[4];
        if(m_pt1.y == m_pt2.y)
        {
            if(viewattr.m_iHexYDim == 0)
            {
                ai[0] = viewattr.m_iLMargin + (m_pt1.x * 8 + 1) * viewattr.m_iScale;
                ai[1] = ai[0] + (m_iLen * 8 - 2) * viewattr.m_iScale;
                ai[2] = ai[1];
                ai[3] = ai[0];
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 8 - 1) * viewattr.m_iScale;
                ai1[1] = ai1[0];
                ai1[2] = ai1[1] + 4 * viewattr.m_iScale;
                ai1[3] = ai1[2];
            } else
            {
                ai[0] = viewattr.m_iLMargin + (((2 * m_pt1.x + viewattr.m_iHexYDim) - m_pt1.y) * 7 + 5) * viewattr.m_iScale;
                ai[1] = ai[0] + (m_iLen * 14 - 2) * viewattr.m_iScale;
                ai[2] = ai[1];
                ai[3] = ai[0];
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 12 + 2) * viewattr.m_iScale;
                ai1[1] = ai1[0];
                ai1[2] = ai1[1] + 6 * viewattr.m_iScale;
                ai1[3] = ai1[2];
            }
        } else
        if(m_pt1.x == m_pt2.x)
        {
            if(viewattr.m_iHexYDim == 0)
            {
                ai[0] = viewattr.m_iLMargin + (m_pt1.x * 8 - 1) * viewattr.m_iScale;
                ai[1] = ai[0] + 4 * viewattr.m_iScale;
                ai[2] = ai[1];
                ai[3] = ai[0];
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 8 + 1) * viewattr.m_iScale;
                ai1[1] = ai1[0];
                ai1[2] = ai1[1] + (m_iLen * 8 - 2) * viewattr.m_iScale;
                ai1[3] = ai1[2];
            } else
            {
                ai[0] = viewattr.m_iLMargin + (((2 * m_pt1.x + viewattr.m_iHexYDim) - m_pt1.y) * 7 + 1) * viewattr.m_iScale;
                ai[1] = ai[0] + 4 * viewattr.m_iScale;
                ai[2] = ai[1] - (m_iLen * 7 - 1) * viewattr.m_iScale;
                ai[3] = ai[2] - 4 * viewattr.m_iScale;
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 12 + 3) * viewattr.m_iScale;
                ai1[1] = ai1[0] + 2 * viewattr.m_iScale;
                ai1[2] = ai1[1] + (m_iLen * 12 - 1) * viewattr.m_iScale;
                ai1[3] = ai1[2] - 2 * viewattr.m_iScale;
            }
        } else
        if(viewattr.m_iHexYDim != 0)
        {
            ai[0] = viewattr.m_iLMargin + (((2 * m_pt1.x + viewattr.m_iHexYDim) - m_pt1.y) * 7 + 2) * viewattr.m_iScale;
            ai[1] = ai[0] + 4 * viewattr.m_iScale;
            ai[2] = ai[1] + (m_iLen * 7 - 1) * viewattr.m_iScale;
            ai[3] = ai[2] - 4 * viewattr.m_iScale;
            ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 12 + 5) * viewattr.m_iScale;
            ai1[1] = ai1[0] - 2 * viewattr.m_iScale;
            ai1[2] = ai1[1] + (m_iLen * 12 - 1) * viewattr.m_iScale;
            ai1[3] = ai1[2] + 2 * viewattr.m_iScale;
        }
        Polygon polygon = new Polygon(ai, ai1, 4);
        return polygon.contains(point);
    }

    public void Normalise()
    {
        Point point = new Point(m_pt2);
        if(point.y == m_pt1.y && point.x < m_pt1.x || point.y != m_pt1.y && point.y < m_pt1.y)
        {
            m_pt2.setLocation(m_pt1);
            m_pt1.setLocation(point);
        }
    }

    public void Reset()
    {
        m_bCarried = false;
        m_bTouching = false;
        m_pt1.setLocation(m_pt1Init);
        m_pt2.setLocation(m_pt2Init);
    }

    public void DrawPlank(Graphics g, ViewAttr viewattr)
    {
        int ai[] = new int[4];
        int ai1[] = new int[4];
        if(m_pt1.y == m_pt2.y)
        {
            if(viewattr.m_iHexYDim == 0)
            {
                ai[0] = viewattr.m_iLMargin + (m_pt1.x * 8 + 1) * viewattr.m_iScale;
                ai[1] = ai[0] + (m_iLen * 8 - 2) * viewattr.m_iScale;
                ai[2] = ai[1];
                ai[3] = ai[0];
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 8 - 1) * viewattr.m_iScale;
                ai1[1] = ai1[0];
                ai1[2] = ai1[1] + 2 * viewattr.m_iScale;
                ai1[3] = ai1[2];
            } else
            {
                ai[0] = viewattr.m_iLMargin + (((2 * m_pt1.x + viewattr.m_iHexYDim) - m_pt1.y) * 7 + 5) * viewattr.m_iScale;
                ai[1] = ai[0] + (m_iLen * 14 - 2) * viewattr.m_iScale;
                ai[2] = ai[1];
                ai[3] = ai[0];
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 12 + 2) * viewattr.m_iScale;
                ai1[1] = ai1[0];
                ai1[2] = ai1[1] + 4 * viewattr.m_iScale;
                ai1[3] = ai1[2];
            }
        } else
        if(m_pt1.x == m_pt2.x)
        {
            if(viewattr.m_iHexYDim == 0)
            {
                ai[0] = viewattr.m_iLMargin + (m_pt1.x * 8 - 1) * viewattr.m_iScale;
                ai[1] = ai[0] + 2 * viewattr.m_iScale;
                ai[2] = ai[1];
                ai[3] = ai[0];
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 8 + 1) * viewattr.m_iScale;
                ai1[1] = ai1[0];
                ai1[2] = ai1[1] + (m_iLen * 8 - 2) * viewattr.m_iScale;
                ai1[3] = ai1[2];
            } else
            {
                ai[0] = viewattr.m_iLMargin + (((2 * m_pt1.x + viewattr.m_iHexYDim) - m_pt1.y) * 7 + 2) * viewattr.m_iScale;
                ai[1] = ai[0] + 3 * viewattr.m_iScale;
                ai[2] = ai[1] - (m_iLen * 7 - 1) * viewattr.m_iScale;
                ai[3] = ai[2] - 3 * viewattr.m_iScale;
                ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 12 + 4) * viewattr.m_iScale;
                ai1[1] = ai1[0] + 2 * viewattr.m_iScale;
                ai1[2] = ai1[1] + (m_iLen * 12 - 2) * viewattr.m_iScale;
                ai1[3] = ai1[2] - 2 * viewattr.m_iScale;
            }
        } else
        if(viewattr.m_iHexYDim != 0)
        {
            ai[0] = viewattr.m_iLMargin + (((2 * m_pt1.x + viewattr.m_iHexYDim) - m_pt1.y) * 7 + 3) * viewattr.m_iScale;
            ai[1] = ai[0] + 3 * viewattr.m_iScale;
            ai[2] = ai[1] + (m_iLen * 7 - 1) * viewattr.m_iScale;
            ai[3] = ai[2] - 3 * viewattr.m_iScale;
            ai1[0] = viewattr.m_iTMargin + (m_pt1.y * 12 + 6) * viewattr.m_iScale;
            ai1[1] = ai1[0] - 2 * viewattr.m_iScale;
            ai1[2] = ai1[1] + (m_iLen * 12 - 2) * viewattr.m_iScale;
            ai1[3] = ai1[2] + 2 * viewattr.m_iScale;
        }
        if(!m_bCarried)
        {
            g.setColor(clrPlank);
            g.fillRect(ai[0], ai1[0], ai[2] - ai[0], ai1[2] - ai1[0]);
//            g.fillPolygon(ai, ai1, 4);
        }
        g.setColor(clrPlankEdge);
        g.drawRect(ai[0], ai1[0], ai[2] - ai[0], ai1[2] - ai1[0]);
//        g.drawPolygon(ai, ai1, 4);
    }

    final int clrPlank;
    final int clrPlankEdge;
    boolean m_bCarried;
    boolean m_bTouching;
    Point m_pt1;
    Point m_pt2;
    Point m_pt1Init;
    Point m_pt2Init;
    int m_iLen;
}
