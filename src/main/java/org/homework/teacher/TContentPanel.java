package org.homework.teacher;

import org.homework.db.model.StudentAnswer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.homework.utils.Utils.*;

public class TContentPanel extends JPanel {

    public static final String PRE = "question\\";

    JLabel labelTitle;
    JScrollPane scrollPane;
    public static boolean isCollectPanel = false;

    static TContentPanel tContentPanel = new TContentPanel();
    public static TContentPanel getTContentPanel() {
        return tContentPanel;
    }

    public JScrollPane getScrollPane(){

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(this);

        labelTitle = new JLabel("�ޱ���");
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitle.setBackground(Color.WHITE);
        scrollPane.setColumnHeaderView(labelTitle);
        labelTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        labelTitle.setFont(new Font("����", Font.BOLD, 20));
        scrollPane.getColumnHeader().setBackground(Color.white);
        return scrollPane;
    }

    public TContentPanel(){
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    public void fullContent(TreeMap<Integer, List<StudentAnswer>> map){

        removeAll();
        labelTitle.setText("������ҵ");

        for (Map.Entry<Integer, List<StudentAnswer>> entry : map.entrySet()) {
            List<StudentAnswer> list = entry.getValue();
            int type = entry.getKey();
            JLabel labelTypeExplain = buildLabel();
            labelTypeExplain.setText(getChineseNum(entry.getKey()) + "��" + getTypeExplain(type));
            labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
            labelTypeExplain.setFont(new Font("����", Font.BOLD, 15));

            add(labelTypeExplain);


            /*
            for(int i=0; i < list.size(); i++) {
                StudentAnswer t = list.get(i);
                tContentPanel.addPanel(t);
            }
            */
            addPanel(entry.getKey(), entry.getValue());
        }

        //�пհ�Panel����ɾ�����һ���ؼ����𰸣�
        JPanel plaitPanel = new JPanel();
        plaitPanel.setBackground(Color.white);
        add(plaitPanel);
        validate();
        repaint();
    }

    //��ʾ��ѡ ��ѡ  �ж�
    private void showObjectiveAnswer(List<StudentAnswer> stuAnsList) {
        //�ɼ�panel
        final JPanel scorePanel = new JPanel();
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        scorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField textField = new JTextField(10);
        scorePanel.add(new JLabel("�ɼ���"));
        scorePanel.add(textField);
        add(scorePanel);

        //��panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        List<JTable> tableList = new ArrayList<JTable>();

//        for (int i = stuAnsList.size(); i < 27; i++) {
//             StudentAnswer stuAns = new StudentAnswer();
//            stuAns.setStudentAnswer("A"+i%4);
//            stuAns.setAnswer("A" + i % 4 + 1);
//            stuAnsList.add(stuAns);
//        }


        int tableCount = stuAnsList.size()/10 +1;
        for (int i = 0; i < tableCount; i++) {
            JTable table = new JTable(new DefaultTableModel(3, 11));
            table.setValueAt("���", 0, 0);
            table.setValueAt("ѧ����", 1, 0);
            table.setValueAt("�ο���", 2, 0);
            table.setBorder(new LineBorder(new Color(0, 0, 0)));
            table.setRowHeight(20);

            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            table.setDefaultRenderer(Object.class, tcr);

            for (int j = 1; j <= 10 && (j+(10*i)) <= stuAnsList.size(); j++) {
                table.setValueAt(j + 10*i, 0, j);
                String answer = stuAnsList.get(j + (10 * i) - 1).getStudentAnswer();
                String rightAnswer = stuAnsList.get(j + (10 * i) - 1).getAnswer();
                table.setValueAt(rightAnswer, 2, j);
                if (answer.equals(rightAnswer))
                    table.setValueAt(answer, 1, j);
                else
                    table.setValueAt("<html><font color=\"red\">" +
                            answer + "</font></html>", 1, j);
            }
            tableList.add(table);
        }


        for (int i = 0; i < tableCount; i++) {
            tablePanel.add(Box.createVerticalStrut(3));
            tablePanel.add(tableList.get(i));
        }

        add(tablePanel);

    }

    //��ʾ���  ���
    private void showSubjectiveAnswer(Integer type, List<StudentAnswer> stuAnsList) {
        int textLineCount = 0;
        if (type == 4) {
            textLineCount = 2;
        }
        else {
            textLineCount = 5;
        }

        //�ɼ���ʾ
        final JPanel scorePanel = new JPanel();
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        scorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField textField = new JTextField(10);
        scorePanel.add(new JLabel("�ɼ���"));
        scorePanel.add(textField);
        add(scorePanel);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBackground(Color.WHITE);
        answerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        answerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        add(answerPanel);

        for (Integer i = 1; i <= stuAnsList.size(); i++) {
            //�����ʾ
            final JPanel scoreNumberPanel = new JPanel();
            scoreNumberPanel.setBackground(Color.WHITE);
            scoreNumberPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            scoreNumberPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            scoreNumberPanel.add(new JLabel(i.toString() + "."));
            answerPanel.add(scoreNumberPanel);

            //ѧ������ο���panel
            JPanel textAreaPanel = new JPanel();
            textAreaPanel.setLayout(new GridLayout(2,1,0,3));
            textAreaPanel.setBackground(Color.WHITE);
            textAreaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            //ѧ����
            final JPanel stuAnsPanel = new JPanel();
            stuAnsPanel.setBackground(Color.WHITE);
            stuAnsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            //stuAnsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            final JTextArea textStuAns = new JTextArea(textLineCount, 120);
            textStuAns.setBackground(Color.WHITE);
            textStuAns.setEditable(false);
            textStuAns.setLineWrap(true);
            //textStuAns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            JScrollPane scrollStuAns = new JScrollPane(textStuAns);
            stuAnsPanel.add(scrollStuAns);

            //�ο���
            final JPanel correctAnsPanel = new JPanel();
            correctAnsPanel.setBackground(Color.WHITE);
            correctAnsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            final JTextArea textCorrectAns = new JTextArea(textLineCount,120);
            textCorrectAns.setBackground(Color.WHITE);
            textCorrectAns.setEditable(false);
            textCorrectAns.setLineWrap(true);
            JScrollPane scrollCorrectAns = new JScrollPane(textCorrectAns);
            correctAnsPanel.add(scrollCorrectAns);

            String stuAnswer = stuAnsList.get(i-1).getStudentAnswer();
            String correctAnswer = stuAnsList.get(i-1).getAnswer();
            if (type == 4 && !(stuAnswer.equals(correctAnswer))) {
//                String[] student = stuAnswer.split(",");
//                String[] correct = stuAnswer.split(",");
//                for (int j = 0; j < correct.length; j++) {
//                    String s = student[j];
//                    String c = correct[j];
//                    if (!s.equals(c)) {
//                        appendToPane(textStuAns, s, Color.RED);
//                    }
//                    if (i == correct.length - 1) {
//                        appendToPane(textStuAns, ",", Color.BLACK);
//                    }
//                }
                textStuAns.setForeground(Color.RED);
            }
            //else {
                textStuAns.setText("ѧ���𰸣�\n\r" + stuAnswer);
            //}
            textCorrectAns.setText("�ο��𰸣�\n\r" + correctAnswer);

            textAreaPanel.add(stuAnsPanel);
            textAreaPanel.add(correctAnsPanel);

            answerPanel.add(Box.createVerticalStrut(3));
            answerPanel.add(textAreaPanel);
        }

    }

    private void addPanel(Integer type, List<StudentAnswer> stuAnsList) {

        switch (type)
        {
            //��ѡ  ��ѡ  �ж�
            case 1:
            case 2:
            case 3:
                showObjectiveAnswer(stuAnsList);
                break;
            //���  ���
            case 4:
            case 5:
                showSubjectiveAnswer(type, stuAnsList);
                break;
            default:
                break;
        }

        /*
        //1.���
        String startSentence = t.getMain_content();
        if(startSentence.startsWith("[")){//ͼƬ
            JLabel numLabel = new JLabel();
            numLabel.setText(index + ". ");
            numLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
            add(numLabel);

            JLabel startLabel = new JLabel();
            startLabel.setIcon(getIcon(PRE + startSentence.substring(1,startSentence.length()-1)));
            startLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
            add(startLabel);
        }else {
            JTextArea area = getMutiLineArea();
            area.setText(index + ". " + startSentence);
            area.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
            add(area);
        }

        //2.ѡ������հ�
        if(t.getType()==1 || t.getType()==3){//��ѡ���ж�
            String[] eles;
            ButtonGroup group = new ButtonGroup();// ������ѡ��ť��
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
                        radioButton = new JRadioButton(value + "��  " + eles[i]);// ������ѡ��ť
                    }else{
                        value = eles[i];
                        radioButton = new JRadioButton("  " + eles[i]);// ������ѡ��ť
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
                    add(radioButton);// Ӧ�õ�ѡ��ť
                    group.add(radioButton);
                }
            }

        }else if(t.getType() == 2){//����ѡ����
            String[] eles = t.getEle_content().split(SPLIT);
            final List<JCheckBox> boxList = new ArrayList<JCheckBox>();
            for (int i=0; i<eles.length; i++){
                if(!eles[i].equals("")){
                    final String value;
                    final JCheckBox jCheckBox;
                    value = num2ABC(i);
                    jCheckBox = new JCheckBox(value + "��  " + eles[i]);// ������ѡ��ť
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
                    add(jCheckBox);// Ӧ�õ�ѡ��ť
                }
            }
        }else if(t.getType()==4 || t.getType() == 5){//��ջ���
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

        //3.��ť
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        add(buttonPanel);

        final JButton buttonAnswer = new JButton("��ʾ��");
        buttonAnswer.addMouseListener(new MouseAdapter() {
            JTextArea answer = getMutiLineArea();

            @Override
            public void mouseClicked(MouseEvent e) {
                if (buttonAnswer.getText().equals("��ʾ��")) {
                    Component[] components = getComponents();
                    for (int i = 0; i < components.length; i++) {
                        if (components[i] == buttonPanel) {
                            setAnswer();
                            add(answer, i + 1);
                            break;
                        }
                    }
                    buttonAnswer.setText("���ش�");
                } else {
                    remove(answer);
                    buttonAnswer.setText("��ʾ��");
                }
            }

            private void setAnswer() {
                String myAnswer = "��";
                if (t.getMyAnswer() != null && !t.getMyAnswer().equals("")) {
                    myAnswer = t.getMyAnswer();
                }
                answer.setText("�ҵĴ𰸣�" + myAnswer + "   " + "\n\r" +
                        "��ȷ�𰸣�" + t.getAnswer()+ "   " + "\n\r" +
                        "����˼·��" + t.getAnswerExplain());
                answer.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
            }
        });
        buttonPanel.add(buttonAnswer);

        JButton buttonCollect = new JButton("�ղر���");
        buttonCollect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                t.setCollectStatus(1);
                DBConnecter.updateQuestion(t.getId(), TableQuestion.COLLECT_STATUS, TableQuestion.COLLECT_YES);
                JOptionPane.showMessageDialog(null,"�ղسɹ���");
            }
        });
        buttonPanel.add(buttonCollect);

        JButton buttonNote = new JButton("ѧϰ�ʼ�");
        buttonNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String note = t.getNote();

                JTextArea text = new JTextArea(note, 5, 40);
                Object[] message = { "ѧϰ�ʼǣ�", new JScrollPane(text)};
                JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = pane.createDialog(null, "Input");
                dialog.setVisible(true);

                String ret = text.getText();
                if(ret!=null && !ret.equals("") && !ret.equals(note)){//��Ҫ����
                    t.setNote(ret);
                    DBConnecter.updateQuestion(t.getId(), TableQuestion.NOTE, ret);
                }
            }
        });
        buttonPanel.add(buttonNote);
    */
    }



    public static void main(String[] args) {
    }
}
