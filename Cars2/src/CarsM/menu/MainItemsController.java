package CarsM.menu;

import CarsM.Login.LoginViewController;
import CarsM.MainCar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainItemsController implements Initializable {

    public static BorderPane mainPane;
    public static Stage mainStage;
    private static boolean show = true;

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
    private ImageView full_Screen;
    @FXML
    private Label Main_Menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    void fullScreenIcon(MouseEvent event) {
        toggleFullScreen();
    }

    @FXML
    void fullScreenKey(KeyEvent event) {
        mainPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F) {
                toggleFullScreen();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                full_Screen.setVisible(true);
            }
        });
    }

    private void toggleFullScreen() {
        if (!mainStage.fullScreenProperty().get()) {
            mainStage.setFullScreen(true);
            mainStage.setFullScreenExitHint("");
            full_Screen.setVisible(false);
        } else {
            mainStage.setFullScreen(false);
            full_Screen.setVisible(true);
        }
    }

    @FXML
    void hide_menu(MouseEvent event) {
        if (show) {
            mainPane.setRight(null);
        } else {
            mainPane.setRight(All_Btns);
        }
        show = !show;
    }

    @FXML
    public void Clear_center(MouseEvent event) {
        mainPane.setCenter(null);
    }

    @FXML
    public void Addbtn() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Add/AddItemAgent.fxml"));
        mainPane.setCenter(loader.load());
        activeBtn(Add);
    }

    @FXML
    public void Edit_Data() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Edit/EditData.fxml"));
        mainPane.setCenter(loader.load());
        activeBtn(Update);
    }

    @FXML
    public void View_Data() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("View/ViewData.fxml"));
        mainPane.setCenter(loader.load());
        activeBtn(View);
    }

    @FXML
    public void Bill() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Bill/Bill.fxml"));
        mainPane.setCenter(loader.load());
        activeBtn(Bill);
    }

    @FXML
    public void Exit_Prog(ActionEvent event) {
        mainStage.close();
        LoginViewController.showLogin();
    }

    public static void showMenu() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainCar.class.getResource("menu/MainItems.fxml"));
            mainPane = loader.load();

            Scene scene = new Scene(mainPane);

            mainStage = new Stage();
            mainStage.setTitle("Main Menu");
            mainStage.setScene(scene);
            mainStage.setMaximized(true);   // to be full screen
            mainStage.show();

            // Close the login stage
            LoginViewController.close();
        } catch (IOException ex) {
            Logger.getLogger(MainItemsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void activeBtn(JFXButton btn) {
        JFXButton[] btns = {Add, Update, View, Bill, Out};

        for (JFXButton b : btns) {
            b.setStyle("-fx-border-color: #e0920b; -fx-border-width: 4; -fx-background-color: #FF6F3C; -fx-text-fill: #F5F5F5;");
        }
        btn.setStyle("-fx-border-color: #e0920b; -fx-border-width: 4; -fx-background-color: #1a7266; -fx-text-fill: #F5F5F5;");
    }

}
