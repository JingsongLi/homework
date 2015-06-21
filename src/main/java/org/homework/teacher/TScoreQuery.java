package org.homework.teacher;

import org.homework.db.DBConnecter;
import org.homework.db.model.AllStudentScore;
import org.homework.io.IoOperator;
import org.homework.io.PDFOperator;
import org.homework.utils.MyScrollPane;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2015/5/29.
 */
public class TScoreQuery extends MouseAdapter {


    private JComboBox classBox;
    private JComboBox courseBox;
    private JButton outputButton;
    JDialog jDialog=null; //����һ���յĶԻ������
//    JComboBox queryAsComboBox;
    JButton queryButton;
    JPanel tablePanel = null;
    JTable table = null;
    static String queryAs = "�༶";
    static String courseAs = "";
    static String classAs = "";

    public TScoreQuery(JFrame jFrame) {
                /* ��ʼ��jDialog1
        * ָ���Ի����ӵ����ΪjFrame,����Ϊ"Dialog",���Ի���Ϊ����ʱ,������������
        * �����û�������(��̬�Ի���) */
        jDialog = new JDialog(jFrame,"�ɼ���ѯ",true);

        jDialog.setSize(new Dimension(650, 620));
        /* ���öԻ����ʼ��ʾ����Ļ���е�λ�� */
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jDialog.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jDialog.getHeight()) / 2;
        jDialog.setLocation(w, h);

        jDialog.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        jDialog.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel courseLabel = new JLabel("��Ŀ");
        courseLabel.setBackground(Color.WHITE);

//        final String[] queryAs = {"�༶", "ѧ��"};
//        queryAsComboBox = new JComboBox(queryAs);
//        queryAsComboBox.setBackground(Color.WHITE);

        queryButton = new JButton("��ѯ");
        queryButton.setBackground(Color.WHITE);
        queryButton.addMouseListener(this);

        outputButton = new JButton("����");
        outputButton.setBackground(Color.WHITE);
        outputButton.addMouseListener(this);

//        queryAsComboBox.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                switch (e.getStateChange()) {
//                    case ItemEvent.SELECTED:
//                        TScoreQuery.queryAs = (String)e.getItem();
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//        });

        //
        List<String> distinctCourse = DBConnecter.getDistinctOwnDB(DBConnecter.ALL_STUDENT_SCORE_TABLE,AllStudentScore.COURSE);
        List<String> distinctClass = DBConnecter.getDistinctOwnDB(DBConnecter.ALL_STUDENT_SCORE_TABLE,AllStudentScore.STUDENT_CLASS);
        if(distinctCourse.size() != 0)
            TScoreQuery.courseAs = distinctCourse.get(0);
        if(distinctClass.size() != 0)
            TScoreQuery.classAs = distinctClass.get(0);

        courseBox = new JComboBox(distinctCourse.toArray(new String[0]));
        courseBox.setBackground(Color.WHITE);
        courseBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        TScoreQuery.courseAs = (String)e.getItem();
                        break;
                    default:
                        break;
                }
            }
        });

        classBox = new JComboBox(distinctClass.toArray(new String[0]));
        classBox.setBackground(Color.WHITE);
        classBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        TScoreQuery.classAs = (String)e.getItem();
                        break;
                    default:
                        break;
                }
            }
        });

        topPanel.add(courseLabel);
        topPanel.add(courseBox);
//        topPanel.add(queryAsComboBox);
        topPanel.add(classBox);
        topPanel.add(queryButton);
        topPanel.add(outputButton);

        jDialog.setVisible(true);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
       if (e.getSource() == queryButton) {

           String course = courseAs;
           String queryAsText = classAs;
           java.util.List<AllStudentScore> allStuScoreList = DBConnecter.getAllStudentScores(course, queryAs, queryAsText);
           //System.out.println("Befor sort: " + allStuScoreList);
           Collections.sort(allStuScoreList, new Comparator<AllStudentScore>() {
               public int compare(AllStudentScore arg0, AllStudentScore arg1) {
                   if (arg0.getStudentNumber().compareTo(arg1.getStudentNumber()) == 0) {
                       return Integer.valueOf(arg0.getChapter()).compareTo(Integer.valueOf(arg1.getChapter()));
                   } else {
                       return arg0.getStudentNumber().compareTo(arg1.getStudentNumber());
                   }
               }

           });
           //System.out.println("After sort: " + allStuScoreList);

           //System.out.println(allStuScoreList);

           Map<String, java.util.List<Float>> allStuScoreMap = new TreeMap<String, java.util.List<Float>>();

           for (AllStudentScore allStudentScore : allStuScoreList) {
               String stuNumber = allStudentScore.getStudentNumber();
               String stuName = allStudentScore.getStudentName();
               String stuNumName = stuNumber + "_" + stuName;
               java.util.List<Float> scoreList = allStuScoreMap.get(stuNumName);
               if (scoreList == null) {
                   scoreList = new ArrayList<Float>();
                   allStuScoreMap.put(stuNumName, scoreList);
               }
               scoreList.add(allStudentScore.getScore());
           }

           int row = allStuScoreMap.size() + 1;
           int column = 0;
           for (Map.Entry<String, List<Float>> entry : allStuScoreMap.entrySet()) {
               int elemSize = entry.getValue().size();
               if (elemSize > column) {
                   column = elemSize;
               }
           }
           column += 3;
           if (tablePanel != null && table != null) {
               tablePanel.remove(table);
               jDialog.getContentPane().remove(tablePanel);
               table = null;
               tablePanel = null;
               System.gc();
           }
           createTable(row, column, allStuScoreMap);
           //System.out.println(allStuScoreMap);


        }else if(e.getSource() == outputButton){
           IoOperator.exportTable(table);
       }
    }

    private void createTable(int row, int column, Map<String, java.util.List<Float>> allStuScoreMap) {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        jDialog.getContentPane().add(tablePanel, BorderLayout.CENTER);

        table = new JTable(new DefaultTableModel(row, column));
        JScrollPane scrollPane = new MyScrollPane();
        scrollPane.setViewportView(table);
        table.setEnabled(false);
        table.setValueAt("ѧ��", 0, 0);
        table.setValueAt("����", 0, 1);
        table.setValueAt("ƽ����", 0, column-1);
        for (int i = 1; i <= column - 3; i++) {
            String s = "��" + i + "��";
            table.setValueAt(s, 0, i+1);
        }
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setRowHeight(20);
        tablePanel.add(scrollPane);

        int rowIndex = 0;
        int columnIndex = 0;
        float scoreSum = 0;
        int index = 0;
        for (Map.Entry<String, List<Float>> entry : allStuScoreMap.entrySet()) {

            rowIndex++;
            columnIndex = 0;
            scoreSum = 0;
            index = 0;

            String[] stuNumName = entry.getKey().split("_");
            table.setValueAt(stuNumName[0], rowIndex, columnIndex++);
            table.setValueAt(stuNumName[1], rowIndex, columnIndex++);
            for (Float score : entry.getValue()) {
                if(score == -1f) {
                    table.setValueAt("����", rowIndex, columnIndex++);
                    scoreSum += 0;
                } else{
                    table.setValueAt(score, rowIndex, columnIndex++);
                    scoreSum += score;
                }
                index++;
            }
            table.setValueAt(scoreSum / index, rowIndex, column - 1);

        }

        jDialog.setVisible(true);
    }
}
