package CarsM;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainCar extends Application {
    
    public static Stage MainStage;
    public static BorderPane MainLayout;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        MainStage = primaryStage;
        
        MainStage.setTitle("Car Management System");
        MainView();
    }

    @FXML
    public void MainView() throws IOException{
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainCar.class.getResource("Login/loginView.fxml"));
        MainLayout = loader.load();
        Scene scene = new Scene(MainLayout);
        MainStage.setResizable(false);
        MainStage.setScene(scene);
        MainStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
