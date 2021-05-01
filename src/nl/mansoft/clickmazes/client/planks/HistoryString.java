// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   planks.java

package nl.mansoft.clickmazes.client.planks;

import nl.mansoft.clickmazes.client.Point;

class HistoryString
{

    static boolean isWhitespace(char c)
    {
        return c == ' ' || c == '\r' || c == '\n';
    }
    
    void Skip()
    {
//        while(Character.isWhitespace(m_ch)) 
        while(isWhitespace(m_ch)) 
            if(m_iIndex < m_iLen)
            {
                m_iIndex++;
                PrimeNext();
            }
    }

    int HexToNum(char c)
    {
        int i = 0;
        if(c >= '0' && c <= '9')
            i = c - 48;
        else
        if(c >= 'A' && c <= 'Z')
            i = (c + 10) - 65;
        else
        if(c >= 'a' && c <= 'z')
            i = (c + 10) - 97;
        return i;
    }

    HistoryString(String s)
    {
        m_strSrc = s;
        m_strHistPos = null;
        m_strHistNodes = null;
        m_strHistory = null;
        m_iLen = s.length();
        m_iIndex = 0;
        m_iLine = 0;
        m_ch = '\0';
        m_iX_Dim = 0;
        m_iY_Dim = 0;
        m_iPlanksDim = 0;
        if(m_iLen > 0)
        {
            m_ch = m_strSrc.charAt(0);
            if(m_ch == '\n')
                m_iLine++;
        }
    }

    public String getHistory()
    {
        return m_strHistory;
    }

    void PrimeNext()
    {
        if(m_iIndex < m_iLen)
        {
            m_ch = m_strSrc.charAt(m_iIndex);
            if(m_ch == '\n')
            {
                m_iLine++;
                return;
            }
        } else
        {
            m_ch = '\0';
        }
    }

    int getPlankTo()
    {
        int i = 0;
        if(ExpectChar('P') && isHexDigit(m_ch))
        {
            i = HexToNum(m_ch);
            m_iIndex++;
            PrimeNext();
            Skip();
            if(!ExpectString("->"))
                i = 0;
        }
        return i;
    }

    public int getErrorLine()
    {
        return m_iLine;
    }

    boolean isHexDigit(char c)
    {
        return m_ch >= '0' && m_ch <= '9' || m_ch >= 'A' && m_ch <= 'Z' || m_ch >= 'a' && m_ch <= 'z';
    }

    public boolean BuildHistory()
    {
        boolean flag = true;
        Skip();
        if(m_iIndex == m_iLen)
            return true;
        if(ExpectString("Planks format 1"))
        {
            Skip();
            m_strHistPos = GetIdEqualsString("Positions");
            if(m_strHistPos == null)
            {
                m_strError = "Missing positions string\n";
                flag = false;
            }
        } else
        {
            m_strError = "Doesn't begin \"Planks format 1\"\n";
            flag = false;
        }
        if(flag)
            if(m_strHistPos.length() < 10)
            {
                m_strError = "Positions string is too short\n";
                flag = false;
            } else
            {
                m_iX_Dim = HexToNum(m_strHistPos.charAt(0));
                m_iY_Dim = HexToNum(m_strHistPos.charAt(1));
                if(m_iX_Dim == 0 || m_iY_Dim == 0)
                {
                    m_strError = "Positions string defines x or y to be zero length\n";
                    flag = false;
                }
            }
        if(flag)
        {
            for(int i = 2; i < (m_strHistPos.length() & -2); i += 2)
            {
                if(HexToNum(m_strHistPos.charAt(i)) >= m_iX_Dim)
                {
                    m_strError = "an x co-ordinate in the positions string is too large\n";
                    flag = false;
                    break;
                }
                if(HexToNum(m_strHistPos.charAt(i + 1)) < m_iY_Dim)
                    continue;
                m_strError = "a y co-ordinate in the positions string is too large\n";
                flag = false;
                break;
            }

        }
        if(flag)
        {
            m_iPlanksDim = (m_strHistPos.length() - 6) / 4;
            Skip();
            m_strHistNodes = GetIdEqualsString("Nodes");
            if(m_strHistNodes == null)
            {
                m_strError = "Missing nodes string\n";
                flag = false;
            }
        }
        if(flag)
        {
            m_strHistNodes.length();
            int l = 0;
            for(int j = 0; j < m_strHistNodes.length();)
            {
                if(l >= m_iY_Dim)
                {
                    m_strError = "a y co-ordinate in the nodes string is too large\n";
                    flag = false;
                    break;
                }
                int k = HexToNum(m_strHistNodes.charAt(j));
                if(j + k >= m_strHistNodes.length())
                {
                    m_strError = "a count field in the nodes string exceeds the array\n";
                    flag = false;
                    break;
                }
                for(j++; k > 0; j++)
                {
                    if(HexToNum(m_strHistNodes.charAt(j)) >= m_iX_Dim)
                        m_strError = "an x co-ordinate in the nodes string is too large\n";
                    k--;
                }

                l++;
            }

        }
        if(flag)
        {
            Skip();
            m_strHistory = "";
            Point point = new Point();
            Point point1 = new Point();
            while(m_iIndex < m_iLen) 
            {
                int i1 = getPlankTo();
                if(i1 == 0)
                {
                    m_strError = "invalid plank identification syntax\n";
                    flag = false;
                    break;
                }
                if(i1 > m_iPlanksDim)
                {
                    m_strError = "invalid plank number\n";
                    flag = false;
                    break;
                }
                m_strHistory += "P" + PlanksMIDlet.toExtendedHex(i1);
                Skip();
                if(m_iIndex >= m_iLen)
                    continue;
                if(!getCoords(point))
                {
                    flag = false;
                } else
                {
                    Skip();
                    if(!ExpectChar('-'))
                    {
                        flag = false;
                    } else
                    {
                        Skip();
                        if(!getCoords(point1))
                            flag = false;
                        else
                        if(point.x < m_iX_Dim && point.y < m_iY_Dim && point1.x < m_iX_Dim && point1.y < m_iY_Dim)
                        {
                            m_strHistory += "L" + PlanksMIDlet.toExtendedHex(point.x) + PlanksMIDlet.toExtendedHex(point.y) + PlanksMIDlet.toExtendedHex(point1.x) + PlanksMIDlet.toExtendedHex(point1.y);
                        } else
                        {
                            m_strError = "invalid plank co-ordinate\n";
                            flag = false;
                            break;
                        }
                    }
                }
                if(!flag)
                {
                    m_strError = "invalid plank destination syntax\n";
                    flag = false;
                    break;
                }
                Skip();
            }
        }
        if(!flag && m_ch == '\n' && m_iLine > 0)
            m_iLine--;
        return flag;
    }

    public String LastError()
    {
        return m_strError;
    }

    boolean ExpectChar(char c)
    {
        boolean flag = false;
        if(m_ch == c)
        {
            m_iIndex++;
            PrimeNext();
            flag = true;
        }
        return flag;
    }

    public String getNodes()
    {
        return m_strHistNodes;
    }

    boolean getCoords(Point point)
    {
        boolean flag = false;
        if(ExpectChar('(') && isHexDigit(m_ch))
        {
            point.x = HexToNum(m_ch);
            m_iIndex++;
            PrimeNext();
            Skip();
            if(ExpectChar(','))
            {
                Skip();
                if(isHexDigit(m_ch))
                {
                    point.y = HexToNum(m_ch);
                    m_iIndex++;
                    PrimeNext();
                    Skip();
                    if(ExpectChar(')'))
                        flag = true;
                }
            }
        }
        return flag;
    }

    boolean ExpectString(String s)
    {
        boolean flag = false;
        int i = s.length();
        if(m_iIndex + i <= m_iLen && Swamp.regionMatches(m_strSrc, m_iIndex, s, 0, i))
        {
            m_iIndex += i;
            PrimeNext();
            flag = true;
        }
        return flag;
    }

    String GetIdEqualsString(String s)
    {
        String s1 = null;
        if(ExpectString(s))
        {
            Skip();
            if(ExpectChar('='))
            {
                Skip();
                if(ExpectChar('"'))
                {
                    Skip();
                    int i = m_iIndex;
                    while(isHexDigit(m_ch)) 
                        if(m_iIndex < m_iLen)
                        {
                            m_iIndex++;
                            PrimeNext();
                        }
                    int j = m_iIndex;
                    Skip();
                    if(ExpectChar('"'))
                        s1 = m_strSrc.substring(i, j);
                }
            }
        }
        return s1;
    }

    public String getPos()
    {
        return m_strHistPos;
    }

    String m_strSrc;
    String m_strHistPos;
    String m_strHistNodes;
    String m_strHistory;
    String m_strError;
    int m_iLen;
    int m_iIndex;
    int m_iLine;
    char m_ch;
    int m_iX_Dim;
    int m_iY_Dim;
    int m_iPlanksDim;
}
