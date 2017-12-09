package CarsM.Bill;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import connectSQLite.Connector;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class BillController implements Initializable{
    @FXML
    JFXButton save;
    
    @FXML
    JFXButton cancel;
    
    @FXML
    Button addToBtn;
    
    @FXML
    Button deleteBtn;
    
    @FXML
    JFXTextField barcodeField;
    
    @FXML
    TextField quantityField;
    
    @FXML
    TextField cashField;
    
    @FXML
    Label itemName;
    
    @FXML
    Label price;
    
    @FXML
    Label remainder;
    
    @FXML
    Label totalPayed;
    
    // For Table
    @FXML
    private TableView<Input_Data> Table;

    @FXML
    private TableColumn<Input_Data, String> barcode;
    
    @FXML
    private TableColumn<Input_Data, String> name;

    @FXML
    private TableColumn<Input_Data, String> type;

    @FXML
    private TableColumn<Input_Data, String> brand;

    @FXML
    private TableColumn<Input_Data, Double> sellCost;

    @FXML
    private TableColumn<Input_Data, Double> quantity;

    @FXML
    private TableColumn<Input_Data, Double> totalCost;

    final ObservableList<Input_Data> tableData = FXCollections.observableArrayList();

    Connection connection = null;
    PreparedStatement statememt = null;
    ResultSet result = null;

    Input_Data currentItem;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        sellCost.setCellValueFactory(new PropertyValueFactory<>("sellCost"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
    }

    @FXML
    void enteredBracode() {
        if(barcodeField.getText().equals("")) {
            return; 
        }

        connection = Connector.getConnection();
        String barcodeValue = barcodeField.getText();
        
        try {
            statememt = connection.prepareStatement("SELECT name, type, brand, sell_cost FROM items WHERE barcode = ?");
            statememt.setString(1, barcodeValue);

            result = statememt.executeQuery();
            if (result.next()) {
                itemName.setText(result.getString("name"));
                price.setText(result.getDouble("sell_cost") + "");
                currentItem = new Input_Data(
                        barcodeValue,
                        itemName.getText(),
                        result.getString("type"),
                        result.getString("brand"),
                        result.getDouble("sell_cost"),
                        1,  // initial value for the quantity
                        result.getDouble("sell_cost")
                );
            }

            statememt.close();
            result.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    void addToTable() {
        if(currentItem == null) {
            return; 
        }
        Input_Data item = (Input_Data) currentItem.clone();
        
        deleteFromTable(barcodeField.getText());
        int quntity = Integer.parseInt(quantityField.getText());
        item.setQuantity(quntity);
        
        double total = item.getSellCost() * quntity;
        item.setTotalCost(total);
        
        tableData.add(item);
        Table.setItems(tableData);
        calculateTotalCost();
        
        quantityField.setText("1");
    }
    
    @FXML
    void removeFromTable() {
        Input_Data selectedItem = Table.getSelectionModel().getSelectedItem();
        Table.getItems().remove(selectedItem);
    }

    private void calculateTotalCost() {
        double total = 0;
        for (Input_Data item : Table.getItems()) {
            total += item.getTotalCost();
        }
        totalPayed.setText(total + "");
    }
    
    @FXML
    void calculateRemainder() {
        double total = Double.parseDouble(totalPayed.getText());
        double cash = Double.parseDouble(cashField.getText());
        remainder.setText(cash - total + "");
    }

    private void deleteFromTable(String barcode) {
        for (Input_Data item : Table.getItems()) {
            if (item.getBarcode().equals(barcode)) {
                Table.getItems().remove(item);
                return;
            }
        }
    }
    
}
