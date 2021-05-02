package nl.mansoft.clickmazes.client.omaze;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import nl.mansoft.clickmazes.client.Point;
import nl.mansoft.clickmazes.client.Rectangle;

public class OMaze implements EntryPoint, MouseDownHandler, MouseUpHandler  {
    class Piece extends Point {

        char type;

        Piece(int i, int j, char c) {
            super(i, j);
            type = c;
        }

        Piece(Piece piece) {
            super(((Point) (piece)).x, ((Point) (piece)).y);
            type = piece.type;
        }
    }
    
    public OMaze() {
        img = new ImageElement[8];
        imgMover = new ImageElement[4];
        backgroundGold = CssColor.make(184, 120, 0);
        brightGold = CssColor.make(255, 198, 51);
        statsGold = CssColor.make(255, 220, 61);
        lastPieceClicked = -1;
        titleFont = "bold 14px sans-serif";
        statsFont = "bold 12px sans-serif";
        hotSpot = new Rectangle[5];
        imagesLoaded = false;
        nPieces = 0;
        saveLocation = new Point[500];
        saveDir = new char[500];
        savePieces = new Piece[500][];
    }

	@Override
	public void onMouseDown(MouseDownEvent event) {
        int j = event.getClientX();
        int k = event.getClientY();
        mouseClick(j, k);
	}
	
	@Override
	public void onMouseUp(MouseUpEvent event) {
        if (lastPieceClicked != -1) {
            lastPieceClicked = -1;
            repaint();
        }
	}
	
    public void init() {
        nPuzzles = allEncStr.length;
        currentPuzNum = 0;
        curMoves = new StringBuffer();
        encStr = allEncStr[currentPuzNum];
        titleStr = allTitleStr[currentPuzNum];
        loadPuzz();
//        initHelpFrame();
		canvas.addMouseDownHandler(this);
		canvas.addMouseUpHandler(this);
/*        
        addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent keyevent) {
            }

            public void keyPressed(KeyEvent keyevent) {
                char c = keyevent.getKeyChar();
                keyPress(c);
            }

            public void keyReleased(KeyEvent keyevent) {
            }
        });
        */
    }

	private void goToPrevPuzzle() {
        if (currentPuzNum > 0) {
            currentPuzNum--;
            encStr = allEncStr[currentPuzNum];
            titleStr = allTitleStr[currentPuzNum];
            loadPuzz();
            repaint();
        }
    }

	private void goToNextPuzzle() {
        if (currentPuzNum < nPuzzles - 1) {
            currentPuzNum++;
            encStr = allEncStr[currentPuzNum];
            titleStr = allTitleStr[currentPuzNum];
            loadPuzz();
            repaint();
        }
    }

    private void undoMove() {
        moveI--;
        if (moveI < 0) {
            moveI = 0;
        } else {
            location = new Point(saveLocation[moveI]);
            dir = saveDir[moveI];
            pieces = new Piece[nPieces];
            for (int i = 0; i < nPieces; i++) {
                pieces[i] = new Piece(savePieces[moveI][i]);
            }

            repaint();
        }
    }

    private void redoMove() {
        moveI++;
        if (moveI > maxRedoI) {
            moveI = maxRedoI;
        } else {
            location = new Point(saveLocation[moveI]);
            dir = saveDir[moveI];
            pieces = new Piece[nPieces];
            for (int i = 0; i < nPieces; i++) {
                pieces[i] = new Piece(savePieces[moveI][i]);
            }

            repaint();
        }
    }

    public void keyPress(char c) {
        char c1 = Character.toLowerCase(c);
        switch (c1) {
            case 117: // 'u'
                undoMove();
                break;

            case 114: // 'r'
                redoMove();
                break;
        }
    }

    public void mouseClick(int i, int j) {
        lastPieceClicked = -1;
        if (hotSpot[0].contains(i, j)) {
            goToPrevPuzzle();
            return;
        }
        if (hotSpot[1].contains(i, j)) {
            goToNextPuzzle();
            return;
        }
        if (hotSpot[2].contains(i, j)) {
            undoMove();
            return;
        }
        if (hotSpot[3].contains(i, j)) {
            redoMove();
            return;
        }
/*        
        if (hotSpot[4].contains(i, j)) {
            helpFrame.show();
            helpFrame.toFront();
            return;
        }
*/        
        if (checkForWin()) {
            return;
        }
        int k = (i - ex) / sqsz;
        int l = (j - ey) / sqsz;
        if (!isTile(k, l)) {
            return;
        }
        if (pieceAt(k, l) >= 0) {
            lastPieceClicked = pieceAt(k, l);
            repaint();
            return;
        }
        if (location.x == k && location.y == l) {
            lastPieceClicked = -2;
            repaint();
            return;
        }
        if (!canSee(k, l)) {
            return;
        }
        int i1 = puzz[k][l];
        char c = "UDLRACSE".charAt(i1);
        if (c == 'E' || c == 'S') {
            return;
        }
        if ("UDLRACSE".charAt(i1) == 'C') {
            int j1 = "URDLU".indexOf("" + dir);
            j1++;
            dir = "URDLU".charAt(j1);
        } else if ("UDLRACSE".charAt(i1) == 'A') {
            int k1 = "URDLU".lastIndexOf("" + dir);
            k1--;
            dir = "URDLU".charAt(k1);
        } else {
            int l1 = location.x + DIFFS[i1].x;
            int j2 = location.y + DIFFS[i1].y;
            if (!isTile(l1, j2)) {
                return;
            }
            if (pieceAt(l1, j2) < 0) {
                location = new Point(l1, j2);
            } else {
                int k2 = pieceAt(l1, j2);
                int l2;
                int i3;
                if (pieces[k2].type == 'B') {
                    l2 = l1 + DIFFS[i1].x;
                    i3 = j2 + DIFFS[i1].y;
                } else {
                    l2 = l1 - DIFFS[i1].x;
                    i3 = j2 - DIFFS[i1].y;
                }
                if (isVacantTile(l2, i3)) {
                    char c1 = pieces[k2].type;
                    pieces[k2] = new Piece(l2, i3, c1);
                    location = new Point(l1, j2);
                } else {
                    return;
                }
            }
        }
        moveI++;
        maxRedoI = moveI;
        saveLocation[moveI] = new Point(location);
        saveDir[moveI] = dir;
        savePieces[moveI] = new Piece[nPieces];
        for (int i2 = 0; i2 < nPieces; i2++) {
            savePieces[moveI][i2] = new Piece(pieces[i2]);
        }

        repaint();        
    }

    public boolean checkForWin() {
        return location.equals(end);
    }

    private boolean canSee(int i, int j) {
        int k = DIFFS["UDLR".indexOf(dir)].x;
        int l = DIFFS["UDLR".indexOf(dir)].y;
        int i1 = location.x;
        int j1 = location.y;
        if (i == i1 && j == j1) {
            return false;
        }
        if (i == i1 && l != 0 && (j - j1) / l > 0) {
            return true;
        }
        return j == j1 && k != 0 && (i - i1) / k > 0;
    }

    private boolean isOnBoard(int i, int j) {
        if (i < 0 || i >= width) {
            return false;
        }
        return j >= 0 && j < height;
    }

    private boolean isTile(int i, int j) {
        return isOnBoard(i, j) && puzz[i][j] >= 0;
    }

    private boolean isVacantTile(int i, int j) {
        if (isTile(i, j)) {
            for (int k = 0; k < nPieces; k++) {
                if (((Point) (pieces[k])).x == i && ((Point) (pieces[k])).y == j) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private int pieceAt(int i, int j) {
        for (int k = 0; k < nPieces; k++) {
            if (((Point) (pieces[k])).x == i && ((Point) (pieces[k])).y == j) {
                return k;
            }
        }

        return -1;
    }

    private void initHelpFrame() {
/*    	
        helpFrame = new Frame("About Orientation Maze Applet");
        helpFrame.setBounds(50, 50, 250, 200);
        helpFrame.setLayout(new BorderLayout());
        helpFrame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent) {
                OMaze.helpFrame.hide();
            }
        });
        TextArea textarea = new TextArea("", 4, 20, 1);
        String s = "About Orientation Maze\n";
        s = s + "\nApplet copyright 2002 by James W. Stephens.\n";
        s = s + "Puzzle concept by Andrea Gilbert - 2002.\n";
        textarea.setText(s);
        textarea.setEditable(false);
        helpFrame.add("Center", textarea);
*/        
    }

    private boolean loadPuzz() {
        char c = encStr.charAt(0);
        char c1 = encStr.charAt(1);
        if (!Character.isDigit(c) || !Character.isDigit(c1)) {
            return false;
        }
        width = Character.digit(c, 16);
        height = Character.digit(c1, 16);
        puzz = new int[width][height];
        if (encStr.length() < width * height + 2) {
            return false;
        }
        int i = 2;
        start = null;
        end = null;
        for (int j = 0; j < height; j++) {
            for (int k = 0; k < width; k++) {
                char c2 = encStr.charAt(i);
                if (c2 == '.') {
                    c2 = ' ';
                }
                int i1;
                if (c2 == ' ') {
                    i1 = -1;
                } else {
                    i1 = "UDLRACSE".indexOf("" + c2);
                }
                puzz[k][j] = i1;
                if (c2 == 'S') {
                    start = new Point(k, j);
                }
                if (c2 == 'E') {
                    end = new Point(k, j);
                }
                i++;
            }

        }

        nPieces = (encStr.length() - i) / 3;
        pieces = new Piece[nPieces];
        for (int l = 0; l < nPieces; l++) {
            char c3 = encStr.charAt(i);
            i++;
            char c4 = encStr.charAt(i);
            i++;
            char c5 = encStr.charAt(i);
            i++;
            pieces[l] = new Piece(Character.digit(c4, 16), Character.digit(c5, 16), c3);
        }

        if (start == null || end == null) {
            return false;
        }
        location = start;
        dir = 'U';
        saveLocation[0] = new Point(location);
        saveDir[0] = dir;
        savePieces[0] = new Piece[nPieces];
        for (int j1 = 0; j1 < nPieces; j1++) {
            savePieces[0][j1] = new Piece(pieces[j1]);
        }

        moveI = 0;
        maxRedoI = 0;
        return true;
    }
    public void calcSqSz() {
        sqsz = 40;
        ex = (canvasWidth - sqsz * width) / 2;
        ey = (canvasHeight - sqsz * height) / 2;
        hotSpot[0] = new Rectangle(0, 0, 25, 25);
        hotSpot[1] = new Rectangle(canvasWidth  - 25, 0, 25, 25);
        hotSpot[2] = new Rectangle(canvasWidth  - 25, canvasHeight - 25, 20, 20);
        hotSpot[3] = new Rectangle(canvasWidth  - 41, canvasHeight - 25, 16, 20);
        hotSpot[4] = new Rectangle(canvasWidth  / 2 - 10, canvasHeight - 16, 20, 16);
    }

    public void repaint()
    {
        calcSqSz();
        if (checkForWin()) {
            ctx.setFillStyle(backgroundGold);
            ctx.fillRect(0, 0, canvasWidth, canvasHeight);
        } else {
            ctx.drawImage(imgBackground, 0, 0);
        }
        int i = currentPuzNum + 1;
        String s = "#" + i + ": " + titleStr;
        ctx.setFont(titleFont);
        ctx.setTextAlign(TextAlign.CENTER);
        ctx.setFillStyle(CssColor.make(0, 0, 0)); // black
        ctx.fillText(s, (canvasWidth) / 2 + 2, 20 + 2);
        ctx.setFillStyle(WHITE);
        ctx.fillText(s, (canvasWidth) / 2, 20);

        String s1 = "Move " + moveI;
        ctx.setFont(statsFont);
        ctx.setTextAlign(TextAlign.LEFT);
        ctx.setFillStyle(BLACK); // black
        ctx.fillText(s1, 5, canvasHeight - 3);
        ctx.setFillStyle(statsGold);
        ctx.fillText(s1, 3, canvasHeight - 5);

        String s2 = "?";
        ctx.setTextAlign(TextAlign.CENTER);
        ctx.setFillStyle(BLACK); // black
        ctx.fillText(s2, (canvasWidth) / 2 + 2, canvasHeight - 3);
        ctx.setFillStyle(statsGold);
        ctx.fillText(s2, (canvasWidth) / 2, canvasHeight - 5);

        ctx.setStrokeStyle(brightGold);
        for (int i1 = 0; i1 < height; i1++) {
            for (int j1 = 0; j1 < width; j1++) {
                if (puzz[j1][i1] >= 0 && img[puzz[j1][i1]] != null) {
                    ctx.drawImage(img[puzz[j1][i1]], ex + j1 * sqsz, ey + i1 * sqsz);
                    if (canSee(j1, i1) && puzz[j1][i1] < 6 && pieceAt(j1, i1) < 0) {
                    	ctx.beginPath();
                    	ctx.rect(ex + j1 * sqsz + 1, ey + i1 * sqsz + 1, sqsz - 3, sqsz - 3);
                    	ctx.rect(ex + j1 * sqsz + 2, ey + i1 * sqsz + 2, sqsz - 5, sqsz - 5);
                    	ctx.closePath();
                    	ctx.stroke();
                    }
                }
            }
        }

        if (lastPieceClicked != -2) {
            int k1 = "UDLR".indexOf("" + dir);
            if (imgMover[k1] != null) {
            	ctx.drawImage(imgMover[k1], ex + location.x * sqsz, ey + location.y * sqsz);
            }
        }
        for (int l1 = 0; l1 < nPieces; l1++) {
            if (lastPieceClicked != l1) {
                ImageElement image = imgBlocker;
                if (pieces[l1].type == 'S') {
                    image = imgSwitcher;
                }
                if (image != null) {
                	ctx.drawImage(image, ex + ((Point) (pieces[l1])).x * sqsz, ey + ((Point) (pieces[l1])).y * sqsz);
                }
            }
        }
        if (currentPuzNum > 0) {
        	ctx.drawImage(imgLeftArrow, hotSpot[0].x, hotSpot[0].y);
        }
        if (currentPuzNum < nPuzzles - 1) {
        	ctx.drawImage(imgRightArrow, hotSpot[1].x, hotSpot[1].y);
        }
        if (moveI > 0) {
        	ctx.drawImage(imgUndoArrow, hotSpot[2].x, hotSpot[2].y);
        }
        if (moveI < maxRedoI) {
        	ctx.drawImage(imgRedoArrow, hotSpot[3].x, hotSpot[3].y);
        }
    }
    
    private static ImageElement getImg(String url)
    {
    	Image.prefetch(url);
    	return (ImageElement) (new Image(url)).getElement().cast();
    }
    
    private void loadImages() {
    	String urls[] = new String[19];
        for (int i = 0; i < 8; i++) {
            urls[i] = "img" + i + ".jpg";
        }

        for (int j = 0; j < 4; j++) {
            urls[8 + j] = "pointer" + j + ".gif";
        }

        urls[12] = "background.jpg";
        urls[13] = "blocker.gif";
        urls[14] = "switcher.gif";
        urls[15] = "leftArrow.gif";
        urls[16] = "rightArrow.gif";
        urls[17] = "undoArrow.gif";
        urls[18] = "redoArrow.gif";
        ImageLoader.loadImages(urls, new ImageLoader.CallBack() {
            public void onImagesLoaded(ImageElement[] imageElements) {
                for (int i = 0; i < 8; i++) {
                    img[i] = imageElements[i];
                }

                for (int j = 0; j < 4; j++) {
                    imgMover[j] = imageElements[8 + j];
                }

                imgBackground = imageElements[12];
                imgBlocker = imageElements[13];
                imgSwitcher = imageElements[14];
                imgLeftArrow = imageElements[15];
                imgRightArrow = imageElements[16];
                imgUndoArrow = imageElements[17];
                imgRedoArrow = imageElements[18];
                
        		canvas = Canvas.createIfSupported();
        		if (canvas != null)
        		{
        		    // init the canvas
        		    canvas.setWidth(canvasWidth + "px");
        		    canvas.setHeight(canvasHeight + "px");
        		    canvas.setCoordinateSpaceWidth(canvasWidth);
        		    canvas.setCoordinateSpaceHeight(canvasHeight);
        			ctx = canvas.getContext2d();
            		RootPanel.get().add(canvas);             
        			init();
        	        repaint();
        		}
            }            
          });
    }

    private String allEncStr[] = {
        "35.E.DRCAUAULC.S.",
        "35.E.RCLCAURUR.S.",
        "46..E.CURCDLRDAUAACUCC.S..",
        "46..E.RURDDLLDACUAUUCU.S..",
        "46..E.DRRUDLCCAUDUALRU.S..",
        "46..E.CURCULRDALLACURC.S..",
        "46..E.DUCURAALCCDUUURA.S..",
        "46..E.UARCRLDLULCRAURL.S..",
        "46..E.RCDUUCLDRCALDUCU.S..",
        "46..E.ACRDULUCACRALULC.S..",
        // Second set
        "35.E.DLCCUARRU.S.S02",
        "46..E.RRRUDLLLALCCCUCU.S..S03",
        "46..E.RCDLDCCRDACLAUUL.S..S01",
        "46..E.UAUDRCLLUALAAUCU.S..S13",
        "46..E.DUDLCARURRCULUCU.S..S33",
        "46..E.LDRDCLRAARURDULR.S..S12",
        "46..E.RUDUURRAALDLDUAD.S..S01S11",
        "46..E.DURDRCLAACULULUD.S..S02S12",
        "46..E.RDCDRADAACUCUULC.S..S11S22",
        "46..E.UDRDDRDAALCUDUAL.S..S01S32",
        "46..E.DRCDRUDLCCAURDUL.S..S11S23",
        "46..E.RLUACALDARUDUURA.S..S04S12",
        "46..E.UALDUARCDLCLRUUL.S..S01S11S33",
        "46..E.RARDRCCLRUALUUUL.S..S04S14S24S34",
        "46..E.DCLDCACCRAUCUULU.S..S12S13S22S23",
        "46..E.DRCURUALCDURRCUL.S..S01S04S34",
        "46..E.DLLLRRRURRCCDUUL.S..S03S13S23S33",
        "46..E.DRRDAAALCRCLUUUL.S..S03S12",
        "46..E.DRCLUDALDUCUULUC.S..S21S31S33"
    };
    private String titleStr;
    private String encStr;
    private String allTitleStr[] = {
        "3x3 Warm-up [12]",
        "3x3 Warm-up [12]",
        "The Corridor [27]",
        "Turbulence [29]",
        "Black Hole [27]",
        "Edge Spin [29]",
        "Sticky Corner [29]",
        "Two Step [27]",
        "The Wall [29]",
        "End Game [28]",
        // Second set
        "16 moves",
        "63 moves",
        "54 moves",
        "51 moves",
        "81 moves",
        "82 moves",
        "41 moves",
        "38 moves",
        "50 moves",
        "69 moves",
        "78 moves",
        "62 moves",
        "45 moves",
        "30 moves",
        "34 moves",
        "54 moves",
        "104 moves",
        "144 moves",
        "188 moves"
    };
    private int currentPuzNum;
    private int nPuzzles;
    private StringBuffer curMoves;
    private int puzz[][];
//    private Image offScrImg;
//    private static Frame helpFrame;
//    private Dimension curDim;
    private int sqsz;
    private int ex;
    private int ey;
    private static final int maxSqsz = 40;
    private int width;
    private int height;
    private ImageElement img[];
    private ImageElement imgMover[];
    private ImageElement imgBackground;
    private ImageElement imgBlocker;
    private ImageElement imgSwitcher;
    private ImageElement imgLeftArrow;
    private ImageElement imgRightArrow;
    private ImageElement imgUndoArrow;
    private ImageElement imgRedoArrow;
    private CssColor backgroundGold;
    private CssColor brightGold;
    private CssColor statsGold;
    private int lastPieceClicked;
    private String titleFont;
    private String statsFont;
    private Rectangle hotSpot[];
    private boolean imagesLoaded;
    private static final String ARROWS = "UDLRACSE";
    private static final String DIRECTIONS = "UDLR";
    private static final Point DIFFS[] = {
        new Point(0, -1), new Point(0, 1), new Point(-1, 0), new Point(1, 0)
    };
    private Point start;
    private Point end;
    private Point location;
    private Piece pieces[];
    private int nPieces;
    private char dir;
    private Point saveLocation[];
    private char saveDir[];
    private Piece savePieces[][];
    private int moveI;
    private int maxRedoI;
    Canvas canvas;
    Context2d ctx;
    // canvas size, in px
    static final int canvasHeight = 300;
    static final int canvasWidth = 300;
    static final CssColor BLACK = CssColor.make("black");
    static final CssColor WHITE = CssColor.make("white");
    
    @Override
	public void onModuleLoad() {
        loadImages();
	}
}
