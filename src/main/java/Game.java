import javax.swing.*;
import java.util.Vector;

public class Game {

    public GameJFrame gameFrame;



    public Game(final int boardSize) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.gameFrame = new GameJFrame(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
