package XXLChess;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import processing.event.MouseEvent;
import XXLChess.Pieces.*;
import jogamp.opengl.util.av.JavaSoundAudioSink;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.jogamp.newt.event.KeyEvent;

import java.awt.Font;
import java.io.*;
import java.util.*;
import java.lang.Math;

public class App extends PApplet {

    public static final boolean DEBUG = false;

    public static final int SPRITESIZE = 480;
    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 120;
    public static final int BOARD_WIDTH = 14;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;
    public static double pieceMovementSpd;
    public static double maxMovementTime;

    protected Tile[][] board;
    protected Piece selPiece;
    protected Tile selTile;
    private Tile destTile;
    private Tile originTile;
    private Tile prevDest;
    private Tile prevOrigin;
    private Piece movementPiece;
    private double movementX;
    private double movementY;
    protected boolean playerBlack;
    protected boolean currentTurn = false;

    protected static boolean bKingInCheck = false;
    protected static boolean wKingInCheck = false;
    private static boolean gameEnded = false;
    protected static Tile bKingPos;
    protected static Tile wKingPos;
    public static final int FPS = 60;
    public static int animation = 0;
    public static boolean inAnimation = false;
    public static int pSeconds;
    private static int pRemainingTime;
    public static int pIncrement;
    public static int cSeconds;
    public static int cIncrement;
    private static int cRemainingTime;
    private static String message = "";
    public static int pTick = 0;
    public static int cTick = 0;

    public String configPath = "config.json";
    public JSONObject json;

    public App() {
        //reading the config file
        this.board = new Tile[BOARD_WIDTH][BOARD_WIDTH];
        boolean dark;
        for(int i = 0; i < BOARD_WIDTH; i++){
            if((i%2) == 0){
                dark = true;
            }else{
                dark = false;
            }
            for(int j = 0; j < BOARD_WIDTH; j++){
                if(dark){
                    dark = false;
                }else{
                    dark = true;
                }
                board[i][j] = new Tile(CELLSIZE, i, j, dark, (Tile.getRowNames(i) + Tile.getColumnNames(j)));
            }
        }
    }


    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);
        noStroke();

		// load config
        JSONObject conf = loadJSONObject(new File(this.configPath));
        String layout = conf.getString("layout");
        JSONObject timeControls = conf.getJSONObject("time_controls");
        JSONObject player = timeControls.getJSONObject("player");
        JSONObject cpu = timeControls.getJSONObject("cpu");
        pSeconds = player.getInt("seconds");
        pIncrement = player.getInt("increment");
        cSeconds = cpu.getInt("seconds");
        cIncrement = cpu.getInt("increment");
        String playerColour = conf.getString("player_colour");
        if(playerColour.equals("white")){
            playerBlack = false;
        }else{
            playerBlack = true;
        }
        pieceMovementSpd = conf.getDouble("piece_movement_speed");
        maxMovementTime = conf.getDouble("max_movement_time");

        try{
            File layoutFile = new File(layout);
            Scanner scan = new Scanner(layoutFile);
            String[] layoutRows = new String[14];
            int j = 0;
            while(scan.hasNextLine()){
                String layoutLine = scan.nextLine();
                int layoutLength = layoutLine.length();
                if(layoutLength < 14){
                    for(int i = 0; i < (14 - layoutLength); i++){
                        layoutLine += " ";
                    }
                }else if(layoutLength > 14){
                    throw new ArithmeticException();
                }
                layoutRows[j] = layoutLine;
                j += 1;
            }
            scan.close();
            if(j < 13){
                throw new ArithmeticException();
            }
            boolean bKing = false;
            boolean wKing = false;
            for(int i = 0; i < 14; i++){
                String rowPieces = layoutRows[i];
                for(int h = 0; h < 14; h++){
                    char placePiece = rowPieces.charAt(h);
                    switch(placePiece){
                        case 'R': board[i][h].updatePiece(new Rook(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'r': board[i][h].updatePiece(new Rook(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'N': board[i][h].updatePiece(new Knight(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'n': board[i][h].updatePiece(new Knight(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'B': board[i][h].updatePiece(new Bishop(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'b': board[i][h].updatePiece(new Bishop(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'H': board[i][h].updatePiece(new Archbishop(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'h': board[i][h].updatePiece(new Archbishop(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'E': board[i][h].updatePiece(new Knook(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'e': board[i][h].updatePiece(new Knook(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'C': board[i][h].updatePiece(new Camel(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'c': board[i][h].updatePiece(new Camel(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'G': board[i][h].updatePiece(new General(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'g': board[i][h].updatePiece(new General(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'A': board[i][h].updatePiece(new Amazon(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'a': board[i][h].updatePiece(new Amazon(board[i][h].getX(), board[i][h].getY(), false)); break;
                        case 'K': {
                            board[i][h].updatePiece(new King(board[i][h].getX(), board[i][h].getY(), true)); 
                            if(bKing){
                                throw new IllegalArgumentException();
                            }
                            bKing = true;
                            bKingPos = board[i][h];
                            break;
                        }
                        case 'k': {
                            board[i][h].updatePiece(new King(board[i][h].getX(), board[i][h].getY(), false)); 
                            if(wKing){
                                throw new IllegalArgumentException();
                            }
                            wKing = true;
                            wKingPos = board[i][h];
                            break;
                        }
                        case 'P': board[i][h].updatePiece(new Pawn(board[i][h].getX(), board[i][h].getY(), true)); break;
                        case 'p': board[i][h].updatePiece(new Pawn(board[i][h].getX(), board[i][h].getY(), false)); break;
                    }
                }
            }
            if(!bKing || !wKing){
                throw new IllegalArgumentException();
            }
        // errors
        }catch(FileNotFoundException e){
            System.out.println("Layout file not found");
            win(4);
        }catch(ArithmeticException e){
            System.out.println("Invalid length of the layout file");
            win(4);
        }catch(NullPointerException e){
            System.out.println("Invalid length of the layout file");
            win(4);
        }catch(IllegalArgumentException e){
            System.out.println("Error: Invalid number of Kings");
            win(4);
        }
        // loading images defined in the piece classes
        for(int j = 0; j < BOARD_WIDTH; j++){
            for(int i = 0; i < BOARD_WIDTH; i++){
                if(board[i][j].getHeldPiece() != null){
                    board[i][j].getHeldPiece().setSprite(this.loadImage(board[i][j].getHeldPiece().getSpriteString()));
                    board[i][j].getHeldPiece().getSprite().resize(48, 0);
                }
            }
        }

        currentTurn = false;
        pRemainingTime = pSeconds;
        cRemainingTime = cSeconds;
        if(playerBlack){
            message = "AI's turn...";
        }else{
            message = "Player's turn...";
        }
        updateAll();
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        if (this.keyCode == 69) {
            win(3);
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(mouseX < 672){
            int boardY = Math.floorDiv(mouseX, CELLSIZE);
            int boardX = Math.floorDiv(mouseY, CELLSIZE);
            if(!inAnimation && !gameEnded){
                if(DEBUG){
                    Tile thisTile = this.board[boardX][boardY];
                    System.out.println();
                    System.out.println(thisTile.getTileName() + " clicked.");
                    System.out.println("White pieces that can attack here:");
                    for(Tile t: thisTile.getAttackedWhite()){
                        System.out.print(t.getTileName() + ", ");
                    }
                    System.out.println("Black pieces that can attack here:");
                    for(Tile t: thisTile.getAttackedBlack()){
                        System.out.print(t.getTileName() + ", ");
                    }
                    if(thisTile.getHeldPiece() != null){
                        System.out.println("Contained piece: ");
                        System.out.println(thisTile.getHeldPiece().getPieceName());
                        System.out.println("Can legally attack:");
                        for(Tile t: thisTile.getLegalAttacks()){
                            System.out.println(t.getTileName());
                        }
                    }
                    System.out.println("BKing pos: " + bKingPos.getTileName());
    
                }
                if(selTile == null){
                    if(board[boardX][boardY].getHeldPiece() != null){
                        if(board[boardX][boardY].getHeldPiece().isBlack() == playerBlack){
                            select(boardX, boardY);
                        }
                    }
                }else if(selTile == board[boardX][boardY]){
                    deselect();
                }else if(selTile.legalMoves.contains(board[boardX][boardY])){
                    move(selTile, board[boardX][boardY]);
                }else if(selTile.legalAttacks.contains(board[boardX][boardY])){
                    move(selTile, board[boardX][boardY]);
                }else{
                    deselect();
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        if(animation > 0){
            animation --;
            movementPiece.incX(movementX);
            movementPiece.incY(movementY);
        }
        if((animation == 0) && inAnimation){
            inAnimation = false;
            completeMovement();
        }
        background(200, 200, 200);
        fill(0,0,0);
        textSize(35);
        if(playerBlack){
            text(toTimeString(pRemainingTime),690,50);
            text(toTimeString(cRemainingTime),690,622);
        }else{
            text(toTimeString(cRemainingTime),690,50);
            text(toTimeString(pRemainingTime),690,622);
        }
        textSize(15);
        text(message, 690,250,100,300);
        if(!gameEnded){
            if((playerBlack == currentTurn)){
                pTick ++;
                if(pTick >= 60){
                    pTick = 0;
                    pRemainingTime --;
                }
            }else{
                cTick ++;
                if(cTick >= 60){
                    cTick = 0;
                    cRemainingTime --;
                }
            }
        }
        if(pRemainingTime <= 0 || cRemainingTime <= 0){
            win(1);
        }
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_WIDTH; j++){
                this.board[i][j].draw(this);
                if(this.board[i][j].getHeldPiece() != null){
                    this.board[i][j].getHeldPiece().draw(this);
                }
            }
        }
        if(animation > 0){
            movementPiece.draw(this);
        }
    }
	
	// Add any additional methods or attributes you want. Please put classes in different files.
    public void select(int x, int y){
        this.selTile = board[x][y];
        this.selTile.updateStatus(3);
        ArrayList<Tile> moves = this.selTile.getLegalMoves();
        ArrayList<Tile> attacks = this.selTile.getLegalAttacks();
        for(Tile t:moves){
            t.updateStatus(1);
        }
        for(Tile t:attacks){
            t.updateStatus(4);
        }
    }

    public void deselect(){
        this.selTile.updateStatus(0);
        ArrayList<Tile> moves = this.selTile.getLegalMoves();
        ArrayList<Tile> attacks = this.selTile.getLegalAttacks();
        for(Tile t:moves){
            t.prevStatus();
        }
        for(Tile t:attacks){
            t.prevStatus();
        }
        if(bKingInCheck){
            bKingPos.updateStatus(5);
        }
        if(wKingInCheck){
            wKingPos.updateStatus(5);
        }
        this.selTile = null;
    }

    public void move(Tile origin, Tile destination){
        movementPiece = origin.getHeldPiece();
        destTile = destination;
        originTile = origin;
        // Intentionally calling this twice to clear PrevStatus for Tile
        if(prevOrigin != null){
            prevOrigin.updateStatus(0);
            prevOrigin.updateStatus(0);
        }
        if(prevDest != null){
            prevDest.updateStatus(0);
            prevDest.updateStatus(0);
        }
        prevOrigin = originTile;
        prevDest = destTile;
        int framesReq;
        double deltaX = destination.getX() - movementPiece.getX();
        double deltaY = destination.getY() - movementPiece.getY();
        double distance = Math.sqrt(Math.pow((deltaX), 2) + Math.pow((deltaY), 2));
        if((distance / (pieceMovementSpd * FPS)) < maxMovementTime){
            framesReq = Math.floorDiv((int)distance, (int)pieceMovementSpd);
            movementX = ((pieceMovementSpd/distance) * deltaX);
            movementY = ((pieceMovementSpd/distance) * deltaY);
        }else{
            double velocity = distance / FPS * maxMovementTime;
            framesReq = (int)maxMovementTime * FPS;
            movementX = ((velocity/distance) * deltaX);
            movementY = ((velocity/distance) * deltaY);
        }
        animation = framesReq;
        inAnimation = true;
        if(selTile != null){
            deselect();
        }
        originTile.updateStatus(2);
        destTile.updateStatus(2);
    }

    public void completeMovement(){
        movementPiece.setX(destTile.getX());
        movementPiece.setY(destTile.getY());
        if((movementPiece.getPieceName() == "P") || ((movementPiece.getPieceName() == "p"))){
            ((Pawn) movementPiece).setHasMoved(true);
        }else if((movementPiece.getPieceName().equals("K"))){
            bKingPos = destTile;
        }else if(((movementPiece.getPieceName().equals("k")))){
            wKingPos = destTile;
        }
        originTile.updatePiece(null);
        if(movementPiece.getPieceName() == "P" && destTile.getXPos() == 7){
            destTile.updatePiece(new Queen(destTile.getX(), destTile.getY(), true));
            destTile.getHeldPiece().setSprite(this.loadImage(destTile.getHeldPiece().getSpriteString()));
            destTile.getHeldPiece().getSprite().resize(48, 0);
        }else if(movementPiece.getPieceName() == "p" && destTile.getXPos() == 6){
            destTile.updatePiece(new Queen(destTile.getX(), destTile.getY(), false));
            destTile.getHeldPiece().setSprite(this.loadImage(destTile.getHeldPiece().getSpriteString()));
            destTile.getHeldPiece().getSprite().resize(48, 0);
        }else{
            destTile.updatePiece(movementPiece);
        }
        movementPiece = null;
        movementX = 0;
        movementY = 0;
        if(playerBlack == currentTurn){
            pRemainingTime += 2;
        }else{
            cRemainingTime += 2;
        }
        updateAll();
        switchTurn();
    }

    public HashMap<Integer, ArrayList<Tile>> checkMoves(Tile t){
        Piece heldPiece = t.getHeldPiece();
        int x = t.getXPos();
        int y = t.getYPos();
        int[] moveset = heldPiece.getMoveset();
        // 0 = can move to this tile
        // 1 = protecting this tile
        // 2 = can attack the piece on this tile
        HashMap<Integer, ArrayList<Tile>> moves = new HashMap<Integer, ArrayList<Tile>>();
        ArrayList<Tile> canMove = new ArrayList<Tile>();
        ArrayList<Tile> canProtect = new ArrayList<Tile>();
        ArrayList<Tile> canCapture = new ArrayList<Tile>();
        moves.put(0,canMove);
        moves.put(1,canProtect);
        moves.put(2,canCapture);
        boolean black = heldPiece.isBlack();
        if(!(heldPiece.getPieceName() == "P" || heldPiece.getPieceName() == "p")){
            if(moveset[0] != 0){
                // moveset for rook-like (straight line) movement
                for(int i = 0; i < moveset[0]; i++){
                    int j = x + i + 1;
                    if(j >= BOARD_WIDTH){
                        break;
                    }else{
                        if(board[j][y].heldPiece == null){
                            canMove.add(board[j][y]);
                            canProtect.add(board[j][y]);
                        }else if(board[j][y].heldPiece.isBlack() == black){
                            canProtect.add(board[j][y]);
                            break;
                        }else if(board[j][y].heldPiece.isBlack() != black){
                            canProtect.add(board[j][y]);
                            canCapture.add(board[j][y]);
                            break;
                        }
                    }
                }
                for(int i = 0; i < moveset[0]; i++){
                    int j = x - i - 1;
                    if(j < 0){
                        break;
                    }else{
                        if(board[j][y].heldPiece == null){
                            canMove.add(board[j][y]);
                            canProtect.add(board[j][y]);
                        }else if(board[j][y].heldPiece.isBlack() == black){
                            canProtect.add(board[j][y]);
                            break;
                        }else if(board[j][y].heldPiece.isBlack() != black){
                            canProtect.add(board[j][y]);
                            canCapture.add(board[j][y]);
                            break;
                        }
                    }
                }
                for(int i = 0; i < moveset[0]; i++){
                    int j = y + i + 1;
                    if(j >= BOARD_WIDTH){
                        break;
                    }else{
                        if(board[x][j].heldPiece == null){
                            canMove.add(board[x][j]);
                            canProtect.add(board[x][j]);
                        }else if(board[x][j].heldPiece.isBlack() == black){
                            canProtect.add(board[x][j]);
                            break;
                        }else if(board[x][j].heldPiece.isBlack() != black){
                            canProtect.add(board[x][j]);
                            canCapture.add(board[x][j]);
                            break;
                        }
                    }
                }
                for(int i = 0; i < moveset[0]; i++){
                    int j = y - i - 1;
                    if(j < 0){
                        break;
                    }else{
                        if(board[x][j].heldPiece == null){
                            canMove.add(board[x][j]);
                            canProtect.add(board[x][j]);
                        }else if(board[x][j].heldPiece.isBlack() == black){
                            canProtect.add(board[x][j]);
                            break;
                        }else if(board[x][j].heldPiece.isBlack() != black){
                            canProtect.add(board[x][j]);
                            canCapture.add(board[x][j]);
                            break;
                        }
                    }
                }
            }
            if(moveset[1] != 0){
                for(int i = 0; i < moveset[1]; i++){
                    int j = x + i + 1;
                    int k = y + i + 1;
                    if((j >= BOARD_WIDTH) || (k >= BOARD_WIDTH)){
                        break;
                    }else{
                        if(board[j][k].heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                            break;
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                            break;
                        }
                    }
                }
                for(int i = 0; i < moveset[1]; i++){
                    int j = x - i - 1;
                    int k = y - i - 1;
                    if((j < 0) || (k < 0)){
                        break;
                    }else{
                        if(board[j][k].heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                            break;
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                            break;
                        }
                    }
                }
                for(int i = 0; i < moveset[1]; i++){
                    int j = x + i + 1;
                    int k = y - i - 1;
                    if((j >= BOARD_WIDTH) || (k < 0)){
                        break;
                    }else{
                        if(board[j][k].heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                            break;
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                            break;
                        }
                    }
                }
                for(int i = 0; i < moveset[1]; i++){
                    int j = x - i - 1;
                    int k = y + i + 1;
                    if((k >= BOARD_WIDTH) || (j < 0)){
                        break;
                    }else{
                        if(board[j][k].heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                            break;
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                            break;
                        }
                    }
                }
            }
            if(moveset[2] != 0){
                for(int i = 0; i < 2; i++){
                    int j = (x + (i * 2)) - 1;
                    int k = moveset[2] + y;
                    if((!(j < 0) && (j < BOARD_WIDTH)) && (!(k < 0) && (k < BOARD_WIDTH))){
                        if((board[j][k]).heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                        }
                    }
                }
                for(int i = 0; i < 2; i++){
                    int j = (x + (i * 2)) - 1;
                    int k = y - moveset[2];
                    if((!(j < 0) && (j < BOARD_WIDTH)) && (!(k < 0) && (k < BOARD_WIDTH))){
                        if((board[j][k]).heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                        }
                    }
                }
                for(int i = 0; i < 2; i++){
                    int j = moveset[2] + x;
                    int k = (y + (i * 2)) - 1;
                    if((!(j < 0) && (j < BOARD_WIDTH)) && (!(k < 0) && (k < BOARD_WIDTH))){
                        if((board[j][k]).heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                        }
                    }
                }
                for(int i = 0; i < 2; i++){
                    int j = x - moveset[2];
                    int k = (y + (i * 2)) - 1;
                    if((!(j < 0) && (j < BOARD_WIDTH)) && (!(k < 0) && (k < BOARD_WIDTH))){
                        if((board[j][k]).heldPiece == null){
                            canMove.add(board[j][k]);
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() == black){
                            canProtect.add(board[j][k]);
                        }else if(board[j][k].heldPiece.isBlack() != black){
                            canProtect.add(board[j][k]);
                            canCapture.add(board[j][k]);
                        }
                    }
                }
            }
        }else{
            boolean hasMoved = ((Pawn) heldPiece).getHasMoved();
            if(black){
                if((x + 1) < BOARD_WIDTH){
                    if((y + 1) < BOARD_WIDTH){
                        // black pawn capture right
                        if(board[x + 1][y + 1].getHeldPiece() != null){
                            if(board[x + 1][y + 1].getHeldPiece().isBlack() == black){
                                canProtect.add(board[x + 1][y + 1]);
                            }else{
                                canProtect.add(board[x + 1][y + 1]);
                                canCapture.add(board[x + 1][y + 1]);
                            }
                        }else{
                            canProtect.add(board[x + 1][y + 1]);
                        }
                    }
                    if((y - 1) >= 0){
                        // black pawn capture left
                        if(board[x + 1][y - 1].getHeldPiece() != null){
                            if(board[x + 1][y - 1].getHeldPiece().isBlack() == black){
                                canProtect.add(board[x + 1][y - 1]);
                            }else{
                                canProtect.add(board[x + 1][y - 1]);
                                canCapture.add(board[x + 1][y - 1]);
                            }
                        }else{
                            canProtect.add(board[x + 1][y - 1]);
                        }
                    }
                    if(board[x + 1][y].getHeldPiece() == null){
                        canMove.add(board[x + 1][y]);
                        if(!(hasMoved)){
                            if(board[x + 2][y].getHeldPiece() == null){
                                canMove.add(board[x + 2][y]);
                            }
                        }
                    }
                }
            }else{
                if((x - 1) > 0){
                    if((y + 1) < BOARD_WIDTH){
                        // white pawn capture right
                        if(board[x - 1][y + 1].getHeldPiece() != null){
                            if(board[x - 1][y + 1].getHeldPiece().isBlack() == black){
                                canProtect.add(board[x - 1][y + 1]);
                            }else{
                                canProtect.add(board[x - 1][y + 1]);
                                canCapture.add(board[x - 1][y + 1]);
                            }
                        }else{
                            canProtect.add(board[x - 1][y + 1]);
                        }
                    }
                    if((y - 1) >= 0){
                        // white pawn capture left
                        if(board[x - 1][y - 1].getHeldPiece() != null){
                            if(board[x - 1][y - 1].getHeldPiece().isBlack() == black){
                                canProtect.add(board[x - 1][y - 1]);
                            }else{
                                canProtect.add(board[x - 1][y - 1]);
                                canCapture.add(board[x - 1][y - 1]);
                            }
                        }else{
                            canProtect.add(board[x - 1][y - 1]);
                        }
                    }
                    if(board[x - 1][y].getHeldPiece() == null){
                        canMove.add(board[x - 1][y]);
                        if(!(hasMoved)){
                            if(board[x - 2][y].getHeldPiece() == null){
                                canMove.add(board[x - 2][y]);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    public void commitMoves(HashMap<Integer, ArrayList<Tile>> moves, Tile commitTile){
        ArrayList<Tile> canMove = moves.get(0);
        ArrayList<Tile> canProtect = moves.get(1);
        ArrayList<Tile> canCapture = moves.get(2);
        for(Tile t: canMove){
            commitTile.addMoves(t);
        }
        for(Tile t: canProtect){
            t.addAttackedBy(commitTile);
        }
        for(Tile t: canCapture){
            commitTile.addAttackable(t);
        }
    }


    public boolean checkLegal(Tile origin, Tile dest){
        /* 
         * Checks whether a move is legal by making the move, seeing if the current turn
         * colour's king is in check, and moves the piece back to the original position
         */
        Piece destPiece;
        boolean hasHeldPiece = dest.getHeldPiece() != null;
        boolean isLegal = true;
        if(dest.getHeldPiece() != null){
            destPiece = dest.getHeldPiece();
        }else{
            destPiece = null;
        }
        Piece originPiece = origin.getHeldPiece();
        if(originPiece.getPieceName().equals("K")){
            bKingPos = dest;
        }else if(originPiece.getPieceName().equals("k")){
            wKingPos = dest;
        }
        dest.updatePiece(originPiece);
        origin.updatePiece(null);
        for(int i = 0; (i < BOARD_WIDTH) && isLegal; i++){
            for(int j = 0; (j < BOARD_WIDTH) && isLegal; j++){
                if(this.board[i][j].getHeldPiece() != null){
                    ArrayList<Tile> moves = checkMoves(this.board[i][j]).get(2);
                    for(Tile t:moves){
                        if(!currentTurn && (bKingPos == t) && (!this.board[i][j].getHeldPiece().isBlack())){
                            isLegal = false;
                            break;
                        }else if(currentTurn && (wKingPos == t) && (this.board[i][j].getHeldPiece().isBlack())){
                            isLegal = false;
                            break;
                        }
                    }
                }
            }
        }
        origin.updatePiece(originPiece);
        if(hasHeldPiece){
            dest.updatePiece(destPiece);
        }else{
            dest.updatePiece(null);
        }
        if(originPiece.getPieceName().equals("K")){
            bKingPos = origin;
        }else if(originPiece.getPieceName().equals("k")){
            wKingPos = origin;
        }
        return isLegal;
    }


    public void updateAll(){
        bKingInCheck = false;
        wKingInCheck = false;
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_WIDTH; j++){
                this.board[i][j].resetMoves();
            }
        }
        // generates psuedo legal moves for all pieces
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_WIDTH; j++){
                if(this.board[i][j].getHeldPiece() != null){
                    commitMoves(checkMoves(this.board[i][j]), this.board[i][j]);
                }
            }
        }
        // checks if pseudo legal moves are legal for all pieces
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_WIDTH; j++){
                ArrayList<Tile> moves = this.board[i][j].getMoves();
                ArrayList<Tile> attacks = this.board[i][j].getAttackable();
                for(Tile t: moves){
                    if(checkLegal(this.board[i][j], t)){
                        this.board[i][j].addLegalMoves(t);
                    }
                }
                for(Tile t: attacks){
                    if(checkLegal(this.board[i][j], t)){
                        this.board[i][j].addLegalAttacks(t);
                        if((this.board[i][j].getHeldPiece().isBlack()) && t == wKingPos){
                            wKingInCheck = true;
                            wKingPos.updateStatus(5);
                        }
                        if(!(this.board[i][j].getHeldPiece().isBlack()) && t == bKingPos){
                            bKingInCheck = true;
                            bKingPos.updateStatus(5);
                        }
                    }
                }
            }
        }
        if(!wKingInCheck){
            if(wKingPos == prevDest){
                wKingPos.updateStatus(2);
            }else{
                wKingPos.updateStatus(0);
            }
        }
        if(!bKingInCheck){
            if(bKingPos == prevDest){
                bKingPos.updateStatus(2);
            }else{
                bKingPos.updateStatus(0);
            }
        }
    }

    public void switchTurn(){
        currentTurn = !currentTurn;
        ArrayList<Tile[]> possibleMoves = getPossibleMoves();
        checkWin(possibleMoves);
        if(possibleMoves.size() != 0){
            if(!(playerBlack == currentTurn)){
                if(!(wKingInCheck || bKingInCheck)){
                    message = "AI's turn...";
                }else{
                    message = "Check!";
                }
                simpleAi(possibleMoves);
            }else{
                if(!(wKingInCheck || bKingInCheck)){
                    message = "Player's turn...";
                }else{
                    message = "Check!";
                }
            }
        }
    }

    public String toTimeString(int s){
        String timeString;
        int mins = 0;
        if(s > 59){
            mins = Math.floorDiv(s, 60);
            s -= 60 * mins;
        }
        if(s < 10){
            timeString = Integer.toString(mins) + ":0" + Integer.toString(s);
        }else{
            timeString = Integer.toString(mins) + ":" + Integer.toString(s);
        }
        return timeString;
    }

    public void win(int status){
        gameEnded = true;
        switch(status){
            case 0:
                if(playerBlack == currentTurn){
                    message = "You lost by checkmate!";
                }else{
                    message = "You won by checkmate!";
                }
                break;
            case 1:
                if(pRemainingTime <= 0){
                    message = "You lost by time!";
                }
                if(cRemainingTime <= 0){
                    message = "You won by time!";
                }
                break;
            case 2:
                message = "Stalemate!";
                break;
            case 3:
                message = "You resigned!";
                break;
            case 4:
                message = "Error reading config file";
                break;
        }
    }

    public ArrayList<Tile[]> getPossibleMoves(){
        ArrayList<Tile[]> possibleMoves = new ArrayList<Tile[]>();
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_WIDTH; j++){
                if(this.board[i][j].getHeldPiece() != null){
                    if(this.board[i][j].getHeldPiece().isBlack() != !currentTurn){
                        ArrayList<Tile> moves = this.board[i][j].getLegalMoves();
                        ArrayList<Tile> attacks = this.board[i][j].getLegalAttacks();
                        for(Tile t:moves){
                            Tile[] m = {this.board[i][j], t};
                            possibleMoves.add(m);
                        }
                        for(Tile t:attacks){
                            Tile[] m = {this.board[i][j], t};
                            possibleMoves.add(m);
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    public void checkWin(ArrayList<Tile[]> possibleMoves){
        if(possibleMoves.size() == 0){
            win(0);
        }
    }

    public void simpleAi(ArrayList<Tile[]> possibleMoves){
        Random rand = new Random();
        int n = rand.nextInt(possibleMoves.size());
        move(possibleMoves.get(n)[0], possibleMoves.get(n)[1]);
    }

    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }
}
