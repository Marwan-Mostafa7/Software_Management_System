package connectSQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private static Connection conn = null;
    
    private Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:MyCar.sqlite");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error in Connection");
        }
    }

    public void DisConnect() {
        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error in disconnect");
        }
    }

    public static Connection getConnection() {
        if (conn == null) {
             new Connector();
        }
        return conn;
    }

}
