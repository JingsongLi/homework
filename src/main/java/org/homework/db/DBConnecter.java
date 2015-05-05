package org.homework.db;

import org.homework.db.model.TableQuestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.homework.utils.Utils.*;

/**
 * Created by hasee on 2015/5/5.
 */
public class DBConnecter {

    static Connection c;
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + getPath("main.db"));
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public static List<TableQuestion> getAllQuestion(){
        List<TableQuestion> list = new ArrayList<TableQuestion>();
        Statement sql_statement = null;
        try {
            sql_statement = c.createStatement();
            ResultSet result = sql_statement.executeQuery("select * from question;");
            while (result.next()) {
                TableQuestion question = new TableQuestion();
                question.setCourse(result.getString(TableQuestion.COURSE));
                question.setChapter(result.getInt(TableQuestion.CHAPTER));
                question.setType(result.getInt(TableQuestion.TYPE));
                question.setMain_content(result.getString(TableQuestion.MAIN_CONTENT));
                question.setEle_content(result.getString(TableQuestion.ELE_CONTENT));
                question.setAnswer(result.getString(TableQuestion.ANSWER));
//                System.out.println(question);
                list.add(question);
            }
            //关闭连接和声明
            sql_statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void main(String[] args) throws SQLException {
        System.out.println(getAllQuestion());
    }
}
