import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;


public class ButtonAct extends JPanel {
    private JTextArea myConsole = new JTextArea("Console> ");
    private static final Object PS = "Console> ";;
    private Screen screen;
    JDialog dialog;
    JDialog dialogFinal;
    JDialog consol;
    JDialog obj;
    WorkerThread first;
    DroneThread second;

    public ButtonAct(Screen screen) {
        super();
        this.screen = screen;
        first = new WorkerThread();
        second = new DroneThread();
        first.start();
        second.start();
        setLayout(new GridLayout(25, 1, 2, 2));
        setPreferredSize(new Dimension(200, 600));
        JButton startThreed = new JButton("Запуск/Остановка Рабочих пчёл");
        startThreed.setFocusable(false);
        add(startThreed);
        startThreed.addActionListener(listener -> {
           first.changeState();
        });
        JButton startThreed1 = new JButton("Запуск/Остановка  Трутней");
        startThreed1.setFocusable(false);
        add(startThreed1);
        startThreed1.addActionListener(listener -> {
            second.changeState();
        });
        JButton start = new JButton("Старт");
        start.setFocusable(false);
        add(start);
        start.addActionListener(listener -> {
            if (!screen.running) {
                try {
                    screen.begin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //////////////////////////////
        JButton remove = new JButton("Стоп");
        remove.setFocusable(false);
        add(remove);
        remove.addActionListener(listener -> {
            if (screen.running) {
                screen.end();
            }
        });
        /////////////////////////////////////////////////////////////


// создание группы переключателей
        ButtonGroup gr = new ButtonGroup();
        // создание переключателей, добавление в группу и в контейнер
        JRadioButton first, second;
        first = new JRadioButton("Показать таймер", true);
        gr.add(first);
        add(first);
// переключатель установлен
        second = new JRadioButton("Скрыть таймер");
        gr.add(second);
        add(second);
        first.addActionListener(listener -> {
            if (!screen.showTime) {
                screen.switchTime();
            }
        });
        second.addActionListener(listener -> {
            if (screen.showTime) {
                screen.switchTime();
            }
        });

        //////////////////////////////////Настройки///////////////////////////////////////////

        BoundedRangeModel model = new DefaultBoundedRangeModel(screen.droneLife, 0, 1, 10);
        BoundedRangeModel model2 = new DefaultBoundedRangeModel(screen.workerLife, 0, 1, 10);

        //////////////////////////////////////////////////////////////////////
        dialog = new JDialog(screen, "Настройки", true);
        dialog.setSize(300, 400);
        dialog.setLocation(200, 100);
        dialog.setResizable(false);
        dialog.setLayout(new GridLayout(12, 1));
        JLabel spawner = new JLabel("Время генерации Рабочих пчёл:"); // что-то вроде текста для вывода)))
        spawner.setFocusable(false);
        //////////////////////////////////////////////////////////////////////
        JSlider slider1 = new JSlider(model);
        JSlider slider2 = new JSlider(model2);

        JLabel label;
        JLabel label1;
        Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        labels.put(new Integer(0), new JLabel("<html><font color=red size=3>0"));
        labels.put(new Integer(60), new JLabel("<html><font color=gray size=3>30"));
        labels.put(new Integer(120), new JLabel("<html><font color=blue size=4>60"));

        label = new JLabel(getPercent(slider1.getValue()));
        label1 = new JLabel(getPercent1(slider2.getValue()));
        slider1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // меняем надпись
                int value = ((JSlider)e.getSource()).getValue();
                label.setText(getPercent(value));
                screen.droneLife = ((JSlider)e.getSource()).getValue();
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // меняем надпись
                int value = ((JSlider)e.getSource()).getValue();
                label1.setText(getPercent1(value));
                screen.workerLife = ((JSlider)e.getSource()).getValue();
            }
        });

        add(label);
        add(slider1);
        add(label1);
        add(slider2);

        add(spawner, BorderLayout.CENTER);
        JComboBox<Integer> combobox = new JComboBox<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        combobox.setEditable(false);
        combobox.setSelectedIndex(screen.time1 - 1);
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                Integer item = (Integer) box.getSelectedItem();

                screen.time1 = item;

            }
        };
        combobox.addActionListener(actionListener);
        add(combobox);
        ////////////////////////////////////////////////////////////////
        JLabel rateWorker = new JLabel("Шанс генерации Рабочих пчёл:"); // что-то вроде текста для вывода)))
        rateWorker.setFocusable(false);
        add(rateWorker, BorderLayout.CENTER);
        JComboBox<Integer> combobox2 = new JComboBox<Integer>(
                new Integer[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100});
        combobox2.setEditable(false);
        combobox2.setSelectedIndex(screen.P / 10 - 1);
        ActionListener actionListener2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                Integer item = (Integer) box.getSelectedItem();

                screen.P = item;

            }
        };
        combobox2.addActionListener(actionListener2);
        add(combobox2);

///////////////////////////////////////////////////////////////////////////////////
        JLabel timeDrone = new JLabel("Время генерации Трутней:"); // что-то вроде текста для вывода)))
        timeDrone.setFocusable(false);
        add(timeDrone, BorderLayout.CENTER);
        JComboBox<Integer> combobox3 = new JComboBox<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        combobox3.setEditable(false);
        combobox3.setSelectedIndex(screen.time2 - 1);
        ActionListener actionListener3 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                Integer item = (Integer) box.getSelectedItem();

                screen.time2 = item;

            }
        };
        combobox3.addActionListener(actionListener3);
        add(combobox3);

        JLabel rateDrone = new JLabel("Процент генерации Трутней от общего кол-ва:"); // что-то вроде текста для вывода)))
        rateDrone.setFocusable(false);
        add(rateDrone, BorderLayout.CENTER);
        JComboBox<Integer> combobox4 = new JComboBox<Integer>(
                new Integer[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100});
        combobox4.setEditable(false);
        combobox4.setSelectedIndex(7);
        ActionListener actionListener4 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                Integer item = (Integer) box.getSelectedItem();

                screen.N = item;

            }
        };
        combobox4.addActionListener(actionListener4);
        add(combobox4);

        //////////////////////////////////Симуляция///////////////////////////////////////////
        JButton sim = new JButton("Итоги симуляции");
        sim.setFocusable(false);
        add(sim);
        dialogFinal = new JDialog(screen, "Итоги симуляции", true);
        dialogFinal.setSize(300, 400);
        dialogFinal.setLocation(200, 100);
        dialogFinal.setResizable(false);

        dialogFinal.setLayout(new GridLayout(3, 1));
        JTextArea field = new JTextArea();
        field.setBounds(0, 0, 10, 10);
        field.setBackground(new Color(215, 215, 215, 255));
        field.setForeground(new Color(21, 21, 21, 255));
        field.setFont(new Font("Helvetica", Font.ITALIC, 20));
        field.setFocusable(false);
        field.setVisible(true);
        dialogFinal.add(field, BorderLayout.SOUTH);

        JButton ok = new JButton("Ok");
        ok.setFocusable(false);
        dialogFinal.add(ok);
        JButton cancel = new JButton("Отмена");
        cancel.setFocusable(false);
        dialogFinal.add(cancel);

        sim.addActionListener(listener -> {
            dialogFinal.setVisible(true);
            String result = "";
            result = screen.panelPrint(result);
            field.setText(result);
            field.setVisible(true);
            ////////////////////////////////////////////////////
            ok.setVisible(true);
            cancel.setVisible(true);

        });
        cancel.addActionListener(listener -> {
            dialogFinal.setVisible(false);
        });
        ok.addActionListener(listener -> {
            if (screen.running) {
                screen.end();
            }
        });

        JButton object = new JButton("Текущие объекты");
        object.setFocusable(false);
        add(object);
        obj = new JDialog(screen, "Текущие объекты", true);
        obj.setSize(300, 400);
        obj.setLocation(200, 100);
        obj.setResizable(true);
        obj.setLayout(new GridLayout(1, 1));
        obj.setBackground(new Color(222, 222, 222));
        JTextArea infoArea = new JTextArea();
        infoArea.setBounds(0, 0, 100, 100);
        infoArea.setBackground(new Color(222, 222, 222));
        infoArea.setFont(new Font("Helvetica", Font.ITALIC, 12));
        infoArea.setFocusable(false);
        object.addActionListener(listener -> {
            obj.setVisible(true);
            String text = screen.getInfoAboutOurObj();
            infoArea.setText(text);
            infoArea.setVisible(true);
        });
        obj.add(infoArea);
        //////////Запись в файл//////////////
        JButton textSave = new JButton("Сохранить");
        textSave.setFocusable(false);
        add(textSave);
        textSave.addActionListener(listener -> {
            FileWriter nFile = null;
            try {
                if(screen.vector != null) {
                    nFile = new FileWriter("object.txt");


                    nFile.write(String.valueOf(screen.vector.size()));
                    nFile.write("\n");
                    nFile.write(String.valueOf(screen.time));
                    nFile.write("\n");

                    for (int i = 0; i < screen.vector.size(); i++) {
                        nFile.write(String.valueOf(screen.vector.get(i).getClass()));
                        nFile.write("\n");
                        nFile.write(String.valueOf(screen.vector.get(i).ID));
                        nFile.write("\n");
                        nFile.write(String.valueOf(screen.vector.get(i).time));
                        nFile.write("\n");
                        nFile.write(String.valueOf(screen.vector.get(i).lifeTime));
                        nFile.write("\n");
                        nFile.write(String.valueOf(screen.vector.get(i).cordX));
                        nFile.write("\n");
                        nFile.write(String.valueOf(screen.vector.get(i).cordY));
                        nFile.write("\n");
                    }

                    nFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        JButton textDownload = new JButton("Загрузить");
        textDownload.setFocusable(false);
        add(textDownload);
        textDownload.addActionListener(listener -> {
            try {
                screen.end();
            } catch (Exception e) {
                e.printStackTrace();
            }



            FileReader nFile = null;
            Scanner scan = null;
            try {
                nFile = new FileReader("object.txt");
                scan = new Scanner(nFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            scan.hasNextLine();
            int size = Integer.parseInt(scan.nextLine());
            scan.hasNextLine();
            long ourTime = Long.parseLong(scan.nextLine());
            scan.hasNextLine();
            if (size != 0) {
                //screen.vector = new Vector<Ant>();
                screen.vector.removeAllElements();
                //screen.vector = null;
                screen.time = ourTime;


                for (int i = 0; i < size; i++) {
                    double cordX, cordY;
                    Integer ID;
                    long time;
                    Integer lifeTime;


                    if (scan.nextLine().equals("class Drone")) {
                        //System.out.println(i + "    " + scan.nextLine());
                        ID = Integer.parseInt(scan.nextLine());
                        scan.hasNextLine();
                        time = Long.parseLong(scan.nextLine());
                        scan.hasNextLine();
                        lifeTime = Integer.parseInt(scan.nextLine());
                        scan.hasNextLine();
                        cordX = Double.parseDouble(scan.nextLine());
                        scan.hasNextLine();
                        cordY = Double.parseDouble(scan.nextLine());
                        scan.hasNextLine();
                        screen.vector.add(new Drone(cordX, cordY, lifeTime, time, ID));
                    }
                    else {
                        //System.out.println(i + "    " + scan.nextLine());
                        ID = Integer.parseInt(scan.nextLine());
                        scan.hasNextLine();
                        time = Long.parseLong(scan.nextLine());
                        scan.hasNextLine();
                        lifeTime = Integer.parseInt(scan.nextLine());
                        scan.hasNextLine();
                        cordX = Double.parseDouble(scan.nextLine());
                        scan.hasNextLine();
                        cordY = Double.parseDouble(scan.nextLine());
                        scan.hasNextLine();
                        screen.vector.add(new Worker(cordX, cordY, lifeTime, time, ID));
                    }
                    //scan.hasNextLine();
                }

            }
            /*
            for (int i = 0; i < screen.vector.size(); i++) {
                System.out.printf(screen.vector.get(i).getClass().toString() + "\t");
                System.out.printf(screen.vector.get(i).ID + "\t");
                System.out.printf(screen.vector.get(i).time + "\t");
                System.out.printf(screen.vector.get(i).cordX + "\t");
                System.out.printf(screen.vector.get(i).cordY + "\t");
                System.out.println(screen.vector.get(i).lifeTime + "\t");
            }

             */


            try {
                nFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                screen.begin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        /*JButton console = new JButton("Консоль");
        console.setFocusable(false);
        add(console);

        dialogConsole = new JDialog(screen, "Консоль", true);
        dialogConsole.setSize(1000, 400);
        dialogConsole.setLocation(200, 100);
        dialogConsole.setResizable(false);
        dialogConsole.setLayout(new GridLayout(3, 1));
        dialogConsole.setBackground(new Color(222, 222, 222));
        dialogConsole.add(field, BorderLayout.SOUTH);
        console.addActionListener(listener -> {
            dialogConsole.setVisible(true);
        });
        this.setSize(800, 500);

        myConsole.setFont(new Font("Consolas", Font.PLAIN, 15));
        myConsole.setBounds(0, 0, 800, 500);
        myConsole.setBackground(Color.BLACK);
        myConsole.setForeground(Color.WHITE);
        myConsole.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 0x0A) {
                    try {
                        setPS();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });

        console.getContentPane().setLayout(new GridLayout());
        console.getContentPane().add(myConsole);
        console.setLocationRelativeTo(null);*/

        JButton consolBtn = new JButton("Консоль");
        consolBtn.setFocusable(false);
        add(consolBtn);


        consol = new JDialog(screen, "Консоль", false);
        consol.setSize(800, 500);
        consol.setLocation(200, 100);
        consol.setResizable(true);

        consolBtn.addActionListener(listener -> {
            consol.setVisible(true);
        });

//////////////////////////////
        //consol.setTitle("Собственная консоль через Swing");
        this.setSize(800, 500);

        myConsole.setFont(new Font("Consolas", Font.PLAIN, 15));
        myConsole.setBounds(0, 0, 800, 500);
        myConsole.setBackground(Color.WHITE);
        myConsole.setForeground(Color.BLACK);
        myConsole.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 0x0A) {
                    try {
                        setPS();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });

        consol.getContentPane().setLayout(new GridLayout());
        consol.getContentPane().add(myConsole);
        consol.setLocationRelativeTo(null);

    }

    private void setPS() throws Exception {
        char[] ss = myConsole.getText().toCharArray();

        myConsole.setText(myConsole.getText() + PS);
        myConsole.moveCaretPosition(myConsole.getText().length());

        int size = ss.length - 2;



        if(ss[size] == 'х' ) {
            //screen.begin();
            myConsole.setText(myConsole.getText() + "Кол-во рабочих :" + Worker.getCount() + "\n" + PS);
        } else if(ss[size] == 'й' ) {
           // screen.end();
            myConsole.setText(myConsole.getText() + "Кол-во трутней :" + Drone.getCount() + "\n" + PS);
        } else {
            myConsole.setText(myConsole.getText() + "Введена неверная команда\n" + PS);
        }


    }

    public void addFrame(Screen screen) {
        this.screen = screen;
    }


    private String getPercent(int value)
    {
        return "Время смерти Рабочих пчёл: " + (int)value;
    }
    private String getPercent1(int value)
    {
        return "Время смерти Трутней: " + (int)value;
    }
}