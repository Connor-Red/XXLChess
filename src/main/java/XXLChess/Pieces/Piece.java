package XXLChess.Pieces;

import processing.core.PImage;
import processing.core.PApplet;

public abstract class Piece{
    protected int x;
    protected int y;
    protected double pieceValue;
    protected boolean black; // white = false, black = true
    private PImage sprite;
    protected String spriteString;
    /*
     * Moveset is a array of ints that determine the moves the piece can make
     * 
     * The first determines straight line moves/captures, such as a Rook, with a nonzero value 
     * referring to the number of spaces it can move at a time
     * 
     * The second determines diagonal moves/captures, such as a bishop, with a nonzero value referring
     * to the number of spaces it can move at a time
     * 
     * The third determines knight-like moves, with a nonzero value determining the number of straight 
     * spaces it can jump before having to turn
     */
    protected int[] moveset;
    protected String pieceName;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getPieceValue() {
        return pieceValue;
    }

    public boolean isBlack() {
        return black;
    }

    public Piece(int x, int y, double pieceValue, boolean black, int[] moveset, String pieceName) {
        this.x = x;
        this.y = y;
        this.pieceValue = pieceValue;
        this.black = black;
        this.moveset = moveset;
        this.pieceName = pieceName;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public String getSpriteString(){
        return this.spriteString;
    }

    public PImage getSprite(){
        return this.sprite;
    }

    public int[] getMoveset(){
        return this.moveset;
    }

    public void tick(){

    };

    public void draw(PApplet app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are coordinates
        app.image(this.sprite, this.x, this.y);
    }

    public String getPieceName() {
        return pieceName;
    }

    
}