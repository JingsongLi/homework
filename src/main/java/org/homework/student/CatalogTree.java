package org.homework.student;

import lombok.Getter;
import org.homework.db.DBConnecter;
import org.homework.db.model.Score;
import org.homework.db.model.TableQuestion;
import org.homework.io.IoOperator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static org.homework.utils.Utils.*;

/**
 * Created by hasee on 2015/5/5.
 */
public class CatalogTree{

    @Getter
    static JTree tree;
    public final static TreeMap<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> allData = new TreeMap();
    public final static TreeMap<String,TreeMap<Integer,Integer>> allScore = new TreeMap();;
    public static Object firstLeafObect;
    static {
        List<TableQuestion> questions = DBConnecter.getAllQuestion();
        add3Index(allData, questions);
        updateAllScore();
    }

    private static DefaultTreeModel defaultTreeModel;

    public static void updateAllScore(){
        List<Score> scores = DBConnecter.getAllScore();
        add2Index(allScore, scores);
    }


    public CatalogTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("catalog");
        DefaultMutableTreeNode firstLeaf = null;
        for (Map.Entry<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> entry1 : allData.entrySet()){
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(entry1.getKey());
            top.add(node1);
            for (Map.Entry<Integer,TreeMap<Integer,List<TableQuestion>>> entry2 : entry1.getValue().entrySet()){
                //1.get score
                Integer score = null;
                if(allScore.containsKey(entry1.getKey()) &&
                        allScore.get(entry1.getKey()).containsKey(entry2.getKey()))
                    score = allScore.get(entry1.getKey()).get(entry2.getKey());
                //2.chapter name
                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
                        new ChapterNode(entry2.getKey(),entry1.getKey(),entry2.getValue(),score));
                node1.add(node2);
                for (Map.Entry<Integer,List<TableQuestion>> entry3 : entry2.getValue().entrySet()){
                    DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(new TypeNode(entry3.getKey(),entry3.getValue()));
                    node2.add(node3);
                    if(firstLeaf == null)
                        firstLeaf = node3;
                }
            }
        }

        defaultTreeModel =new DefaultTreeModel(top);
        tree = new JTree(defaultTreeModel);
        // ����ѡ���¼�
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                if (node == null)
                    return;

                Object object = node.getUserObject();
                if (node.isLeaf()) {
                    click(object,false);
                } else if (object instanceof ChapterNode && e.getButton()==MouseEvent.BUTTON3) {
                    final ChapterNode chapterNode = (ChapterNode) object;
                    JPopupMenu jPopupMenu = new JPopupMenu();
                    JMenuItem jmenuItem1 = new JMenuItem("�ύ");
                    jmenuItem1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            IoOperator.submitWork(chapterNode.chapter, chapterNode.course, chapterNode.map);
                        }
                    });
                    JMenuItem jmenuItem2 = new JMenuItem("����");
                    jmenuItem2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            IoOperator.generatePDF(chapterNode.chapter, chapterNode.map);
                        }
                    });
                    jPopupMenu.add(jmenuItem1);
                    jPopupMenu.add(jmenuItem2);
                    jPopupMenu.show(tree, e.getX(),e.getY());
                }
            }
        });
        tree.setRootVisible(false);
//        tree.setShowsRootHandles(true);
        ecTreeTest(tree);
        firstLeafObect = firstLeaf.getUserObject();
        clickFirst();
    }

    public static void clickFirst(){
        click(firstLeafObect, true);
    }

    private static void click(Object object,boolean isReload){
        TypeNode typeNode = (TypeNode) object;
        System.out.println("��ѡ���ˣ�" + typeNode.list);
        TreeMap<Integer, List<TableQuestion>> listMap = new TreeMap<Integer, List<TableQuestion>>();
        listMap.put(typeNode.type,typeNode.list);
        ContentPanel.getContentPanel().fullContent(getTypeWord(typeNode.type), listMap);
        if(isReload){
            defaultTreeModel.reload();
            ecTreeTest(tree);
        }
    }

    public static void ecTreeTest(JTree tree) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandTree(tree, new TreePath(root));
    }

    private static void expandTree(JTree tree, TreePath parent) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandTree(tree, path);
            }
        }
        tree.expandPath(parent);
    }

    public static class TypeNode{
        int type;
        List<TableQuestion> list;

        public TypeNode(int type, List<TableQuestion> list){
            this.type = type;
            this.list = list;
        }

        @Override
        public String toString(){
            return getTypeWord(type);
        }
    }

    public static class ChapterNode{
        int chapter;
        String course;
        TreeMap<Integer,List<TableQuestion>> map;
        Integer score;

        public ChapterNode(int chapter,String course, TreeMap<Integer,List<TableQuestion>> map, Integer score){
            this.chapter = chapter;
            this.course = course;
            this.map = map;
            this.score = score;
        }

        @Override
        public String toString(){
            String name = "��" + getChineseNum(chapter) + "��";
            if(score != null)
                name += "(" + score + ")";
            return name;
        }
    }

    public static void main(String[] args) {
        CatalogTree cata = new CatalogTree();
        final JTree tree = cata.getTree();
        JFrame f = new JFrame("JTreeDemo");
        f.add(tree);
        f.setSize(300, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}