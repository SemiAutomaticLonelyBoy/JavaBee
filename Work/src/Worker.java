import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Worker extends Bee{
    protected static int count;
    //private static Image image;
    private static BufferedImage image;
    //static Image img = null;


    public Worker(double cordX, double cordY){
        this.cordX = cordX;
        this.cordY = cordY;
        /*if (this.image == null){
            try{
                this.image = ImageIO.read(getClass().getResource("C:\\Users\\semen\\IdeaProjects\\Work\\res\\Worker.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }
        }*/
        count++;
    }

    public static Image GET_IMAGE() {
        return image;
    }

    public static void setImage(String path){


        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getCount(){
        return count;
    }

    public static void zeroCount(){ count = 0; }

    @Override
    public void Draw(Graphics g, int w, int h){
        if (image == null){
            return;
        }
        int x = (int)(cordX * (w-320));
        int y = (int)(cordY * (h-100));
        g.drawImage(image, x, y, null);
    }

    /*@Override
    protected void paintComponent(Graphics g) {
        g.draw3DRect(100,100, 10, 10, true);
    }*/
}

