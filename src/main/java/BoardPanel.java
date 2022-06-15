import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class BoardPanel extends JPanel{

    public final Square[][] board;
    public GameJFrame frameRef;
    public final int cellSize;
    private int currRow;
    private int currCol;
    public final int margin;
    private boolean whiteMove;
    private int turnCounter;
    private Square enPassantTarget;
    private int enPassantTurn;
    private StockfishClient stockfish;

    public final String WHITE_PAWN;
    public final String WHITE_KNIGHT;
    public final String WHITE_BISHOP;
    public final String WHITE_ROOK;
    public final String WHITE_QUEEN;
    public final String WHITE_KING;
    public final String BLACK_PAWN;
    public final String BLACK_KNIGHT;
    public final String BLACK_BISHOP;
    public final String BLACK_ROOK;
    public final String BLACK_QUEEN;
    public final String BLACK_KING;

    public BoardPanel(GameJFrame frameRef) {
        this.frameRef = frameRef;

        StockfishClient stockfish = new StockfishClient(this);
        this.stockfish = stockfish;
        this.stockfish.initialize();

        //establishing close behavior
        this.frameRef.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stockfish.exitClient();
            }
        });

        this.whiteMove = true;

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.currRow = -1;
        this.currCol = -1;
        this.setVisible(true);
        this.board = new Square[8][8];
        this.margin = 64;
        this.cellSize = (this.frameRef.getHeight() - this.margin) / 8;
        this.turnCounter = 1;


        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {

                    if (isCoordOnBoard(e.getY() / cellSize, e.getX() / cellSize)) {
                        int newRow = e.getY() / cellSize;
                        int newCol = e.getX() / cellSize;



                        if (isCoordOnBoard(currRow, currCol)
                                && board[currRow][currCol].piece.isPieceWhite() == whiteMove
                                && (board[currRow][currCol].piece.getAllMoves().contains(board[newRow][newCol]))) {

                            makeMove(board[currRow][currCol], board[newRow][newCol]);
                            //adding computer move
                            Square[] computerMoves = new Square[0];
                            try {
                                computerMoves = stockfish.computerMove();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            } catch (ExecutionException ex) {
                                ex.printStackTrace();
                            } catch (TimeoutException ex) {
                                ex.printStackTrace();
                            }
                            makeMove(computerMoves[0], computerMoves[1]);
                        } else if (board[newRow][newCol].piece != null && board[newRow][newCol].piece.isPieceWhite() == whiteMove) {
                            currRow = newRow;
                            currCol = newCol;
                        }
                    }
                    e.getComponent().repaint();
                }
            }


            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //initializing squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    this.board[i][j] = new Square(Color.WHITE, this, i, j);
                } else {
                    this.board[i][j] = new Square(Color.GRAY, this, i, j);
                }
            }
        }

        //initializing pieces
        this.WHITE_PAWN = "Resources/wpawn.png";
        this.WHITE_KNIGHT = "Resources/wknight.png";
        this.WHITE_BISHOP = "Resources/wbishop.png";
        this.WHITE_ROOK = "Resources/wrook.png";
        this.WHITE_QUEEN = "Resources/wqueen.png";
        this.WHITE_KING = "Resources/wking.png";

        this.BLACK_PAWN = "Resources/bpawn.png";
        this.BLACK_KNIGHT = "Resources/bknight.png";
        this.BLACK_BISHOP = "Resources/bbishop.png";
        this.BLACK_ROOK = "Resources/brook.png";
        this.BLACK_QUEEN = "Resources/bqueen.png";
        this.BLACK_KING = "Resources/bking.png";

        //adding white pawns
        for (int i = 0; i < 8; i++) {
            this.board[6][i].setPiece(new Pawn(this.board[6][i], WHITE_PAWN, true));
        }

        //adding black pawns
        for (int i = 0; i < 8; i++) {
            this.board[1][i].setPiece(new Pawn(this.board[1][i], BLACK_PAWN, false));
        }

        //adding rooks
        this.board[0][0].setPiece(new Rook(this.board[0][0], BLACK_ROOK, false));
        this.board[0][7].setPiece(new Rook(this.board[0][7], BLACK_ROOK, false));
        this.board[7][0].setPiece(new Rook(this.board[7][0], WHITE_ROOK, true));
        this.board[7][7].setPiece(new Rook(this.board[7][7], WHITE_ROOK, true));

        //adding knights
        this.board[0][1].setPiece(new Knight(this.board[0][1], BLACK_KNIGHT, false));
        this.board[0][6].setPiece(new Knight(this.board[0][6], BLACK_KNIGHT, false));
        this.board[7][1].setPiece(new Knight(this.board[7][1], WHITE_KNIGHT, true));
        this.board[7][6].setPiece(new Knight(this.board[7][6], WHITE_KNIGHT, true));

        //adding bishops
        this.board[0][2].setPiece(new Bishop(this.board[0][2], BLACK_BISHOP, false));
        this.board[0][5].setPiece(new Bishop(this.board[0][5], BLACK_BISHOP, false));
        this.board[7][2].setPiece(new Bishop(this.board[7][2], WHITE_BISHOP, true));
        this.board[7][5].setPiece(new Bishop(this.board[7][5], WHITE_BISHOP, true));

        //adding queens
        this.board[0][3].setPiece(new Queen(this.board[0][3], BLACK_QUEEN, false));
        this.board[7][3].setPiece(new Queen(this.board[7][3], WHITE_QUEEN, true));

        //adding kings
        this.board[0][4].setPiece(new King(this.board[0][4], BLACK_KING, false));
        this.board[7][4].setPiece(new King(this.board[7][4], WHITE_KING, true));


    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        System.out.println("currRow: "+ currRow + ", currCol: " + currCol);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j].paintComponent(g);
            }
        }

        //highlighting currently selected piece
        if (this.currRow != -1 && this.currCol != -1) {
            g.setColor(Color.ORANGE);
            g.fillRect(currCol * cellSize,currRow * cellSize, cellSize, cellSize);
            Square currSquare = this.board[currRow][currCol];
            if (currSquare.piece != null) {
                currSquare.piece.draw(g);
            }
            assert currSquare.piece != null;
            ArrayList<Square> possibleMoves = currSquare.piece.getAllMoves();
            possibleMoves.forEach((square) -> {
                g.setColor(Color.CYAN);
                g.fillRect(square.col * cellSize, square.row * cellSize, cellSize, cellSize);
                if (square.piece != null) {
                    square.piece.draw(g);
                }
            });

        }
        g.setColor(Color.BLACK);
        g.drawLine(0, this.frameRef.getHeight() - this.margin, this.getWidth(), this.frameRef.getHeight() - this.margin);
        for (int i = 0; i < 8; i++) {
            g.drawLine(0, i * cellSize, frameRef.getWidth(), i * cellSize);
            g.drawLine(i * cellSize, 0, i * cellSize, frameRef.getHeight() - this.margin);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        final int messageX = 204;
        final int messageY = 560;
        g.drawString("Move: " + ((whiteMove) ? "White": "Black"), 10, messageY);

        if (this.whiteWins()) {
            g.drawString("* White Wins *", messageX, messageY);
        } else if (this.blackWins()) {
            g.drawString("* Black Wins *", messageX, messageY);
        } else if (this.isWhiteInCheck()) {
            g.drawString("White in check", messageX, messageY);
        } else if (this.isBlackInCheck()) {
            g.drawString("Black in check", messageX, messageY);
        } else if (this.stalemate()) {
            g.drawString("* Stalemate *", messageX, messageY);
        } else {
            g.drawString("Game in Progress", messageX, messageY);
        }


    }

    public void makeMove(Square currSquare, Square goalSquare) {

        //handling castling
        if (currSquare.piece.isPieceKing()
                && !currSquare.piece.hasMoved) {
            int colorRow = (currSquare.piece.isPieceWhite()) ? 7 : 0;
            if (goalSquare.col == 2 && ((King) currSquare.piece).canCastleQueenside()) {
                //queenside
                this.board[colorRow][3].setPiece(this.board[colorRow][0].piece);
                this.board[colorRow][0].piece = null;
                this.board[colorRow][3].piece.hasMoved = true;
            } else if (goalSquare.col == 6 && ((King) currSquare.piece).canCastleKingside()) {
                //kingside
                this.board[colorRow][5].setPiece(this.board[colorRow][7].piece);
                this.board[colorRow][7].piece = null;
                this.board[colorRow][5].piece.hasMoved = true;
            }
        }

        //enabling pawns that can en passant capture for the next move
        if (!currSquare.piece.hasMoved
                && currSquare.piece.isPiecePawn()
                && (Math.abs(currSquare.row - goalSquare.row) == 2)) {

            boolean isWhite = currSquare.piece.isPieceWhite();

            //adding en passant target for stockfish
            this.enPassantTarget = this.board[currSquare.row + ((isWhite) ? -1 : 1)][currSquare.col];
            this.enPassantTurn = this.turnCounter + 1;

            int goalRow = goalSquare.row;
            //adding en passant turn to the left pawn
            if (isCoordOnBoard(goalRow, goalSquare.col - 1)) {
                Piece attacker = this.board[goalRow][goalSquare.col - 1].piece;
                if (attacker != null
                        && attacker.isPiecePawn()
                        && isWhite != attacker.isPieceWhite()) {
                    ((Pawn) attacker).enPassantTurn = this.turnCounter + 1;
                    ((Pawn) attacker).enPassantVictimSquare = goalSquare;
                }
            }
            //adding en passant turn to the right pawn
            if (isCoordOnBoard(goalRow, goalSquare.col + 1)) {
                Piece attacker = this.board[goalRow][goalSquare.col + 1].piece;
                if (attacker != null
                        && attacker.isPiecePawn()
                        && isWhite != attacker.isPieceWhite()) {
                    ((Pawn) attacker).enPassantTurn = this.turnCounter + 1;
                    ((Pawn) attacker).enPassantVictimSquare = goalSquare;
                }
            }
        }

        Piece capturedPiece = goalSquare.piece;
        goalSquare.setPiece(currSquare.piece);
        goalSquare.piece.hasMoved = true;
        currSquare.piece = null;
        this.currRow = -1;
        this.currCol = -1;
        this.whiteMove = !this.whiteMove;
        this.turnCounter++;

        //removing captured piece from en passant capture
        if (goalSquare.piece.isPiecePawn()
                && goalSquare.col != currSquare.col
                && capturedPiece == null) {
            this.board[(goalSquare.piece.isPieceWhite()) ? goalSquare.row + 1 : goalSquare.row - 1][goalSquare.col].piece = null;
        }

        //handling promotion
        if (goalSquare.piece.isPiecePawn() && goalSquare.piece.isPieceWhite() && goalSquare.row == 0
                || goalSquare.piece.isPiecePawn() && !goalSquare.piece.isPieceWhite() && goalSquare.row == 7) {
            Object[] possibilities = {"Queen", "Rook", "Bishop", "Knight"};
            String pieceChoice = (String)JOptionPane.showInputDialog(
                    this.frameRef,
                    "Select piece for pawn promotion: ",
                    "Promotion",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "Queen");

            boolean isWhite = goalSquare.piece.imageFilePath.equals(WHITE_PAWN);

            if (pieceChoice != null) {
                switch (pieceChoice) {
                    case "Queen" -> goalSquare.setPiece(new Queen(goalSquare, (isWhite) ? WHITE_QUEEN : BLACK_QUEEN, isWhite));
                    case "Rook" -> goalSquare.setPiece(new Rook(goalSquare, (isWhite) ? WHITE_ROOK : BLACK_ROOK, isWhite));
                    case "Bishop" -> goalSquare.setPiece(new Bishop(goalSquare, (isWhite) ? WHITE_BISHOP : BLACK_BISHOP, isWhite));
                    case "Knight" -> goalSquare.setPiece(new Knight(goalSquare, (isWhite) ? WHITE_KNIGHT : BLACK_KNIGHT, isWhite));
                }
            } else {
                //undoing move because user closed inquiry window
                currSquare.setPiece(goalSquare.piece);
                goalSquare.setPiece(capturedPiece);
                this.whiteMove = !this.whiteMove;
                this.turnCounter--;
            }
        }

        System.out.println(this.parseBoardToFEN());

    }

    public int getCurrRow() {
        return this.currRow;
    }

    public int getCurrCol() {
        return this.currCol;
    }

    public boolean isWhiteInCheck() {
        return this.checkHelper(true);
    }

    public boolean isBlackInCheck() {
        return this.checkHelper(false);
    }

    private boolean checkHelper(boolean isWhite) {

        Square kingSquare = (isWhite) ? this.whiteKingSquare() : this.blackKingSquare();

        if (kingSquare == null) {
            return true;
        }

        int row = kingSquare.row;
        int col = kingSquare.col;

        int[][] knightOffsets = {{-2, -1}, {2, -1}, {-2, 1}, {2, 1}, {-1, -2}, {1, -2}, {-1, 2}, {1, 2}};
        for (int[] offset : knightOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            if (isCoordOnBoard(newRow, newCol) && this.board[newRow][newCol].piece != null) {
                Piece offender = this.board[newRow][newCol].piece;
                if (offender.isPieceKnight() && offender.isPieceWhite() != isWhite) {
                    return true;
                }
            }
        }

        ArrayList<Square> vertAndHoriz = kingSquare.piece.getVertAndHorizOpenings();
        for (Square square : vertAndHoriz) {
            if (square.piece != null) {
                if ((square.piece.isPieceQueen() || square.piece.isPieceRook())
                        && square.piece.isPieceWhite() != isWhite) {
                    return true;
                }
            }
        }

        ArrayList<Square> diagonals = kingSquare.piece.getDiagOpenings();
        for (Square square : diagonals) {
            if (square.piece != null) {
                if ((square.piece.isPieceQueen() || square.piece.isPieceBishop())
                        && square.piece.isPieceWhite() != isWhite) {
                    return true;
                }
            }
        }

        int[][] adjacentOffsets = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        for (int[] offset : adjacentOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            if (isCoordOnBoard(newRow, newCol) && this.board[newRow][newCol].piece != null) {
                Piece offender = this.board[newRow][newCol].piece;
                if (offender.isPieceKing() && offender.isPieceWhite() != isWhite) {
                    return true;
                } else if (offset[0] == ((isWhite) ? -1 : 1) && (offset[1] == -1 || offset[1] == 1)
                        && offender.isPiecePawn()
                        && offender.isPieceWhite() != isWhite) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCoordOnBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean whiteWins() {
        return this.checkmateHelper(true) && this.isBlackInCheck();
    }

    public boolean blackWins() {
        return this.checkmateHelper(false) && this.isWhiteInCheck();
    }

    public boolean stalemate() {
        return this.checkmateHelper(true) && !this.isBlackInCheck()
                || this.checkmateHelper(false) && this.isBlackInCheck();
    }

    private boolean checkmateHelper(boolean isWhite) {

        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = this.board[i][j].piece;
                if (piece != null && piece.isPieceWhite() != isWhite) {
                    pieces.add(piece);
                }
            }
        }
        ArrayList<Square> possibleMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece != null) {
                possibleMoves.addAll(piece.getAllMoves());
            }
        }
        return possibleMoves.size() == 0;
    }

    public Square whiteKingSquare() {
        return this.kingFinder(true);
    }

    public Square blackKingSquare() {
        return this.kingFinder(false);
    }

    private Square kingFinder(boolean isWhite) {
        Square kingSquare = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j].piece;
                if (piece != null && piece.isPieceKing() && piece.isPieceWhite() == isWhite) {
                    kingSquare = board[i][j];
                }
            }
        }
        return kingSquare;
    }

    public int getTurnCounter() {
        return this.turnCounter;
    }



    public String parseBoardToFEN() {

        StringBuilder out = new StringBuilder("");

        for (int row = 0; row < 8; row++) {
            int spaceCount = 0;
            for (int col = 0; col < 8; col++) {
                if (this.board[row][col].piece != null) {

                    //adding spaces before adding piece
                    if (spaceCount > 0) {
                        out.append(String.valueOf(spaceCount));
                        spaceCount = 0;
                    }

                    Piece piece = this.board[row][col].piece;
                    boolean pieceIsWhite = piece.isPieceWhite();
                    int outputPiece = (pieceIsWhite) ? 0 : 32; //used to find ascii char, black is lowercase so add 32 to white's char
                    if (piece.isPieceKing()) {
                        outputPiece += (int) 'K';
                    } else if (piece.isPieceQueen()) {
                        outputPiece += (int) 'Q';
                    } else if (piece.isPieceRook()) {
                        outputPiece += (int) 'R';
                    } else if (piece.isPieceBishop()) {
                        outputPiece += (int) 'B';
                    } else if (piece.isPieceKnight()) {
                        outputPiece += (int) 'N';
                    } else if (piece.isPiecePawn()) {
                        outputPiece += (int) 'P';
                    }
                    out.append((char) outputPiece);

                } else /*on a space, count spaces*/ {
                    spaceCount++;
                    if (col == 7) {
                        out.append(String.valueOf(spaceCount));
                    }
                }
            }
            //adding slash after rank
            if (row != 7) {
                out.append("/");
            }
        }

        //adding player to move to string
        if (this.whiteMove) {
            out.append(" w ");
        } else {
            out.append(" b ");
        }

        boolean castleAdded = false;
        if (((King) this.whiteKingSquare().piece).canCastleKingside()) {
            castleAdded = true;
            out.append("K");
        }

        if (((King) this.whiteKingSquare().piece).canCastleQueenside()) {
            castleAdded = true;
            out.append("Q");
        }

        if (((King) this.blackKingSquare().piece).canCastleKingside()) {
            castleAdded = true;
            out.append("k");
        }

        if (((King) this.blackKingSquare().piece).canCastleQueenside()) {
            castleAdded = true;
            out.append("q");
        }

        if (!castleAdded) {
            out.append("-");
        }

        //adding enpassant target
        if (this.turnCounter == this.enPassantTurn) {
            out.append(" " + this.enPassantTarget.getNotationCoord());
        } else {
            out.append(" -");
        }


        //adding zero for 50 move rule so stockfish will ignore it
        out.append(" 0 ");
        //adding turn counter
        out.append(String.valueOf(this.turnCounter/2)+"\n");


        return out.toString();
    }

}
