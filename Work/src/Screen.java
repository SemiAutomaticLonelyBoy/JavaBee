import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.Timer;

public class Screen extends JFrame {
    private double cordX, cordY;
    ////////////////////////////////
    private int PERIOD = 100;
    protected long BEGIN_TIME = 0;
    protected long END_TIME = 0;
    private JLabel countLabel;

    private Timer clock;
    protected boolean showTime = true;
    protected boolean running = false;
    protected Integer time1 = 5;
    protected Integer time2 = 2;
    protected Integer P = 50;
    protected Integer N = 80;
    protected Integer workerLife = 5;
    protected Integer droneLife = 5;
    public static Vector<Bee> vector = new Vector<Bee>();
    public HashSet<Integer> hashSet = new HashSet<Integer>();
    public TreeMap<Integer, Long> treeMap = new TreeMap<Integer, Long>();

    long time;

    private JTextArea area;

    public Screen(int wigth, int heigth) throws Exception{
        super("Симуляция пчёл"); //Заголовок окна
        /*WorkerThread first = new WorkerThread();
        first.start();
        first.continue_();
        DroneThread second = new DroneThread();
        second.start();
        second.continue_();*/

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //останавливаем программу после выполнения
        setBounds(100, 100, wigth, heigth); // создаём окно
        countLabel = new JLabel("Время: 0"); // что-то вроде текста для вывода)))
        add(countLabel, BorderLayout.NORTH); //указываем место для вывода
        countLabel.setVisible(true);
        ImageIcon icon = new ImageIcon("res/gos.jpg");
        setIconImage(icon.getImage());
        Worker.setImage("res/Worker.png");
        Drone.setImage("res/Drone.png");
        //this.setBackground(new Color(215, 215, 215, 255));
        //////////////////////////ОТЧЁТ//////////////////////////////////////
        area = new JTextArea();
        area.setBounds(0, 0, 10, 10);
        area.setBackground(new Color(215, 215, 215, 255));
        area.setForeground(new Color(21, 21, 21, 255));
        area.setFont(new Font("Helvetica", Font.ITALIC, 20));
        area.setFocusable(false);
        area.setVisible(false);
        this.add(area);
        this.setVisible(true);
        FileReader nFile = new FileReader("file1.txt");
        Scanner scan = new Scanner(nFile);

        scan.hasNextLine();
        time1 = Integer.parseInt(scan.nextLine());
        scan.hasNextLine();
        time2 = Integer.parseInt(scan.nextLine());
        scan.hasNextLine();
        P = Integer.parseInt(scan.nextLine());
        scan.hasNextLine();
        N = Integer.parseInt(scan.nextLine());
        scan.hasNextLine();
        workerLife = Integer.parseInt(scan.nextLine());
        scan.hasNextLine();
        droneLife = Integer.parseInt(scan.nextLine());

        nFile.close();
        ////////////////////////////////////////////////////////////
        ButtonAct butt = new ButtonAct(this);
        butt.addFrame(this);
        add(butt, BorderLayout.EAST);

        /////////////////РАБОТА С КЛАВИШАМИ////////////////////////
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 66 && !running) { //начало симуляции (B)
                    //System.out.println(1);
                    try {
                        begin();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (keyEvent.getKeyCode() == 69 && running) { //конец симуляции (E)
                    //System.out.println(0);
                    end();
                } else if (keyEvent.getKeyCode() == 84) { //показать/убрать время (T)
                    switchTime();
                }
            }
        });
    }

    public static synchronized void workerMove(){
        for(Bee bee : vector){
            if(bee instanceof Worker ){
                bee.drive();
            }
        }
    }

    public static synchronized void droneMove(){
        for(Bee bee : vector){
            if(bee instanceof Drone){
                bee.drive();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Iterator<Integer> iterator = hashSet.iterator();
        if (running) {
            if (vector != null) {
                for (int i = 0; i < vector.size(); i++) {
                    vector.get(i).Draw(g, getWidth(), getHeight());
                    if (vector.get(i).check(time)) {
                        vector.remove(i);
                        //hashSet.remove(bee.get(i).ID);
                        //treeMap.remove(bee.get(i).ID);
                        this.repaint();


                    }
                }
            }
        }

    }

    public void switchTime() {
        showTime = !showTime;
        countLabel.setVisible(showTime);
    }

    public String panelPrint(String a) {
        a += "Время симуляции: ";
        a += (END_TIME - BEGIN_TIME) / 1000;
        a += "\n" + "Кол-во Пчёл: ";
        a += Worker.getCount() + Drone.getCount();
        a += "\nКол-во Рабочих пчёл: ";
        a += Worker.getCount();
        a += "\nКол-во Трутней: ";
        a += Drone.getCount();
        return a;
    }

    public void begin() throws Exception {
        FileWriter nFile = new FileWriter("file1.txt");
        nFile.write(time1.toString()); nFile.write("\n");
        nFile.write(time2.toString()); nFile.write("\n");
        nFile.write(P.toString()); nFile.write("\n");
        nFile.write(N.toString()); nFile.write("\n");
        nFile.write(workerLife.toString()); nFile.write("\n");
        nFile.write(droneLife.toString()); nFile.write("\n");
        nFile.close();
        Worker.zeroCount();
        Drone.zeroCount();
        countLabel.setVisible(showTime);
        running = true;

        area.setVisible(false);
        //vector = new Vector<Bee>();
        clock = new Timer();
        BEGIN_TIME = System.currentTimeMillis() / PERIOD * PERIOD; //System.currentTimeMillis() измерение длительности
        clock.schedule(new TimerTask() {
            @Override
            public void run() {
                END_TIME = System.currentTimeMillis() / PERIOD * PERIOD;
                time = END_TIME - BEGIN_TIME;
                update(time);
            }
        }, 0, PERIOD);
    }

    public void end() {
        countLabel.setVisible(false);
        running = false;

        clock.cancel();
        String result = "";
        result += "Время симуляции: ";
        result += (END_TIME - BEGIN_TIME) / 1000;
        result += "\n" + "Кол-во Пчёл: ";
        result += Worker.getCount() + Drone.getCount();
        result += "\nКол-во Рабочих пчёл: ";
        result += Worker.getCount();
        result += "\nКол-во Трутней: ";
        result += Drone.getCount();
        area.setText(result);
        area.setVisible(true);
        //bee.remove();


    }

    private void update(long time) {
        boolean flag = false;
        Random random = new Random();
        Integer randInd;
        Iterator<Integer> iterator = hashSet.iterator();

        if ((time) % (time1 * 1000) == 0) {
            if ((int) (Math.random() * 100) <= P) {

                do {
                    randInd = random.nextInt(899 + 1) + 100;
                    flag = false;
                    while (iterator.hasNext()) {
                        if (iterator.next() == randInd) flag = true;
                    }
                } while (flag);
                treeMap.put(randInd, time / 1000);

                cordX = Math.random();
                cordY = Math.random();
                vector.add(new Worker(cordX, cordY, workerLife, time, randInd));

            }
        }///трутень/(трутень+рабочий)*100
        if ((time) % (time2 * 1000) == 0) {
            if (stat() <= N) {

                do {
                    randInd = random.nextInt(899 + 1) + 100;
                    flag = false;
                    while (iterator.hasNext()) {
                        if (iterator.next() == randInd) flag = true;
                    }
                } while (flag);
                treeMap.put(randInd, time / 1000);

                cordX = Math.random();
                cordY = Math.random();
                vector.add(new Drone(cordX, cordY, droneLife, time, randInd));
                //this.repaint();
            }
        }
        if (time % 300 == 0) {
            this.repaint();
        }
        countLabel.setText("Время: " + time / 1000);
    }

    public String getInfoAboutOurObj() {
        String text = "Текущие объекты: (Press F)\n";
        for (int i = 0; i < vector.size(); i++) {

            text += ("ID пчелы: " + vector.get(i).ID.toString() + "\t Время жизни: " + treeMap.get(vector.get(i).ID).toString() + "\n");
        }
        /*
        for (Map.Entry<Integer, Long> entry : treeMap.entrySet()) {
            System.out.println("id: " + entry.getValue() + ", born time: " + entry.getKey() + "\n");
            text += ("id: " + entry.getKey() + ", born time: " + entry.getValue()+ "\n");
        }

         */
        return text;
    }

    public float stat() {
        float a = (float) (Drone.getCount()) / (Drone.getCount() + Worker.getCount()) * 100;
        return a;
    }
}
