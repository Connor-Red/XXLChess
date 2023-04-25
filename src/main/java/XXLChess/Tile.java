package XXLChess;

import processing.core.PApplet;
import XXLChess.Pieces.*;
import java.util.ArrayList;

public class Tile{
    private int x;
    private int y;
    private int xPos;
    private int yPos;
    private int prevStatus;
    protected int cellSize;
    // light or dark colour, true = dark, false = light
    protected boolean dark;
    // 0 = default, 1 = highlighted with move, 2 = previous move, 3 = selected, 4 = threatened, 5 = check
    protected int status;
    protected Piece heldPiece;
    protected ArrayList<Tile> attackedBlack;
    protected ArrayList<Tile> attackedWhite;
    protected ArrayList<Tile> moves;
    protected ArrayList<Tile> attackable;

    protected ArrayList<Tile> legalMoves;
    protected ArrayList<Tile> legalAttacks;
    protected String tileName;
    private static final String[] ROWNAMES = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
    private static final String[] COLUMNNAMES = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V"};

    private static final int DARK_BROWN = 0xFFB58863;
    private static final int LIGHT_BROWN = 0xFFF0D9B5;
    private static final int DARK_BLUE = 0xFFAAD2DD;
    private static final int LIGHT_BLUE = 0xFFC4E0E8;
    private static final int SELECTION_GREEN = 0xFF698A4C;
    private static final int LIGHT_GREEN = 0xFFCDD26A;
    private static final int DARK_GREEN = 0xFFAAA23A;
    private static final int LIGHT_RED = 0xFFFFA466;
    private static final int DARK_RED = 0xFFFF0000;

    public Tile(int cellSize, int x, int y, boolean dark, String tileName){
        this.cellSize = cellSize;
        this.tileName = tileName;
        this.xPos = x;
        this.yPos = y;
        this.x = y * cellSize;
        this.y = x * cellSize;
        this.dark = dark;
        this.status = 0;
        this.prevStatus = 0;
        this.attackedBlack = new ArrayList<Tile>();
        this.attackedWhite = new ArrayList<Tile>();
        this.moves = new ArrayList<Tile>();
        this.attackable = new ArrayList<Tile>();
        this.legalAttacks = new ArrayList<Tile>();
        this.legalMoves = new ArrayList<Tile>();
    }

    public void updatePiece(Piece pc){
        this.heldPiece = pc;
    }

    public void updateStatus(int i){
        this.prevStatus = this.status;
        this.status = i;
    }

    public int getX(){
        // x position in pixels
        return this.x;
    }

    public int getY(){
        // y position in pixels
        return this.y;
    }

    public int getXPos(){
        // x position in cells
        return this.xPos;
    }

    public int getYPos(){
        // y position in cells
        return this.yPos;
    }

    public int getStatus() {
        return status;
    }

    public void prevStatus(){
        status = prevStatus;
    }

    public Piece getHeldPiece() {
        return heldPiece;
    }

    public static String getRowNames(int i){
        return ROWNAMES[i];
    }

    public static String getColumnNames(int i){
        return COLUMNNAMES[i];
    }

    public String getTileName(){
        return tileName;
    }

    public void addAttackedBy(Tile t){
        if(t.getHeldPiece().isBlack()){
            attackedBlack.add(t);
        }else{
            attackedWhite.add(t);
        }
        
    }

    public ArrayList<Tile> getAttackedBlack(){
        return attackedBlack;
    }

    public ArrayList<Tile> getAttackedWhite(){
        return attackedWhite;
    }

    public void addMoves(Tile t){
        this.moves.add(t);
    }

    public void addAttackable(Tile t){
        this.attackable.add(t);
    }

    public void resetMoves(){
        this.moves.clear();
        this.attackable.clear();
        this.attackedBlack.clear();
        this.attackedWhite.clear();
        this.legalAttacks.clear();
        this.legalMoves.clear();
    }

    public ArrayList<Tile> getMoves(){
        return this.moves;
    }

    public ArrayList<Tile> getAttackable(){
        return this.attackable;
    }

    public ArrayList<Tile> getLegalMoves() {
        return this.legalMoves;
    }

    public void addLegalMoves(Tile t) {
        this.legalMoves.add(t);
    }

    public ArrayList<Tile> getLegalAttacks() {
        return this.legalAttacks;
    }

    public int getAllMoves(){
        return this.legalAttacks.size() + this.legalMoves.size();
    }

    public void addLegalAttacks(Tile t) {
        this.legalAttacks.add(t);
    }

    // returns the difference in value of the attackable piece vs current piece
    public double getValueDelta(Tile t){
        return t.getHeldPiece().getPieceValue() - this.getHeldPiece().getPieceValue();
    }

    public void draw(PApplet app){
        switch(this.status){
            case 0:
                if(this.dark){
                    app.fill(DARK_BROWN);
                }else{
                    app.fill(LIGHT_BROWN);
                }
                break;
            case 1:
                if(this.dark){
                    app.fill(DARK_BLUE);
                }else{
                    app.fill(LIGHT_BLUE);
                }
                break;
            case 2:
                if(this.dark){
                    app.fill(DARK_GREEN);
                }else{
                    app.fill(LIGHT_GREEN);
                }
                break;
            case 3:
                app.fill(SELECTION_GREEN);
                break;
            case 4:
                app.fill(LIGHT_RED);
                break;
            case 5:
                app.fill(DARK_RED);
                break;
                
        }
        app.rect(x,y,cellSize,cellSize);
    }
}