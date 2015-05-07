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

        labelTitle = new JLabel("�ޱ���");
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitle.setBackground(Color.WHITE);
        scrollPane.setColumnHeaderView(labelTitle);
        labelTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        labelTitle.setFont(new Font("����", Font.BOLD, 20));
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
        labelTypeExplain.setFont(new Font("����", Font.BOLD, 15));
        add(labelTypeExplain);

        for(int i=0; i < list.size(); i++){
            contentPanel.addPanel(i+1,list.get(i));
        }

        //�пհ�Panel����ɾ�����һ���ؼ����𰸣�
        JPanel plaitPanel = new JPanel();
        plaitPanel.setBackground(Color.white);
        add(plaitPanel);
        validate();
        repaint();
    }

    public void addPanel(int index,final TableQuestion t){
        //1.���
        JLabel lblNewLabel = buildLabel();
        lblNewLabel.setText(index + ". " + t.getMain_content());
        lblNewLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        add(lblNewLabel);

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
                        value = (char)(i+65) + "";
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
                    value = (char)(i+65) + "";
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
            JLabel answer = new JLabel();

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
                answer.setText("<html>�ҵĴ𰸣�" + myAnswer + "   " + "<br/>" +
                        "��ȷ�𰸣�" + t.getAnswer() + "   " + "<br/>" +
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

        JButton buttonNote = new JButton("����ʼ�");
        buttonNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String note = t.getNote();

                JTextArea text = new JTextArea(note, 5, 40);
                Object[] message = { "�ʼǣ�", new JScrollPane(text)};
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

    }

    public JLabel buildLabel() {
        JLabel label = new JLabel();
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        label.setFont(new Font("����", Font.BOLD, 15));
        return label;
    }

    public static void main(String[] args) {
    }
}
