package XXLChess.Pieces;

public class Camel extends Piece{
    private static final int[] moveset = {0,0,3};
    private static final String bname = "C";
    private static final String wname = "c";
    private static final double pieceValue = 2;
    public Camel(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-camel.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-camel.png";
            this.pieceName = wname;
        }
    }
}