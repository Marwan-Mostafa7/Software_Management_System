/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CarsM.menu;

import CarsM.Login.LoginViewController;
import CarsM.MainCar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class MainItemsController implements Initializable {

    
    
    @FXML
    private JFXButton Add;
    @FXML
    private JFXButton Update;
    @FXML
    private JFXButton View;
    @FXML
    private JFXButton Bill;
    @FXML
    private JFXButton Out;

    @FXML
    private ImageView back_arrow;

    @FXML
    private JFXHamburger hamburger;
    
     @FXML
     private AnchorPane All_Btns;


     private static int cnt  = 0;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    public void Addbtn() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Add/AddItemClient.fxml"));
        LoginViewController.menuPane.setCenter(loader.load());
    }
    
    @FXML
    void hide_menu(MouseEvent event) {

        cnt++;
        if(cnt%2 ==1)
            LoginViewController.menuPane.setLeft(null);
        
        if(cnt%2 ==0)
            LoginViewController.menuPane.setLeft(All_Btns);
    }

    
 
    
    
}
