import javax.swing.*;
import java.awt.*;

public abstract class Bee implements IBehaviour {
    protected double cordX, cordY;
    protected Integer ID;
    protected long time;
    protected static Integer lifeTime;

    abstract public void Draw(Graphics g, int w, int h);
    abstract public boolean check(long time);
    abstract public void drive();
    /*@Override public int getX() { return cordX; }
    @Override public int getY() {
        return ;
    }
    @Override public void setX(int x) {
        this.x = x;
    }
    @Override public void setY(int y) {
        this.y = y;
    }*/
}