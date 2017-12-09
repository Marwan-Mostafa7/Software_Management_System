package CarsM.Bill;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Input_Data implements Cloneable{

    private final SimpleStringProperty barcode;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleStringProperty brand;
    private final SimpleDoubleProperty sellCost;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty totalCost;

    public Input_Data(String barcode, String name, String type, String brand, double sellCost, int quantity, double totalCost) {
        this.barcode = new SimpleStringProperty(barcode);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.brand = new SimpleStringProperty(brand);
        this.sellCost = new SimpleDoubleProperty(sellCost);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalCost = new SimpleDoubleProperty(totalCost);
    }
    
    @Override
    public Object clone() {
        Input_Data item = new Input_Data(
                this.getBarcode(),
                this.getName(),
                this.getType(),
                this.getBrand(),
                this.getSellCost(),
                this.getQuantity(),
                this.getTotalCost()
        );
        return item;
    }

    public String getBarcode() {
        return barcode.get();
    }
    
    public String getName() {
        return name.get();
    }

    public String getType() {
        return type.get();
    }

    public String getBrand() {
        return brand.get();
    }

    public Double getSellCost() {
        return sellCost.get();
    }

    public Integer getQuantity() {
        return quantity.get();
    }
    
    public Double getTotalCost() {
        return totalCost.get();
    }

    public void setBarcode(String v) {
        barcode.set(v);
    }
    
    public void setName(String v) {
        name.set(v);
    }

    public void setType(String v) {
        type.set(v);
    }

    public void setBrand(String v) {
        brand.set(v);
    }

    public void setSellCost(Double v) {
        sellCost.set(v);
    }

    public void setQuantity(Integer v) {
        quantity.set(v);
    }
    
    public void setTotalCost(Double v) {
        totalCost.set(v);
    }

}
