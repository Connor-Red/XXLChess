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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final boolean DEBUG = true;

    public static final int SPRITESIZE = 480;
    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 120;
    public static final int BOARD_WIDTH = 14;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    protected Tile[][] board;
    protected Piece selPiece;
    protected Tile selTile;
    protected boolean playerBlack;
    protected boolean currentTurn;
    protected static boolean bKingInCheck = false;
    protected static boolean wKingInCheck = false;
    protected static Tile bKingPos;
    protected static Tile wKingPos;
    public static final int FPS = 60;
	
    public String configPath;

    public App() {
        this.configPath = "config.json";
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

        // placing pieces
        // Placing rooks
        board[0][0].updatePiece(new Rook(board[0][0].getX(), board[0][0].getY(), true));
        board[0][13].updatePiece(new Rook(board[0][13].getX(), board[0][13].getY(), true));
        board[13][0].updatePiece(new Rook(board[13][0].getX(), board[13][0].getY(), false));
        board[13][13].updatePiece(new Rook(board[13][13].getX(), board[13][13].getY(), false));

        // Placing Knights
        board[0][1].updatePiece(new Knight(board[0][1].getX(), board[0][1].getY(), true));
        board[0][12].updatePiece(new Knight(board[0][12].getX(), board[0][12].getY(), true));
        board[13][1].updatePiece(new Knight(board[13][1].getX(), board[13][1].getY(), false));
        board[13][12].updatePiece(new Knight(board[13][12].getX(), board[13][12].getY(), false));

        // Placing Bishops
        board[0][2].updatePiece(new Bishop(board[0][2].getX(), board[0][2].getY(), true));
        board[0][11].updatePiece(new Bishop(board[0][11].getX(), board[0][11].getY(), true));
        board[13][2].updatePiece(new Bishop(board[13][2].getX(), board[13][2].getY(), false));
        board[13][11].updatePiece(new Bishop(board[13][11].getX(), board[13][11].getY(), false));

        // Placing Archbishops
        board[0][3].updatePiece(new Archbishop(board[0][3].getX(), board[0][3].getY(), true));
        board[13][3].updatePiece(new Archbishop(board[13][3].getX(), board[13][3].getY(), false));

        // Placing Chancellors
        board[0][10].updatePiece(new Knook(board[0][10].getX(), board[0][10].getY(), true));
        board[13][10].updatePiece(new Knook(board[13][10].getX(), board[13][10].getY(), false));

        // Placing Camels
        board[0][4].updatePiece(new Camel(board[0][4].getX(), board[0][4].getY(), true));
        board[0][9].updatePiece(new Camel(board[0][9].getX(), board[0][9].getY(), true));
        board[13][4].updatePiece(new Camel(board[13][4].getX(), board[13][4].getY(), false));
        board[13][9].updatePiece(new Camel(board[13][9].getX(), board[13][9].getY(), false));

        // Placing Generals
        board[0][5].updatePiece(new General(board[0][5].getX(), board[0][5].getY(), true));
        board[0][8].updatePiece(new General(board[0][8].getX(), board[0][8].getY(), true));
        board[13][5].updatePiece(new General(board[13][5].getX(), board[13][5].getY(), false));
        board[13][8].updatePiece(new General(board[13][8].getX(), board[13][8].getY(), false));

        // Placing Amazons
        board[0][6].updatePiece(new Amazon(board[0][6].getX(), board[0][6].getY(), true));
        board[13][6].updatePiece(new Amazon(board[13][6].getX(), board[13][6].getY(), false));

        // Placing Kings
        board[0][7].updatePiece(new King(board[0][7].getX(), board[0][7].getY(), true));
        bKingPos = board[0][7];
        board[13][7].updatePiece(new King(board[13][7].getX(), board[13][7].getY(), false));
        wKingPos = board[13][7];

        // Placing Pawns
        for(int i = 0; i < BOARD_WIDTH; i++){
            board[1][i].updatePiece(new Pawn(board[1][i].getX(), board[1][i].getY(), true));
            board[12][i].updatePiece(new Pawn(board[12][i].getX(), board[12][i].getY(), false));
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

        // Load images during setup

        // PImage spr = loadImage("src/main/resources/XXLChess/"+...);

		// load config
        JSONObject conf = loadJSONObject(new File(this.configPath));
        for(int j = 0; j < BOARD_WIDTH; j++){
            for(int i = 0; i < 2; i++){
                board[i][j].getHeldPiece().setSprite(this.loadImage(board[i][j].getHeldPiece().getSpriteString()));
                board[i][j].getHeldPiece().getSprite().resize(48, 0);
            }
            for(int i = BOARD_WIDTH - 2; i < BOARD_WIDTH; i++){
                board[i][j].getHeldPiece().setSprite(this.loadImage(board[i][j].getHeldPiece().getSpriteString()));
                board[i][j].getHeldPiece().getSprite().resize(48, 0);
            }
        }

        playerBlack = true;
        currentTurn = true;
        updateAll();
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){

    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(mouseX < 672){
            int boardY = Math.floorDiv(mouseX, CELLSIZE);
            int boardX = Math.floorDiv(mouseY, CELLSIZE);
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
                System.out.println("King pos: " + bKingPos.getTileName());

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

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(200, 200, 200);
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_WIDTH; j++){
                this.board[i][j].draw(this);
                if(this.board[i][j].getHeldPiece() != null){
                    this.board[i][j].getHeldPiece().draw(this);
                }
            }
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
        ArrayList<Tile> moves = this.selTile.getMoves();
        ArrayList<Tile> attacks = this.selTile.getAttackable();
        for(Tile t:moves){
            t.prevStatus();
        }
        for(Tile t:attacks){
            t.prevStatus();
        }
        this.selTile = null;
    }

    public void move(Tile origin, Tile destination){
        Piece targetPiece = origin.getHeldPiece();
        targetPiece.setX(destination.getX());
        targetPiece.setY(destination.getY());
        destination.updatePiece(targetPiece);
        origin.updatePiece(null);
        if((targetPiece.getPieceName() == "P") || ((targetPiece.getPieceName() == "P"))){
            ((Pawn) targetPiece).setHasMoved(true);
        }else if((targetPiece.getPieceName().equals("K"))){
            bKingPos = destination;
        }else if(((targetPiece.getPieceName().equals("k")))){
            wKingPos = destination;
        }
        deselect();
        updateAll();
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
                    if(((j > 0) && (j < BOARD_WIDTH)) && ((k > 0) && (k < BOARD_WIDTH))){
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
                    if(((j > 0) && (j < BOARD_WIDTH)) && ((k > 0) && (k < BOARD_WIDTH))){
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
                    if(((j > 0) && (j < BOARD_WIDTH)) && ((k > 0) && (k < BOARD_WIDTH))){
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
                    if(((j > 0) && (j < BOARD_WIDTH)) && ((k > 0) && (k < BOARD_WIDTH))){
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
            System.out.println("bking moved to " + dest.getTileName());
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
                        if(currentTurn && (bKingPos == t) && (!this.board[i][j].getHeldPiece().isBlack())){
                            isLegal = false;
                            System.out.println("King at " + t.getTileName() + " illegal");
                            break;
                        }else if(!currentTurn && (wKingPos == t) && (this.board[i][j].getHeldPiece().isBlack())){
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
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }
}
