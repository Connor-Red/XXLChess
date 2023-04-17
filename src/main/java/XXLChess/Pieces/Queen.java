package XXLChess.Pieces;

public class Queen extends Piece{
    private static final int[] moveset = {17,17,0};
    private static final String bname = "Q";
    private static final String wname = "q";
    private static final double pieceValue = 9.5;
    
    public Queen(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-queen.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-queen.png";
            this.pieceName = wname;
        }
    }
}