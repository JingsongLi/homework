package org.homework.teacher;

import org.homework.db.DBConnecter;
import org.homework.db.model.TableQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import static org.homework.utils.Utils.*;

/**
 * Created by lenovo on 2015/6/1.
 */
public class TTestPaper extends MouseAdapter {
    //                           科目           题型             章节                 题干
    public final static TreeMap<String,TreeMap<Integer,TreeMap<Integer, java.util.List<TableQuestion>>>> allTestQuestion = new TreeMap();

    JDialog jDialog=null; //创建一个空的对话框对象
    JButton createPaperBtn;
    JPanel centerPanel;
    boolean hehe = true;

    static String course = null;

    ArrayList<ArrayList<JTextField>> textFieldArray = new ArrayList<ArrayList<JTextField>>();

    public TTestPaper(JFrame jFrame) {

        java.util.List<TableQuestion> questions = DBConnecter.getAllQuestion();
        mapAllTestQuestion(allTestQuestion, questions);
        String[] courseArray = new String[allTestQuestion.size()];
        int i = 0;
        boolean flag = true;
        for (Map.Entry<String, TreeMap<Integer, TreeMap<Integer, List<TableQuestion>>>> entry : allTestQuestion.entrySet()) {
            courseArray[i++] = entry.getKey();
            if (flag) {
                for (Map.Entry<Integer, TreeMap<Integer, List<TableQuestion>>> entry2 : entry.getValue().entrySet()) {
                    ArrayList<JTextField> tmp = new ArrayList<JTextField>();
                    for (Map.Entry<Integer, List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {
                        tmp.add(new JTextField(20));
                    }
                    textFieldArray.add(tmp);
                }
                flag = false;
            }
        }

                /* 初始化jDialog1
        * 指定对话框的拥有者为jFrame,标题为"Dialog",当对话框为可视时,其他构件不能
        * 接受用户的输入(静态对话框) */
        jDialog = new JDialog(jFrame,"生成试卷",true);

        jDialog.setSize(new Dimension(650, 620));
        /* 设置对话框初始显示在屏幕当中的位置 */
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jDialog.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jDialog.getHeight()) / 2;
        jDialog.setLocation(w, h);

        jDialog.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        jDialog.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel courseLabel = new JLabel("科目");
        courseLabel.setBackground(Color.WHITE);
        JComboBox courseComboBox = new JComboBox(courseArray);
        courseComboBox.setBackground(Color.WHITE);

        topPanel.add(courseLabel);
        topPanel.add(courseComboBox);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        jDialog.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        jDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        createPaperBtn = new JButton("生成试卷");
        bottomPanel.add(createPaperBtn);

        createPaperBtn.addMouseListener(this);

        courseComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        TTestPaper.course = (String)e.getItem();
                        System.out.println("FUCK" + TTestPaper.course);
                        showCenterPanel();
                        break;
                    default:
                        break;
                }
            }
        });

        jDialog.setVisible(true);
    }

    private void showCenterPanel() {
//        for (int i = 0; i < 50; i++) {
//            JLabel lab = new JLabel("i");
//            centerPanel.add(lab);
//        }
        int i = 0;
        int j = 0;
        //科目
        for (Map.Entry<String, TreeMap<Integer, TreeMap<Integer, java.util.List<TableQuestion>>>> entry1 : allTestQuestion.entrySet()) {
            if (TTestPaper.course.equals(entry1.getKey())) {
                //题型
                for (Map.Entry<Integer,TreeMap<Integer,java.util.List<TableQuestion>>> entry2 : entry1.getValue().entrySet()) {

                    if (entry2.getKey() != 5) {
                        int type = entry2.getKey();

                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.setBackground(Color.WHITE);
                        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                        JScrollPane scrollPane = new JScrollPane(panel);
                        centerPanel.add(scrollPane);

                        JPanel panel_2 = new JPanel();
                        panel_2.setLayout(new GridLayout(entry2.getValue().size()+1, 1));
                        panel_2.setBackground(Color.WHITE);
                        panel.add(panel_2);

                        JLabel labelTypeExplain = buildLabel();
                        labelTypeExplain.setText(getTypeWord(type));
                        labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                        labelTypeExplain.setFont(new Font("宋体", Font.BOLD, 15));
                        panel_2.add(labelTypeExplain);

                        //章节
                        for (Map.Entry<Integer,java.util.List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {
                            JPanel panel_3 = new JPanel();
                            panel_3.setLayout(new FlowLayout(FlowLayout.LEFT));
                            panel_3.setBackground(Color.WHITE);
                            panel_2.add(panel_3);

                            String s1 = "第" + entry3.getKey() + "章(共" + entry3.getValue().size() + "个小题)";
                            JLabel label_1 = new JLabel(s1);
                            panel_3.add(label_1);
                            panel_3.add(textFieldArray.get(i).get(j++));
                            panel_3.add(new JLabel("个"));
                        }
                        j = 0;
                        i++;
                    }

                }
            }
        }
        if (hehe) {
            jDialog.resize(800, 750);
            hehe = false;
        }
        else {
            jDialog.resize(800, 740);
            hehe = true;
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == createPaperBtn) {

            System.out.println("createPaperBtn clicked");
            System.out.println(allTestQuestion);
        }
    }
}
