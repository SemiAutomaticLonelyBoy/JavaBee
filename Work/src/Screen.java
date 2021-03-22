import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

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
    private Vector<Bee> bee = null;

    private JTextArea area;

    public Screen(int wigth, int heigth) {
        super("Пчёлки"); //Заголовок окна
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
        ////////////////////////////////////////////////////////////
        ButtonAct butt = new ButtonAct();
        butt.addFrame(this);
        add(butt,BorderLayout.EAST );

        /////////////////РАБОТА С КЛАВИШАМИ////////////////////////
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 66 && !running) { //начало симуляции (B)
                    //System.out.println(1);
                    begin();
                } else if (keyEvent.getKeyCode() == 69 && running) { //конец симуляции (E)
                    //System.out.println(0);
                    end();
                } else if (keyEvent.getKeyCode() == 84) { //показать/убрать время (T)
                    switchTime();
                }
            }
        });
        ////////////////////////////////////////////////////////////

        //////////////////////////////////////////

    }

    @Override
    public void paint(Graphics g) {
        if (running) {
            if (bee != null) {
                for (int i = 0; i < bee.size(); i++) {
                    bee.get(i).Draw(g, getWidth(), getHeight());
                }
            }
        }


    }
    public void switchTime(){
        showTime = !showTime;
        countLabel.setVisible(showTime);
    }
    public String panelPrint(String a){
        a += "Время симуляции: ";
        a += (END_TIME - BEGIN_TIME) / 1000;
        a += "\n" + "Кол-во пчёл: ";
        a += Worker.getCount() + Drone.getCount();
        a += "\nКол-во рабочих: ";
        a += Worker.getCount();
        a += "\nКол-во трутней: ";
        a += Drone.getCount();
        return a;
    }
    public void begin() {
        Worker.zeroCount();
        Drone.zeroCount();
        countLabel.setVisible(showTime);
        running = true;

        area.setVisible(false);
        bee = new Vector<Bee>();
        clock = new Timer();
        BEGIN_TIME = System.currentTimeMillis() / PERIOD * PERIOD; //System.currentTimeMillis() измерение длительности
        clock.schedule(new TimerTask() {
            @Override
            public void run() {
                END_TIME = System.currentTimeMillis() / PERIOD * PERIOD;
                long time = END_TIME - BEGIN_TIME;
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
        result += "\n" + "Кол-во пчёл: ";
        result += Worker.getCount() + Drone.getCount();
        result += "\nКол-во рабочих: ";
        result += Worker.getCount();
        result += "\nКол-во трутней: ";
        result += Drone.getCount();
        area.setText(result);
        area.setVisible(true);
        bee.removeAllElements();


    }

    private void update(long time) {
        if ((time) % (time1 * 1000) == 0) {
            if ((int) (Math.random() * 100) <= P) {
                cordX = Math.random();
                cordY = Math.random();
                bee.addElement(new Worker(cordX, cordY));
            }
        }///трутень/(трутень+рабочий)*100
        if ((time) % (time2 * 1000) == 0) {
            if (stat() <= N) {
                cordX = Math.random();
                cordY = Math.random();
                bee.addElement(new Drone(cordX, cordY));
            }
        }
        this.repaint();
        countLabel.setText("Время: " + time / 1000);
    }

    public float stat() {
        float a = (float) (Drone.getCount()) / (Drone.getCount() + Worker.getCount()) * 100;
        return a;
    }

}
