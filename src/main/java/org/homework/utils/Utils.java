package org.homework.utils;

import javax.swing.*;

/**
 * Created by hasee on 2015/5/5.
 */
public class Utils {

    public static ImageIcon getIcon(String url){
        return new ImageIcon(Utils.class.getClassLoader().getResource(url));
    }

    public static String getPath(String url){
        return Utils.class.getClassLoader().getResource(url).getPath();
    }

    public static void main(String[] args){
        System.out.println(Utils.class.getClassLoader().getResource("Maindb.db").getPath());
    }


}
