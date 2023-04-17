package XXLChess.Pieces;

public class Knook extends Piece{
    private static final int[] moveset = {17,0,2};
    private static final String bname = "E";
    private static final String wname = "e";
    private static final double pieceValue = 8.5;
    public Knook(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-chancellor.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-chancellor.png";
            this.pieceName = wname;
        }
    }
}