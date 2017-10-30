package CarsM.Add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

public class AddItemClientController implements Initializable{

    Connection conn=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    
    @FXML
    private JFXTextField barcode;

    @FXML
    private JFXComboBox<?> type_Of_product;
    
    @FXML
    private JFXTextField Add_New_Type;
    
    ArrayList<String> Arr_Types;

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
     
     // list for Types of Product
     // < نوع المنتج >
     ObservableList Def_TypesA ;

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
       String[] Def_Types = {"وش سلندر","بوجيه","زجاج","دينامو","مارش","تيل فرامل","كشاف امامي","كشاف خلفي","عجلات","باب امامي"
               ,"باب خلفي","كاميرا خلفية","كاست وسماعات","مروحة","دركسيون باور","شنطة خلفية","فرش جلد","شياله على السقف"
               ,"الكاربراتير" , "تروس الكرونه","مساعدين","وش كارونه","زيت موبيل","مساعد خلفى","مساعدامامي","+"};
       
       Arr_Types = new ArrayList<>();
       Arr_Types.addAll(Arrays.asList(Def_Types));
       
       Def_TypesA = FXCollections.observableArrayList(Arr_Types);
       type_Of_product.getItems().addAll(Def_TypesA);
       
       Add_New_Type.setDisable(true);
       Add_New_Type.setVisible(false);
       
       //
       // Default Values For the Combo Box
       // ماركة المنتج
       //Combo box variable name <  brand_Of_Product >
       String[] brand_Types = {"Endurance","Varroc","BaJaJ","MB","+"};
       
       ObservableList brand_TypesA = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(brand_Types)));
       brand_Of_Product.getItems().addAll(brand_TypesA);
       
    }


    // To add new Type of Product
    //
    String toggledData;
    String typeOfproduct;
    @FXML
    void Done_btn(ActionEvent event) {
        
        // < حالة المتنج> 
        
        
        
         for (int i = 0; i < State.getToggles().size(); i++) {
            if(State.getToggles().get(i).isSelected())
                toggledData=State.getToggles().get(i).getUserData().toString();
        }
        
        //Check if an already existing item
        //OR from TextField ::  < Add_New_Type >
        
        // if the Text Field is Active ... then its a new type
        if(!Add_New_Type.isDisable())
        {
            
            String Added_Item_type = Add_New_Type.getText();
            typeOfproduct=Add_New_Type.getText();
            
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
            
        }else   // the + sign is not Selected
        {
            
            type_Of_product.setDisable(false);
            type_Of_product.setVisible(true);
            typeOfproduct=(String)type_Of_product.getSelectionModel().getSelectedItem();
        }
        
        
        clear_Inputs();

        // Add to Database
        
          conn=CarsM.Login.sqlDbAdd.connectDb();
    String query="insert into items (barcode,type_product,name_product,state_product,"
            + "sell_product,buy_product,quantity_product)"
            + " values (?,?,?,?,?,?,?,?)";
    
        //int count=0;
    try{
    //Object selectedItem=type_Of_product.getSelectionModel();
    //String status=(String)(type_Of_product.getSelectionModel().getSelectedItem());
        //System.out.println(status);
    //String selectedItemStr = selectedItem.toString();
        pst=conn.prepareStatement(query);
        pst.setString(1, barcode.getText());
        pst.setString(2, typeOfproduct);
        pst.setString(3, name_Of_Product.getText());
        pst.setString(4, toggledData);
        //pst.setString(5, barcode.getText());
        pst.setString(5, buy_cost.getText());
        pst.setString(6, sell_cost.getText());
        pst.setString(7, number_Of_Product.getText());
            
        int res1=pst.executeUpdate();

        
        if(res1>0){
            System.out.println("Inserted Suucessfully ");
           

        }else{
            System.out.println("Wrong inserting");
        }
        
        pst.close();
    }catch(Exception ex){
    
        System.out.println("error in DB ");
    }
    
        
    }

    
    // Called when user chooses a type of Product
    // <نوع المنتج > 
    
    @FXML
    void Add_new_Type(ActionEvent event){
        
        int n = type_Of_product.getItems().size();
          
            if(type_Of_product.getSelectionModel().getSelectedItem() == null){
                System.out.println("Hello world ");
                return;
            }
        
              if(type_Of_product.getSelectionModel().getSelectedItem().equals("+")){
                 
                  // el mafrood ta5od el Text men < Add_New_Type >
                  // wete3mello Add fel DB 3andak ... kanoo3 Gdeed fel Types of Product <نوع المنتج >
                  // lama yedoos 3ala OK fel 2a5er
                  
                  System.out.println("the + sign");
                  // activate the textField <Add_New_Type>
                  Add_New_Type.setDisable(false);
                  Add_New_Type.setVisible(true);
                  // de-activate the ComboBox <type_Of_product>
                  type_Of_product.setDisable(true);
                  type_Of_product.setVisible(false);
                  
                  System.out.println("PartB if");
                  
              }else{
                  
                  
                  // de-activate the Text
                  Add_New_Type.setDisable(true);
                  Add_New_Type.setVisible(false);
                  
                  // activate the ComboBox <type_Of_product>
                  type_Of_product.setDisable(false);
                  type_Of_product.setVisible(true);
                  
                  
                  System.out.println("PartB else");
              }
    }
    
    private void clear_Inputs(){
        
        Add_New_Type.clear();
        barcode.clear();
        type_Of_product.setValue(null);
        name_Of_Product.clear();

        New_state.setSelected(false);
        used_State.setSelected(false);
        unknown_State.setSelected(false);
        
        brand_Of_Product.setValue(null);
        buy_cost.clear();
        sell_cost.clear();
        number_Of_Product.clear();
        
        
    }
}
