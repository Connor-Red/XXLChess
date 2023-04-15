package XXLChess.Pieces;

public class Pawn extends Piece{
    private static final int[] moveset = {0,0,0};
    private static final String name = "Pawn";
    private static final double pieceValue = 1;
    public Pawn(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, name);
        if(black){
            this.spriteString = "src/main/resources/XXLChess/b-pawn.png";
        }else{
            this.spriteString = "src/main/resources/XXLChess/w-pawn.png";
        }
    }
}