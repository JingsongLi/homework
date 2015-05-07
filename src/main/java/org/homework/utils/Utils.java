package org.homework.utils;

import org.homework.db.model.TableQuestion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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

    public static void add3Index(TreeMap<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> map,
                                 List<TableQuestion> questions){
        for(TableQuestion q : questions){
            //1级目录
            String course = q.getCourse();
            TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>> sub1Map = map.get(course);
            if(sub1Map == null){
                sub1Map = new TreeMap();
                map.put(course,sub1Map);
            }
            //2级目录
            int  chapter = q.getChapter();
            TreeMap<Integer,List<TableQuestion>> sub2Map = sub1Map.get(chapter);
            if (sub2Map == null){
                sub2Map = new TreeMap();
                sub1Map.put(chapter,sub2Map);
            }
            //3机目录
            int type = q.getType();
            List<TableQuestion> subList = sub2Map.get(type);
            if (subList == null){
                subList = new ArrayList();
                sub2Map.put(type,subList);
            }
            subList.add(q);
        }
    }

    public static String getChineseNum(int num){
        String ret = "";
        String[] table={"零","一","二","三","四","五","六","七","八","九"};
        String s= num+"";
        for (char c : s.toCharArray()) {
            if(c>='0' && c<='9'){
                ret += table[c-48];
            }
        }
        return ret;
    }

    static final String[] TYPE_INDEX = {"单项选择题", "多项选择题","判断题", "填空题", "简答题"};
    static final String[] TYPE_EXPLAIN = {
            "单向选择题：每题只有一个正确答案。",
            "多向选择题：每题有一个或多个正确答案。",
            "判断题：对或错，请选择。",
            "填空题：填空，以逗号分隔答案。",
            "简答题：请写到下面的框中。"};
    public static final String[] JUDGE_OPTION = {"对", "错"};
    public static final String SPLIT = "#";
    public static String getTypeWord(int i){
        return TYPE_INDEX[i-1];
    }
    public static String getTypeExplain(int i){
        return TYPE_EXPLAIN[i-1];
    }

    public static void main(String[] args){
        System.out.println("A#B#C".split("#").length);
    }
}
