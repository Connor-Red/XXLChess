package XXLChess.Pieces;

public class Pawn extends Piece{
    private static final int[] moveset = {0,0,0};
    private static final String name = "Pawn";
    private static final double pieceValue = 1;
    public Pawn(int x, int y, boolean colour){
        super(x, y, pieceValue, colour, moveset, name);
    }
}