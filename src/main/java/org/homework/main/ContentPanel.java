package org.homework.main;

import org.homework.db.model.TableQuestion;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ContentPanel extends JPanel {

    JLabel labelTitle;

    static ContentPanel contentPanel = new ContentPanel();
    public static ContentPanel getContentPanel(){
        return contentPanel;
    }

    public ContentPanel(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    public void addTitle(String str){
        labelTitle = new JLabel(str,JLabel.RIGHT);
        labelTitle.setPreferredSize(new Dimension(-1, 60));
        labelTitle.setMinimumSize(new Dimension(60, 150));
        labelTitle.setFont(new Font("����", Font.BOLD, 16));
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitle);
    }

    public void fullContent(String title, List<TableQuestion> list){
        removeAll();
        contentPanel.addTitle(title);
        for(int i=0; i < list.size(); i++){
            contentPanel.addPanel(i+1,list.get(i));
        }
        validate();
        repaint();
    }

    public void addPanel(int index,final TableQuestion t){

        final JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setPreferredSize(new Dimension(55, 180));
        panel.setMaximumSize(new Dimension(5000, 180));
        add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(index + ". " + t.getMain_content());
        panel.add(label);

        if(t.getType() == 1){//��ѡ
            String[] eles = t.getEle_content().split("#");
            ButtonGroup group = new ButtonGroup();// ������ѡ��ť��
            for (String e : eles){
                if(!e.equals("")){
                    JRadioButton radioButton = new JRadioButton(e);// ������ѡ��ť
                    panel.add(radioButton);// Ӧ�õ�ѡ��ť
                    group.add(radioButton);
                }
            }
        }

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        final JButton buttonAnswer = new JButton("��");
        buttonPanel.add(buttonAnswer);
        JButton buttonCollect = new JButton("�ղ�");
        buttonPanel.add(buttonCollect);
        JButton buttonNote = new JButton("�ʼ�");
        buttonPanel.add(buttonNote);

        buttonAnswer.addMouseListener(new MouseAdapter(){
            boolean isOpen = false;
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!isOpen){
                    JLabel labelAnswer = new JLabel();
                    panel.add(labelAnswer);
                    labelAnswer.setText("��ȷ�𰸣� " + t.getAnswer());
                    buttonAnswer.setText("����");
                    isOpen = true;
                }else{
//                    panel.remove(panel.);
                }
            }
        });
    }

    public static void main ( String[] args )  {
    }
}
