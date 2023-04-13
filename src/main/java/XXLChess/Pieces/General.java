package XXLChess.Pieces;

public class General extends Piece{
    private static final int[] moveset = {1,1,2};
    private static final String name = "General";
    private static final double pieceValue = 5;
    public General(int x, int y, boolean colour){
        super(x, y, pieceValue, colour, moveset, name);
    }
}