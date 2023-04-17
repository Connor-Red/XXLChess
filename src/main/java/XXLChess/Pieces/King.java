package XXLChess.Pieces;

public class King extends Piece{
    private static final int[] moveset = {1,1,0};
    private static final String bname = "K";
    private static final String wname = "k";
    private static final double pieceValue = 99999;
    private boolean hasMoved;
    public King(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-king.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-king.png";
            this.pieceName = wname;
        }
    }
    public boolean isHasMoved() {
        return hasMoved;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}