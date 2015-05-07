package org.homework.db;

import org.homework.db.model.TableQuestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

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
    public static final String QUESTION_TABLE = "question";

    public static List<TableQuestion> getAllQuestion(){
        List<TableQuestion> list = new ArrayList<TableQuestion>();
        Statement sql_statement = null;
        try {
            sql_statement = c.createStatement();
            ResultSet result = sql_statement.executeQuery("select * from " + QUESTION_TABLE +";");
            while (result.next()) {
                TableQuestion question = new TableQuestion();
                question.setId(result.getInt(TableQuestion.ID));

                question.setCourse(result.getString(TableQuestion.COURSE));
                question.setChapter(result.getInt(TableQuestion.CHAPTER));
                question.setType(result.getInt(TableQuestion.TYPE));
                question.setMain_content(result.getString(TableQuestion.MAIN_CONTENT));
                question.setEle_content(result.getString(TableQuestion.ELE_CONTENT));
                question.setAnswer(result.getString(TableQuestion.ANSWER));
                question.setAnswerExplain(result.getString(TableQuestion.ANSWER_EXPLAIN));

                question.setMyAnswer(result.getString(TableQuestion.MY_ANSWER));
                question.setNote(result.getString(TableQuestion.NOTE));
                question.setCollectStatus(result.getInt(TableQuestion.COLLECT_STATUS));
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


    public static void updateQuestion(int id, String name, Object value){
        update(QUESTION_TABLE,id ,name,value);
    }
    public static void update(String tablename,int id, String name, Object value){
        Updater.queue.add(new Updater(tablename,id, name, value));
    }
    public static class Updater{
        String tablename;
        int id;
        String name;
        Object value;
        static BlockingQueue<Updater> queue = new LinkedBlockingQueue();
        static {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while(true){
                        try {
                            Updater u = queue.take();
                            String sql = "update " + u.tablename + " set " +
                                    u.name + " = ";
                            if(u.value instanceof Integer)
                                sql += u.value;
                            else if(u.value instanceof String)
                                sql += "'" + u.value + "'";
                            sql += " where id = " + u.id;
                            System.out.println(sql);
                            update(sql);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }
        public static void  update(String sql){
            Statement sql_statement = null;
            try {
                sql_statement = c.createStatement();
                sql_statement.executeUpdate(sql);
                //关闭连接和声明
                sql_statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public Updater(String tablename,int id, String name, Object value) {
            this.tablename = tablename;
            this.id = id;
            this.name = name;
            this.value = value;
        }
    }



    public static void main(String[] args) throws SQLException {

        updateQuestion(2,"note","我勒个去啊");

        System.out.println(getAllQuestion());

//        Statement sql_statement = null;
//        try {
//            sql_statement = c.createStatement();
//            boolean bool = sql_statement.execute("" +
//                    "CREATE TABLE question (\n" +
//                    "  [id] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
//                    "  [course] VARCHAR(50) NOT NULL ON CONFLICT ABORT, \n" +
//                    "  [chapter] TINYINT NOT NULL ON CONFLICT ABORT, \n" +
//                    "  [type] TINYINT NOT NULL ON CONFLICT ABORT, \n" +
//                    "  [main_content] VARCHAR(3000) NOT NULL ON CONFLICT ABORT, \n" +
//                    "  [element_content] VARCHAR(200), \n" +
//                    "  [answer] VARCHAR(500), \n" +
//                    "  [answer_explain] VARCHAR(500), \n" +
//                    "  [my_answer] VARCHAR(500), \n" +
//                    "  [note] VARCHAR(1000), \n" +
//                    "  [collect_status] TINYINT);");
//            System.out.println(bool);
//            //关闭连接和声明
//            sql_statement.close();
//            c.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

}
