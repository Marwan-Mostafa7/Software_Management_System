package CarsM.Add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import connectSQLite.Connector;
import connectSQLite.fetchData;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

public class AddItemClientController implements Initializable{

    @FXML
    private JFXTextField barcode;

    @FXML
    private JFXComboBox<?> type_Of_product;
    
    @FXML
    private JFXTextField Add_New_Type;
    
    ArrayList<String> Arr_Types;
    ArrayList<String> Arr_brands;

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
     
     
    private static PreparedStatement pst;
    private static  ResultSet rst;
    private  static Connection conn;

     
     // list for Types of Product
     // < نوع المنتج >
     ObservableList Def_TypesA;
     ObservableList Def_TypesBrand;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormatT = new SimpleDateFormat("HH:mm:ss");
        
        //get current date time with Date()

        Date date = new Date();
        Date_label.setText(dateFormat.format(date));    // date
        Time_label.setText(dateFormatT.format(date));   // time

        //Added Part For the toggle group
       // de el Asma2 elly 7atedaf fel DB a david ... lazem ne3mel setUserData el Awel
       // 3ashan ne2dar na5odHa bel Get UserData
       State.getToggles().get(0).setUserData("New");
       State.getToggles().get(1).setUserData("Used");
       State.getToggles().get(2).setUserData("Unknown");


       // Default Values For the Combo Box
       // نوع المنتج
       //Combo box variable name <  type_Of_product >
//       String[] Def_Types = {"وش سلندر","بوجيه","زجاج","دينامو","مارش","تيل فرامل","كشاف امامي","كشاف خلفي","عجلات","باب امامي"
//               ,"باب خلفي","كاميرا خلفية","كاست وسماعات","مروحة","دركسيون باور","شنطة خلفية","فرش جلد","شياله على السقف"
//               ,"الكاربراتير" , "تروس الكرونه","مساعدين","وش كارونه","زيت موبيل","مساعد خلفى","مساعدامامي","+"};
//       
       Arr_Types = new ArrayList<>();
        
       Arr_Types = Get_types_From_DB();
       
       //Arr_Types.addAll(Arrays.asList(Def_Types));
       
       Def_TypesA = FXCollections.observableArrayList(Arr_Types);
       type_Of_product.getItems().addAll(Def_TypesA);
       
       Add_New_Type.setDisable(true);
       Add_New_Type.setVisible(false);
       
       Add_New_Brand.setDisable(true);
       Add_New_Brand.setVisible(false);
       
       //
       // Default Values For the Combo Box
       // ماركة المنتج
       //Combo box variable name <  brand_Of_Product >
       
       String[] brand_Types = {"Endurance","Varroc","BaJaJ","MB","+"};
       Arr_brands = new ArrayList<>();
       Arr_brands.addAll(Arrays.asList(brand_Types));
       
       Def_TypesBrand = FXCollections.observableArrayList(Arr_brands);
       brand_Of_Product.getItems().addAll(Def_TypesBrand);
              
       /*
       ObservableList brand_TypesA = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(brand_Types)));
       brand_Of_Product.getItems().addAll(brand_TypesA);
       */
    }

    
    private ArrayList Get_types_From_DB(){
        
        ArrayList<String> types = new ArrayList<>();
        
        conn = Connector.connect();
        
        String query = "select type from typeDB";
        try {
            pst = conn.prepareStatement(query);
            rst = pst.executeQuery();
            while(rst.next()){
                types.add(rst.getString("type"));
            }
            types.add("+");
            
            return types;
            
        } catch (SQLException ex) {
            Logger.getLogger(AddItemClientController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            
            try {
                pst.close();
                rst.close();
            } catch (SQLException ex) {
                Logger.getLogger(AddItemClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
        
    }

    // To add new Type of Product
    //
    
    String typeOfProduct = "";
    String brandOfProduct="";
    String ToggledData="";
    
    @FXML
    void Done_btn(ActionEvent event){
        
     typeOfProduct  = "";
     brandOfProduct = "";
     ToggledData = "";
        
        //OR from TextField ::  < Add_New_Type >
        // if the Text Field is Active ... then its a new type
        
        // نوع المنتج
        get_type_data();
        
        System.out.println("type of product = " + typeOfProduct);
        
        // ماركة المنتج
        get_brand_data();
        
        System.out.println("brand of product = " + brandOfProduct);
        
        // < حالة المتنج> 
        
        boolean flag = false;
        for (int i = 0; i < State.getToggles().size(); i++) {
            if(State.getToggles().get(i).isSelected())
                flag = true;
        }
        if(flag)
            ToggledData = State.getSelectedToggle().getUserData().toString();
        else
            ToggledData = "";
        
        System.out.println("Toggled = " + ToggledData);
        
        insert_Data();
        
        clear_Inputs();
    }

    
    public void get_type_data(){
        
        if(!Add_New_Type.isDisable())
        {
            
            String Added_Item_type = Add_New_Type.getText();
            
            // Clear all previous data From both comboBox and observable list
            //... and add the new element in the ArrayList (same old arraylist)
            // Then we'll add it all again to the comboBox and observable list
            type_Of_product.getItems().clear();
            Def_TypesA.clear();
            
            // Add the new element to the arrayList
            //Arr_Types.add(Added_Item_type);
           
            Arr_Types.add(0, Added_Item_type);
            // Add the ArrayList in the 
            // Observable List <Def_TypesA>
            Def_TypesA.addAll(Arr_Types);
            // and in the ComboBox 
            type_Of_product.getItems().addAll(Def_TypesA);
            
            //Disable the TextField .... since Addition is done ... and text is written
            // So its ready for another addition in next time
            Add_New_Type.setDisable(true);
            Add_New_Type.setVisible(false);
            
           
            // Enable the comboBox 
            type_Of_product.setDisable(false);
            type_Of_product.setVisible(true);
            
            // Get String from Here
            typeOfProduct = Added_Item_type;
            
         }else   // the + sign is not Selected
            {
            type_Of_product.setDisable(false);
            type_Of_product.setVisible(true);
            // Get the string from Here
            typeOfProduct = (String)type_Of_product.getSelectionModel().getSelectedItem();
        }
    }
    
     public void get_brand_data(){
        
        if(!Add_New_Brand.isDisable())
        {
            
            String Added_brand_type = Add_New_Brand.getText();
            
            // Clear all previous data From both comboBox and observable list
            //... and add the new element in the ArrayList (same old arraylist)
            // Then we'll add it all again to the comboBox and observable list
            brand_Of_Product.getItems().clear();
            Def_TypesBrand.clear();
            
            // Add the new element to the arrayList
            //Arr_Types.add(Added_Item_type);
           
            Arr_brands.add(0, Added_brand_type);
            // Add the ArrayList in the 
            // Observable List <Def_TypesA>
            Def_TypesBrand.addAll(Arr_brands);
            // and in the ComboBox 
            brand_Of_Product.getItems().addAll(Def_TypesBrand);
            
            //Disable the TextField .... since Addition is done ... and text is written
            // So its ready for another addition in next time
            Add_New_Brand.setDisable(true);
            Add_New_Brand.setVisible(false);
            
            // Enable the comboBox 
            brand_Of_Product.setDisable(false);
            brand_Of_Product.setVisible(true);
            
            // Get String from Here
            brandOfProduct = Added_brand_type;
            
         }else   // the + sign is not Selected
            {
            brand_Of_Product.setDisable(false);
            brand_Of_Product.setVisible(true);
            // Get the string from Here
            brandOfProduct = (String)brand_Of_Product.getSelectionModel().getSelectedItem();
        }
    }
    
 
    public void insert_Data() {
        
        String query = "insert into Items VALUES (?,?,?,?,?,?,?,?,?,?)";
       
        try{
            
                conn = Connector.connect();
        
                pst = conn.prepareStatement(query);
                
                pst.setString(1, barcode.getText());
                pst.setString(2, typeOfProduct);
                pst.setString(3, name_Of_Product.getText());
                pst.setString(4, ToggledData);
                pst.setString(5, brandOfProduct);
                pst.setDouble(6, Double.parseDouble(buy_cost.getText()));
                pst.setDouble(7, Double.parseDouble(sell_cost.getText()));
                pst.setInt(8,    Integer.parseInt(number_Of_Product.getText()));
                pst.setString(9, Time_label.getText());
                pst.setString(10,Date_label.getText());

                int x  = pst.executeUpdate();
                
                if( x > 0){
                    System.out.println("Done Insertion");
                    pst.close();
                }else{
                    System.out.println("Not inserted");
                }

         } catch (SQLException ex) {
            Logger.getLogger(fetchData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Called when user chooses a type of Product
    // <نوع المنتج > 
    
    @FXML
    void Add_new_Type(ActionEvent event){
        
          
            if(type_Of_product.getSelectionModel().getSelectedItem() == null){
                System.out.println("Hello world ");
                return;
            }
        
              if(type_Of_product.getSelectionModel().getSelectedItem().equals("+")){
                 
                  // el mafrood ta5od el Text men < Add_New_Type >
                  // wete3mello Add fel DB 3andak ... kanoo3 Gdeed fel Types of Product <نوع المنتج >
                  // lama yedoos 3ala OK fel 2a5er
                  
                  
                  // activate the textField <Add_New_Type>
                  Add_New_Type.setDisable(false);
                  Add_New_Type.setVisible(true);
                  
                  // de-activate the ComboBox <type_Of_product>
                  type_Of_product.setDisable(true);
                  type_Of_product.setVisible(false);
              }else{
                  // madash 3ala "+"
                  
                  // de-activate the Text
                  Add_New_Type.setDisable(true);
                  Add_New_Type.setVisible(false);
                  
                  // activate the ComboBox <type_Of_product>
                  type_Of_product.setDisable(false);
                  type_Of_product.setVisible(true);
              }
    }
    
    @FXML
    void Add_new_brand(ActionEvent event){
        
          
            if(brand_Of_Product.getSelectionModel().getSelectedItem() == null)
                return;
            
        
              if(brand_Of_Product.getSelectionModel().getSelectedItem().equals("+")){
                 
                  // el mafrood ta5od el Text men < Add_New_Type >
                  // wete3mello Add fel DB 3andak ... kanoo3 Gdeed fel Types of Product <نوع المنتج >
                  // lama yedoos 3ala OK fel 2a5er
                  
                  // activate the textField <Add_New_brand>
                  Add_New_Brand.setDisable(false);
                  Add_New_Brand.setVisible(true);
                  
                  // de-activate the ComboBox <brand_Of_Product>
                  brand_Of_Product.setDisable(true);
                  brand_Of_Product.setVisible(false);
                  
                  
              }else{
                  // madash 3ala "+"
                  
                  // de-activate the Text
                  Add_New_Brand.setDisable(true);
                  Add_New_Brand.setVisible(false);
                  
                  // activate the ComboBox <type_Of_product>
                  brand_Of_Product.setDisable(false);
                  brand_Of_Product.setVisible(true);
              }
    }
    
    @FXML
    void showData(ActionEvent event) {

        readData();
    }
    
    private void readData(){
        
        conn = Connector.connect();
        
        String query = "select barcode from Items";
        
        try {
            pst = conn.prepareStatement(query);
            rst = pst.executeQuery();
            while(rst.next())
            {
                System.out.println(rst.getString("barcode"));
            }
            
            pst.close();
            rst.close();
        
        } catch (SQLException ex) {
            Logger.getLogger(fetchData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
     @FXML
    void refresh_page(ActionEvent event) {

        clear_Inputs();
    }
    
    
    
    
    private void clear_Inputs(){
        
        Add_New_Type.clear();
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
        unknown_State.setSelected(false);
        
        brand_Of_Product.setValue(null);
        Add_New_Brand.clear();
        Add_New_Brand.setDisable(true);
        Add_New_Brand.setVisible(false);
        brand_Of_Product.setDisable(false);
        brand_Of_Product.setVisible(true);
        
        buy_cost.clear();
        sell_cost.clear();
        number_Of_Product.clear();
        
        
    }
}
