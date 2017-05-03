package objects;

/**
 * Created by Najla AlHazzani on 11/19/2016.
 */

public class Car {

    private String Plate;
    private String Type;
    private String Model;
    private String color;
    private String company;
    //private Photo;
    private int Capacity;
    private int yearOfManufacture;

    //**************************************** Constructor(S) ▼▼▼▼▼
    public Car(){}


    //  **************************************** service method HERE ▼▼▼▼▼


    public void EditCarInfo(Car car){

    }


    //**************************************** SETTERS &&& GETTERS HERE ▼▼▼▼▼

    public String getPlate() {
        return Plate;
    }

    public void setPlate(String plate) {
        Plate = plate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCompany() {return company;}

    public void setCompany(String company) {this.company = company;}

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }
}
