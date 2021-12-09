import java.util.ArrayList;

public class King extends Piece {

    public King(Square currSquare, String imageFilePath, boolean isWhite) {
        super(currSquare, imageFilePath, isWhite);
    }

    @Override
    public ArrayList<Square> getAllMoves() {

        ArrayList<Square> moveList = new ArrayList<>();
        Square currSquare = this.getSquare();
        int currRow = currSquare.row;
        int currCol = currSquare.col;
        Square[][] boardRef = currSquare.boardPanelRef.board;

        int[][] possibleOffsets = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        for (int[] offsets : possibleOffsets) {
            int newRow = currRow + offsets[0];
            int newCol = currCol + offsets[1];
            if (currSquare.boardPanelRef.isCoordOnBoard(newRow, newCol)) {
                if (boardRef[newRow][newCol].piece == null) {
                    moveList.add(boardRef[newRow][newCol]);
                } else if (boardRef[newRow][newCol].piece.isPieceWhite() != this.isPieceWhite()) {
                    moveList.add(boardRef[newRow][newCol]);
                }
            }
        }

        if (this.canCastleKingside()) {
            moveList.add(boardRef[(this.isPieceWhite()) ? 7 : 0][6]);
        }
        if (this.canCastleQueenside()) {
            moveList.add(boardRef[(this.isPieceWhite()) ? 7 : 0][2]);
        }
        return this.forwardCheckMoves(moveList);
    }


    public boolean canCastleKingside() {

        Square currSquare = this.getSquare();
        BoardPanel panelRef = currSquare.boardPanelRef;
        Square[][] board = panelRef.board;

        //making sure king does not castle through check
        ArrayList<Square> moveList = new ArrayList<>();
        moveList.add(board[(this.isPieceWhite()) ? 7 : 0][5]);
        if (this.forwardCheckMoves(moveList).size() != 1) {
            return false;
        }

        //also can't castle while in check
        if ((this.isPieceWhite()) ? panelRef.isWhiteInCheck() : panelRef.isBlackInCheck()) {
            return false;
        }

        int rowIndex = (this.isPieceWhite()) ? 7 : 0;
        if (!this.hasMoved
                && board[rowIndex][7].piece != null
                && !board[rowIndex][7].piece.hasMoved
                && board[rowIndex][6].piece == null
                && board[rowIndex][5].piece == null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean canCastleQueenside() {

        Square currSquare = this.getSquare();
        BoardPanel panelRef = currSquare.boardPanelRef;
        Square[][] board = panelRef.board;

        //making sure king does not castle through check
        ArrayList<Square> moveList = new ArrayList<>();
        moveList.add(board[(this.isPieceWhite()) ? 7 : 0][3]);
        if (this.forwardCheckMoves(moveList).size() != 1) {
            return false;
        }

        //also can't castle while in check
        if ((this.isPieceWhite()) ? panelRef.isWhiteInCheck() : panelRef.isBlackInCheck()) {
            return false;
        }

        int rowIndex = (this.isPieceWhite()) ? 7 : 0;
        return !this.hasMoved
                && board[rowIndex][0].piece != null
                && !board[rowIndex][0].piece.hasMoved
                && board[rowIndex][1].piece == null
                && board[rowIndex][2].piece == null
                && board[rowIndex][3].piece == null;
    }


}
