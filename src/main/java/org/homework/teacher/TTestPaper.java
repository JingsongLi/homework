package org.homework.teacher;

import org.homework.db.DBConnecter;
import org.homework.db.model.TableQuestion;
import org.homework.io.IoOperator;
import org.homework.student.ContentPanel;
import org.homework.utils.MyScrollPane;
import org.homework.utils.Pair;
import org.homework.utils.Utils;

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
    //                           ��Ŀ           ����             �½�                 ���
    public final static TreeMap<String,TreeMap<Integer,TreeMap<Integer, java.util.List<TableQuestion>>>> allTestQuestion = new TreeMap();

    JDialog jDialog=null; //����һ���յĶԻ������
    JButton createPaperBtn;
    JPanel centerPanel;
    JScrollPane centerScrollPane;

    static String course = null;

    //ÿ�����ͣ����µ�����
    List<Pair<JTextField,List<TableQuestion>>> textFieldArray = new ArrayList();
    List<Pair<JCheckBox,TableQuestion>> checkboxList = new ArrayList();

    static {
        java.util.List<TableQuestion> questions = DBConnecter.getAllQuestion();
        mapAllTestQuestion(allTestQuestion, questions);
    }

    public TTestPaper(JFrame jFrame) {

        String[] courseArray = new String[allTestQuestion.size() + 1];
        courseArray[0] = "";
        int i = 1;
        int rowMax = 0;
        int columnMax = 0;

        //��Ŀ       ����         �½�
        for (Map.Entry<String, TreeMap<Integer, TreeMap<Integer, List<TableQuestion>>>> entry : allTestQuestion.entrySet()) {
            courseArray[i++] = entry.getKey();
            //if (flag) {
                for (Map.Entry<Integer, TreeMap<Integer, List<TableQuestion>>> entry2 : entry.getValue().entrySet()) {
                    if (entry.getValue().size() > rowMax) {
                        rowMax = entry.getValue().size();
                    }
                    for (Map.Entry<Integer, List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {
                        if (entry2.getValue().size() > columnMax) {
                            columnMax = entry2.getValue().size();
                        }
                    }
                }
        }

                /* ��ʼ��jDialog1
        * ָ���Ի����ӵ����ΪjFrame,����Ϊ"Dialog",���Ի���Ϊ����ʱ,������������
        * �����û�������(��̬�Ի���) */
        jDialog = new JDialog(jFrame,"�����Ծ�",true);

        jDialog.setSize(new Dimension(650, 620));
        /* ���öԻ����ʼ��ʾ����Ļ���е�λ�� */
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jDialog.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jDialog.getHeight()) / 2;
        jDialog.setLocation(w, h);

        jDialog.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        jDialog.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel courseLabel = new JLabel("��Ŀ");
        courseLabel.setBackground(Color.WHITE);
        final JComboBox courseComboBox = new JComboBox(courseArray);
        courseComboBox.setBackground(Color.WHITE);

        topPanel.add(courseLabel);
        topPanel.add(courseComboBox);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        centerScrollPane = new MyScrollPane(centerPanel);
        jDialog.getContentPane().add(centerScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        jDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        createPaperBtn = new JButton("�����Ծ�");
        bottomPanel.add(createPaperBtn);

        createPaperBtn.addMouseListener(this);

        courseComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        TTestPaper.course = (String)e.getItem();
                        textFieldArray.clear();
                        checkboxList.clear();
                        showCenterPanel();
                        jDialog.validate();
                        jDialog.repaint();
                        break;
                    default:
                        break;
                }
            }
        });

        jDialog.setVisible(true);
    }

    /**
     * һ��JScrollPane����ֱ��add JLable
     * ��������JPanel����������setAlignmentX(Component.LEFT_ALIGNMENT);
     * ��Ȼû���õģ��Ὣ���в��������м�
     */
    private void showCenterPanel() {

        jDialog.getContentPane().remove(centerScrollPane);
        centerPanel.removeAll();

        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setBackground(Color.WHITE);
//        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JScrollPane scrollPane = new MyScrollPane(totalPanel);
        centerPanel.add(scrollPane);

        //���ڷ���textFieldArray
        int i = 0;
        int j = 0;
        //��Ŀ
        for (Map.Entry<String, TreeMap<Integer, TreeMap<Integer, java.util.List<TableQuestion>>>> entry1 : allTestQuestion.entrySet()) {
            if (TTestPaper.course.equals(entry1.getKey())) {
                //����
                for (Map.Entry<Integer,TreeMap<Integer,java.util.List<TableQuestion>>> entry2 : entry1.getValue().entrySet()) {

                    int type = entry2.getKey();
                    if (entry2.getKey() != 5 && entry2.getKey() != 4) {

                        JLabel labelTypeExplain = buildLabel();
                        labelTypeExplain.setText(getTypeWord(type));
                        labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                        labelTypeExplain.setFont(new Font("����", Font.BOLD, 15));
                        totalPanel.add(labelTypeExplain);

                        //�½�
                        for (Map.Entry<Integer,List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {
                            JPanel panel_3 = new JPanel();
                            panel_3.setLayout(new FlowLayout(FlowLayout.LEFT));
                            panel_3.setBackground(Color.WHITE);
                            panel_3.setAlignmentX(Component.LEFT_ALIGNMENT);
                            totalPanel.add(panel_3);

                            String s1 = Utils.getChapterName(entry1.getKey(),entry3.getKey(),null) + "(��" + entry3.getValue().size() + "��С��)";
                            JLabel label_1 = new JLabel(s1);
                            panel_3.add(label_1);
                            JTextField field =  new JTextField(20);
                            textFieldArray.add(new Pair<JTextField, List<TableQuestion>>(field,entry3.getValue()));
                            panel_3.add(field);
                            panel_3.add(new JLabel("��"));
                            j++;
                        }
                        j = 0;
                        i++;
                    } else {
                        JLabel labelTypeExplain = buildLabel();
                        labelTypeExplain.setText(getTypeWord(type));
                        labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                        labelTypeExplain.setFont(new Font("����", Font.BOLD, 15));
                        totalPanel.add(labelTypeExplain);

                        //�½�
                        for (final Map.Entry<Integer,java.util.List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {

                            for (int k = 1; k <= entry3.getValue().size(); k++) {
                                String s1 =  Utils.getChapterName(entry1.getKey(), entry3.getKey(), null) + ".��" + k + "��";
                                JCheckBox checkBox = new JCheckBox(s1);
                                checkBox.setBackground(Color.WHITE);
                                totalPanel.add(checkBox);
                                checkboxList.add(new Pair<JCheckBox, TableQuestion>(checkBox, entry3.getValue().get(k - 1)));
                                String startSentence = entry3.getValue().get(k-1).getMain_content();
                                if(startSentence.startsWith("[")){//ͼƬ
                                    JLabel startLabel = new JLabel();
                                    startLabel.setIcon(getIcon(ContentPanel.PRE + startSentence.substring(1,startSentence.length()-1)));
                                    startLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
                                    totalPanel.add(startLabel);
                                }else {
                                    JTextArea jTextArea = new JTextArea();
                                    jTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
                                    jTextArea.setBackground(null);
                                    jTextArea.setEditable(false);
                                    jTextArea.setLineWrap(true);
                                    jTextArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
                                    jTextArea.setText(startSentence);
                                    jTextArea.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                                    totalPanel.add(jTextArea);
                                }
                            }
                        }
                    }
                }

                //�пհ�Panel����ɾ�����һ���ؼ����𰸣�
                JPanel plaitPanel = new JPanel();
                plaitPanel.setBackground(Color.white);
                jDialog.getContentPane().add(plaitPanel);
                jDialog.getContentPane().validate();
                jDialog.getContentPane().repaint();
            }
        }
        jDialog.getContentPane().add(centerScrollPane, BorderLayout.CENTER);
        jDialog.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TreeMap<Integer, List<TableQuestion>> resultTestMap = new TreeMap<Integer, List<TableQuestion>>();
        if (e.getSource() == createPaperBtn) {
            for (int i = 0; i < textFieldArray.size(); i++) {
                String s = textFieldArray.get(i).getK().getText();
                if (!s.equals("")) {
                    int count = Integer.parseInt(s);
                    List<TableQuestion> questions = textFieldArray.get(i).getV();


                    List<TableQuestion> tmpResultList = find2QuestionList(count,questions);
                    List<TableQuestion> resultList = resultTestMap.get(questions.get(0).getType());
                    if (resultList == null) {
                        resultList = new ArrayList<TableQuestion>();
                        resultTestMap.put(questions.get(0).getType(), resultList);
                    }
                    resultList.addAll(tmpResultList);
                }
            }
            for (Pair<JCheckBox, TableQuestion> entry4 : checkboxList) {
                if (entry4.getK().isSelected()) {
                    List<TableQuestion> list = resultTestMap.get(entry4.getV().getType());
                    if (list == null) {
                        list = new ArrayList<TableQuestion>();
                        resultTestMap.put(entry4.getV().getType(), list);
                    }
                    list.add(entry4.getV());
                }
            }

            IoOperator.generatePDF(-1,null, resultTestMap);
        }
    }

    private List<TableQuestion> find2QuestionList(int number,List<TableQuestion> oldList){
        List<TableQuestion> list = new ArrayList<TableQuestion>();
        if(oldList != null){
            List<TableQuestion> tmpList = new ArrayList<TableQuestion>();
            tmpList.addAll(oldList);
            Collections.shuffle(tmpList);
            for (int i = 0; i<number && i<tmpList.size(); i++) {
                list.add(tmpList.get(i));
            }
        }
        return list;
    }
}
