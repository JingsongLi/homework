package org.homework.teacher;

import org.homework.db.DBConnecter;
import org.homework.db.model.AllStudentScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by lenovo on 2015/5/29.
 */
public class TScoreQuery extends MouseAdapter {


    JDialog jDialog=null; //创建一个空的对话框对象
    JComboBox queryAsComboBox;
    JButton queryButton;
    JTextField courseTextField;
    JTextField queryAsTextField;
    static String queryAs = "班级";

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
        courseTextField = new JTextField(20);
        courseTextField.setBackground(Color.WHITE);

        final String[] queryAs = {"班级", "学号"};
        queryAsComboBox = new JComboBox(queryAs);
        queryAsComboBox.setBackground(Color.WHITE);
        queryAsTextField = new JTextField(30);
        queryAsTextField.setBackground(Color.WHITE);

        queryButton = new JButton("查询");
        queryButton.setBackground(Color.WHITE);

        topPanel.add(courseLabel);
        topPanel.add(courseTextField);
        topPanel.add(queryAsComboBox);
        topPanel.add(queryAsTextField);
        topPanel.add(queryButton);

        queryAsComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        TScoreQuery.queryAs = (String)e.getItem();
                        break;
                    default:
                        break;
                }

            }
        });
        queryButton.addMouseListener(this);


        jDialog.setVisible(true);
    }

    private void showQueryResult() {
        System.out.println("FUCK  query out");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       if (e.getSource() == queryButton) {

           String course = courseTextField.getText();
           String queryAsText = queryAsTextField.getText();
           java.util.List<AllStudentScore> allStuScoreList = DBConnecter.getAllStudentScores(course, queryAs, queryAsText);
           System.out.println(allStuScoreList);


        }
    }
}
