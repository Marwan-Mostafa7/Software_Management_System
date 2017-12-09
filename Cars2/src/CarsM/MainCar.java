package CarsM;

import CarsM.Login.LoginViewController;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainCar extends Application {
    
    public static Stage MainStage;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        MainStage = primaryStage;
        MainStage.setTitle("Car Management System");
        
        LoginViewController.showLogin();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
