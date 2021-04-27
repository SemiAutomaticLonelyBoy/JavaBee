abstract public class Treads extends Thread{
    static int dX;
    static int dY;
    boolean running = true;
    abstract public void run();



    public synchronized void changeState() {
        running = !running;
        notifyAll();
    }

    public synchronized void pause() {
        running = false;
        notifyAll();
    }

    public synchronized void continue_() {
        running = true;
        notifyAll();
    }

    public synchronized boolean isRunning() {
        return running;
    }

}
