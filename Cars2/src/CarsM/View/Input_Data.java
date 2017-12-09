package CarsM.View;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Input_Data {

    private final SimpleStringProperty barcode;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleStringProperty state;
    private final SimpleStringProperty brand;
    private final SimpleDoubleProperty buyCost;
    private final SimpleDoubleProperty sellCost;
    private final SimpleIntegerProperty number;
    private final SimpleStringProperty time;
    private final SimpleStringProperty date;
    private final SimpleStringProperty agent;

    public Input_Data(String barcode, String name, String type, String state, String brand, double buycost, double sellcost, int n, String time, String date, String agent) {
        this.barcode = new SimpleStringProperty(barcode);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.state = new SimpleStringProperty(state);
        this.brand = new SimpleStringProperty(brand);
        this.buyCost = new SimpleDoubleProperty(buycost);
        this.sellCost = new SimpleDoubleProperty(sellcost);
        this.number = new SimpleIntegerProperty(n);
        this.time = new SimpleStringProperty(time);
        this.date = new SimpleStringProperty(date);
        this.agent = new SimpleStringProperty(agent);
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

    public String getState() {
        return state.get();
    }

    public String getBrand() {
        return brand.get();
    }

    public Double getBuyCost() {
        return buyCost.get();
    }

    public Double getSellCost() {
        return sellCost.get();
    }

    public Integer getNumber() {
        return number.get();
    }
    
    public String getTime() {
        return time.get();
    }
    
    public String getDate() {
        return date.get();
    }
    
    public String getAgent() {
        return agent.get();
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

    public void setState(String v) {
        state.set(v);
    }

    public void setBrand(String v) {
        brand.set(v);
    }

    public void setBuyCost(Double v) {
        buyCost.set(v);
    }

    public void setSellCost(Double v) {
        sellCost.set(v);
    }

    public void setNumber(Integer v) {
        number.set(v);
    }
    
    public void setTime(String v) {
        time.set(v);
    }
    
    public void setDate(String v) {
        date.set(v);
    }
    
    public void setAgent(String v) {
        agent.set(v);
    }

}
