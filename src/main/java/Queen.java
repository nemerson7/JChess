import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(Square currSquare, String imageFilePath, boolean isWhite) {
        super(currSquare, imageFilePath, isWhite);
    }

    @Override
    public ArrayList<Square> getAllMoves() {
        ArrayList<Square> openings = this.forwardCheckMoves(this.getDiagOpenings());
        openings.addAll(this.forwardCheckMoves(this.getVertAndHorizOpenings()));
        return openings;
    }
}
