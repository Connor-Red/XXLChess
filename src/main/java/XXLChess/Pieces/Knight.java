package XXLChess.Pieces;

public class Knight extends Piece{
    private static final int[] moveset = {0,0,2};
    private static final String bname = "N";
    private static final String wname = "n";
    private static final double pieceValue = 2;
    public Knight(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, bname);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-knight.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-knight.png";
            this.pieceName = wname;
        }
    }
}