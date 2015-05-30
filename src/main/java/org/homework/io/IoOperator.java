package org.homework.io;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import org.homework.db.DBConnecter;
import org.homework.db.model.TableQuestion;
import org.homework.student.CatalogTree;
import org.homework.student.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.homework.io.PDFOperator.PDFLiner;
import static org.homework.utils.Utils.*;

/**
 * Created by hasee on 2015/5/7.
 */
public class IoOperator {

    public static String user = "小明";
    public static String userId = "201522010444";

    public static void submitWork(int chapter, String course, TreeMap<Integer, List<TableQuestion>> map) {
        String direct = getFileDirectChoose();
        if (direct != null) {
            String message = user + "_" + userId + "_" +
                    course + "_" + getChineseNum(chapter) + "章" + "作业";
            String path = direct + "\\" + message;
            try {
                FileOutputStream out = new FileOutputStream(path);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(message);
                writer.newLine();
                writer.flush();

                ObjectOutputStream oos = null;
                ByteArrayOutputStream baos = null;
                // 序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(map);
                byte[] bytes = baos.toByteArray();
                out.write(bytes);

                writer.close();
                out.close();
                JOptionPane.showMessageDialog(null, "提交成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generatePDF(int chapter, TreeMap<Integer, List<TableQuestion>> map) {
        String direct = getFileDirectChoose();
        if (direct != null) {
            String path = direct + "\\" + "第" + getChineseNum(chapter) + "章" +
                    "试题.pdf";
            List<PDFLiner> list = new ArrayList<PDFLiner>();
            list.add(new PDFLiner("第" + getChineseNum(chapter) + "章" + "试题", 25, Font.BOLD));
            for (Map.Entry<Integer, List<TableQuestion>> entry : map.entrySet()) {
                list.add(new PDFLiner(getTypeWord(entry.getKey()), 20, Font.BOLD));
                for (int i = 0; i < entry.getValue().size(); i++) {
                    TableQuestion t = entry.getValue().get(i);

                    String startSentence = t.getMain_content();
                    if (startSentence.startsWith("[")) {//图片
                        list.add(new PDFLiner((i + 1) + ". ", 15, Font.PLAIN));
                        try {
                            list.add(new PDFLiner(Image.getInstance(getURL(
                                    ContentPanel.PRE + startSentence.substring(1, startSentence.length() - 1)))
                                    , 15, Font.PLAIN));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        list.add(new PDFLiner((i + 1) + ". " + t.getMain_content(), 15, Font.PLAIN));
                    if (t.getEle_content() != null && !t.getEle_content().equals("")) {
                        String[] strs = t.getEle_content().split(SPLIT);
                        for (int j = 0; j < strs.length; j++) {
                            list.add(new PDFLiner(("  " + num2ABC(j) + ". " + strs[j]), 15, Font.PLAIN));
                        }
                    }
                }
            }
            try {
                PDFOperator.writePdf(list, path);
                JOptionPane.showMessageDialog(null, "导出成功！");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void importScore() {
        JFileChooser fileChooser = new JFileChooser("F:\\");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            //file解析
            DBConnecter.updateScore("数学", 1, 69);
            CatalogTree.updateAllScore();
        }
    }

    public static String getFileDirectChoose() {
        JFileChooser fileChooser = new JFileChooser("F:\\");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            return filePath;
        }
        return null;
    }

    public static void main(String[] args) {
    }
}
