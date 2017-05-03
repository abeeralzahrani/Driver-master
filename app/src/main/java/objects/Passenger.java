package objects;

/**
 * Created by Najla AlHazzani on 11/19/2016.
 */

public class Passenger {
    private int ID;
    private String FName;
    private String LName;
    //private String Passwrod;
    private String Email;
    private String Phone;
    private String School;
    private  String absent ;


//**************************************** Constructor(S) ▼▼▼▼▼

    public Passenger(){}//default Constructor

    public Passenger(String FName,String LName,String Phone,String Absent){
        this.FName=FName;
        this.LName=LName;
        this.Phone=Phone;
        this.absent=Absent;
    }

    public Passenger(int id , String fname,String lname ){
        this.ID = id ;
        this.FName=fname;
        this.LName=lname;
    }


    //  **************************************** service method HERE ▼▼▼▼▼.

    //public void EditPassengerInfo(all passenger attributes){}

    public boolean checkPassword(String email){
        // this method will ......!?
        return false;
    }

    public void editProfile(int P_ID){
        //this method will edit passenger profile in the DB.

    }

    public Passenger getPassengerInfo(int R_ID){
        // this method will !!!!!!
        return null;
    }

    public Driver viewDriverInfo(int D_ID){
        // this method will retreive driver's info based on D_ID.
        return null;
    }

    public Passenger getPassengerInfo(){
        // ..........!?!?!?
        return null;
    }

    public boolean deleteAccount(int P_ID){
        //this method will delete passenger account in DB.
        return false;
    }


    //**************************************** SETTERS &&& GETTERS HERE ▼▼▼▼▼

    public String getAbsent() {return absent; }

    public void setAbsent(String absent) { this.absent = absent;}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }
}
