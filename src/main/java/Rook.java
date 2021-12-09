import java.util.ArrayList;

public class Rook extends Piece {


    public Rook(Square currSquare, String imageFilePath, boolean isWhite) {
        super(currSquare, imageFilePath, isWhite);
    }

    @Override
    public ArrayList<Square> getAllMoves() {

        return this.forwardCheckMoves(this.getVertAndHorizOpenings());
    }

}
