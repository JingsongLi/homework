package org.homework.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * Created by hasee on 2015/5/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableQuestion {
    public static final String COURSE = "course";
    public static final String CHAPTER = "chapter";
    public static final String TYPE = "type";
    public static final String MAIN_CONTENT = "main_content";
    public static final String ELE_CONTENT = "element_content";
    public static final String ANSWER = "answer";

    String course;
    int chapter;
    int type;
    String main_content;
    String ele_content;
    String answer;
}
