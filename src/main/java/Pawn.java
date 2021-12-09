import java.util.ArrayList;

public class Pawn extends Piece {

    public int enPassantTurn;
    public Square enPassantVictimSquare;

    public Pawn(Square currSquare, String imageFilePath, boolean isWhite) {
        super(currSquare, imageFilePath, isWhite);

        this.enPassantTurn = -1;
    }

    @Override
    public ArrayList<Square> getAllMoves() {
        Square currSquare = this.getSquare();
        BoardPanel panelRef = currSquare.boardPanelRef;
        int currRow = currSquare.row;
        int currCol = currSquare.col;

        ArrayList<Square> moveList = new ArrayList<>();

        ArrayList<int[]> offsets = new ArrayList<>();
        if (this.isPieceWhite()) {
            offsets.add(new int[] {-1, 0});
            offsets.add(new int[] {-1, -1});
            offsets.add(new int[] {-1, 1});
            if (!this.hasMoved) {
                offsets.add(new int[] {-2, 0});
            }
        } else {
            offsets.add(new int[] {1, 0});
            offsets.add(new int[] {1, -1});
            offsets.add(new int[] {1, 1});
            if (!this.hasMoved) {
                offsets.add(new int[] {2, 0});
            }
        }

        for (int[] offset : offsets) {
            int newRow = currRow + offset[0];
            int newCol = currCol + offset[1];
            if (!currSquare.boardPanelRef.isCoordOnBoard(newRow, newCol)) {
                continue;
            }
            Square newSquare = currSquare.boardPanelRef.board[newRow][newCol];
            if (newSquare.piece == null && offset[1] == 0) {
                moveList.add(newSquare);
            } else if (newSquare.piece != null && newSquare.piece.isPieceWhite() != this.isPieceWhite() && offset[1] != 0) {
                moveList.add(newSquare);
            }
        }

        //adding possible en passant capture opportunity
        if (this.enPassantTurn == panelRef.getTurnCounter()) {
            moveList.add(panelRef.board[enPassantVictimSquare.row + ((this.isPieceWhite()) ? -1 : 1)][enPassantVictimSquare.col]);
        }


        return this.forwardCheckMoves(moveList);
    }

}
