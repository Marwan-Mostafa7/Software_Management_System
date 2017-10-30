/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CarsM.Login;
 import java.sql.*;

/**
 *
 * @author David Wagih
 */
public class sqlDbAdd {
    Connection conn=null;
    public static Connection connectDb(){
    try{
        Class.forName("org.sqlite.JDBC");
        Connection conn=DriverManager.getConnection("jdbc:sqlite:C:\\Users\\David Wagih\\Desktop\\login.sqlite");
        return conn;
    }catch(Exception e){
        System.out.println("Error in database connecction");
        return null;
    }
    
    
    }
}
