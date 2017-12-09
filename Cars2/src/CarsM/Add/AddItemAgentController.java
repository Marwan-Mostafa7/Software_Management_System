package CarsM.Add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import connectSQLite.Connector;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javax.swing.JOptionPane;
import utils.Validator;

public class AddItemAgentController implements Initializable {

    @FXML
    private JFXTextField barcode;

    @FXML
    private JFXComboBox<?> type_Of_product;

    @FXML
    private JFXTextField Add_New_Type;

    @FXML
    private JFXTextField name_Of_Product;

    @FXML
    private ToggleGroup State;

    @FXML
    private JFXRadioButton New_state;

    @FXML
    private JFXRadioButton used_State;

    @FXML
    private JFXRadioButton unknown_State;

    @FXML
    private JFXComboBox<?> brand_Of_Product;

    @FXML
    private JFXTextField Add_New_Brand;

    @FXML
    private JFXTextField buy_cost;

    @FXML
    private JFXTextField sell_cost;

    @FXML
    private JFXTextField number_Of_Product;
    
    @FXML
    private Label Date_label;

    @FXML
    private Label Time_label;

    @FXML
    private JFXButton cancel_Btn;
    
    // Agent data
    @FXML
    private JFXTextField agent_name;
    
    @FXML
    private JFXTextField agent_phone1;
    
    @FXML
    private JFXTextField agent_phone2;
    
    @FXML
    private JFXTextField agent_email;
    
    @FXML
    private JFXTextField agent_company;
    
    @FXML
    private JFXComboBox<?> agent_selection;
    

    private PreparedStatement pst;
    private ResultSet rst;
    private Connection conn;

    // List for Types|Brands of Product
    private ObservableList Def_TypesA;
    private ObservableList Def_TypesBrand;

    private ArrayList<String> Arr_Types;
    private ArrayList<String> Arr_brands;
    
    // Errors Container
    private String[] errors;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->
        {
            Show_data();
        });
    }
    
    private void Show_data(){
        // Get current date|time & Generate [Date|Time label]
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormatT = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Date_label.setText(dateFormat.format(date));    // date
        Time_label.setText(dateFormatT.format(date));   // time

        // Generate [the state of the item]
        State.getToggles().get(0).setUserData("New");
        State.getToggles().get(1).setUserData("Used");
        State.getToggles().get(2).setUserData("Unknown");
        unknown_State.setSelected(true);

        // Get item types & generate ComboBox[type_Of_product]
        Arr_Types = new ArrayList<>();
        Arr_Types = Get_types_From_DB();
        Def_TypesA = FXCollections.observableArrayList(Arr_Types);
        type_Of_product.getItems().addAll(Def_TypesA);

        // Get item brands & generate ComboBox[brand_Of_Product]
        Arr_brands = new ArrayList<>();
        Arr_brands = Get_brands_From_DB();
        Def_TypesBrand = FXCollections.observableArrayList(Arr_brands);
        brand_Of_Product.getItems().addAll(Def_TypesBrand);

        // Disable ComboBox[brand|type_Of_product]
        Add_New_Type.setDisable(true);
        Add_New_Type.setVisible(false);
        Add_New_Brand.setDisable(true);
        Add_New_Brand.setVisible(false);
    }

    private ArrayList Get_types_From_DB() {
        conn = Connector.getConnection();
        ArrayList<String> types = new ArrayList<>();

        try {
            pst = conn.prepareStatement("SELECT type FROM typedb");
            rst = pst.executeQuery();
            while (rst.next()) {
                types.add(rst.getString("type"));
            }
            types.add("+");

            pst.close();
            rst.close();
            
            return types;
        } catch (SQLException ex) {
            System.out.println("CarsM.Add.AddItemAgentController.Get_types_From_DB()");
        }
        return null;
    }

    private ArrayList Get_brands_From_DB() {
        conn = Connector.getConnection();
        ArrayList<String> brands = new ArrayList<>();

        try {
            pst = conn.prepareStatement("SELECT brand FROM branddb");
            rst = pst.executeQuery();
            while (rst.next()) {
                brands.add(rst.getString("brand"));
            }
            brands.add("+");

            pst.close();
            rst.close();
            return brands;
        } catch (SQLException ex) {
            System.out.println("CarsM.Add.AddItemAgentController.Get_types_From_DB()");
        }
        return null;
    }    

    @FXML
    void Done_btn(ActionEvent event) {
        boolean agent = false;
        
        // Get validator instance
        Validator validator = Validator.getInstance();
        
        // Validate the given data
        validator.required("اسم المنتج", name_Of_Product.getText(), null)
                 .unique("اسم المنتج", name_Of_Product.getText(), new String[] {"items", "name"}, null)
                 .floatNumber("سعر الشراء", buy_cost.getText(), "سعر الشراء يابابا سعر الشراء يا حبيبي")
                 .floatNumber("سعر البيع", sell_cost.getText(), null)
                 .intNumber("عدد المنتج", number_Of_Product.getText(), null);
        
        // Check if all data are valid & return the result
        if (!validator.pass()) {
            errors = validator.getMessages();
            
            JOptionPane.showMessageDialog(null , "Enter Values");
            
            for (String error : errors) {
                System.out.println(error);
            }
            return;
        }
        
        // Determine if the user want to store the agent information
        if(!agent_name.getText().equals("")) {
            // Get validator instance
            validator = Validator.getInstance();
            
            // Validate the given data
            validator.required("اسم العميل", agent_name.getText(), null)
                     .unique("اسم العميل", agent_name.getText(), new String[] {"agents", "name"}, null)
                     .phone("رقم العميل1", agent_phone1.getText(), null)
                     .phone("رقم العميل2", agent_phone2.getText(), null);
            
            
            // Check if all data are valid & return the result
            if (!validator.pass()) {
                errors = validator.getMessages();
                for (String error : errors) {
                    System.out.println(error);
                }
                return;
            }
            
            agent = true;
            insertIntoAgents();
        }
        
        // Insert the data
        insert_Data();
        clear_Inputs(agent);
    }

    public String get_type_data() {
        String type;
        if (type_Of_product.getSelectionModel().getSelectedItem().equals("+")) {    // the + sign is Selected
            type = Add_New_Type.getText();
            insertIntoTypes(type);
        } else {    // the + sign is not Selected
            type = type_Of_product.getSelectionModel().getSelectedItem().toString();
        }
        return type;
    }

    public String get_brand_data() {
        String brand;
        
        if (!Add_New_Brand.isDisable()) {   // the + sign is Selected
            brand = Add_New_Brand.getText();
            insertIntoBrands(brand);
        } else {    // the + sign is not Selected
            brand = (String) brand_Of_Product.getSelectionModel().getSelectedItem();
        }
        return brand;
    }
    
    private void insertIntoTypes(String type) {
        try {
            conn = Connector.getConnection();
            pst = conn.prepareStatement("insert INTO typedb VALUES (?)");
            pst.setString(1, type);
            
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            System.out.println("CarsM.Add.AddItemAgentController.insertIntoTypes()");
        }
    }
    
    private void insertIntoBrands(String brand) {
        try {
            conn = Connector.getConnection();
            pst = conn.prepareStatement("insert INTO branddb VALUES (?)");
            pst.setString(1, brand);
            
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            System.out.println("CarsM.Add.AddItemAgentController.insertIntoBrands()");
        }
    }

    public void insert_Data() {
        String query = "insert into Items VALUES (?,?,?,?,?,?,?,?,?,?)";
        
        // Get type|brand product
        String typeOfProduct = get_type_data();
        String brandOfProduct = get_brand_data();
        try {
            conn = Connector.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, barcode.getText());
            pst.setString(2, typeOfProduct);
            pst.setString(3, name_Of_Product.getText());
            pst.setString(4, State.getSelectedToggle().getUserData().toString());
            pst.setString(5, brandOfProduct);
            pst.setDouble(6, Double.parseDouble(buy_cost.getText()));
            pst.setDouble(7, Double.parseDouble(sell_cost.getText()));
            pst.setInt(8, Integer.parseInt(number_Of_Product.getText()));
            pst.setString(9, Time_label.getText());
            pst.setString(10, Date_label.getText());

            int x = pst.executeUpdate();
            if (x > 0) {
                System.out.println("Done Insertion into items");
                pst.close();
            } else {
                System.out.println("Not inserted into items");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    private void insertIntoAgents() {
        try {
            conn = Connector.getConnection();
            pst = conn.prepareStatement("INSERT INTO agents (name, company, phone1, phone2, email) VALUES (?, ?, ?, ?, ?)");

            pst.setString(1, agent_name.getText());
            pst.setString(2, agent_company.getText());
            pst.setString(3, agent_phone1.getText());
            pst.setString(4, agent_phone2.getText());
            pst.setString(5, agent_email.getText());

            int x = pst.executeUpdate();
            if (x > 0) {
                System.out.println("Done Insertion into agents");
                int agent_id = (pst.getGeneratedKeys()).getInt(1);
                System.out.println(agent_id);
                pst.close();
                insertIntoAgentItem(agent_id);
            } else {
                System.out.println("Not inserted into agents");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    private void insertIntoAgentItem(int agent_id) {
        try {
            conn = Connector.getConnection();
            pst = conn.prepareStatement("INSERT INTO agent_item (agent_id, barcode) VALUES (?, ?)");

            pst.setInt(1, agent_id);
            pst.setString(2, barcode.getText());

            int x = pst.executeUpdate();
            if (x > 0) {
                System.out.println("Done Insertion into agent_item");
                pst.close();
            } else {
                System.out.println("Not inserted into agent_item");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    // Called when User chooses a type of Product <نوع المنتج >
    @FXML
    void Add_new_Type(ActionEvent event) {
        if (type_Of_product.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        
        // If user want to add new type || If there is no types
        if (type_Of_product.getSelectionModel().getSelectedItem().equals("+")) {
            // Activate the textField <Add_New_Type>
            Add_New_Type.setDisable(false);
            Add_New_Type.setVisible(true);
            // De-activate the ComboBox <type_Of_product>
            type_Of_product.setDisable(true);
            type_Of_product.setVisible(false);
        } else {
            // De-activate the textField <Add_New_Type>
            Add_New_Type.setDisable(true);
            Add_New_Type.setVisible(false);

            // Activate the ComboBox <type_Of_product>
            type_Of_product.setDisable(false);
            type_Of_product.setVisible(true);
        }
    }

    // Called when User chooses a brand of Product <ماركة المنتج >
    @FXML
    void Add_new_brand(ActionEvent event) {
        if (brand_Of_Product.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        
        // If user want to add new brand || If there is no brands
        if (brand_Of_Product.getSelectionModel().getSelectedItem().equals("+")) {
            // Activate the textField <Add_New_Brand>
            Add_New_Brand.setDisable(false);
            Add_New_Brand.setVisible(true);

            // De-activate the ComboBox <brand_Of_Product>
            brand_Of_Product.setDisable(true);
            brand_Of_Product.setVisible(false);
        } else {
            // De-activate the textField <Add_New_Brand>
            Add_New_Brand.setDisable(true);
            Add_New_Brand.setVisible(false);

            // Activate the ComboBox <brand_Of_Product>
            brand_Of_Product.setDisable(false);
            brand_Of_Product.setVisible(true);
        }
    }

    @FXML
    void showData(ActionEvent event) {
        readData();
    }

    private void readData() {

        conn = Connector.getConnection();

        String query = "select barcode from Items";

        try {
            pst = conn.prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                System.out.println(rst.getString("barcode"));
            }

            pst.close();
            rst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    // When user click in cancel Btn
    @FXML
    void refresh_page(ActionEvent event) {
        clear_Inputs(false);
    }

    private void clear_Inputs(boolean agent) {
        barcode.clear();
        
        type_Of_product.setValue(null);
        Add_New_Type.clear();
        Add_New_Type.setDisable(true);
        Add_New_Type.setVisible(false);
        type_Of_product.setDisable(false);
        type_Of_product.setVisible(true);

        name_Of_Product.clear();

        New_state.setSelected(false);
        used_State.setSelected(false);
        unknown_State.setSelected(true);

        brand_Of_Product.setValue(null);
        Add_New_Brand.clear();
        Add_New_Brand.setDisable(true);
        Add_New_Brand.setVisible(false);
        brand_Of_Product.setDisable(false);
        brand_Of_Product.setVisible(true);

        buy_cost.clear();
        sell_cost.clear();
        number_Of_Product.clear();
        
        Arr_brands = Get_brands_From_DB();  // Generate the new Arraylist of brands
        brand_Of_Product.getItems().clear();    // Clear the ComboBox
        Def_TypesBrand.clear();
        // ReGenerate the ComboBox
        Def_TypesBrand.addAll(Arr_brands);
        brand_Of_Product.getItems().addAll(Def_TypesBrand);
        
        Arr_Types = Get_types_From_DB();  // Generate the new Arraylist of Types
        type_Of_product.getItems().clear();    // Clear the ComboBox
        Def_TypesA.clear();
        // ReGenerate the ComboBox
        Def_TypesA.addAll(Arr_Types);
        type_Of_product.getItems().addAll(Def_TypesA);
        
        
        if (agent) {
            agent_name.clear();
            agent_company.clear();
            agent_phone1.clear();
            agent_phone2.clear();
            agent_email.clear();
        }
    }
}
