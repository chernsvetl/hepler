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
import ru.nsu.fit.chernyavtseva.assistant.document.type.core.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static ru.nsu.fit.chernyavtseva.assistant.Main.generateTemplates;

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
    public JFrame frame12;
    private ActionListener action;
    private JButton deleteButtonbach4, exitButton, createButton, menuButton, backButtonMenu, watchButton, backButton, thirdCourseButton, forthCourseButton,
            allDocsView, bachelors, masters, mastersDelete, bachelorsDelete, deleteButton, deleteButtonMag2,
            bachWatch, mastersWatch, backButtonDelete, createMasterButton, createBachelorButton, createMasterButtonTRPS, backBtnFromGen,
    izBtn, practiceFeedBackBtn, practiceReportBtn, reviewerFeedbackBtn, supervisorFeedbackBtn, backButtonMenuBtn, allDocsCreateBtn;
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

        frame12 = new JFrame(title);
        frame12.setSize(1200, 800);
        frame12.setLocationRelativeTo(null);

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

        JPanel contentPane13 = new JPanel();
        contentPane13.setBackground(Color.decode("#FFE4C4"));

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
        createButton.setForeground(Color.decode("#000000"));
        createButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        createButton.setBounds(450,190,325,70);
        createButton.setBorder(new RoundedBorder(30));
        createButton.setForeground(Color.BLUE);



        createMasterButton = new JButton("Магистратура 2 курс КМиАД 4 семестр");
        createMasterButton.setForeground(Color.decode("#000000"));
        createMasterButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        createMasterButton.setBounds(450,190,325,70);
        createMasterButton.setBorder(new RoundedBorder(30));
        createMasterButton.setForeground(Color.BLUE);


        createMasterButtonTRPS = new JButton("Магистратура 2 курс ТРПС 4 семестр");
        createMasterButtonTRPS.setForeground(Color.decode("#000000"));
        createMasterButtonTRPS.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        createMasterButtonTRPS.setBounds(450,280,325,70);
        createMasterButtonTRPS.setBorder(new RoundedBorder(30));
        createMasterButtonTRPS.setForeground(Color.BLUE);

        createBachelorButton = new JButton("Бакалавриат 4 курс ПИиКН 8 семестр");
        createBachelorButton.setForeground(Color.decode("#000000"));
        createBachelorButton.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        createBachelorButton.setBounds(450,370,325,70);
        createBachelorButton.setBorder(new RoundedBorder(30));
        createBachelorButton.setForeground(Color.BLUE);


        allDocsCreateBtn = new JButton("Все документы");
        allDocsCreateBtn.setForeground(Color.decode("#000000"));
        allDocsCreateBtn.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        allDocsCreateBtn.setBounds(450,100,325,70);
        allDocsCreateBtn.setBorder(new RoundedBorder(30));
        allDocsCreateBtn.setForeground(Color.BLUE);

        izBtn = new JButton("Индивидуальное задание");
        izBtn.setForeground(Color.decode("#000000"));
        izBtn.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        izBtn.setBounds(450,190,325,70);
        izBtn.setBorder(new RoundedBorder(30));
        izBtn.setForeground(Color.BLUE);

        practiceReportBtn = new JButton("Отчет о практике");
        practiceReportBtn.setForeground(Color.decode("#000000"));
        practiceReportBtn.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        practiceReportBtn.setBounds(450,280,325,70);
        practiceReportBtn.setBorder(new RoundedBorder(30));
        practiceReportBtn.setForeground(Color.BLUE);

        practiceFeedBackBtn = new JButton("Отзыв");
        practiceFeedBackBtn.setForeground(Color.decode("#000000"));
        practiceFeedBackBtn.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        practiceFeedBackBtn.setBounds(450,370,325,70);
        practiceFeedBackBtn.setBorder(new RoundedBorder(30));
        practiceFeedBackBtn.setForeground(Color.BLUE);

        reviewerFeedbackBtn = new JButton("Отзыв руководителя ВКР");
        reviewerFeedbackBtn.setForeground(Color.decode("#000000"));
        reviewerFeedbackBtn.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        reviewerFeedbackBtn.setBounds(450,460,325,70);
        reviewerFeedbackBtn.setBorder(new RoundedBorder(30));
        reviewerFeedbackBtn.setForeground(Color.BLUE);

        supervisorFeedbackBtn = new JButton("Рецензия");
        supervisorFeedbackBtn.setForeground(Color.decode("#000000"));
        supervisorFeedbackBtn.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        supervisorFeedbackBtn.setBounds(450,550,325,70);
        supervisorFeedbackBtn.setBorder(new RoundedBorder(30));
        supervisorFeedbackBtn.setForeground(Color.BLUE);

        backButtonMenuBtn = new JButton("Вернуться в главное меню");
        backButtonMenuBtn.setForeground(Color.decode("#000000"));
        backButtonMenuBtn.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        backButtonMenuBtn.setBounds(450,640,325,70);
        backButtonMenuBtn.setBorder(new RoundedBorder(30));
        backButtonMenuBtn.setForeground(Color.BLUE);


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

        backBtnFromGen = new JButton("Вернуться в главное меню");
        backBtnFromGen.setForeground(Color.decode("#000000"));
        backBtnFromGen.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        backBtnFromGen.setBounds(450,640,325,70);
        backBtnFromGen.setBorder(new RoundedBorder(30));
        backBtnFromGen.setForeground(Color.BLUE);

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


                else if  (button == createButton){
                    frame12.setVisible(true);
                }

                else if  (button == allDocsCreateBtn)
                {
                    try {
                        generateTemplates(template -> template instanceof IndividualTaskMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof IndividualTask);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof IndividualTaskTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeFeedbackTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeFeedbackMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeFeedback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                    generateTemplates(template -> template instanceof PracticeReport);
                    } catch (IOException e) {
                    throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeReportTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeReportMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof ReviewerFeedbackMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof ReviewerFeedback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof ReviewerFeedbackTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof SupervisorFeedbackMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof SupervisorFeedback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof SupervisorFeedbackTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("All documents generated!");
                    frame3.setVisible(true);
                }

                else if  (button == izBtn)
                {
                    try {
                        generateTemplates(template -> template instanceof IndividualTaskMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof IndividualTask);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof IndividualTaskTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Documents IZ generated!");
                    frame3.setVisible(true);
                }

                else if  (button == practiceFeedBackBtn)
                {
                    try {
                        generateTemplates(template -> template instanceof PracticeFeedbackTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeFeedbackMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeFeedback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Documents header feedback generated!");
                    frame3.setVisible(true);
                }

                else if  (button == practiceReportBtn)
                {
                    try {
                        generateTemplates(template -> template instanceof PracticeReport);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeReportTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof PracticeReportMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Documents report generated!");
                    frame3.setVisible(true);
                }

                else if  (button == reviewerFeedbackBtn)
                {
                    try {
                        generateTemplates(template -> template instanceof ReviewerFeedbackMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof ReviewerFeedback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof ReviewerFeedbackTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Documents VKR recenziya generated!");
                    frame3.setVisible(true);
                }

                else if  (button == supervisorFeedbackBtn)
                {
                    try {
                        generateTemplates(template -> template instanceof SupervisorFeedbackMDA);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof SupervisorFeedback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        generateTemplates(template -> template instanceof SupervisorFeedbackTRPS);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Documents VKR otzyv generated!");
                    frame3.setVisible(true);
                }

                else if (button == backButtonMenuBtn)
                {
                    frame2.setVisible(true);
                }

                else if (button == backBtnFromGen)
                {
                    frame2.setVisible(true);
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
        createBachelorButton.addActionListener(action);
        createMasterButton.addActionListener(action);
        createMasterButtonTRPS.addActionListener(action);
        backBtnFromGen.addActionListener(action);
        allDocsCreateBtn.addActionListener(action);

        izBtn.addActionListener(action);
        reviewerFeedbackBtn.addActionListener(action);
        practiceReportBtn.addActionListener(action);
        supervisorFeedbackBtn.addActionListener(action);
        practiceFeedBackBtn.addActionListener(action);
        backButtonMenuBtn.addActionListener(action);

        frame2.add(deleteButton);
        frame2.add(exitButton);
        frame2.add(createButton);
        frame2.add(watchButton);
        // frame2.add(createMasterButton);
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


        frame11.add(createMasterButton);
        frame11.add(createBachelorButton);
        frame11.add(createMasterButtonTRPS);
        frame11.add(backBtnFromGen);
        frame11.getContentPane().add(contentPane12);

        frame12.add(izBtn);
        frame12.add(reviewerFeedbackBtn);
        frame12.add(supervisorFeedbackBtn);
        frame12.add(practiceFeedBackBtn);
        frame12.add(practiceReportBtn);
        frame12.add(backButtonMenuBtn);
        frame12.add(allDocsCreateBtn);
        frame12.getContentPane().add(contentPane13);

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