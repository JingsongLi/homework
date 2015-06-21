package org.homework.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jslee on 2015/6/21.
 */
public class MyPanel extends JPanel {
    public MyPanel(){
        setBackground(Color.WHITE);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }
}
