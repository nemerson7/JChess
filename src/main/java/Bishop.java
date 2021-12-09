import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Square currSquare, String imageFilePath, boolean isWhite) {
        super(currSquare, imageFilePath, isWhite);
    }

    @Override
    public ArrayList<Square> getAllMoves() {
        return this.forwardCheckMoves(this.getDiagOpenings());
    }


}
