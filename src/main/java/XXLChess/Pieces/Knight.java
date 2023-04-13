package XXLChess.Pieces;

public class Knight extends Piece{
    private static final int[] moveset = {0,0,2};
    private static final String name = "Knight";
    private static final double pieceValue = 2;
    public Knight(int x, int y, boolean colour){
        super(x, y, pieceValue, colour, moveset, name);
    }
}