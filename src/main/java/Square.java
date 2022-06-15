import javax.swing.*;
import java.awt.*;

public class Square extends JComponent {

    public final Color color;
    public final BoardPanel boardPanelRef;
    public Piece piece;
    public final int row;
    public final int col;

    public Square(Color color, BoardPanel boardPanelRef, final int row, final int col) {
        this.color = color;
        this.boardPanelRef = boardPanelRef;
        this.row = row;
        this.col = col;

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(this.color);
        g2.fillRect(
                this.col * this.boardPanelRef.cellSize,
                this.row * this.boardPanelRef.cellSize,
                this.boardPanelRef.cellSize,
                this.boardPanelRef.cellSize);

        if (this.piece != null) {
            this.piece.draw(g);
        }
    }


    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        piece.setSquare(this);
        this.piece = piece;
    }

    @Override
    public int getX() {
        return this.col * this.boardPanelRef.cellSize;
    }

    @Override
    public int getY() {
        return this.row * this.boardPanelRef.cellSize;
    }

    public String getNotationCoord() {
        int row = Math.abs(this.row - 8);
        int col = this.col + 97; //ascii arithmetic
        return String.valueOf((char) col) + String.valueOf(row);
    }

}
