package XXLChess.Pieces;

public class Bishop extends Piece{
    private static final int[] moveset = {0,17,0};
    private static final String bname = "B";
    private static final String wname = "b";
    private static final double pieceValue = 3.625;
    public Bishop(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-bishop.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-bishop.png";
            this.pieceName = wname;
        }
    }
}