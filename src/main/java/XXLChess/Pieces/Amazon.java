package XXLChess.Pieces;

public class Amazon extends Piece{
    private static final int[] moveset = {17,17,2};
    private static final String bname = "A";
    private static final String wname = "a";
    private static final double pieceValue = 12;
    public Amazon(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-amazon.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-amazon.png";
            this.pieceName = wname;
        }
    }
}