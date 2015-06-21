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
        render.setLeafIcon(getIcon(PRE + "文件.png"));
        render.setClosedIcon(getIcon(PRE + "关闭文件夹.png"));
        render.setOpenIcon(getIcon(PRE + "打开文件夹.png"));

//        BasicTreeUI ui=(BasicTreeUI)(getUI());
//        ui.setCollapsedIcon(CollapsedIcon);
//        ui.setExpandedIcon(ExpandedIcon);
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        JLabel label = new JLabel("我去");
        label.setIcon(getIcon(PRE + "文件.png"));
        frame.getContentPane().add(label);
        frame.setVisible(true);
    }
}
