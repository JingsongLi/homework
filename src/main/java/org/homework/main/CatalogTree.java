package org.homework.main;

import lombok.Getter;
import org.homework.db.DBConnecter;
import org.homework.db.model.TableQuestion;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
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
    JTree tree;


    public CatalogTree() {

        List<TableQuestion> questions = DBConnecter.getAllQuestion();
        TreeMap<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> map = new TreeMap();
        add3Index(map,questions);

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("catalog");

        DefaultMutableTreeNode firstLeaf = null;

        for (Map.Entry<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> entry1 : map.entrySet()){
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(entry1.getKey());
            top.add(node1);
            for (Map.Entry<Integer,TreeMap<Integer,List<TableQuestion>>> entry2 : entry1.getValue().entrySet()){
                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
                        "第" + getChineseNum(entry2.getKey()) + "章");
                node1.add(node2);
                for (Map.Entry<Integer,List<TableQuestion>> entry3 : entry2.getValue().entrySet()){
                    DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(new TypeNode(entry3.getKey(),entry3.getValue()));
                    node2.add(node3);
                    if(firstLeaf == null)
                        firstLeaf = node3;
                }
            }
        }

        tree = new JTree(top);
        // 添加选择事件
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                if (node == null)
                    return;

                Object object = node.getUserObject();
                if (node.isLeaf()) {
                    click(object);
                }
            }
        });
        tree.setRootVisible(false);
//        tree.setShowsRootHandles(true);
        ecTreeTest(tree);
        click(firstLeaf.getUserObject());
    }

    private void click(Object object){
        TypeNode typeNode = (TypeNode) object;
        System.out.println("你选择了：" + typeNode.list);
        ContentPanel.getContentPanel().fullContent(getTypeWord(typeNode.type), typeNode.list);
    }

    public void ecTreeTest(JTree tree) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandTree(tree, new TreePath(root));
    }

    private void expandTree(JTree tree, TreePath parent) {
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
