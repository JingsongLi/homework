package org.homework.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jslee on 2015/6/20.
 */
public class MyScrollPane extends JScrollPane {
    public MyScrollPane(){
        getVerticalScrollBar().setUnitIncrement(20);
    }
    public MyScrollPane(Component view) {
        super(view);
        getVerticalScrollBar().setUnitIncrement(20);
    }
}
