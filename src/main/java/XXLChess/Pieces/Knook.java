package XXLChess.Pieces;

public class Knook extends Piece{
    private static final int[] moveset = {17,0,2};
    private static final String name = "Knook";
    private static final double pieceValue = 8.5;
    public Knook(int x, int y, boolean colour){
        super(x, y, pieceValue, colour, moveset, name);
    }
}