import javax.swing.*;

public class JChess {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game(8));
    }

}
