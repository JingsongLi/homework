package org.homework.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jslee on 2015/6/21.
 */
public class MyTextArea extends JTextArea{
    public MyTextArea(){
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setWrapStyleWord(true);
        setBackground(null);//����͸��
        setEditable(false);//���ɱ༭
        setLineWrap(true);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }
}
