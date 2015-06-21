package org.homework.utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import static org.homework.utils.Utils.getIcon;

/**
 * Created by jslee on 2015/6/21.
 */
public class MyTree extends JTree {
    public static final String PRE = "backgroud\\";
    public MyTree(DefaultTreeModel model){
        super(model);

        DefaultTreeCellRenderer render= (DefaultTreeCellRenderer)(getCellRenderer());
        render.setLeafIcon(getIcon(PRE + "�ļ�.png"));
        render.setClosedIcon(getIcon(PRE + "�ر��ļ���.png"));
        render.setOpenIcon(getIcon(PRE + "���ļ���.png"));

//        BasicTreeUI ui=(BasicTreeUI)(getUI());
//        ui.setCollapsedIcon(CollapsedIcon);
//        ui.setExpandedIcon(ExpandedIcon);
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        JLabel label = new JLabel("��ȥ");
        label.setIcon(getIcon(PRE + "�ļ�.png"));
        frame.getContentPane().add(label);
        frame.setVisible(true);
    }
}
