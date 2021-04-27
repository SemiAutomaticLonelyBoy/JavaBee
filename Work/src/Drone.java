import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Drone extends Bee {
    protected static int count;
    //private static Image image;
    private static BufferedImage image;
    //protected long time;
    //protected static Integer lifeTime;

    public Drone(double cordX, double cordY, Integer life, long time, Integer ID) {
        this.ID = ID;
        this.cordX = cordX;
        this.cordY = cordY;
        this.time = time/1000;
        lifeTime = life;
        count++;
    }

    public void drive(){
        //this.cordX -= 0.005;
        //this.cordY += 0.005;
        if (count == 1){
            this.cordY -= 0.005;
            //this.cordY = 0.005;
        } else if(count == 2){
            this.cordX -= 0.005;
        } else{
            this.cordY += 0.005;
        }
    }

    public boolean check(long time){
        if(time/1000 == this.time + lifeTime){
            count--;
            return true;
        } else {
            return false;
        }
    }

    public static Image GET_IMAGE() {
        return image;
    }

    public static void setImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getCount() {
        return count;
    }
    public static void zeroCount(){ count = 0; }

    @Override
    public void Draw(Graphics g, int w, int h) {
        if (image == null) {
            return;
        }
        int x = (int) (cordX * (w - 320));
        int y = (int) (cordY * (h - 100));
        g.drawImage(image, x, y, null);
    }
}