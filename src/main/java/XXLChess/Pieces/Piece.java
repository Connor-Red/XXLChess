package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;

public abstract class Piece{
    protected int x;
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getPieceValue() {
        return pieceValue;
    }

    public boolean isColour() {
        return colour;
    }

    protected int y;
    protected float pieceValue;
    protected boolean colour; // white = 0, black = 1
    private PImage sprite;

    public Piece(int x, int y, float pieceValue, boolean colour) {
        this.x = x;
        this.y = y;
        this.pieceValue = pieceValue;
        this.colour = colour;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public abstract void tick();

    public void draw(PApplet app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are coordinates
        app.image(this.sprite, this.x, this.y);
    }

    
}