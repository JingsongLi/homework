package org.homework.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by jslee on 2015/6/22.
 */
public class TxtOperator {
    public static void writeTxt(List<String> list,String path) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(new File(path));
        for(String s : list){
            printStream.println(s);
        }
        printStream.close();
    }
}
