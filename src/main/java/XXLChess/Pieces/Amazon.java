package XXLChess.Pieces;

public class Amazon extends Piece{
    private static final int[] moveset = {17,17,2};
    private static final String name = "Amazon";
    private static final double pieceValue = 12;
    private String Sprite;
    public Amazon(int x, int y, boolean black){
        super(x, y, pieceValue, black, moveset, name);
        if(black){
            this.Sprite = "src/main/resources/b-amazon.png";
        }else{
            this.Sprite = "src/main/resources/w-amazon.png";
        }
    }

    public String getSprite(){
        return Sprite;
    }
}