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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    @FXML
    private Label Main_Menu;

    private static int cnt = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void hide_menu(MouseEvent event) {

        cnt++;
        if (cnt % 2 == 1) {
            LoginViewController.menuPane.setRight(null);
            Main_Menu.setDisable(true);
            Main_Menu.setVisible(false);
        }

        if (cnt % 2 == 0) {
            LoginViewController.menuPane.setRight(All_Btns);
            Main_Menu.setDisable(false);
            Main_Menu.setVisible(true);
        }
    }

    @FXML
    public void Exit_Prog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Login/loginView.fxml"));
        MainCar.MainLayout = (loader.load());
        Scene scene = new Scene(MainCar.MainLayout);
        MainCar.MainStage.setScene(scene);
        LoginViewController.menuStage.close();
        MainCar.MainStage.show();
    }

    @FXML
    public void Clear_center(MouseEvent event) {
        LoginViewController.menuPane.setCenter(null);
    }

    @FXML
    public void Addbtn() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Add/AddItemClient.fxml"));

        LoginViewController.menuPane.setCenter(loader.load());
    }

    @FXML
    public void Edit_Data(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Edit/EditData.fxml"));
        LoginViewController.menuPane.setCenter(loader.load());
    }

    
    
    
}
