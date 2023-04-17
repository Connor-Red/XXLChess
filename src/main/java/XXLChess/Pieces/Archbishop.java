package XXLChess.Pieces;

public class Archbishop extends Piece{
    private static final int[] moveset = {0,17,2};
    private static final String bname = "H";
    private static final String wname = "h";
    private static final double pieceValue = 7.5;
    public Archbishop(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-archbishop.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-archbishop.png";
            this.pieceName = wname;
        }
    }
}