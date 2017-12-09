package CarsM.Login;

import CarsM.MainCar;
import CarsM.menu.MainItemsController;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connectSQLite.Connector;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    
    private boolean loggedIn = false;
    
    public static Stage MainStage;
    private static BorderPane MainLayout;    

    @FXML
    public JFXTextField username;

    @FXML
    public JFXPasswordField pass;

    @FXML
    public JFXTextField temp;   // hidden textfield to show password

    @FXML
    public ImageView toggle_pass;   // show password icon

    @FXML
    public Label nameV;

    @FXML
    public Label passV;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // hide the red stars
        passV.setVisible(false);
        nameV.setVisible(false);

        // hide the hidden textfield
        temp.setDisable(true);
        temp.setVisible(false);
    }
    
    public static void showLogin() {
        LoginViewController.MainStage = MainCar.MainStage;
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainCar.class.getResource("Login/loginView.fxml"));
            MainLayout = loader.load();
            Scene scene = new Scene(MainLayout);
            MainStage.setResizable(false);
            MainStage.setScene(scene);
            MainStage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void showPass() {
        // show the hidden textfield
        temp.setText(pass.getText());
        temp.setDisable(false);
        temp.setVisible(true);
        
        // hide the passowrd field
        pass.setVisible(false);
    }

    @FXML
    public void hidePass() {
        // show the password field
        pass.setVisible(true);
        
        // hide the hidden textfield
        temp.setDisable(true);
        temp.setVisible(false);
    }

    @FXML
    public void check_data(ActionEvent e) throws IOException {
        // connect to the database
        conn = Connector.getConnection();
        
        try {
            pst = conn.prepareStatement("SELECT * FROM logindb WHERE username=? AND password=?");
            pst.setString(1, username.getText());
            pst.setString(2, pass.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Logged In");
                loggedIn = true;
                MainItemsController.showMenu();
            } else {
                System.out.println("Wrong username or password");
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            System.out.println("Erorr while Login");
        }

        if (!loggedIn) {
            nameV.setVisible(true);
            passV.setVisible(true);
        }

    }
    
    public static void close() {
        MainStage.close();
    }
    
}
