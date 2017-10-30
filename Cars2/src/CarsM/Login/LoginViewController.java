package CarsM.Login;

import CarsM.MainCar;
import static CarsM.MainCar.MainStage;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class LoginViewController implements Initializable {
//
       
    Connection conn=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    
    @FXML
    public JFXTextField username;
    
    @FXML
    public  JFXPasswordField pass;
    
    @FXML
    public JFXTextField temp;

    @FXML
    public ImageView toggle_pass;
    
    @FXML
    public Label nameV;
    
    @FXML
    public Label passV;

    // Pane 600 * 400 ... For menu
    // the Add , Update , View and Bill Pane ... will be of Full Height/Width
    
    public static BorderPane menuPane;
    public static Stage menuStage; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
       passV.setVisible(false);
       nameV.setVisible(false);
       
       temp.setDisable(true);
       temp.setVisible(false);
    }
    
    @FXML 
    public void showPass(){
        
            temp.setDisable(false);
            
            temp.setText(pass.getText());
            pass.setVisible(false);
            temp.setVisible(true);  
    }
    
    @FXML
    public void hidePass(){
            pass.setVisible(true);
            temp.setDisable(true);
            temp.setVisible(false);
    }
    
    @FXML 
    public void check_data(ActionEvent e) throws IOException{
        
    conn=sqlDbAdd.connectDb();
    String query="select * from usrs where email=? and password=?";
    
        int count=0;
    try{
    
        pst=conn.prepareStatement(query);
        pst.setString(1, username.getText());
        pst.setString(2, pass.getText());
        rs=pst.executeQuery();
        
        
        while(rs.next()){
        count++;
        }
        if(count==1){
            System.out.println("Connect successfully and cpunt ->"+count);
            
                 
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainCar.class.getResource("menu/MainItems.fxml"));
            menuPane = loader.load();
            Scene scene = new Scene(menuPane);
            
            menuStage = new Stage();
            // to be full screen
            menuStage.setMaximized(true);
            
            menuStage.setTitle("Main Menu");
            menuStage.setScene(scene);
            menuStage.show();
            // close the login stage
            MainStage.close();

        }else{
            System.out.println("Wrong email or password");
        }
        rs.close();
        pst.close();
    }catch(Exception ex){
    
        System.out.println("error logging in and cpunt ->"+count);
    }
           
        if(!username.getText().equals("a"))    
            nameV.setVisible(true);
        
        if(!pass.getText().equals("s"))    
            passV.setVisible(true);
        
        
            
            
        
        
    }
       
    
}
