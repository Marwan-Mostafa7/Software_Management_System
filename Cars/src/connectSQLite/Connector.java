package connectSQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {
    
    private static Connection conn;
    
    public static Connection connect(){
        
        try{
        Class.forName("org.sqlite.JDBC");
        
       conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\carnival\\Desktop\\dataBase\\MyCar.sqlite");
       
       System.out.println("Connection established");
       
        return conn;
      
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Error in Connection ");
        }
        
      return null;
    }
    
    public void DisConnect()
        {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    
     public static Connection getConnection(){
         return conn;
     }
     
    
}
