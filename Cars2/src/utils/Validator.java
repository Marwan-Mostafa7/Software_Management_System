package utils;

import connectSQLite.Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Validator {
    
    /**
     * Connection data
     */
    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;
     
    /**
     * the instance of the class
     */ 
    private static Validator instance;
    
    /**
     * Errors Container
     */
    Map<String, String> errors = new HashMap<>();
    
    /**
     * Get instance of the class[only one shared for all project]
     * 
     * @return Validator
     */
    public static Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }
        instance.errors.clear();
        return instance;
    }
    
    /**
     * Determine if the given string is not empty
     * [EX: required("productName", name_Of_Product.getText(), null)]
     * 
     * @param dataName the name of the data that will be checked
     * @param dataValue the value of the data that will be checked
     * @param errorMessage the optional message that will be stored
     * in the errors container {null to let the message generate itself}
     * @return Validator
     */
    public Validator required(String dataName, String dataValue, String errorMessage) {
        if(errors.containsKey(dataName)) {
            return this;
        } else if (dataValue.length() == 0) {
            String message = (errorMessage != null) ? errorMessage : "لا يمكن ترك " + dataName + " فارغ";
            errors.put(dataName, message);
        }
        return this;
    }
    
    /**
     * Determine if the given string is float number or integer
     * 
     * @param dataName the name of the data that will be checked
     * @param dataValue the value of the data that will be checked
     * @param errorMessage the optional message that will be stored
     * in the errors container {null to let the message generate itself}
     * @return Validator
     */
    public Validator floatNumber(String dataName, String dataValue, String errorMessage) {
        if(errors.containsKey(dataName)) {
            return this;
        } else if (!dataValue.matches("^(\\d+\\.)?\\d+$")) {
            String message = (errorMessage != null) ? errorMessage : dataName + " يجب ان يكون رقم صحيح او عشري";
            errors.put(dataName, message);
        }
        return this;
    }
    
    /**
     * Determine if the given string is integer number
     * 
     * @param dataName the name of the data that will be checked
     * @param dataValue the value of the data that will be checked
     * @param errorMessage the optional message that will be stored
     * in the errors container {null to let the message generate itself}
     * @return Validator
     */
    public Validator intNumber(String dataName, String dataValue, String errorMessage) {
        if(errors.containsKey(dataName)) {
            return this;
        } else if (!dataValue.matches("^(\\d+)$")) {
            String message = (errorMessage != null) ? errorMessage : dataName + " يجب ان يكون رقم صحيح";
            errors.put(dataName, message);
        }
        return this;
    }
    
    /**
     * Determine if the given data doesn't exist in the database
     * 
     * @param dataName the name of the data that will be checked
     * @param dataValue the value of the data that will be checked
     * @param databaseData array [tableName, ColName]
     * @param errorMessage the optional message that will be stored
     * in the errors container {null to let the message generate itself}
     * 
     * @return Validator
     */
    public Validator unique(String dataName, String dataValue, String[] databaseData, String errorMessage) {
        if(errors.containsKey(dataName)) {
            return this;
        } else {
            connection = Connector.getConnection();
            try {
                prepare = connection.prepareStatement("SELECT " + databaseData[1] + " FROM " + databaseData[0] + " WHERE " + databaseData[1] + " = ?");
                prepare.setString(1, dataValue);  // the given data
                result = prepare.executeQuery();

                if (result.next()) { // the given data is already exist
                    String message = (errorMessage != null) ? errorMessage : dataName + " موجود مسبقا في قاعدة البيانات";
                    errors.put(dataName, message);
                }
                result.close();
                prepare.close();
            } catch (SQLException ex) {
                System.out.println("Erorr while checking the data " + dataName + " is unique");
            }
        }
        return this;
    }
    
    
    public Validator phone(String dataName, String dataValue, String errorMessage) {
        if(errors.containsKey(dataName)) {
            return this;
        } else if (!dataValue.matches("^(\\d+)?$")) {
            String message = (errorMessage != null) ? errorMessage : dataName + " ليس رقم هاتف صحيح";
            errors.put(dataName, message);
        }
        return this;
    }
    
    /**
     * Get the errors Containers
     * 
     * @return String Array Contains all the messages
     */
    public String[] getMessages() {
        return errors.values().toArray(new String[0]);
    }
    
    /**
     * Determine if all data are valid
     * 
     * @return boolean
     */
    public boolean pass() {
        return errors.isEmpty();
    }
        
}
