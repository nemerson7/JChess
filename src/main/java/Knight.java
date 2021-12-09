import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(Square currSquare, String imageFilePath, boolean isWhite) {
        super(currSquare, imageFilePath, isWhite);
    }

    @Override
    public ArrayList<Square> getAllMoves() {

        ArrayList<Square> moveList = new ArrayList<>();
        Square currSquare = this.getSquare();
        Square[][] boardRef = currSquare.boardPanelRef.board;


        int[][] coordOffsetPairs = {{-2, -1}, {2, -1}, {-2, 1}, {2, 1}, {-1, -2}, {1, -2}, {-1, 2}, {1, 2}};
        for (int[] offsets : coordOffsetPairs) {
            int newRow = currSquare.row - offsets[0];
            int newCol = currSquare.col - offsets[1];
            if (currSquare.boardPanelRef.isCoordOnBoard(newRow, newCol)) {
                if (boardRef[newRow][newCol] == null
                || boardRef[newRow][newCol].piece == null
                || !boardRef[newRow][newCol].piece.isPieceWhite() && this.isPieceWhite()
                || boardRef[newRow][newCol].piece.isPieceWhite() && !this.isPieceWhite()) {
                    moveList.add(boardRef[newRow][newCol]);
                }
            }
        }

        return this.forwardCheckMoves(moveList);

    }
}
