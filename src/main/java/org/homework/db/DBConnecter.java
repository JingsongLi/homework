package org.homework.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.homework.utils.Utils.*;

/**
 * Created by hasee on 2015/5/5.
 */
public class DBConnecter {

    public static void main(String[] args) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + getPath("main.db"));
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        ResultSet tableSet = c.getMetaData().getTables(null, "%", "%", new String[]{"TABLE"});

    }
}
