import javax.swing.*;
import java.awt.*;

public abstract class Bee extends JComponent {
    protected double cordX, cordY;

    abstract public void Draw(Graphics g, int w, int h);
}
