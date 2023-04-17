package XXLChess.Pieces;

public class Pawn extends Piece{
    private static final int[] moveset = {0,0,0};
    private static final String bname = "P";
    private static final String wname = "p";    
    private static final double pieceValue = 1;
    private boolean hasMoved;
    public Pawn(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        hasMoved = false;
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-pawn.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-pawn.png";
            this.pieceName = wname;
        }
    }

    public boolean getHasMoved(){
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}