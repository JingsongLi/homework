package org.homework.main;

import org.homework.db.DBConnecter;
import org.homework.db.model.TableQuestion;
import org.homework.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import static org.homework.utils.Utils.*;

public class ContentPanel extends JPanel {

    JLabel labelTitle;
    JScrollPane scrollPane;

    static ContentPanel contentPanel = new ContentPanel();
    public static ContentPanel getContentPanel(){
        return contentPanel;
    }

    public JScrollPane getScrollPane(){

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(this);

        labelTitle = new JLabel("无标题");
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitle.setBackground(Color.WHITE);
        scrollPane.setColumnHeaderView(labelTitle);
        labelTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        labelTitle.setFont(new Font("黑体", Font.BOLD, 20));
        scrollPane.getColumnHeader().setBackground(Color.white);
        return scrollPane;
    }

    public ContentPanel(){
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void fullContent(int type, List<TableQuestion> list){
        removeAll();
        labelTitle.setText(getTypeWord(type));

        JLabel labelTypeExplain = buildLabel();
        labelTypeExplain.setText(getTypeExplain(type));
        labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        labelTypeExplain.setFont(new Font("宋体", Font.BOLD, 15));
        add(labelTypeExplain);

        for(int i=0; i < list.size(); i++){
            contentPanel.addPanel(i+1,list.get(i));
        }

        //有空白Panel才能删除最后一个控件（答案）
        JPanel plaitPanel = new JPanel();
        plaitPanel.setBackground(Color.white);
        add(plaitPanel);
        validate();
        repaint();
    }

    public void addPanel(int index,final TableQuestion t){
        //1.题干
        JLabel lblNewLabel = buildLabel();
        lblNewLabel.setText(index + ". " + t.getMain_content());
        lblNewLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        add(lblNewLabel);

        //2.选项或答题空白
        if(t.getType()==1 || t.getType()==3){//单选或判断
            String[] eles;
            ButtonGroup group = new ButtonGroup();// 创建单选按钮组
            if(t.getType() == 1){
                eles = t.getEle_content().split(SPLIT);
            }else
                eles = Utils.JUDGE_OPTION;
            for (int i=0; i<eles.length; i++){
                if(!eles[i].equals("")){
                    final String value;
                    JRadioButton radioButton;
                    if(t.getType() == 1){
                        value = (char)(i+65) + "";
                        radioButton = new JRadioButton(value + "、  " + eles[i]);// 创建单选按钮
                    }else{
                        value = eles[i];
                        radioButton = new JRadioButton("  " + eles[i]);// 创建单选按钮
                    }
                    radioButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
                    radioButton.setBackground(Color.WHITE);
                    radioButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            String answer = value;
                            DBConnecter.updateQuestion(t.getId(),TableQuestion.MY_ANSWER,answer);
                            t.setMyAnswer(answer);
                        }
                    });
                    add(radioButton);// 应用单选按钮
                    group.add(radioButton);
                }
            }

        }else if(t.getType() == 2){//多项选择题
            String[] eles = t.getEle_content().split(SPLIT);
            final List<JCheckBox> boxList = new ArrayList<JCheckBox>();
            for (int i=0; i<eles.length; i++){
                if(!eles[i].equals("")){
                    final String value;
                    final JCheckBox jCheckBox;
                    value = (char)(i+65) + "";
                    jCheckBox = new JCheckBox(value + "、  " + eles[i]);// 创建复选按钮
                    boxList.add(jCheckBox);
                    jCheckBox.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
                    jCheckBox.setBackground(Color.WHITE);
                    jCheckBox.addMouseListener(new MouseAdapter() {
                        @Override
                            public void mouseClicked(MouseEvent e) {
                                String answer;
                                TreeSet<String> set = new TreeSet();
                                for (JCheckBox box : boxList) {
                                    if (box.isSelected())
                                        set.add(box.getText().charAt(0) + "");
                            }
                            if (set.size() > 0) {
                                Iterator<String> iter = set.iterator();
                                answer = iter.next();
                                while (iter.hasNext())
                                    answer += SPLIT + iter.next();
                            } else
                                answer = "";
                            DBConnecter.updateQuestion(t.getId(), TableQuestion.MY_ANSWER, answer);
                            t.setMyAnswer(answer);
                        }
                    });
                    add(jCheckBox);// 应用单选按钮
                }
            }
        }else if(t.getType()==4 || t.getType() == 5){//填空或简答
            final JPanel textPanel = new JPanel();
            textPanel.setBackground(Color.WHITE);
            textPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            textPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            add(textPanel);

            final JTextArea text = new JTextArea(5, 90);
            text.setLineWrap(true);
            JScrollPane scroll = new JScrollPane(text);
            textPanel.add(scroll);
            text.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String answer = text.getText();
                    DBConnecter.updateQuestion(t.getId(), TableQuestion.MY_ANSWER, answer);
                    t.setMyAnswer(answer);
                }
            });
        }

        //3.按钮
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        add(buttonPanel);

        final JButton buttonAnswer = new JButton("显示答案");
        buttonAnswer.addMouseListener(new MouseAdapter() {
            JLabel answer = new JLabel();

            @Override
            public void mouseClicked(MouseEvent e) {
                if (buttonAnswer.getText().equals("显示答案")) {
                    Component[] components = getComponents();
                    for (int i = 0; i < components.length; i++) {
                        if (components[i] == buttonPanel) {
                            setAnswer();
                            add(answer, i + 1);
                            break;
                        }
                    }
                    buttonAnswer.setText("隐藏答案");
                } else {
                    remove(answer);
                    buttonAnswer.setText("显示答案");
                }
            }

            private void setAnswer() {
                String myAnswer = "无";
                if (t.getMyAnswer() != null && !t.getMyAnswer().equals("")) {
                    myAnswer = t.getMyAnswer();
                }
                answer.setText("<html>我的答案：" + myAnswer + "   " + "<br/>" +
                        "正确答案：" + t.getAnswer() + "   " + "<br/>" +
                        "解题思路：" + t.getAnswerExplain());
                answer.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
            }
        });
        buttonPanel.add(buttonAnswer);

        JButton buttonCollect = new JButton("收藏本题");
        buttonCollect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                t.setCollectStatus(1);
                DBConnecter.updateQuestion(t.getId(), TableQuestion.COLLECT_STATUS, TableQuestion.COLLECT_YES);
                JOptionPane.showMessageDialog(null,"收藏成功！");
            }
        });
        buttonPanel.add(buttonCollect);

        JButton buttonNote = new JButton("试题笔记");
        buttonNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String note = t.getNote();

                JTextArea text = new JTextArea(note, 5, 40);
                Object[] message = { "笔记：", new JScrollPane(text)};
                JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = pane.createDialog(null, "Input");
                dialog.setVisible(true);

                String ret = text.getText();
                if(ret!=null && !ret.equals("") && !ret.equals(note)){//需要更新
                    t.setNote(ret);
                    DBConnecter.updateQuestion(t.getId(), TableQuestion.NOTE, ret);
                }
            }
        });
        buttonPanel.add(buttonNote);

    }

    public JLabel buildLabel() {
        JLabel label = new JLabel();
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        label.setFont(new Font("宋体", Font.BOLD, 15));
        return label;
    }

    public static void main(String[] args) {
    }
}
