package Interface;

import Interface.ButtonView.RoundedBorder;
import Interface.Delete.BachelorsDelete;
import Interface.Delete.MastersDelete;
import Interface.Show.AllShow;
import Interface.Show.BachelorsShow;
import Interface.Show.MastersShow;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import ru.nsu.fit.chernyavtseva.assistant.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class WorkMenu {
    public JFrame frame0;
    public JFrame frame2;
    public JFrame frame3;
    public static JFrame frame4;
    public JFrame frame5;
    public JFrame frame6;
    public JFrame frame7;
    public JFrame frame8;
    public JFrame frame9;
    public JFrame frame10;
    public JFrame frame11;
    private ActionListener action;
    private JButton deleteButtonbach4, exitButton, createButton, menuButton, backButtonMenu, watchButton, backButton, thirdCourseButton, forthCourseButton,
            allDocsView, bachelors, masters, mastersDelete, bachelorsDelete, deleteButton, deleteButtonMag2,
            bachWatch, mastersWatch, backButtonDelete, createMasterButton, createBachelorButton;
    private JLabel label;

    private String title = "Интеллектуальный помощник секретаря кафедры";

    Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);


    public void createAndDisplayGUI()
    {
        int textSize = 23;
        frame2 = new JFrame(title);
        frame2.setSize(1200, 800);
        frame2.setLocationRelativeTo(null);

        frame0 = new JFrame(title);
        frame0.setSize(1200, 800);
        frame0.setLocationRelativeTo(null);

        frame3 = new JFrame(title);
        frame3.setSize(1200, 800);
        frame3.setLocationRelativeTo(null);

        frame4 = new JFrame(title);
        frame4.setSize(1200, 800);
        frame4.setLocationRelativeTo(null);

        frame5 = new JFrame(title);
        frame5.setSize(1200, 800);
        frame5.setLocationRelativeTo(null);

        frame6 = new JFrame(title);
        frame6.setSize(1200, 800);
        frame6.setLocationRelativeTo(null);

        frame7 = new JFrame(title);
        frame7.setSize(1200, 800);
        frame7.setLocationRelativeTo(null);

        frame8 = new JFrame(title);
        frame8.setSize(1200, 800);
        frame8.setLocationRelativeTo(null);

        frame9 = new JFrame(title);
        frame9.setSize(1200, 800);
        frame9.setLocationRelativeTo(null);

        frame10 = new JFrame(title);
        frame10.setSize(1200, 800);
        frame10.setLocationRelativeTo(null);

        frame11 = new JFrame(title);
        frame11.setSize(1200, 800);
        frame11.setLocationRelativeTo(null);

        JPanel contentPane3 = new JPanel();
        contentPane3.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane1 = new JPanel();
        contentPane1.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane4 = new JPanel();
        contentPane4.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane5 = new JPanel();
        contentPane5.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane6 = new JPanel();
        contentPane6.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane7 = new JPanel();
        contentPane7.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane8 = new JPanel();
        contentPane8.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane9 = new JPanel();
        contentPane9.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane10 = new JPanel();
        contentPane10.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane11 = new JPanel();
        contentPane11.setBackground(Color.decode("#FFE4C4"));

        JPanel contentPane12 = new JPanel();
        contentPane12.setBackground(Color.decode("#FFE4C4"));

        label = new JLabel("Выберите действие, которое хотите выполнить");
        label.setFont(new Font("Arial", Font.BOLD, 30));;
        label.setForeground(Color.decode("#9e6e52"));

        deleteButton = new JButton("Удалить документ");
        deleteButton.setForeground(Color.decode("#000000"));
        deleteButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        deleteButton.setBounds(450,280,325,70);
        deleteButton.setBorder(new RoundedBorder(30));
        deleteButton.setForeground(Color.BLUE);


        exitButton = new JButton("Выход");
        exitButton.setForeground(Color.decode("#000000"));
        exitButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        exitButton.setBounds(450,600,325,70);
        exitButton.setBorder(new RoundedBorder(30));
        exitButton.setForeground(Color.BLUE);



        createButton = new JButton("Заполнить документы");
        createButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        createButton.setBounds(450,190,325,70);
        createButton.setBorder(new RoundedBorder(30));
        createButton.setForeground(Color.BLUE);


        backButtonMenu = new JButton("Вернуться в главное меню");
        backButtonMenu.setForeground(Color.decode("#000000"));
        backButtonMenu.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        backButtonMenu.setBounds(450,190,325,70);
        backButtonMenu.setBorder(new RoundedBorder(30));
        backButtonMenu.setForeground(Color.BLUE);

        backButton = new JButton("Вернуться в главное меню");
        backButton.setForeground(Color.decode("#000000"));
        backButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        backButton.setBounds(450,190,325,70);
        backButton.setBorder(new RoundedBorder(30));
        backButton.setForeground(Color.BLUE);

        menuButton = new JButton("Перейти в главное меню");
        menuButton.setForeground(Color.decode("#000000"));
        menuButton.setBounds(450,310,325,110);
        menuButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));;
        menuButton.setBorder(new RoundedBorder(30));
        menuButton.setForeground(Color.BLUE);


        watchButton = new JButton("Посмотреть документы");
        watchButton.setForeground(Color.decode("#000000"));
        watchButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        watchButton.setBounds(450,370,325,70);
        watchButton.setBorder(new RoundedBorder(30));
        watchButton.setForeground(Color.BLUE);

        forthCourseButton = new JButton("2 курс");
        forthCourseButton.setForeground(Color.decode("#000000"));
        forthCourseButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        forthCourseButton.setBounds(450,290,325,70);
        forthCourseButton.setBorder(new RoundedBorder(30));
        forthCourseButton.setForeground(Color.BLUE);


        thirdCourseButton = new JButton("4 курс");
        thirdCourseButton.setForeground(Color.decode("#000000"));
        thirdCourseButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        thirdCourseButton.setBounds(450,290,325,70);
        thirdCourseButton.setBorder(new RoundedBorder(30));
        thirdCourseButton.setForeground(Color.BLUE);

        allDocsView = new JButton("Все документы за 2023 г.");
        allDocsView.setForeground(Color.decode("#000000"));
        allDocsView.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        allDocsView.setBounds(450,510,325,70);
        allDocsView.setBorder(new RoundedBorder(30));
        allDocsView.setForeground(Color.BLUE);


        bachelors = new JButton("Бакалавриат");
        bachelors.setForeground(Color.decode("#000000"));
        bachelors.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        bachelors.setBounds(450,290,325,70);
        bachelors.setBorder(new RoundedBorder(30));
        bachelors.setForeground(Color.BLUE);


        masters = new JButton("Магистратура");
        masters.setForeground(Color.decode("#000000"));
        masters.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        masters.setBounds(450,400,325,70);
        masters.setBorder(new RoundedBorder(30));
        masters.setForeground(Color.BLUE);


        bachelorsDelete = new JButton("Бакалавриат");
        bachelorsDelete.setForeground(Color.decode("#000000"));
        bachelorsDelete.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        bachelorsDelete.setBounds(450,290,325,70);
        bachelorsDelete.setBorder(new RoundedBorder(30));
        bachelorsDelete.setForeground(Color.BLUE);


        mastersDelete = new JButton("Магистратура");
        mastersDelete.setForeground(Color.decode("#000000"));
        mastersDelete.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        mastersDelete.setBounds(450,400,325,70);
        mastersDelete.setBorder(new RoundedBorder(30));
        mastersDelete.setForeground(Color.BLUE);


        deleteButtonbach4 = new JButton("Бакалавриат");
        deleteButtonbach4.setForeground(Color.decode("#000000"));
        deleteButtonbach4.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        deleteButtonbach4.setBounds(450,290,325,70);
        deleteButtonbach4.setBorder(new RoundedBorder(30));
        deleteButtonbach4.setForeground(Color.BLUE);


        deleteButtonMag2 = new JButton("Магистратура");
        deleteButtonMag2.setForeground(Color.decode("#000000"));
        deleteButtonMag2.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        deleteButtonMag2.setBounds(450,400,325,70);
        deleteButtonMag2.setBorder(new RoundedBorder(30));
        deleteButtonMag2.setForeground(Color.BLUE);


        bachWatch = new JButton("Бакалавриат");
        bachWatch.setForeground(Color.decode("#000000"));
        bachWatch.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        bachWatch.setBounds(450,290,325,70);
        bachWatch.setBorder(new RoundedBorder(30));
        bachWatch.setForeground(Color.BLUE);


        mastersWatch = new JButton("Магистратура");
        mastersWatch.setForeground(Color.decode("#000000"));
        mastersWatch.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        mastersWatch.setBounds(450,400,325,70);
        mastersWatch.setBorder(new RoundedBorder(30));
        mastersWatch.setForeground(Color.BLUE);


        backButtonDelete = new JButton("Вернуться в главное меню");
        backButtonDelete.setForeground(Color.decode("#000000"));
        backButtonDelete.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        backButtonDelete.setBounds(450,190,325,70);
        backButtonDelete.setBorder(new RoundedBorder(30));
        backButtonDelete.setForeground(Color.BLUE);

        action  = new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {

                JButton button = (JButton) ae.getSource();
                if (button == deleteButtonbach4)
                {
                    BachelorsDelete.delete();

                }

                if (button == deleteButtonMag2)
                {
                    MastersDelete.delete();
                }


                else if (button == deleteButton)
                {
                    frame8.setVisible(true);
                }


//                else if (button == createBachelorButton){
//                    try {
//                        Main.main();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.out.println("Documents generated!");
//                    frame3.setVisible(true);
//                }


                else if  (button == createButton)
                {
                    try {
                        Main.main(null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Documents generated!");


                    frame3.setVisible(true);
                }
                else if (button == menuButton)
                {
                    frame2.setVisible(true);
                }

                else if (button == bachelors)
                {
                    frame6.setVisible(true);
                }
                else if (button == masters)
                {
                    frame7.setVisible(true);
                }
                else if (button == watchButton)
                {
                    frame4.setVisible(true);
                }

                else if (button == backButtonDelete)
                {
                    frame2.setVisible(true);
                }
                else if (button == backButtonMenu)
                {
                    frame2.setVisible(true);
                }

                else if (button == allDocsView)
                {
                    AllShow.show();
                }
                else if (button == thirdCourseButton)
                {
                    BachelorsShow.show();
                }
                else if (button == forthCourseButton)
                {
                    MastersShow.show();
                }
                else if (button == backButton)
                {
                    frame2.setVisible(true);
                }
            }
        };

        deleteButtonbach4.addActionListener(action);
        exitButton.addActionListener((event) -> System.exit(0));
        createButton.addActionListener(action);
        menuButton.addActionListener(action);
        backButtonMenu.addActionListener(action);
        backButton.addActionListener(action);
        watchButton.addActionListener(action);
        thirdCourseButton.addActionListener(action);
        forthCourseButton.addActionListener(action);
        allDocsView.addActionListener(action);
        bachelors.addActionListener(action);
        masters.addActionListener(action);
        bachelorsDelete.addActionListener(action);
        mastersDelete.addActionListener(action);
        deleteButton.addActionListener(action);
        deleteButtonMag2.addActionListener(action);
        bachWatch.addActionListener(action);
        mastersWatch.addActionListener(action);
        backButtonDelete.addActionListener(action);
//        createBachelorButton.addActionListener(action);
//        createMasterButton.addActionListener(action);

        frame2.add(deleteButton);
        frame2.add(exitButton);
        frame2.add(createButton);
        frame2.add(watchButton);
        frame2.add(label);
        label.setBounds(270,100,750,40);
        frame2.getContentPane().add(contentPane1);
        frame2.setVisible(false);


        frame0.add(menuButton);
        frame0.getContentPane().add(contentPane3);
        frame0.setLocationRelativeTo(null);
        frame0.setVisible(true);


        frame3.add(backButtonMenu);
        frame3.getContentPane().add(contentPane4);
        frame3.setVisible(false);


        frame4.add(backButton);
        frame4.add(bachelors);
        frame4.add(masters);
        frame4.add(allDocsView);
        frame4.getContentPane().add(contentPane5);
        frame4.setVisible(false);

        frame6.add(thirdCourseButton);
        frame7.add(forthCourseButton);

        frame6.getContentPane().add(contentPane6);
        frame7.getContentPane().add(contentPane7);

        frame8.add(backButtonDelete);
        frame8.add(deleteButtonbach4);
        frame8.add(deleteButtonMag2);
        frame8.getContentPane().add(contentPane9);

        frame11.add(createBachelorButton);
        frame11.add(createMasterButton);
        frame11.getContentPane().add(contentPane12);


    }
    public static void main(String... args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new WorkMenu().createAndDisplayGUI();
            }
        });
    }
}