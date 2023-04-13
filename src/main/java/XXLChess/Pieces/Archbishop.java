package XXLChess.Pieces;

public class Archbishop extends Piece{
    private static final int[] moveset = {0,17,2};
    private static final String name = "Archbishop";
    private static final double pieceValue = 7.5;
    public Archbishop(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, name);
    }
}