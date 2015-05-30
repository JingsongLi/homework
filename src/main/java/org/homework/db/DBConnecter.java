package org.homework.db;

import org.homework.db.model.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.homework.utils.Utils.getPath;

/**
 * Created by hasee on 2015/5/5.
 */
public class DBConnecter {

    static Connection c;
    static Connection ownConn;
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + getPath("main.db"));
            ownConn = DriverManager.getConnection("jdbc:sqlite:" + getPath("own.db"));
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
    public static final String QUESTION_OWN_TABLE = "question_own";
    public static final String QUESTION_TABLE = "question";
    public static final String SCORE_OWN_TABLE = "score_own";
    public static final String STUDENT_ANSWER_TABLE = "student_answer";
    public static final String ALL_STUDENT_SCORE_TABLE = "all_student_score";
    public static final String USER_TABLE = "user";

    //需要手动join两个库中的表
    public static List<TableQuestion> getAllQuestion(){
        List<TableQuestion> list = new ArrayList<TableQuestion>();
        Map<Integer,TableQuestion> map = new HashMap<Integer, TableQuestion>();
        Statement sql_statement = null;
        Statement ownStatement = null;
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
                list.add(question);
                map.put(question.getId(),question);
            }

            ownStatement = ownConn.createStatement();
            ResultSet ownResult = ownStatement.executeQuery("select * from " + QUESTION_OWN_TABLE +";");
            while (ownResult.next()) {
                TableQuestion question = map.get(ownResult.getInt(TableQuestion.ID));
                if(question != null){
                    question.setMyAnswer(ownResult.getString(TableQuestion.MY_ANSWER));
                    question.setNote(ownResult.getString(TableQuestion.NOTE));
                    question.setCollectStatus(ownResult.getInt(TableQuestion.COLLECT_STATUS));
                }
            }
            //关闭连接和声明
            sql_statement.close();
            ownStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void updateQuestion(int id, String name, Object value){
        update(QUESTION_OWN_TABLE, id, name, value);
    }
    public static void update(String tablename,int id, String name, Object value){
        Updater.queue.add(new Updater(tablename,id, name, value));
    }

    public static List<StudentAnswer> getAllStudentAnswers() {
        List<StudentAnswer> studentAnswerList = new ArrayList<StudentAnswer>();

        Map<Integer, List<StudentAnswer>> map = new HashMap<Integer, List<StudentAnswer>>();

        Statement studentAnswerStatement = null;
        Statement correctAnswerStatement = null;

        try {
            studentAnswerStatement = ownConn.createStatement();
            correctAnswerStatement = c.createStatement();

            ResultSet result = studentAnswerStatement.executeQuery("select * from " + STUDENT_ANSWER_TABLE +";");

            while (result.next()) {
                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setId(result.getInt(StudentAnswer.ID));
                studentAnswer.setCourse(result.getString(StudentAnswer.COURSE));
                studentAnswer.setChapter(result.getInt(StudentAnswer.CHAPTER));
                studentAnswer.setStudentClass(result.getString(StudentAnswer.STUDENT_CLASS));
                studentAnswer.setStudentNumber(result.getString(StudentAnswer.STUDENT_NUMBER));
                studentAnswer.setStudentName(result.getString(StudentAnswer.STUDENT_NAME));
                studentAnswer.setType(result.getInt(StudentAnswer.TYPE));
                studentAnswer.setStudentAnswer(result.getString(StudentAnswer.STUDENT_ANSWER));

                List<StudentAnswer> stuAnsList = map.get(studentAnswer.getId());
                if (stuAnsList == null) {
                    stuAnsList = new ArrayList<StudentAnswer>();
                    map.put(studentAnswer.getId(), stuAnsList);
                }
                stuAnsList.add(studentAnswer);

                studentAnswerList.add(studentAnswer);
            }

            for (Iterator<StudentAnswer> iterator = studentAnswerList.iterator(); iterator.hasNext(); ) {
                StudentAnswer next =  iterator.next();
                System.out.println(next);
            }


            ResultSet correctAnswer = correctAnswerStatement.executeQuery("select * from " + QUESTION_TABLE + ";");
            while (correctAnswer.next()) {
                List<StudentAnswer> stuAnsL = map.get(correctAnswer.getInt(TableQuestion.ID));
                if(stuAnsL != null) {
                    for (Iterator<StudentAnswer> iterator = stuAnsL.iterator(); iterator.hasNext(); ) {
                        StudentAnswer next =  iterator.next();
                        next.setAnswer(correctAnswer.getString(TableQuestion.ANSWER));
                    }
                }
            }


            for (Iterator<StudentAnswer> iterator = studentAnswerList.iterator(); iterator.hasNext(); ) {
                StudentAnswer next =  iterator.next();
                System.out.println(next);
            }


            //关闭连接和声明
            studentAnswerStatement.close();
            correctAnswerStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentAnswerList;
    }

    public static List<AllStudentScore> getAllStudentScores(String course, String queryAs, String queryAsText) {

        List<AllStudentScore> allStudentScoreList = new ArrayList<AllStudentScore>();
        Statement allStudentScoreStatement = null;

        course = "'" + course + "'";
        queryAsText = "'" + queryAsText + "'";
        String sql = null;
        if (queryAs.equals("班级")) {
            sql = "select * from " + ALL_STUDENT_SCORE_TABLE +
                    " where " + AllStudentScore.COURSE + " = " + course +
                    " and " + AllStudentScore.STUDENT_CLASS + " = " + queryAsText + ";";
        }
        else {
            sql = "select * from " + ALL_STUDENT_SCORE_TABLE +
                    " where " + AllStudentScore.COURSE + " = " + course +
                    " and " + AllStudentScore.STUDENT_NUMBER + " = " + queryAsText + ";";
        }

        try {
            allStudentScoreStatement = ownConn.createStatement();

            System.out.println(sql);

            ResultSet result = allStudentScoreStatement.executeQuery(sql);

            while (result.next()) {
                AllStudentScore allStudentScore = new AllStudentScore();
                allStudentScore.setId(result.getInt(AllStudentScore.ID));
                allStudentScore.setCourse(result.getString(AllStudentScore.COURSE));
                allStudentScore.setChapter(result.getInt(AllStudentScore.CHAPTER));
                allStudentScore.setStudentClass(result.getString(AllStudentScore.STUDENT_CLASS));
                allStudentScore.setStudentNumber(result.getString(AllStudentScore.STUDENT_NUMBER));
                allStudentScore.setStudentName(result.getString(AllStudentScore.STUDENT_NAME));
                allStudentScore.setScore(result.getFloat(AllStudentScore.SCORE));

                allStudentScoreList.add(allStudentScore);

            }

            for (Iterator<AllStudentScore> iterator = allStudentScoreList.iterator(); iterator.hasNext(); ) {
                AllStudentScore next =  iterator.next();
                System.out.println(next);
            }


            //关闭连接和声明
            allStudentScoreStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allStudentScoreList;
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
                            String sqlValue = "";
                            if(u.value instanceof Integer)
                                sqlValue += u.value;
                            else if(u.value instanceof String)
                                sqlValue += "'" + u.value + "'";
                            sql += sqlValue + " where id = " + u.id;
                            if(update(sql) == 0){//需要插入
                                String insertSql = "insert into " + u.tablename + " (" + u.name + ")" +
                                        " values " + "(" + sqlValue + ")";
                                update(insertSql);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }

        public static int  update(String sql){
            Statement sql_statement = null;
            System.out.println(sql);
            try {
                sql_statement = ownConn.createStatement();
                int upRet = sql_statement.executeUpdate(sql);
                return upRet;
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }finally {
                try {
                    sql_statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public Updater(String tablename,int id, String name, Object value) {
            this.tablename = tablename;
            this.id = id;
            this.name = name;
            this.value = value;
        }
    }


    public static List<Score> getAllScore(){
        List<Score> list = new ArrayList<Score>();
        Statement sql_statement = null;
        try {
            sql_statement = ownConn.createStatement();

            ResultSet result = sql_statement.executeQuery("select * from " + SCORE_OWN_TABLE +";");

            while (result.next()) {
                Score score = new Score();
                score.setId(result.getInt(Score.ID));
                score.setCourse(result.getString(Score.COURSE));
                score.setChapter(result.getInt(Score.CHAPTER));
                score.setScore(result.getInt(Score.SCORE));
                list.add(score);
            }
        }catch (SQLException e) {
                e.printStackTrace();
        }finally {
            try {
                sql_statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }


    public static void  updateScore(String course, int chapter, int score){
        Statement sql_statement = null;
        course = "'" + course + "'";
        try {
            sql_statement = ownConn.createStatement();
            String sql =  "update " + SCORE_OWN_TABLE +
                    " set " + Score.SCORE + " = " + score +
                    " where " + Score.COURSE +" = " + course +
                    " and " + Score.CHAPTER + " = " + chapter;
            int upRet = sql_statement.executeUpdate(sql);
            System.out.println(sql);
            if (upRet == 0){
                String insertSql = "insert into " + SCORE_OWN_TABLE +
                        " (" + Score.COURSE + "," + Score.CHAPTER + "," + Score.SCORE + ")" +
                        " values " + "(" + course + "," + chapter + "," + score + ")";
                sql_statement.executeUpdate(insertSql);
                System.out.println(insertSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                sql_statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static User getUser(String name){
        Statement sql_statement = null;
        try {
            sql_statement = c.createStatement();
            String sql = "select password,type,finger from " + USER_TABLE +
            " where name='" + name +"';";
            ResultSet result = sql_statement.executeQuery(sql);
//            System.out.println(sql);
            if (result.next()) {
                User user = new User(name,result.getString("password"),
                        result.getInt("type"),result.getString("finger"));
                return user;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                sql_statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws SQLException {

//        System.out.println(getAllScore());
//        updateQuestion(2,"note","我勒个去啊");

//        System.out.println(getAllQuestion());

        Statement sql_statement = null;
        try {
            sql_statement = c.createStatement();
            boolean bool = sql_statement.execute("" +
                    "CREATE TABLE user (\n" +
                    "  [name] VARCHAR(50) PRIMARY KEY, \n" +
                    "  [password] VARCHAR(50) NOT NULL,  \n" +
                    "  [type] INT);");
            System.out.println(bool);
            //关闭连接和声明
            sql_statement.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
