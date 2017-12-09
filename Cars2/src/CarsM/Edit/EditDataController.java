package CarsM.Edit;

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
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class EditDataController implements Initializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    // Ids for edit item
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

    // Ids for edit agents
    @FXML
    private JFXTextField agent_name;

    @FXML
    private JFXTextField company_name;

    @FXML
    private JFXTextField agent_email;

    @FXML
    private JFXTextField agent_phone1;

    @FXML
    private JFXTextField agent_phone2;

    // Ids for table
    @FXML
    private TableView<Input_Data> TableDB;

    @FXML
    private TableColumn<Input_Data, String> batcodeT;

    @FXML
    private TableColumn<Input_Data, String> name;

    @FXML
    private TableColumn<Input_Data, String> type;

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

// ---------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------
    final ObservableList<Input_Data> Inputted = FXCollections.observableArrayList();

    Input_Data GselectedR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            Show_data();
        });
    }

    private void Show_data() {

        conn = Connector.getConnection();
        String query = "SELECT * FROM items";

        try {
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Inputted.add(new Input_Data(
                        rs.getString("barcode"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("state"),
                        rs.getString("brand"),
                        rs.getInt("buy_cost"),
                        rs.getInt("sell_cost"),
                        rs.getInt("number")
                ));
            }

            pst.close();
            rs.close();

        } catch (SQLException ex) {
            System.out.println(ex);
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

    @FXML
    void Table_Clicked(MouseEvent event) {
        if (TableDB.getSelectionModel().getSelectedItem() != null) {

            int selectedIndex = TableDB.getSelectionModel().getFocusedIndex();

            Input_Data selectedrecord = (Input_Data) TableDB.getItems().get(selectedIndex);

            GselectedR = selectedrecord;
            String barcode = selectedrecord.getBarcode();

            barcodeTXT.setText(barcode);
            typeTXT.setText(selectedrecord.getType());
            nameTXT.setText(selectedrecord.getName());
            statusTXT.setText(selectedrecord.getState());
            brandTXT.setText(selectedrecord.getBrand());
            buyTXT.setText(selectedrecord.getBuyCost().toString());
            sellTXT.setText(selectedrecord.getSellCost().toString());
            quantityTXT.setText(selectedrecord.getNumber().toString());

            getAgentData(barcode);
        }
    }

    /**
     * Get agent data from DB by using the Barcode 
     * and generate the input fields of the agent
     * 
     * @param barcode 
     */
    private void getAgentData(String barcode) {
        conn = Connector.getConnection();

        String query = "SELECT * FROM agents a WHERE a.id = (SELECT agent_id FROM agent_item WHERE barcode = ?)";

        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, barcode);

            rs = pst.executeQuery();
            if (rs.next()) {
                agent_name.setText(rs.getString("name"));
                company_name.setText(rs.getString("company"));
                agent_email.setText(rs.getString("email"));
                agent_phone1.setText(rs.getString("phone1"));
                agent_phone2.setText(rs.getString("phone2"));
            } else {
                clearAgentData();
            }

        } catch (SQLException ex) {
            Logger.getLogger(EditDataController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                rs.close();

            } catch (SQLException ex) {
                Logger.getLogger(EditDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    void Edit_Data() {
        if (GselectedR != null) {
            System.out.println("David And Maro :D"); // O_o XDDDDDD -------- Btal l3b ya 7beby
            String barcode = GselectedR.getBarcode();

            String queryEdit = "update items set "
                    + "type=?,name=?,state=?,brand=?,buy_cost=?,sell_cost=?, number=? where barcode='" + barcode + "'";

            try {
                conn = Connector.getConnection();
                pst = conn.prepareStatement(queryEdit);

                pst.setString(1, typeTXT.getText());
                pst.setString(2, nameTXT.getText());
                pst.setString(3, statusTXT.getText());
                pst.setString(4, brandTXT.getText());
                pst.setDouble(5, Double.parseDouble(buyTXT.getText()));
                pst.setDouble(6, Double.parseDouble(sellTXT.getText()));
                pst.setInt(7, Integer.parseInt(quantityTXT.getText()));

                int x = pst.executeUpdate();

                if (x > 0) {
                    System.out.println("Done Insertion");
                    pst.close();
                } else {
                    System.out.println("Not inserted");
                }
                Inputted.clear();
                Show_data();
            } catch (SQLException ex) {
            System.out.println(ex);

            }
            editAgent(barcode);
            
        }
    }

    /**
     * Edit agent if and only if the agent name was filled
     * 
     * @param barcode 
     */
    private void editAgent(String barcode) {
        if (agent_name.getText() == null) {
            return;
        }
            
        String query = "UPDATE agents SET name = ?, company= ?, email = ?, phone1 = ?, phone2 = ? "
                + "WHERE id = (SELECT agent_id FROM agent_item WHERE barcode = ?)";

        try {
            conn = Connector.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, agent_name.getText());
            pst.setString(2, company_name.getText());
            pst.setString(3, agent_email.getText());
            pst.setString(4, agent_phone1.getText());
            pst.setString(5, agent_phone2.getText());
            pst.setString(6, barcode);

            int x = pst.executeUpdate();

            if (x > 0) {
                System.out.println("Done Insertion agent");
                pst.close();
            } else {
                System.out.println("Not inserted agent");
            }
            Inputted.clear();
            initialize(null, null);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }

    @FXML
    void Auto_Edit(KeyEvent event) {

        if (barcodeTXT.getText() != null) {
            conn = Connector.getConnection();

            String query2 = "select * from items where barcode='" + barcodeTXT.getText() + "'";

            try {
                pst = conn.prepareStatement(query2);

                rs = pst.executeQuery();

                if (!rs.next()) {

                    clear_Inputs();

                } else {

                    Double buyCost = rs.getDouble("buy_cost");
                    Double sellCost = rs.getDouble("sell_cost");
                    Integer num = rs.getInt("number");

                    nameTXT.setText(rs.getString("name"));
                    typeTXT.setText(rs.getString("type"));
                    statusTXT.setText(rs.getString("state"));
                    brandTXT.setText(rs.getString("brand"));
                    buyTXT.setText(buyCost.toString());
                    sellTXT.setText(sellCost.toString());
                    quantityTXT.setText(num.toString());
                    getAgentData(barcodeTXT.getText());
                }

                pst.close();
                rs.close();

            } catch (SQLException ex) {
            System.out.println(ex);
            }
            if (barcodeTXT.getText().equals("")) {
                clear_Inputs();
            }
        }
    }

    private void clear_Inputs() {
        //barcodeTXT.clear();
        typeTXT.setText("");
        nameTXT.clear();

        brandTXT.setText("");
        buyTXT.clear();
        statusTXT.clear();
        sellTXT.clear();
        quantityTXT.clear();
        
        clearAgentData();

    }
    
    private void clearAgentData() {
        agent_name.clear();
        company_name.clear();
        agent_email.clear();
        agent_phone1.clear();
        agent_phone2.clear();
    }

    /*    
    private void clear_InputsNotBarcode() {

       Add_New_Type.clear();
        type_Of_product.setValue(null);
        name_Of_Product.clear();

        State.getSelectedToggle().setSelected(false);
        New_state.setSelected(false);
        used_State.setSelected(false);
        unknown_State.setSelected(false);

        brand_Of_Product.setValue(null);
        buy_cost.clear();
        sell_cost.clear();
        number_Of_Product.clear();


    }

     */

}
