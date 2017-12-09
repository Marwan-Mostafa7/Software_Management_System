package CarsM.View;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import connectSQLite.Connector;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * @todo make the table focus in the first row [at the beginning - after search]
 *       make sure that agent data at the bottom listen to this event
 *       text indents inside fields
 * 
 *      record the state new => جديد
 * 
 */
public class ViewDataController implements Initializable {

    Connection connection = null;
    PreparedStatement statememt = null;
    ResultSet result = null;

    // For Search
    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXComboBox<String> searchType;

    // For Table
    @FXML
    private TableView<Input_Data> Table;

    @FXML
    private TableColumn<Input_Data, String> barcode;

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
    private TableColumn<Input_Data, Integer> quantity;

    @FXML
    private TableColumn<Input_Data, String> time;

    @FXML
    private TableColumn<Input_Data, String> date;
    
    @FXML
    private TableColumn<Input_Data, String> agent;

    final ObservableList<Input_Data> tableData = FXCollections.observableArrayList();

    // For Agent
    @FXML
    private Label agent_name;

    @FXML
    private Label agent_email;

    @FXML
    private Label agent_company;

    @FXML
    private Label agent_phone1;

    @FXML
    private Label agent_phone2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Platform.runLater(() -> {
            Show_data();
        });
      
    }
    
    
    private void Show_data(){
        
          // Generate The Table
        connection = Connector.getConnection();
        String query = "SELECT i.*, a.name AS agent FROM items i LEFT JOIN agents a ON a.id = (SELECT ai.agent_id FROM agent_item ai WHERE ai.barcode = i.barcode)";
        try {
            statememt = connection.prepareStatement(query);
            result = statememt.executeQuery();
            while (result.next()) {
                tableData.add(new Input_Data(
                        result.getString("barcode"),
                        result.getString("name"),
                        result.getString("type"),
                        result.getString("state"),
                        result.getString("brand"),
                        result.getInt("buy_cost"),
                        result.getInt("sell_cost"),
                        result.getInt("number"),
                        result.getString("Time"),
                        result.getString("Date"),
                        result.getString("agent")
                ));
            }

            statememt.close();
            result.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        buy_cost.setCellValueFactory(new PropertyValueFactory<>("buyCost"));
        sell_cost.setCellValueFactory(new PropertyValueFactory<>("sellCost"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("number"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        agent.setCellValueFactory(new PropertyValueFactory<>("agent"));

        Table.setItems(tableData);
        
        // focus on the first row
        Table.getSelectionModel().select(0);
        Table_Clicked(null);

        // Generate the ComboBox<searchType>
        searchType.getItems().removeAll(searchType.getItems());
        searchType.getItems().addAll("الياركود", "اسم المنتج", "اسم المندوب", "نوع المنتج", "الحالة", "الماركة");
        searchType.getSelectionModel().select("الياركود");
        
        // focus on the searchField
        searchField.requestFocus(); //???????????????????????
    }

    @FXML
    void Table_Clicked(MouseEvent event) {
        if (Table.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = Table.getSelectionModel().getFocusedIndex();
            String barcode = Table.getItems().get(selectedIndex).getBarcode();
            setAgentData(barcode);
        }
    }

    @FXML
    void Search() {
        String type = searchType.getValue();
        FilteredList<Input_Data> filteredData = new FilteredList<>(tableData, p -> true);

        // Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, keyword) -> {
            filteredData.setPredicate(tableData -> {
                // If filter text is empty, display all.
                if (keyword == null || keyword.isEmpty()) {
                    return true;
                }

                // search by the selected type
                if ("الياركود".equals(type) && String.valueOf(tableData.getBarcode()).toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                } else if ("اسم المنتج".equals(type) && String.valueOf(tableData.getName()).toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                } else if ("نوع المنتج".equals(type) && String.valueOf(tableData.getType()).toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                } else if ("الحالة".equals(type) && String.valueOf(tableData.getState()).toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                } else if ("الماركة".equals(type) && String.valueOf(tableData.getBrand()).toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                } else if ("اسم المندوب".equals(type) && String.valueOf(tableData.getAgent()).toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Input_Data> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(Table.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        Table.setItems(sortedData);
        
        // focus on the first row
        Table.getSelectionModel().select(0);//???????????????????????
        Table_Clicked(null);
    }

    private void setAgentData(String barCode) {
        connection = Connector.getConnection();

        try {
            statememt = connection.prepareStatement("SELECT * FROM agents WHERE id = (SELECT agent_id FROM agent_item WHERE barcode = ?)");
            statememt.setString(1, barCode);

            result = statememt.executeQuery();
            if (result.next()) {
                agent_name.setText(result.getString("name"));   // getString vs  getNString
                agent_company.setText(result.getString("company"));
                agent_email.setText(result.getString("email"));
                agent_phone1.setText(result.getString("phone1"));
                agent_phone2.setText(result.getString("phone2"));
            } else {
                setDefaultAgentData();
            }
            statememt.close();
            result.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void setDefaultAgentData() {
        agent_name.setText("غير معرف");
        agent_company.setText("غير معرف");
        agent_email.setText("emailaddress@website.com");
        agent_phone1.setText("011111111111");
        agent_phone2.setText("1111111");
    }

}
