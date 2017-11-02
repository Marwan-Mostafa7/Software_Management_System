package connectSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class fetchData {

    private static PreparedStatement pst;
    private static  ResultSet rst;
    private  static Connection conn;

    
    
    public static void main(String[] args){
        
          readData();
          insert_Data();
          remove_Data();
           readData();
    }
    
    private static void readData(){
        
        conn = Connector.connect();
        
        String query = "select barcode from Items";
        
        try {
            pst = conn.prepareStatement(query);
            rst = pst.executeQuery();
            while(rst.next())
            {
                System.out.println(rst.getString("barcode"));
            }
            
            pst.close();
            rst.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(fetchData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void insert_Data() {
        
        System.out.println("Inserting Data");
        
        //String query1 ="insert into Items (barcode,type,name,state,brand,buy_cost,sell_cost,number) values (?,?,?,?,?,?,?,?)";
       
        
        try{
        
        pst = conn.prepareStatement("insert into Items VALUES (?,?,?,?,?,?,?,?)");
       
        pst.setString(1, "80008000GG");
        pst.setString(2, "زجاج");
        pst.setString(3, "زجاج جانبي");
        pst.setString(4, "جديد");
        pst.setString(5, "Vensus");
        pst.setDouble(6, 800.98);
        pst.setDouble(7, 9000.508);
        pst.setInt(8, 64);

           int x  = pst.executeUpdate();
        if( x > 0){
            System.out.println("Done Insertion");
            pst.close();
            rst.close();
        }else{
            System.out.println("Not inserted");
        }
        
         } catch (SQLException ex) {
            Logger.getLogger(fetchData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void remove_Data() {

        String query2 = "delete from Items where type = 'زجاج'";

        try {
            pst = conn.prepareStatement(query2);
            
           int x  = pst.executeUpdate();
            
          if( x > 0){
          
              System.out.println("Done delete");
              pst.close();
        }else{
            System.out.println("Not inserted");
        }
        
            
        } catch (SQLException ex) {
            Logger.getLogger(fetchData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
