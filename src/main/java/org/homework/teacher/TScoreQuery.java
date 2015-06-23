package org.homework.teacher;

import lombok.Data;
import org.homework.db.DBConnecter;
import org.homework.db.model.AllStudentScore;
import org.homework.io.IoOperator;
import org.homework.io.PDFOperator;
import org.homework.utils.MyScrollPane;
import org.homework.utils.Utils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
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
    JDialog jDialog=null; //创建一个空的对话框对象
//    JComboBox queryAsComboBox;
    JButton queryButton;
    JPanel tablePanel = null;
    JTable table = null;
    static String queryAs = "班级";
    static String courseAs = "";
    static String classAs = "";

    public TScoreQuery(JFrame jFrame) {
                /* 初始化jDialog1
        * 指定对话框的拥有者为jFrame,标题为"Dialog",当对话框为可视时,其他构件不能
        * 接受用户的输入(静态对话框) */
        jDialog = new JDialog(jFrame,"成绩查询",true);

        jDialog.setSize(new Dimension(650, 620));
        /* 设置对话框初始显示在屏幕当中的位置 */
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jDialog.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jDialog.getHeight()) / 2;
        jDialog.setLocation(w, h);

        jDialog.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        jDialog.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel courseLabel = new JLabel("科目");
        courseLabel.setBackground(Color.WHITE);

        queryButton = new JButton("查询");
        queryButton.setBackground(Color.WHITE);
        queryButton.addMouseListener(this);

        outputButton = new JButton("导出");
        outputButton.setBackground(Color.WHITE);
        outputButton.addMouseListener(this);

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

           Map<String, List<AllStudentScore>> allStuScoreMap = new TreeMap();

           for (AllStudentScore allStudentScore : allStuScoreList) {
               String stuNumber = allStudentScore.getStudentNumber();
               String stuName = allStudentScore.getStudentName();
               String stuNumName = stuNumber + "_" + stuName;
               List<AllStudentScore> scoreList = allStuScoreMap.get(stuNumName);
               if (scoreList == null) {
                   scoreList = new ArrayList<AllStudentScore>();
                   allStuScoreMap.put(stuNumName, scoreList);
               }
               scoreList.add(allStudentScore);
           }

           int row = allStuScoreMap.size() + 1;

           Set<TableChapterNode> tableChapterNodes = new HashSet<TableChapterNode>();
           for (Map.Entry<String, List<AllStudentScore>> entry : allStuScoreMap.entrySet()) {
               for (AllStudentScore ass : entry.getValue()){
                   tableChapterNodes.add(new TableChapterNode(ass.getChapter(),ass.getCourse()));
               }
           }
           int column = tableChapterNodes.size();
           column += 4;
           if (tablePanel != null && table != null) {
               tablePanel.remove(table);
               jDialog.getContentPane().remove(tablePanel);
               table = null;
               tablePanel = null;
               System.gc();
           }
           createTable(row, column, allStuScoreMap,tableChapterNodes);
           //System.out.println(allStuScoreMap);


        }else if(e.getSource() == outputButton){
           IoOperator.exportTable(table);
       }
    }

    private void createTable(int row, int column, Map<String, List<AllStudentScore>> allStuScoreMap,
                             Set<TableChapterNode> tableChapterNodes) {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        jDialog.getContentPane().add(tablePanel, BorderLayout.CENTER);

        Vector columnNames = new Vector(column);
        for (int i = 0; i < column; i++) {
            columnNames.add("");
        }
        table = new JTable(new DefaultTableModel(columnNames,row));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //设置居中
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, defaultTableCellRenderer);

        //设置列宽
        for(int i = 0; i < column; i++) {
            table.getColumnModel().getColumn(i).setWidth(30);
        }

        JScrollPane scrollPane = new MyScrollPane();
        scrollPane.setViewportView(table);
        table.setEnabled(false);
        table.setValueAt("学号", 0, 0);
        table.setValueAt("姓名", 0, 1);
        table.setValueAt("平均分", 0, column-2);
        table.setValueAt("百分值", 0, column-1);
        List<TableChapterNode> tableChapterNodeList = new ArrayList<TableChapterNode>();
        tableChapterNodeList.addAll(tableChapterNodes);
        for (int i = 1; i <= column - 4; i++) {
            TableChapterNode node = tableChapterNodeList.get(i-1);
            table.setValueAt(node, 0, i+1);
        }
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setRowHeight(20);
        tablePanel.add(scrollPane);

        int rowIndex = 0;
        int columnIndex = 0;
        float scoreSum = 0;
        float maxScore = 0;
        Map<String, Float> scoreMap = new TreeMap();
        for (Map.Entry<String, List<AllStudentScore>> entry : allStuScoreMap.entrySet()) {

            rowIndex++;
            columnIndex = 0;
            scoreSum = 0;

            String[] stuNumName = entry.getKey().split("_");
            table.setValueAt(stuNumName[0], rowIndex, columnIndex++);
            table.setValueAt(stuNumName[1], rowIndex, columnIndex++);
            for (AllStudentScore ass : entry.getValue()) {
                float score = ass.getScore();
                //先找到能填充的row和column
                Integer tmpJ=null;
                for (int i = 1; i <= column - 4; i++) {
                    TableChapterNode tmpAss = (TableChapterNode) table.getValueAt(0, i + 1);
                    if(tmpAss.course.equals(ass.getCourse()) &&
                            tmpAss.chapter == ass.getChapter()){
                        tmpJ = i+1;
                        break;
                    }
                }
                if(tmpJ != null){
                    if(score == -1f) {
                        table.setValueAt("作弊", rowIndex, tmpJ);
                        scoreSum += 0;
                    } else {
                        table.setValueAt(ass.getScore(), rowIndex, tmpJ);
                        scoreSum += score;
                    }
                }
                columnIndex++;
            }
            float score = scoreSum / tableChapterNodes.size();
            table.setValueAt(score, rowIndex, column - 2);
            scoreMap.put(entry.getKey(),score);
            if(score > maxScore)
                maxScore = score;
        }

        rowIndex = 0;
        for (Map.Entry<String, Float> entry : scoreMap.entrySet()) {
            rowIndex++;
            float num = (100*entry.getValue())/maxScore;
            num = (float)((int)(num * 10f) / 10d);
            table.setValueAt(num, rowIndex, column - 1);
        }

        jDialog.setVisible(true);
    }

    @Data
    public static class TableChapterNode {
        int chapter;
        String course;

        public TableChapterNode(int chapter,String course){
            this.chapter = chapter;
            this.course = course;
        }

        @Override
        public String toString(){
            return Utils.getChapterName(course,chapter,null);
        }
    }
}
