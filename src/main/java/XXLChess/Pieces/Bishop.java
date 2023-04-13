package XXLChess.Pieces;

public class Bishop extends Piece{
    private static final int[] moveset = {0,17,0};
    private static final String name = "Bishop";
    private static final double pieceValue = 3.625;
    public Bishop(int x, int y, boolean colour){
        super(x, y, pieceValue, colour, moveset, name);
    }
}