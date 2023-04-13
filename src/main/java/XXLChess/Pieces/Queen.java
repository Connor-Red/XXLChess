package XXLChess.Pieces;

public class Queen extends Piece{
    private static final int[] moveset = {17,17,0};
    private static final String name = "Queen";
    private static final double pieceValue = 9.5;
    public Queen(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, name);
    }
}