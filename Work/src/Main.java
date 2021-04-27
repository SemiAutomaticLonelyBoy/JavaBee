

public class Main {
    public static void main(String[] argc){
        int width = 900;
        int height = 700;
        Screen app = null;
        try {
            app = new Screen(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        app.setVisible(true);

        //app.pack();
    }
}
