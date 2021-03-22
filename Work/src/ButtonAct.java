import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ButtonAct extends JPanel {
    private Screen screen;
    JDialog dialog;
    JDialog dialogFinal;
    public ButtonAct() {
        super();
        setLayout(new GridLayout(10, 1));
        setPreferredSize(new Dimension(200, 600));
        //////////////////////////
        JButton start = new JButton("Start");
        start.setFocusable(false);
        add(start);
        start.addActionListener(listener -> {
            if (!screen.running) {
                screen.begin();
            }
        });
        //////////////////////////////
        JButton remove = new JButton("Stop");
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
        first = new JRadioButton("Показать время", true);
        gr.add(first);
        add(first);
// переключатель установлен
        second = new JRadioButton("Скрыть время");
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
        JButton info = new JButton("Настройки");
        info.setFocusable(false);
        add(info);

        //////////////////////////////////////////////////////////////////////
        dialog = new JDialog(screen, "Настройки", true);
        dialog.setSize(300, 400);
        dialog.setLocation(200, 100);
        dialog.setResizable(false);


        dialog.setLayout(new GridLayout(8, 1));
        JLabel spawner = new JLabel("Время генерации Рабочих пчёл:"); // что-то вроде текста для вывода)))
        spawner.setFocusable(false);
        dialog.add(spawner, BorderLayout.CENTER);
        JComboBox<Integer> combobox = new JComboBox<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        combobox.setEditable(false);
        combobox.setSelectedIndex(4);
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                Integer item = (Integer) box.getSelectedItem();

                screen.time1 = item;

            }
        };
        combobox.addActionListener(actionListener);
        dialog.add(combobox);
        ////////////////////////////////////////////////////////////////
        JLabel rateWorker = new JLabel("Шанс генерации Рабочих пчёл:"); // что-то вроде текста для вывода)))
        rateWorker.setFocusable(false);
        dialog.add(rateWorker, BorderLayout.CENTER);
        JComboBox<Integer> combobox2 = new JComboBox<Integer>(
                new Integer[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100});
        combobox2.setEditable(false);
        combobox2.setSelectedIndex(4);
        ActionListener actionListener2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                Integer item = (Integer)box.getSelectedItem();

                screen.P = item;

            }
        };
        combobox2.addActionListener(actionListener2);
        dialog.add(combobox2);

        JLabel timeDrone = new JLabel("Время генерации Трутней:"); // что-то вроде текста для вывода)))
        timeDrone.setFocusable(false);
        dialog.add(timeDrone, BorderLayout.CENTER);
        JComboBox<Integer> combobox3 = new JComboBox<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        combobox3.setEditable(false);
        combobox3.setSelectedIndex(4);
        ActionListener actionListener3 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                Integer item = (Integer) box.getSelectedItem();

                screen.time2 = item;

            }
        };
        combobox3.addActionListener(actionListener3);
        dialog.add(combobox3);

        JLabel rateDrone = new JLabel("Процент генерации Трутней от общего кол-ва:"); // что-то вроде текста для вывода)))
        rateDrone.setFocusable(false);
        dialog.add(rateDrone, BorderLayout.CENTER);
        JComboBox<Integer> combobox4 = new JComboBox<Integer>(
                new Integer[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100});
        combobox4.setEditable(false);
        combobox4.setSelectedIndex(7);
        ActionListener actionListener4 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                Integer item = (Integer)box.getSelectedItem();

                screen.N = item;

            }
        };
        combobox4.addActionListener(actionListener4);
        dialog.add(combobox4);










        //JTextField textField = new JTextField(3);


        /*String t = "";
        //t = screen.time1.toString();
        //textField.setText();
        System.out.println(screen.time1);
        dialog.add(textField);
        textField.setVisible(true);*/

        info.addActionListener(listener -> {
            dialog.setVisible(true);
            spawner.setVisible(true);
        });
        //////////////////////////////////Симуляция///////////////////////////////////////////
        JButton sim = new JButton("Симуляция");
        sim.setFocusable(false);
        add(sim);
        dialogFinal = new JDialog(screen, "Настройки", true);
        dialogFinal.setSize(300,400);
        dialogFinal.setLocation(200, 100);
        dialogFinal.setResizable(false);

        dialogFinal.setLayout(new GridLayout(3,1));
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
            if(screen.running) {
                screen.end();
            }
        });


    }
    public void addFrame(Screen screen){
        this.screen = screen;
    }


}
