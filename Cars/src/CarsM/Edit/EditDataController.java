package CarsM.Edit;

import com.jfoenix.controls.JFXTextField;
import connectSQLite.Connector;
import connectSQLite.fetchData;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class EditDataController implements Initializable {

    Connection conn=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    
     @FXML
    private JFXTextField barcodeTXT;

    @FXML
    private JFXTextField typeTXT;

    @FXML
    private JFXTextField nameTXT;

    @FXML
    private JFXTextField brandTXT;

    @FXML
    private JFXTextField statusTXT;

    @FXML
    private JFXTextField buyTXT;

    @FXML
    private JFXTextField sellTXT;

    @FXML
    private JFXTextField quantityTXT;
    
    @FXML
    private TableView<Input_Data> TableDB;

    @FXML
    private TableColumn<Input_Data, String> batcodeT;

    @FXML
    private TableColumn<Input_Data, String> type;

    @FXML
    private TableColumn<Input_Data, String> name;

    @FXML
    private TableColumn<Input_Data, String> state;

    @FXML
    private TableColumn<Input_Data, String> brand;

    @FXML
    private TableColumn<Input_Data, Double> buy_cost;

    @FXML
    private TableColumn<Input_Data, Double> sell_cost;

    @FXML
    private TableColumn<Input_Data, Integer> number;
    
    
    
    final ObservableList<Input_Data> Inputted = FXCollections.observableArrayList();
    
         @FXML
    void Table_Clicked(MouseEvent event){
        if(TableDB.getSelectionModel().getSelectedItem() != null){
        
            int selectedIndex= TableDB.getSelectionModel().getFocusedIndex();
            
            System.out.println(selectedIndex);
            Input_Data selectedrecord = (Input_Data)TableDB.getItems().get(selectedIndex);
            
            barcodeTXT.setText(selectedrecord.getBarcode());
            typeTXT.setText(selectedrecord.getType());
            nameTXT.setText(selectedrecord.getName());
            statusTXT.setText(selectedrecord.getState());
            brandTXT.setText(selectedrecord.getBrand());
            buyTXT.setText(selectedrecord.getBuyCost().toString());
            sellTXT.setText(selectedrecord.getSellCost().toString());
            quantityTXT.setText(selectedrecord.getNumber().toString());
            
            /*
            barcodeTXT.setText(inp1.getBarcode());
            
            statusTXT.setText(inp1.getState());
            nameTXT.setText(inp1.getName());
            buyTXT.setText(inp1.getBuyCost().toString());
            sellTXT.setText(inp1.getSellCost().toString());
            quantityTXT.setText(inp1.getNumber().toString());        
            */
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
            conn = Connector.connect();
        
        String query = "select * from items ";
        
        try {
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next())
            {
               Inputted.add(new Input_Data(
                       rs.getString("barcode"),
                       rs.getString("name"), 
                       rs.getString("type"),
                       rs.getString("state"),
                       rs.getString("brand"),
                       rs.getInt("buy_cost"),
                       rs.getInt("sell_cost"),
                       rs.getInt("number")));
            }
           
            pst.close();
            rs.close();
          
        } catch (SQLException ex) {
            Logger.getLogger(fetchData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        batcodeT.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        buy_cost.setCellValueFactory(new PropertyValueFactory<>("buyCost"));
        sell_cost.setCellValueFactory(new PropertyValueFactory<>("sellCost"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        
        TableDB.setItems(Inputted);
    }

}
