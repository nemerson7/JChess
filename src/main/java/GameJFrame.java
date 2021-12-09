import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GameJFrame extends JFrame {

    public final Game gameRef;
    public BoardPanel mainPanel;
    public JPanel textPanel;
    //public final int frameSideSize;



    public GameJFrame(final Game gameRef) {
        super("JChess");

        int frameWidth = 536;
        int frameHeight = 600;

        this.gameRef = gameRef;



        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mainPanel = new BoardPanel(this);
        this.setContentPane(this.mainPanel);



    }









}
