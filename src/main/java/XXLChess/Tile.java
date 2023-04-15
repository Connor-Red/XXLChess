package XXLChess;

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
    private static final String DARK_BROWN = "B58863";
    private static final String LIGHT_BROWN = "F0D9B5";
    private static final String DARK_BLUE = "AAD2DD";
    private static final String LIGHT_BLUE = "C4E0E8";
    private static final String SELECTION_GREEN = "698A4C";
    private static final String LIGHT_GREEN = "CDD26A";
    private static final String DARK_GREEN = "AAA23A";
    private static final String LIGHT_RED = "FFA466";
    private static final String DARK_RED = "FF0000";

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

    public int getStatus() {
        return status;
    }

    public Piece getHeldPiece() {
        return heldPiece;
    }

    public void draw(PApplet app){
        switch(this.status){
            case 0:
                if(this.dark){
                    app.fill(PApplet.unhex(DARK_BROWN));
                }else{
                    app.fill(PApplet.unhex(LIGHT_BROWN));
                }
                break;
            case 1:
                if(this.dark){
                    app.fill(PApplet.unhex(DARK_BLUE));
                }else{
                    app.fill(PApplet.unhex(LIGHT_BLUE));
                }
                break;
            case 2:
                if(this.dark){
                    app.fill(PApplet.unhex(DARK_GREEN));
                }else{
                    app.fill(PApplet.unhex(LIGHT_GREEN));
                }
                break;
            case 3:
                app.fill(PApplet.unhex(SELECTION_GREEN));
                break;
            case 4:
                app.fill(PApplet.unhex(LIGHT_RED));
                break;
            case 5:
                app.fill(PApplet.unhex(DARK_RED));
                break;
                
        }
        app.rect(x,y,cellSize,cellSize);
    }



}