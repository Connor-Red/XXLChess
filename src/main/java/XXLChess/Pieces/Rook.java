package XXLChess.Pieces;

public class Rook extends Piece{
    private static final int[] moveset = {17,0,0};
    private static final String name = "Rook";
    private static final double pieceValue = 5.25;
    public Rook(int x, int y, boolean colour){
        super(x, y, pieceValue, colour, moveset, name);
    }
}