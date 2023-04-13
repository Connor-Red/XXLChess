package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;
import XXLChess.Pieces.*;;

public class Tile{
    private int x;
    private int y;
    protected int cellSize;
    // light or dark colour, true = dark, false = light
    protected boolean dark;
    // 0 = default, 1 = highlighted with move, 2 = previous move, 3 = selected, 4 = threatened, 5 = check
    protected int status;
    protected int[] center = new int[2];
    protected Piece heldPiece;

    public Tile(int cellSize, int x, int y, boolean dark){
        this.cellSize = cellSize;
        this.x = x;
        this.y = y;
        this.dark = dark;
        this.status = 0;
        this.center = new int[] {(x + (cellSize/2)), (y + (cellSize/2))};
    }

    public void updatePiece(Piece pc){
        this.heldPiece = pc;
    }

    public void updateStatus(int i){
        this.status = i;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getCenterX(){
        return this.center[0];
    }

    public int getCenterY(){
        return this.center[1];
    }



}