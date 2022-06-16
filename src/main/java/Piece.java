import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Piece {

    private Square square;
    private BufferedImage image;
    private boolean isWhite;
    public final String imageFilePath;
    public boolean hasMoved;


    public Piece(Square currSquare, final String imageFilePath, boolean isWhite) {
        this.square = currSquare;
        this.isWhite = isWhite;
        this.imageFilePath = imageFilePath;
        this.hasMoved = false;

        try {
            if (this.image == null) {
                this.image = ImageIO.read(getClass().getResource(imageFilePath));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public void draw(Graphics g, boolean flippedPerspective) {
        int imageWidth = this.image.getWidth();
        int imageHeight = this.image.getHeight();
        int cellSize = this.square.boardPanelRef.cellSize;
        //offseting so that the piece is centered in the square
        int squareY = (flippedPerspective) ? Math.abs(this.square.getY() - (7*this.square.boardPanelRef.cellSize)) : this.square.getY();

        int xPos = this.square.getX() + (cellSize / 2) - (imageWidth / 2);
        int yPos = squareY + (cellSize / 2) - (imageHeight / 2);

        g.drawImage(this.image, xPos, yPos, null);
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public Square getSquare() {
        return this.square;
    }

    public boolean isPieceWhite() {
        return this.isWhite;
    }


    public abstract ArrayList<Square> getAllMoves();

    /**
     * Forward checks each move in a given moveList to filter out the moves that would put the current player in check
     * @param moveList list of moves for a given piece
     * @return moveList after filtering
     */
    public ArrayList<Square> forwardCheckMoves(ArrayList<Square> moveList) {

        Square currSquare = this.square;
        for (int i = moveList.size() - 1; i >= 0; i--) {
            Square iSquare = moveList.get(i);
            Piece possibleCapturedPiece = iSquare.piece;
            Piece currentPiece = currSquare.piece;
            //iSquare.piece = currentPiece;
            //currSquare.piece = null;

            iSquare.setPiece(currSquare.piece);
            currSquare.piece = null;

            boolean removeThisIndex = false;
            if (currSquare.boardPanelRef.isWhiteInCheck() && this.isPieceWhite()
            || currSquare.boardPanelRef.isBlackInCheck() && !this.isPieceWhite()) {
                removeThisIndex = true;
            }
            //undoing the move
            //currSquare.piece = currentPiece;
            //iSquare.piece = possibleCapturedPiece;
            currSquare.setPiece(currentPiece);
            if (possibleCapturedPiece != null) {
                iSquare.setPiece((possibleCapturedPiece));
            } else {
                iSquare.piece = null;
            }
            if (removeThisIndex) {
                moveList.remove(i);
            }
        }

        return moveList;
    }

    public boolean isPiecePawn() {
        return this.imageFilePath.equals(this.square.boardPanelRef.WHITE_PAWN)
                || this.imageFilePath.equals(this.square.boardPanelRef.BLACK_PAWN);
    }

    public boolean isPieceKnight() {
        return this.imageFilePath.equals(this.square.boardPanelRef.WHITE_KNIGHT)
                || this.imageFilePath.equals(this.square.boardPanelRef.BLACK_KNIGHT);
    }

    public boolean isPieceBishop() {
        return this.imageFilePath.equals(this.square.boardPanelRef.WHITE_BISHOP)
                || this.imageFilePath.equals(this.square.boardPanelRef.BLACK_BISHOP);
    }

    public boolean isPieceRook() {
        return this.imageFilePath.equals(this.square.boardPanelRef.WHITE_ROOK)
                || this.imageFilePath.equals(this.square.boardPanelRef.BLACK_ROOK);
    }

    public boolean isPieceQueen() {
        return this.imageFilePath.equals(this.square.boardPanelRef.WHITE_QUEEN)
                || this.imageFilePath.equals(this.square.boardPanelRef.BLACK_QUEEN);
    }

    public boolean isPieceKing() {
        return this.imageFilePath.equals(this.square.boardPanelRef.WHITE_KING)
                || this.imageFilePath.equals(this.square.boardPanelRef.BLACK_KING);
    }


    public ArrayList<Square> getDiagOpenings() {
        ArrayList<Square> moveList = new ArrayList<>();
        Square currSquare = this.getSquare();
        Square[][] boardRef = currSquare.boardPanelRef.board;
        int currRow = currSquare.row;
        int currCol = currSquare.col;

        boolean upLeftBlocked = false;
        boolean upRightBlocked = false;
        boolean downLeftBlocked = false;
        boolean downRightBlocked = false;
        for (int offset = 1; offset < 8; offset++) {
            Square goalSquare = null;

            if (!upLeftBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow - offset, currCol - offset)) {
                    goalSquare = boardRef[currRow - offset][currCol - offset];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        upLeftBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        upLeftBlocked = true;
                    }

                }
            }
            if (!upRightBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow - offset, currCol + offset)) {
                    goalSquare = boardRef[currRow - offset][currCol + offset];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        upRightBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        upRightBlocked = true;
                    }
                }
            }
            if (!downLeftBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow + offset, currCol - offset)) {
                    goalSquare = boardRef[currRow + offset][currCol - offset];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        downLeftBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        downLeftBlocked = true;
                    }
                }
            }
            if (!downRightBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow + offset, currCol + offset)) {
                    goalSquare = boardRef[currRow + offset][currCol + offset];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        downRightBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        downRightBlocked = true;
                    }
                }
            }

        }

        return moveList;
    }

    public ArrayList<Square> getVertAndHorizOpenings() {
        ArrayList<Square> moveList = new ArrayList<>();
        Square currSquare = this.getSquare();
        Square[][] boardRef = currSquare.boardPanelRef.board;
        int currRow = currSquare.row;
        int currCol = currSquare.col;

        boolean upBlocked = false;
        boolean rightBlocked = false;
        boolean downBlocked = false;
        boolean leftBlocked = false;
        for (int offset = 1; offset < 8; offset++) {
            Square goalSquare = null;

            if (!upBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow - offset, currCol)) {
                    goalSquare = boardRef[currRow - offset][currCol];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        upBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        upBlocked = true;
                    }

                }
            }
            if (!rightBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow, currCol + offset)) {
                    goalSquare = boardRef[currRow][currCol + offset];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        rightBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        rightBlocked = true;
                    }
                }
            }
            if (!downBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow + offset, currCol)) {
                    goalSquare = boardRef[currRow + offset][currCol];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        downBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        downBlocked = true;
                    }
                }
            }
            if (!leftBlocked) {
                if (currSquare.boardPanelRef.isCoordOnBoard(currRow, currCol - offset)) {
                    goalSquare = boardRef[currRow][currCol - offset];
                    if (goalSquare.piece != null && goalSquare.piece.isPieceWhite() == this.isPieceWhite()) {
                        leftBlocked = true;
                    } else if (goalSquare.piece == null)  {
                        moveList.add(goalSquare);
                    } else if (!goalSquare.piece.isPieceWhite() && this.isPieceWhite()
                            || goalSquare.piece.isPieceWhite() && !this.isPieceWhite()) {
                        moveList.add(goalSquare);
                        leftBlocked = true;
                    }
                }
            }

        }

        return moveList;
    }
}

