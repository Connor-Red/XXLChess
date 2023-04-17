package XXLChess.Pieces;

public class General extends Piece{
    private static final int[] moveset = {1,1,2};
    private static final String bname = "G";
    private static final String wname = "g";
    private static final double pieceValue = 5;
    public General(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-knight-king.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-knight-king.png";
            this.pieceName = wname;
        }
    }
}