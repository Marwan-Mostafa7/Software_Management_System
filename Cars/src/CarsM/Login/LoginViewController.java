package CarsM.Login;

import CarsM.MainCar;
import static CarsM.MainCar.MainStage;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
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
        
        if(pass.getText().equals("s") && username.getText().equals("a")){
            // Go to Main Items
            
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

        }
       
        if(!username.getText().equals("a"))    
            nameV.setVisible(true);
        
        if(!pass.getText().equals("s"))    
            passV.setVisible(true);
        
        
            
            
        
        
    }
       
    
}
