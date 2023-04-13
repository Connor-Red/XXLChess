package XXLChess.Pieces;

public class King extends Piece{
    private static final int[] moveset = {1,1,0};
    private static final String name = "King";
    private static final double pieceValue = 99999;
    public King(int x, int y, boolean colour){
        super(x, y, pieceValue, colour, moveset, name);
    }
}