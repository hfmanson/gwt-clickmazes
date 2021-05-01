package nl.mansoft.clickmazes.client.planks;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import nl.mansoft.clickmazes.client.Point;
import nl.mansoft.clickmazes.client.Dimension;;

public class Swamp extends Canvas {

    public void SquareOne() {
        m_plankHeld = null;
        for (int i = 0; i < m_planks.length; i++) {
            m_planks[i].Reset();
        }

        m_ptMan.setLocation(m_ptStart);
        m_iNextMove = 0;
        m_iCount = 0;
        MarkActivePlanks();
    }

    public void PickUpPlank(int i) {
        m_planks[i].m_bCarried = true;
        m_plankHeld = m_planks[i];
        if (!m_bReplay) {
            RecordMove("P" + (i + 1));
        }
    }

    public boolean UndoMove() {
        boolean flag = true;
        if (m_iNextMove == 0) {
            flag = false;
        } else {
            m_iNextMove -= 2;
            if (m_strHistory.charAt(m_iNextMove) != 'P') {
                m_iNextMove -= 3;
            }
        }
        return flag;
    }

    public void RestartAction(String s, String s1, String s2) {
        m_strPositions = s;
        m_strNodes = s1;
        m_strHistory = s2;
        // TODO
        /*
        if(s2.length() == 0)
        m_btnSave.setLabel("Load");
        else
        m_btnSave.setLabel("Save");
         * 
         */
        m_iXDim = GetNumber(m_strPositions, 0);
        m_iYDim = GetNumber(m_strPositions, 1);
        m_ptStart = new Point(GetNumber(m_strPositions, 2), GetNumber(m_strPositions, 3));
        m_ptMan = new Point(m_ptStart);
        m_ptTarget = new Point(GetNumber(m_strPositions, 4), GetNumber(m_strPositions, 5));
        int i = (m_strPositions.length() - 6) / 4;
        if ((m_strPositions.length() - 6) % 4 != 0) {
            m_iShape = GetNumber(m_strPositions, 6 + i * 4);
        } else {
            m_iShape = 0;
        }
        m_planks = new plank[i];
        for (int j = 0; j < i; j++) {
            m_planks[j] = new plank(GetNumber(m_strPositions, 6 + j * 4), GetNumber(m_strPositions, 7 + j * 4), GetNumber(m_strPositions, 8 + j * 4), GetNumber(m_strPositions, 9 + j * 4));
        }

        m_rgArea = new byte[m_iXDim][];
        for (int k = 0; k < m_iXDim; k++) {
            m_rgArea[k] = new byte[m_iYDim];
            for (int i1 = 0; i1 < m_iYDim; i1++) {
                m_rgArea[k][i1] = 0;
            }

        }

        int l = 0;
        for (int l1 = 0; l1 < m_iYDim; l1++) {
            for (int j1 = GetNumber(m_strNodes, l++); j1-- > 0;) {
                int k1 = GetNumber(m_strNodes, l++);
                m_rgArea[k1][l1] = 1;
            }

        }

        m_Dim = null;
        SquareOne();
        repaint();
    }

    public void RedoAction() {
        m_bReplay = true;
        if (m_iNextMove < m_strHistory.length()) {
            RedoMove();
        }
        m_bReplay = false;
        MarkActivePlanks();
        repaint();
    }

    public String MakeHistory() {
        String s = "Planks format 1\nPositions = \"" + m_strPositions + "\"\nNodes = \"" + m_strNodes + "\"\n";
        for (int i = 0; i < m_iNextMove;) {
            if (m_strHistory.charAt(i++) == 'P') {
                s += "P" + m_strHistory.charAt(i++) + " -> ";
            } else {
                s += "(" + m_strHistory.charAt(i) + "," + m_strHistory.charAt(i + 1) + ") - (" + m_strHistory.charAt(i + 2) + "," + m_strHistory.charAt(i + 3) + ")\n";
                i += 4;
            }
        }

        return s;
    }

    public void DrawHex(Graphics g, int i, int j, int color, int color1) {
        int ai[] = new int[6];
        int ai1[] = new int[6];
        ai[0] = m_va.m_iLMargin + (((i + i + m_iYDim) - j) * 7 - 3) * m_va.m_iScale;
        ai[1] = ai[0];
        ai[2] = ai[1] + 7 * m_va.m_iScale;
        ai[3] = ai[2] + 7 * m_va.m_iScale;
        ai[4] = ai[3];
        ai[5] = ai[4] - 7 * m_va.m_iScale;
        ai1[0] = m_va.m_iTMargin + j * 12 * m_va.m_iScale;
        ai1[1] = ai1[0] + 8 * m_va.m_iScale;
        ai1[2] = ai1[1] + 4 * m_va.m_iScale;
        ai1[3] = ai1[2] - 4 * m_va.m_iScale;
        ai1[4] = ai1[3] - 8 * m_va.m_iScale;
        ai1[5] = ai1[4] - 4 * m_va.m_iScale;
        g.setColor(color);
//TODO        g.fillPolygon(ai, ai1, 6);
        g.setColor(color1);
//TODO        g.drawPolygon(ai, ai1, 6);
    }

    public void MarkActivePlanks() {
        for (int i = 0; i < m_planks.length; i++) {
            m_planks[i].m_bTouching = m_planks[i].m_pt1.equals(m_ptMan) || m_planks[i].m_pt2.equals(m_ptMan);
        }

        boolean flag;
        do {
            flag = false;
            for (int j = 0; j < m_planks.length; j++) {
                if (m_planks[j].m_bTouching) {
                    continue;
                }
                int k;
                for (k = 0; k < m_planks.length; k++) {
                    if (!m_planks[k].m_bTouching || !m_planks[j].m_pt1.equals(m_planks[k].m_pt1) && !m_planks[j].m_pt1.equals(m_planks[k].m_pt2) && !m_planks[j].m_pt2.equals(m_planks[k].m_pt1) && !m_planks[j].m_pt2.equals(m_planks[k].m_pt2)) {
                        continue;
                    }
                    m_planks[j].m_bTouching = true;
                    flag = true;
                    break;
                }

                if (k < m_planks.length) {
                    break;
                }
            }

        } while (flag);
    }

    public void ReScale() {
        m_Dim = null;
        repaint();
    }

    int GetNumber(String s, int i) {
        char c = s.charAt(i);
        return Character.digit(c, 36);
    }

    public void OutMessage(String s, Graphics g, int i) {
//        FontMetrics fontmetrics = g.getFontMetrics();
//        int j = fontmetrics.getMaxAscent();
//        int k = fontmetrics.getMaxDescent();
//        int l = j + k;
//        int i1 = fontmetrics.stringWidth(s);
        Font font = g.getFont();
        int height = font.getHeight();
        int baseline = font.getBaselinePosition();
        int j = height - baseline;
        int k = baseline;
        int l = j + k;
        int i1 = font.stringWidth(s);
        int j1 = 0;
        int k1 = 0;
        if (i == 0) {
            if (m_iShape == 6) {
                j1 = m_va.m_iLMargin;
                k1 = (m_va.m_iTMargin + (m_iYDim * 12 + 8) * m_va.m_iScale) - k - 1;
            } else {
                j1 = (m_va.m_iLMargin + 4 * m_va.m_iScale) - 2;
                k1 = ((m_va.m_iTMargin * 3 - l) / 2 + ((m_iYDim - 1) * 8 + 2) * m_va.m_iScale) - 1;
            }
            g.setColor(clrLand);
        } else if (i == 1) {
            if (m_iShape == 6) {
                j1 = m_va.m_iLMargin + ((2 * m_iXDim + m_iYDim) * 7 * m_va.m_iScale - i1) / 2;
                k1 = (m_va.m_iTMargin + (m_iYDim * 12 + 8) * m_va.m_iScale) - k - 1;
            } else {
                j1 = (m_va.m_iLMargin + ((m_iXDim - 1) * 8 * m_va.m_iScale - i1) / 2) - 2;
                k1 = ((m_va.m_iTMargin * 3 - l) / 2 + ((m_iYDim - 1) * 8 + 2) * m_va.m_iScale) - 1;
            }
            g.setColor(clrLand);
        } else if (i == 2) {
            if (m_iShape == 6) {
                j1 = (m_va.m_iLMargin + ((2 * m_iXDim + m_iYDim) - 1) * 7 * m_va.m_iScale) - i1;
                k1 = (m_va.m_iTMargin + (m_iYDim * 12 + 8) * m_va.m_iScale) - k - 1;
            } else {
                j1 = (m_va.m_iLMargin + ((m_iXDim - 1) * 8 - 4) * m_va.m_iScale) - i1 - 2;
                k1 = ((m_va.m_iTMargin * 3 - l) / 2 + ((m_iYDim - 1) * 8 + 2) * m_va.m_iScale) - 1;
            }
            g.setColor(clrLand);
        } else {
            if (m_iShape == 6) {
                j1 = m_va.m_iLMargin + ((2 * m_iXDim + m_iYDim) * 7 * m_va.m_iScale - i1) / 2;
                k1 = (m_va.m_iTMargin - 4 * m_va.m_iScale - l) / 2 - 1;
            } else {
                j1 = (m_va.m_iLMargin + ((m_iXDim - 1) * 8 * m_va.m_iScale - i1) / 2) - 2;
                k1 = (m_va.m_iTMargin - 4 * m_va.m_iScale - l) / 2 - 1;
            }
            g.setColor(0xFFFFFF /*Color.white*/);
        }
        if (k1 < 0) {
            k1 = 0;
        }
        g.fillRect(j1, k1, i1 + 4, l + 2);
        g.setColor(clrWords);
        g.drawString(s, j1 + 2, k1 - 2 + j, Graphics.TOP | Graphics.LEFT);
    }

    public boolean DropPlankIfInRange(Point point, Point point1) {
        Point point2 = new Point();
        for (int i = 0; i < (m_iShape == 6 ? 6 : 4); i++) {
            point2.setLocation(point1);
            int j;
            for (j = 1; j <= m_plankHeld.m_iLen; j++) {
                switch (i) {
                    case 0: // '\0'
                        point2.y++;
                        if (point2.y >= m_iYDim) {
                            continue;
                        }
                        break;

                    case 1: // '\001'
                        point2.x++;
                        if (point2.x >= m_iXDim) {
                            continue;
                        }
                        break;

                    case 2: // '\002'
                        point2.y--;
                        if (point2.y < 0) {
                            continue;
                        }
                        break;

                    case 3: // '\003'
                        point2.x--;
                        if (point2.x < 0) {
                            continue;
                        }
                        break;

                    case 4: // '\004'
                        point2.x--;
                        point2.y--;
                        if (point2.x < 0 || point2.y < 0) {
                            continue;
                        }
                        break;

                    case 5: // '\005'
                        point2.x++;
                        point2.y++;
                        if (point2.x >= m_iXDim || point2.y >= m_iYDim) {
                            continue;
                        }
                        break;
                }
                if ((m_rgArea[point2.x][point2.y] & 1) != 0) {
                    break;
                }
            }

            if (j != m_plankHeld.m_iLen) {
                continue;
            }
            for (j = 0; j < m_planks.length; j++) {
                if (!m_planks[j].m_bCarried && (i < 2 || i > 4 ? PlanksCross(m_planks[j].m_pt1, m_planks[j].m_pt2, point1, point2) : PlanksCross(m_planks[j].m_pt1, m_planks[j].m_pt2, point2, point1))) {
                    break;
                }
            }

            if (j < m_planks.length) {
                continue;
            }
            plank plank1 = new plank(point2.x, point2.y, point1.x, point1.y);
            plank1.Normalise();
            if (!PointIsOverStump(point, point2) && !plank1.PointIsInside(point, m_va)) {
                continue;
            }
            if (!m_ptMan.equals(point1)) {
                MoveMan(point1);
            }
            LowerPlank(point2);
            MarkActivePlanks();
            repaint();
            break;
        }

        return m_plankHeld == null;
    }

    public void setColours(int color, int color1, int color2, int color3) {
        if (color != -1) {
            clrLand = color;
        }
        if (color1 != -1) {
            clrSwamp = color1;
        }
        if (color2 != -1) {
            clrSwampWin = color2;
        }
        if (color3 != -1) {
            clrGrid = color3;
        }
    }

    public void LowerPlank(Point point) {
        m_plankHeld.m_pt1.setLocation(m_ptMan);
        m_plankHeld.m_pt2.setLocation(point);
        m_plankHeld.Normalise();
        m_plankHeld.m_bCarried = false;
        m_plankHeld = null;
        m_iCount++;
        if (!m_bReplay) {
            String s = "L" + PlanksMIDlet.toExtendedHex(m_ptMan.x) + PlanksMIDlet.toExtendedHex(m_ptMan.y) + PlanksMIDlet.toExtendedHex(point.x) + PlanksMIDlet.toExtendedHex(point.y);
            RecordMove(s);
        }
        if (point.equals(m_ptTarget)) {
            m_ptMan.setLocation(m_ptTarget);
            m_iScreenCode = 0;
            for (int i = 0; i < m_iXDim; i++) {
                for (int j = 0; j < m_iYDim; j++) {
                    if ((m_rgArea[i][j] & 1) != 0) {
                        m_iScreenCode += (i + 1) * (j + 1);
                    }
                }

            }

        }
    }

    public void UndoAction() {
        if (UndoMove()) {
            Replay(0);
            repaint();
        }
    }

    public void drawLog(Graphics g, int color, int i, int j) {
        int k;
        int l;
        int i1;
        if (m_iShape != 6) {
            k = m_va.m_iLMargin + (i * 8 - 2) * m_va.m_iScale;
            l = m_va.m_iTMargin + (j * 8 - 2) * m_va.m_iScale;
            i1 = 4 * m_va.m_iScale;
        } else {
            k = m_va.m_iLMargin + (((2 * i + m_iYDim) - j) * 7 + 1) * m_va.m_iScale;
            l = m_va.m_iTMargin + (j * 12 + 1) * m_va.m_iScale;
            i1 = 6 * m_va.m_iScale;
        }
        g.setColor(color);
        g.fillArc(k, l, i1, i1, 0, 360);
        g.setColor(clrLogEdge);
        g.drawArc(k, l, i1, i1, 0, 360);
    }

    public void drawLog(Graphics g, int color, Point point) {
        drawLog(g, color, point.x, point.y);
    }

    public void pointerPressed(int x, int y) {
        Point point = new Point(x, y);
        if (m_plankHeld == null) {
            for (int i = 0; i < m_planks.length; i++) {
                if (m_planks[i].m_bTouching && m_planks[i].PointIsInside(point, m_va)) {
                    PickUpPlank(i);
                    repaint();
                    return;
                }
            }

        }
        if (m_plankHeld != null) {
            Point point1 = new Point(m_ptMan);
            for (int j = 0; m_plankHeld != null && j < m_planks.length; j++) {
                if (m_planks[j].m_bTouching) {
                    int k = 0;
                    do {
                        point1.setLocation(k != 0 ? m_planks[j].m_pt2 : m_planks[j].m_pt1);
                    } while (!DropPlankIfInRange(point, point1) && ++k < 2);
                }
            }

        }
    }

    public Swamp(boolean flag, /*Button button,*/ String s, String s1, String s2, boolean flag1) {
        clrOutLine = 0 /* Color.black */;
        clrWords = 0 /* Color.black */;
        clrLogEdge = 0 /* Color.black */;
        clrLogOccupied = 0xffc800 /*Color.orange*/;
        clrTarget = 0xff0000 /*Color.red*/;
        clrLand = 0x00b200 /*Color.green.darker()*/;
        clrGrid = 0 /* Color.black */;
        clrSwamp = 0x00ff00 /*Color.green*/;
        clrSwampWin = 0x00ffff /*Color.cyan*/;
        m_Dim = null;
//        m_OffScrImg = null;
        m_va = null;
        m_iScreenCode = 0;
//        if(flag)
//            setSize(352, 320);
        m_rcFlag = flag1;
//        m_btnSave = button;
        RestartAction(s, s1, s2);
    }

    public void MoveMan(Point point) {
        m_ptMan.setLocation(point);
    }

    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public void paint(Graphics g) {
        Dimension dimension = getSize();
        if (m_Dim == null || !m_Dim.equals(dimension)) {
            m_Dim = dimension;
//            m_OffScrImg = createImage(dimension.width, dimension.height);
            int i;
            int j;
            if (m_iShape == 6) {
                i = dimension.width / ((m_iXDim * 2 + 1 + m_iYDim) * 7);
                j = dimension.height / ((m_iYDim + 1) * 12);
            } else {
                i = dimension.width / (m_iXDim * 8);
                j = dimension.height / ((m_iYDim + 1) * 8);
            }
            if (i > j) {
                i = j;
            }
            if (m_iShape == 6) {
                m_va = new ViewAttr((dimension.width - (m_iXDim * 2 + m_iYDim) * 7 * i) / 2, (dimension.height - m_iYDim * 12 * i) / 2, i, m_iYDim);
            } else {
                m_va = new ViewAttr((dimension.width - (m_iXDim - 1) * 8 * i) / 2, (dimension.height - (m_iYDim - 1) * 8 * i) / 2, i, 0);
            }
        }
//        Graphics g1 = m_OffScrImg.getGraphics();
        g.setColor(clrLand);
        g.fillRect(0, 0, dimension.width, dimension.height);
        int color;
        if (m_ptMan.equals(m_ptTarget)) {
            color = clrSwampWin;
        } else {
            color = clrSwamp;
            g.setColor(clrSwamp);
        }
        if (m_iShape != 6) {
            g.setColor(color);
            g.fillRect(m_va.m_iLMargin + 4 * m_va.m_iScale, m_va.m_iTMargin - 4 * m_va.m_iScale, dimension.width - 2 * m_va.m_iLMargin - 8 * m_va.m_iScale, (dimension.height - 2 * m_va.m_iTMargin) + 8 * m_va.m_iScale);
            g.setColor(clrGrid);
        }
        for (int l1 = 0; l1 < m_iYDim; l1++) {
            if (m_iShape == 6) {
                for (int k = 1; k < m_iXDim - 1; k++) {
                    DrawHex(g, k, l1, color, clrGrid);
                }

            } else {
                g.drawLine(m_va.m_iLMargin + 8 * m_va.m_iScale, m_va.m_iTMargin + l1 * 8 * m_va.m_iScale, m_va.m_iLMargin + (m_iXDim - 2) * 8 * m_va.m_iScale, m_va.m_iTMargin + l1 * 8 * m_va.m_iScale);
            }
        }

        if (m_ptStart.x == 0) {
            if (m_iShape == 6) {
                DrawHex(g, m_ptStart.x, m_ptStart.y, clrLand, clrGrid);
            } else {
                g.drawLine(m_va.m_iLMargin, m_va.m_iTMargin + m_ptStart.y * 8 * m_va.m_iScale, m_va.m_iLMargin + 8 * m_va.m_iScale, m_va.m_iTMargin + m_ptStart.y * 8 * m_va.m_iScale);
            }
        }
        if (m_ptTarget.x == m_iXDim - 1) {
            if (m_iShape == 6) {
                DrawHex(g, m_ptTarget.x, m_ptTarget.y, clrLand, clrGrid);
            } else {
                g.drawLine(m_va.m_iLMargin + (m_iXDim - 2) * 8 * m_va.m_iScale, m_va.m_iTMargin + m_ptTarget.y * 8 * m_va.m_iScale, m_va.m_iLMargin + (m_iXDim - 1) * 8 * m_va.m_iScale, m_va.m_iTMargin + m_ptTarget.y * 8 * m_va.m_iScale);
            }
        }
        if (m_iShape != 6) {
            for (int l = 1; l < m_iXDim - 1; l++) {
                g.drawLine(m_va.m_iLMargin + l * 8 * m_va.m_iScale, m_va.m_iTMargin, m_va.m_iLMargin + l * 8 * m_va.m_iScale, m_va.m_iTMargin + (m_iYDim - 1) * 8 * m_va.m_iScale);
            }
        }
        g.setColor(clrOutLine);
        for (int i1 = 0; i1 < m_iXDim; i1++) {
            for (int i2 = 0; i2 < m_iYDim; i2++) {
                if ((m_rgArea[i1][i2] & 1) != 0) {
                    drawLog(g, clrLog, i1, i2);
                }
            }
        }

        for (int j1 = 0; j1 < m_planks.length; j1++) {
            plank plank1 = m_planks[j1];
            if (plank1.m_bTouching) {
                drawLog(g, clrLogOccupied, plank1.m_pt1);
                drawLog(g, clrLogOccupied, plank1.m_pt2);
            }
        }

        drawLog(g, clrTarget, m_ptStart);
        drawLog(g, clrTarget, m_ptTarget);
        for (int k1 = 0; k1 < m_planks.length; k1++) {
            m_planks[k1].DrawPlank(g, m_va);
        }

        if (m_ptMan.equals(m_ptTarget)) {
            if (!m_rcFlag) {
                OutMessage("Code: " + Integer.toHexString(m_iScreenCode).toUpperCase(), g, 0);
            }
            OutMessage("!! Congratulations !!", g, 3);
        }
        Credits(g);
        OutMessage("" + m_iCount, g, 2);
//        g.drawImage(m_OffScrImg, 0, 0, this);
    }

    public boolean PlanksCross(Point point, Point point1, Point point2, Point point3) {
        boolean flag = false;
        byte byte0 = 1;
        byte byte1 = 1;
        if (point.equals(point2) && point1.equals(point3)) {
            return true;
        }
        if (point.y == point1.y) {
            byte0 = 0;
        } else if (point.x == point1.x) {
            byte0 = 2;
        }
        if (point2.y == point3.y) {
            byte1 = 0;
        } else if (point2.x == point3.x) {
            byte1 = 2;
        }
        if (byte0 != byte1) {
            Point point4;
            Point point5;
            Point point6;
            Point point7;
            if (byte0 < byte1) {
                point4 = point;
                point5 = point1;
                point6 = point2;
                point7 = point3;
            } else {
                point4 = point2;
                point5 = point3;
                point6 = point;
                point7 = point1;
            }
            switch (byte0 + byte1) {
                case 1: // '\001'
                    int i = (point4.y - point6.y) + point6.x;
                    flag = i > point4.x && i < point5.x && i > point6.x && i < point7.x && point4.y > point6.y && point4.y < point7.y;
                    break;

                case 2: // '\002'
                    flag = point4.y > point6.y && point4.y < point7.y && point6.x > point4.x && point6.x < point5.x;
                    break;

                case 3: // '\003'
                    int j = (point6.x + point4.y) - point4.x;
                    flag = j > point4.y && j < point5.y && j > point6.y && j < point7.y && point6.x > point4.x && point6.x < point5.x;
                    break;
            }
        }
        return flag;
    }

    public boolean PointIsOverStump(Point point, Point point1) {
        int i = m_va.m_iLMargin + (point1.x * 8 - 2) * m_va.m_iScale;
        int j = m_va.m_iTMargin + (point1.y * 8 - 2) * m_va.m_iScale;
        return point.x >= i && point.x <= i + 4 * m_va.m_iScale && point.y >= j && point.y <= j + 4 * m_va.m_iScale;
    }

    public void RedoMove() {
        if (m_strHistory.charAt(m_iNextMove) == 'P') {
            m_iNextMove++;
            PickUpPlank(GetNumber(m_strHistory, m_iNextMove++) - 1);
            return;
        }
        if (m_strHistory.charAt(m_iNextMove) == 'L') {
            m_iNextMove++;
            int i = GetNumber(m_strHistory, m_iNextMove++);
            Point point = new Point(i, GetNumber(m_strHistory, m_iNextMove++));
            MoveMan(point);
            i = GetNumber(m_strHistory, m_iNextMove++);
            point = new Point(i, GetNumber(m_strHistory, m_iNextMove++));
            LowerPlank(point);
        }
    }

    public void Credits(Graphics g) {
        OutMessage("\251 see About", g, 1);
    }

    public void ReplayAction() {
        SquareOne();
        repaint();
    }

    public void Replay(int i) {
        m_bReplay = true;
        int j = m_iNextMove;
        SquareOne();
        while (m_iNextMove < j) {
            RedoMove();
            if (i > 0) {
                repaint();
            }
        }
        m_bReplay = false;
        MarkActivePlanks();
        repaint();
    }

    /**
     * Predicate which determines if this String matches another String
     * starting at a specified offset for each String and continuing
     * for a specified length, optionally ignoring case. Indices out of bounds
     * are harmless, and give a false result. Case comparisons are based on
     * <code>Character.toLowerCase()</code> and
     * <code>Character.toUpperCase()</code>, not on multi-character
     * capitalization expansions.
     *
     * @param ignoreCase true if case should be ignored in comparision
     * @param toffset index to start comparison at for this String
     * @param other String to compare region to this String
     * @param ooffset index to start comparison at for other
     * @param len number of characters to compare
     * @return true if regions match, false otherwise
     * @throws NullPointerException if other is null
     */
    public static boolean regionMatches(String str, int toffset,
            String other, int ooffset, int len) {
        if (toffset < 0 || ooffset < 0 || toffset + len > str.length()
                || ooffset + len > other.length()) {
            return false;
        }
//    toffset += offset;
//    ooffset += other.offset;
        while (--len >= 0) {
            char c1 = str.charAt(toffset++);
            char c2 = other.charAt(ooffset++);
            // Note that checking c1 != c2 is redundant when ignoreCase is true,
            // but it avoids method calls.
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    public void RecordMove(String s) {
        if (m_iNextMove < m_strHistory.length()) {
            int i = s.length();
//            if(!m_strHistory.regionMatches(m_iNextMove, s, 0, i))
            if (!regionMatches(m_strHistory, m_iNextMove, s, 0, i)) {
                m_strHistory = m_strHistory.substring(0, m_iNextMove) + s;
            }
            m_iNextMove += i;
            return;
        }
// TODO        if(m_strHistory.length() == 0)
//            m_btnSave.setLabel("Save");
        m_strHistory += s;
        m_iNextMove = m_strHistory.length();

    }
    
    protected void sizeChanged(int w, int h)
    {
        repaint();
    }
    
    final int clrOutLine;
    final int clrWords;
    final int clrLog = 0x909000/*new Color(144, 144, 0)*/;
    final int clrLogEdge;
    final int clrLogOccupied;
    final int clrTarget;
    int clrLand;
    int clrGrid;
    int clrSwamp;
    int clrSwampWin;
    String m_strNodes;
    String m_strPositions;
    final String m_strSave = "Save";
    final String m_strLoad = "Load";
    final byte byLog = 1;
    final int m_iLowLeft = 0;
    final int m_iLowMid = 1;
    final int m_iLowRight = 2;
    final int m_iHigh = 3;
    String m_strHistory;
    int m_iNextMove;
    int m_iCount;
// TODO    Button m_btnSave;
    Dimension m_Dim;
    ViewAttr m_va;
    final int m_iHex = 6;
    int m_iShape;
    int m_iXDim;
    int m_iYDim;
    Point m_ptMan;
    Point m_ptStart;
    Point m_ptTarget;
    plank m_planks[];
    plank m_plankHeld;
    boolean m_bReplay;
    byte m_rgArea[][];
    int m_iScreenCode;
    boolean m_rcFlag;
}
