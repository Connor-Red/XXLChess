package XXLChess;

import XXLChess.Pieces.*;

public class Board{
    Tile[][] tiles;
    
    public Board(int cellSize, int boardWidth){
        tiles = new Tile[boardWidth][boardWidth];
        boolean dark = true;
        for(int i = 0; i < boardWidth; i++){
            for(int j = 0; j < boardWidth; j++){
                if(dark){
                    dark = false;
                }else{
                    dark = true;
                }
                tiles[i][j] = new Tile(cellSize, i * cellSize, j * cellSize, dark);
            }
        }
        // Placing pieces
        
        // Placing rooks
        tiles[0][0].updatePiece(new Rook(tiles[0][0].getCenterX(), tiles[0][0].getCenterY(), true));
        tiles[0][13].updatePiece(new Rook(tiles[0][13].getCenterX(), tiles[0][13].getCenterY(), true));
        tiles[13][0].updatePiece(new Rook(tiles[13][0].getCenterX(), tiles[13][0].getCenterY(), false));
        tiles[13][13].updatePiece(new Rook(tiles[13][13].getCenterX(), tiles[13][13].getCenterY(), false));

        // Placing Knights
        tiles[0][1].updatePiece(new Knight(tiles[0][1].getCenterX(), tiles[0][1].getCenterY(), true));
        tiles[0][12].updatePiece(new Knight(tiles[0][12].getCenterX(), tiles[0][12].getCenterY(), true));
        tiles[13][1].updatePiece(new Knight(tiles[13][1].getCenterX(), tiles[13][1].getCenterY(), false));
        tiles[13][12].updatePiece(new Knight(tiles[13][12].getCenterX(), tiles[13][12].getCenterY(), false));

        // Placing Bishops
        tiles[0][2].updatePiece(new Bishop(tiles[0][2].getCenterX(), tiles[0][2].getCenterY(), true));
        tiles[0][11].updatePiece(new Bishop(tiles[0][11].getCenterX(), tiles[0][11].getCenterY(), true));
        tiles[13][2].updatePiece(new Bishop(tiles[13][2].getCenterX(), tiles[13][2].getCenterY(), false));
        tiles[13][11].updatePiece(new Bishop(tiles[13][11].getCenterX(), tiles[13][11].getCenterY(), false));

        // Placing Archbishops
        tiles[0][3].updatePiece(new Archbishop(tiles[0][3].getCenterX(), tiles[0][3].getCenterY(), true));
        tiles[13][3].updatePiece(new Archbishop(tiles[13][3].getCenterX(), tiles[13][3].getCenterY(), false));

        // Placing Chancellors
        tiles[0][10].updatePiece(new Knook(tiles[0][10].getCenterX(), tiles[0][10].getCenterY(), true));
        tiles[13][10].updatePiece(new Knook(tiles[13][10].getCenterX(), tiles[13][10].getCenterY(), false));

        // Placing Camels
        tiles[0][4].updatePiece(new Camel(tiles[0][4].getCenterX(), tiles[0][4].getCenterY(), true));
        tiles[0][9].updatePiece(new Camel(tiles[0][9].getCenterX(), tiles[0][9].getCenterY(), true));
        tiles[13][4].updatePiece(new Camel(tiles[13][4].getCenterX(), tiles[13][4].getCenterY(), false));
        tiles[13][9].updatePiece(new Camel(tiles[13][9].getCenterX(), tiles[13][9].getCenterY(), false));

        // Placing Generals
        tiles[0][5].updatePiece(new General(tiles[0][5].getCenterX(), tiles[0][5].getCenterY(), true));
        tiles[0][8].updatePiece(new General(tiles[0][8].getCenterX(), tiles[0][8].getCenterY(), true));
        tiles[13][5].updatePiece(new General(tiles[13][5].getCenterX(), tiles[13][5].getCenterY(), false));
        tiles[13][8].updatePiece(new General(tiles[13][8].getCenterX(), tiles[13][8].getCenterY(), false));

        // Placing Amazons
        tiles[0][6].updatePiece(new Amazon(tiles[0][6].getCenterX(), tiles[0][6].getCenterY(), true));
        tiles[13][6].updatePiece(new Amazon(tiles[13][6].getCenterX(), tiles[13][6].getCenterY(), false));

        // Placing Kings
        tiles[0][7].updatePiece(new King(tiles[0][7].getCenterX(), tiles[0][7].getCenterY(), true));
        tiles[13][7].updatePiece(new King(tiles[13][7].getCenterX(), tiles[13][7].getCenterY(), false));

        // Placing Pawns
        for(int i = 0; i < boardWidth; i++){
            tiles[1][i].updatePiece(new Pawn(tiles[1][i].getCenterX(), tiles[1][i].getCenterY(), true));
            tiles[12][i].updatePiece(new Pawn(tiles[12][i].getCenterX(), tiles[12][i].getCenterY(), false));
        }
    }
}