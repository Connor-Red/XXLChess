package XXLChess.Pieces;

public class Rook extends Piece{
    private static final int[] moveset = {17,0,0};
    private static final String bname = "R";
    private static final String wname = "r";
    private static final double pieceValue = 5.25;
    private boolean hasMoved;

    public Rook(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        this.hasMoved = false;
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-rook.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-rook.png";
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