package org.homework.student;

import org.homework.db.DBConnecter;
import org.homework.db.model.TableQuestion;
import org.homework.db.model.User;
import org.homework.main.MainFrame;
import org.homework.main.login.LoginPanel;
import org.homework.utils.MyPanel;
import org.homework.utils.MyScrollPane;
import org.homework.utils.MyTextArea;
import org.homework.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static org.homework.utils.Utils.*;

public class ContentPanel extends JPanel {

    public static final String PRE = "question\\";

    JLabel labelTitle;
    static JScrollPane scrollPane;
    public static boolean isCollectPanel = false;

    static ContentPanel contentPanel = new ContentPanel();
    public static ContentPanel loadContentPanel(){
        return contentPanel;
    }

    public static ContentPanel reloadContentPanel(){
        scrollPane.repaint();
//        StudentPanel.rightPanel.remove(contentPanel);
//        contentPanel = new ContentPanel();
//        StudentPanel.rightPanel.add(contentPanel.getScrollPane(), BorderLayout.CENTER);
        return contentPanel;
    }

    public JScrollPane getScrollPane(){

        scrollPane = new MyScrollPane();
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

    public void fullContent(String title, TreeMap<Integer, List<TableQuestion>> listMap){
        removeAll();
        labelTitle.setText(title);

        for (Map.Entry<Integer, List<TableQuestion>> entry : listMap.entrySet()){
            List<TableQuestion> list = entry.getValue();
            int type = entry.getKey();
            JLabel labelTypeExplain = buildLabel();
            labelTypeExplain.setText(getTypeExplain(type));
            labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
            labelTypeExplain.setFont(new Font("宋体", Font.BOLD, 15));
            add(labelTypeExplain);

            for(int i=0; i < list.size(); i++){
                TableQuestion t = list.get(i);
                if(isCollectPanel){
                    if(t.getCollectStatus() == TableQuestion.COLLECT_NOT)
                        continue;
                }
                contentPanel.addPanel(i+1,t);
            }
        }

        //有空白Panel才能删除最后一个控件（答案）
        JPanel plaitPanel = new JPanel();
        plaitPanel.setBackground(Color.white);
        add(plaitPanel);
        validate();
        repaint();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JScrollBar vBar = scrollPane.getVerticalScrollBar(); //得到了该JScrollBar
                    vBar.setValue(vBar.getMinimum()); //设置一个具体位置，value为具体的位置

                    JScrollBar hBar = scrollPane.getHorizontalScrollBar(); //得到了该JScrollBar
                    hBar.setValue(hBar.getMinimum()); //设置一个具体位置，value为具体的位置
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addPanel(int index,final TableQuestion t){
        //1.题干
        String startSentence = t.getMain_content();
        if(startSentence.startsWith("[")){//图片
            JLabel numLabel = new JLabel();
            numLabel.setText(index + ". ");
            numLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
            add(numLabel);

            JLabel startLabel = new JLabel();
            startLabel.setIcon(getIcon(PRE + startSentence.substring(1,startSentence.length()-1)));
//            startLabel.setIcon(getIcon(PRE +"/"+ startSentence));
            startLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
            add(startLabel);
        }else {
            JTextArea area = new MyTextArea();
            area.setText(index + ". " + startSentence);
            area.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
            add(area);
        }

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
                        value = num2ABC(i);
                        radioButton = new JRadioButton(value + "、  ");// 创建单选按钮
                    }else{
                        value = eles[i];
                        radioButton = new JRadioButton("  ");// 创建单选按钮
                    }
                    radioButton.setAlignmentY(Component.TOP_ALIGNMENT);
                    radioButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
                    radioButton.setBackground(Color.WHITE);
                    radioButton.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            String answer = value;
                            DBConnecter.updateQuestion(t.getId(), TableQuestion.MY_ANSWER, answer);
                            t.setMyAnswer(answer);
                        }
                    });
                    MyPanel myPanel = new MyPanel();
                    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.X_AXIS));
                    myPanel.add(radioButton);
                    JTextArea radioArea = new MyTextArea();
                    radioArea.setAlignmentY(Component.TOP_ALIGNMENT);
                    radioArea.setText(eles[i]);
                    myPanel.add(radioArea);

                    add(myPanel);// 应用单选按钮
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
                    value = num2ABC(i);
                    jCheckBox = new JCheckBox(value + "、  ");// 创建复选按钮
                    jCheckBox.setAlignmentY(Component.TOP_ALIGNMENT);
                    boxList.add(jCheckBox);
                    jCheckBox.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
                    jCheckBox.setBackground(Color.WHITE);
                    jCheckBox.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e){
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

                    MyPanel myPanel = new MyPanel();
                    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.X_AXIS));
                    myPanel.add(jCheckBox);
                    JTextArea radioArea = new MyTextArea();
                    radioArea.setAlignmentY(Component.TOP_ALIGNMENT);
                    radioArea.setText(eles[i]);
                    myPanel.add(radioArea);
                    add(myPanel);
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
            JScrollPane scroll = new MyScrollPane(text);
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
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        add(buttonPanel);

        final JButton buttonAnswer = new JButton("显示答案");
        buttonAnswer.addMouseListener(new MouseAdapter() {
            JTextArea answer = new MyTextArea();

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
                String rightAnswer = null;
                String rightAnswerExplain = null;
                if((CatalogTree.allScore.get(t.getCourse()) != null && CatalogTree.allScore.get(t.getCourse()).containsKey(t.getChapter()))
                        || (MainFrame.user.getType() == User.TEACHER)){
                    rightAnswer = t.getAnswer();
                    rightAnswerExplain = t.getAnswerExplain();
                }else {
                    rightAnswer = "此章节未公布答案。";
                    rightAnswerExplain = "此章节未公布答案。";
                }
                answer.setText("我的答案：" + myAnswer.replace("#","") + "   " + "\n\r" +
                        "正确答案：" + rightAnswer+ "   " + "\n\r" +
                        "解题思路：" + rightAnswerExplain);
                answer.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
            }
        });
        buttonPanel.add(buttonAnswer);

        final JButton buttonCollect = new JButton("收藏本题");
        if (t.getCollectStatus() == TableQuestion.COLLECT_YES) {
            buttonCollect.setText("去除收藏");
        }
        buttonCollect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (buttonCollect.getText().equals("收藏本题")) {
                    t.setCollectStatus(1);
                    DBConnecter.updateQuestion(t.getId(), TableQuestion.COLLECT_STATUS, TableQuestion.COLLECT_YES);
                    buttonCollect.setText("去除收藏");
                    JOptionPane.showMessageDialog(null,"收藏成功！");
                }
                else {
                    t.setCollectStatus(0);
                    DBConnecter.updateQuestion(t.getId(), TableQuestion.COLLECT_STATUS, TableQuestion.COLLECT_NOT);
                    buttonCollect.setText("收藏本题");
                    JOptionPane.showMessageDialog(null, "去除收藏成功");
                }
            }
        });
        buttonPanel.add(buttonCollect);

        JButton buttonNote = new JButton("学习笔记");
        buttonNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String note = t.getNote();

                JTextArea text = new JTextArea(note, 5, 40);
                Object[] message = { "学习笔记：", new MyScrollPane(text)};
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



    public static void main(String[] args) {
    }
}
