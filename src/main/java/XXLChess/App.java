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

    public static final int SPRITESIZE = 480;
    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 120;
    public static final int BOARD_WIDTH = 14;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    protected Tile[][] board;
    protected Piece selPiece;
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
                board[j][i] = new Tile(CELLSIZE, i * CELLSIZE, j * CELLSIZE, dark);
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
        board[13][7].updatePiece(new King(board[13][7].getX(), board[13][7].getY(), false));

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


    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }

}
