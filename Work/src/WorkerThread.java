public class WorkerThread extends Treads {
    public WorkerThread() {
        Thread.currentThread().setName("1");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if(Screen.vector != null){
                    Screen.workerMove();
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continue;
            }

            synchronized (this) {
                while (!running) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        continue;
                    }
                }
            }
        }
    }

}
