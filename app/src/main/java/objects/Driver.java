package objects;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

/**
 * Created by Najla AlHazzani on 11/19/2016.
 */

public class Driver{

    //private Photo;
    private int ID;
    private String Passwrod;
    private String Email;
    private String FName;
    private String LName;
    private String Phone;
    private String Company;
    //private int DayPrice;
    //private int MonthPrice;
    private String License;
    private String Nationality;
    private char FemaleCompanion;
    private String ID_Iqama;
    private String age;
    private double Rating;
    private char confirmed;

    private Car car;

    //**************************************** Constructor(S) ▼▼▼▼▼


    public Driver (){}//default constructor.

    public Driver(int ID, String passwrod, String email, String FName, String LName, String phone, String company, String license, String nationality, char femaleCompanion, String ID_Iqama, String age, double rating, char confirmed, Car car) {
        this.ID = ID;
        //Passwrod = passwrod;
        Email = email;
        this.FName = FName;
        this.LName = LName;
        Phone = phone;
        Company = company;
        //DayPrice = dayPrice;
        //MonthPrice = monthPrice;
        License = license;
        Nationality = nationality;
        FemaleCompanion = femaleCompanion;
        this.ID_Iqama = ID_Iqama;
        this.age = age;
        Rating = rating;
        this.confirmed = confirmed;
        this.car = car;
    }

    //*************************************** service method HERE ▼▼▼▼▼


    public void EditDriverInfo(Driver d)
    {
        // this method will assign a new Driver to the d object.
    }

    public boolean checkPassword(String email){
        // this method will ......!?
        return false;
    }

    public void editProfile(int D_ID)
    {
        // this method will ......!?

    }

    public boolean setCarInfo(Car c)
    {
        // this method will take a car and assign it to be the driver's car.
        return false;
    }

    /*public boolean setDriverInfo(all driver attributes){
    * //seems like the constructor and like editDriverInfo()
    * }
    * */

    public void listPassengers(){
        /* this method will list the passengers who registered with the driver
        * i thik it should return a list of passengers .
        * i think the timeslot should be provided !!
        * */

    }

    /*public Comment viewComment(){
      // this method will display comments !!

        return null;
    }
    */
/*
* these two overloaded methods are used to retrieve driver's data and car data from DB , and assign it to MyApp.driver_from_DB.
* */
    public String getDriverInfoFromDB(int D_ID, Activity a){
        //this method will retreive driver information based on the given ID, from DB and assign it's value to MyApp.driver_from_DB.
        String res= getJSON("ID", D_ID+"",a);
        return getJSON("ID", D_ID+"",a);
        //set MyApp.driver_from_DB;
    }
    public String getDriverInfoFromDB(String email,Activity a){
        //this method will retreive driver information based on the given Email, from DB and assign it's value to MyApp.driver_from_DB.
        String res= getJSON("Email", email,a);
   return res;
        //set MyApp.driver_from_DB;
    }

    public void rate(int value){
        // this method will set a rating for certain driver.

    }

    /*public void assignComment (Comment c){
       this method will assign a comment to the database.

    }*/

    public boolean checkTrackingAllowed(){

        return false;
    }

    public Location trackDriver(int D_ID){

        return null;
    }

    public boolean Validate(int D_ID){
        // this method will ......!?
        return false;
    }

    public Car getCarInfo(){
        //this method will retrieve the data of the car that assigned to a driver
        // i think that the driver id should be provided, no need for this, it should be called by MyApp.driver_from_session
        return car;
    }

    /*public Car EditCarInfo( all car attributes){

    }
*/

    public boolean ConfirmRequest(int request_ID){

        return false;
    }

    public boolean deleteAccount(int D_ID){

        return false;
    }

    private String getJSON(String type, String value,Activity a) {

        ProgressDialog loading;
        // final Driver[] driv = new Driver[1];
        class GetJSON extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;
            private Activity activity;
            public GetJSON(Activity a){
                super();
                activity=a;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(getActivity(), "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {
                //super.doInBackground(params);
                String uri="http://wassilni.com/db/GetDriv.php";

                BufferedReader bufferedReader = null;
                try {
                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ in backgroundTask 11111111");
                    URL url = new URL(uri);
                    HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    //[0]type  [1]value
                    String data;
                    if(params[0].equalsIgnoreCase("ID"))
                        data = URLEncoder.encode("D_ID","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8");
                    else //if (params[0].equalsIgnoreCase("email"))
                        data = URLEncoder.encode("D_Email","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8");
                    //Log.d("F1",data);
                    System.out.println("in backgroundTask printing data********************************************"+data);
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();
                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ in backgroundTask 222222");
                    bufferedReader = new BufferedReader(new InputStreamReader( httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    InputStream Is=httpURLConnection.getInputStream();
                    Is.close();
                    httpURLConnection.disconnect();
                    String result= sb.toString().trim();
                    System.out.println(result);
                    parseData(result);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ in backgroundTask after parsing data");
                    return result;

                }catch(IOException e){
                    e.printStackTrace();
                    return null;}
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //parseData(s);
                System.out.println("************************************* OnPostExecute Finished now!");
            }
        }
        GetJSON gj = new GetJSON(a);
        String driverFlag="failed";
        //System.out.println("^^^^^^^^^^^^^^^^^"+driverFlag);
        try {
            driverFlag=gj.execute(type,value).get();
            return driverFlag;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return driverFlag;//*/
        //return " O ";
    }//end getJson class ***************************************************************************************

    private void parseData(String s){//was showData()
// check if d != null
        Driver d = new Driver();
        try {

            JSONArray auser= null;
            JSONObject j = new JSONObject(s);
            auser=j.getJSONArray("result");
            JSONObject jsonObject=auser.getJSONObject(0);

            //String fullName = jsonObject.getString(FName)+" "+jsonObject.getString(LName);
            String JSON_ARRAY ="result";// the string must be the same as the name of the json object in the php file
            String FName = "D_F_Name";// the string must be the same as the key name in the php file
            String LName= "D_L_Name";// the string must be the same as the key name in the php file
            String phoneNum = "D_Phone";// the string must be the same as the key name in the php file
            String pass = "Password";// the string must be the same as the key name in the php file
            String age = "age";// the string must be the same as the key name in the php file
            String nationality= "nationality";// the string must be the same as the key name in the php file
            String companyName= "compName";// the string must be the same as the key name in the php file
            String female = "female_companion";// the string must be the same as the key name in the php file
            String licence= "license_number";// the string must be the same as the key name in the php file
            String d_id= "D_ID";// the string must be the same as the key name in the php file
            String email= "D_Email";// the string must be the same as the key name in the php file
            //String dayPrice="DayPrice";// the string must be the same as the key name in the php file
            //String monthPrice="MonthPrice";// the string must be the same as the key name in the php file
            String id_iqama= "id_iqama";// the string must be the same as the key name in the php file
            String rating= "rating";// the string must be the same as the key name in the php file
            String confirmed= "confirmed";// the string must be the same as the key name in the php file
            String carType= "carType";// the string must be the same as the key name in the php file
            String carComp= "carComp";// the string must be the same as the key name in the php file
            String carModel= "carModel";// the string must be the same as the key name in the php file
            String carColor= "carColor";// the string must be the same as the key name in the php file
            String plateNum= "plateNum";// the string must be the same as the key name in the php file
            String capacity= "capacity";// the string must be the same as the key name in the php file
            String yearOfManufacture= "yearOfManufacture";// the string must be the same as the key name in the php file


            d.setID(jsonObject.getInt(d_id));
            d.setEmail(jsonObject.getString(email));
            //System.out.println("***************************************************"+jsonObject.getString(email));
            d.setFName(jsonObject.getString(FName));
            d.setLName(jsonObject.getString(LName));
            d.setPhone(jsonObject.getString(phoneNum));
            d.setPasswrod(jsonObject.getString(pass));
            d.setCompany(jsonObject.getString(companyName));
            //d.setDayPrice(jsonObject.getInt(dayPrice));
            //d.setMonthPrice(jsonObject.getInt(monthPrice));
            d.setLicense(jsonObject.getString(licence));
            d.setNationality(jsonObject.getString(nationality));
            d.setFemaleCompanion(jsonObject.getString(female).charAt(0));
            d.setID_Iqama(jsonObject.getString(id_iqama));
            d.setAge(jsonObject.getString(age));
            d.setRating(jsonObject.getDouble(rating));
            d.setConfirmed(jsonObject.getString(confirmed).charAt(0));
            //Log.d("Driver", "=============================="+d.getEmail() );
            // car ===================================== car

            Car car= new Car();

            car.setPlate(jsonObject.getString(plateNum));
            car.setType(jsonObject.getString(carType));
            car.setModel(jsonObject.getString(carModel));
            car.setColor(jsonObject.getString(carColor));
            car.setCompany(jsonObject.getString(carComp));
            car.setCapacity(jsonObject.getInt(capacity));
            car.setYearOfManufacture(jsonObject.getInt(yearOfManufacture));
            d.setCar(car);
            System.out.println("yearOfManufacture "+d.car.getYearOfManufacture());
            //System.out.println(d.Email+"\t'ffffffffffffffffffffffffffffffffffffffffffff");
            MyApp.driver_from_DB= d;
            MyApp.driver_from_DB.setCar(d.getCar());
            System.out.println(" ################ --1-- IN Parse Data class "+MyApp.driver_from_DB.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void retrieveSession(SharedPreferences spRetrieve) {
        Driver driver =null;// new Driver();
        System.out.println("استرجاع سييييييييييييشششششششننن");
        //From XML to MyApp.driver.
        driver= new Driver();
        driver.setID(spRetrieve.getInt("ID", -1));
        driver.setEmail(spRetrieve.getString("Email", ""));
        driver.setFName(spRetrieve.getString("FName", ""));
        driver.setLName(spRetrieve.getString("LName", ""));
        driver.setPhone(spRetrieve.getString("Phone", ""));
        driver.setPasswrod(spRetrieve.getString("Password", ""));
        driver.setCompany(spRetrieve.getString("Company", ""));
        //driver.setDayPrice(spRetrieve.getInt("DayPrice", -1));
        //driver.setMonthPrice(spRetrieve.getInt("MonthPrice", -1));
        driver.setLicense(spRetrieve.getString("License", ""));
        driver.setNationality(spRetrieve.getString("Nationality", ""));
        driver.setFemaleCompanion(spRetrieve.getString("FemaleCompanion", "").charAt(0));
        driver.setID_Iqama(spRetrieve.getString("ID_Iqama", ""));
        driver.setAge(spRetrieve.getString("Age", ""));
        driver.setRating(Double.parseDouble(spRetrieve.getFloat("Rating", 0.0f) + ""));
        driver.setConfirmed(spRetrieve.getString("Confirmed", "").charAt(0));

        //=================== Car

        Car car = new Car();
        car.setPlate(spRetrieve.getString("Plate", ""));
        car.setType(spRetrieve.getString("Type", ""));
        car.setModel(spRetrieve.getString("Model", ""));
        car.setColor(spRetrieve.getString("Color", ""));
        car.setCompany(spRetrieve.getString("CarCompany", ""));
        car.setCapacity(spRetrieve.getInt("Capacity", -1));
        car.setYearOfManufacture(spRetrieve.getInt("YearOfManufacture", -1));

        driver.setCar(car);
        MyApp.driver_from_session=driver;
        MyApp.driver_from_session.setCar(driver.getCar());

        System.out.println(" ################ --2-- IN RetreiveSession Method "+MyApp.driver_from_session.toString());

        //set MyApp.driver_from_session;

    }//retreive session

    public static void createSession(SharedPreferences sp)
    {

        SharedPreferences.Editor editor = sp.edit();
        editor = editor.clear();//to make sure there's no duplicates.
        System.out.println("ادخال السيييييييييششششششششن ");
        // driver photo.
        // edit ::::: SECOND PARAMETER WILL BE taken from the DB. **************************************************
        editor.putInt("ID", MyApp.driver_from_DB.getID());
        editor.putString("Email", MyApp.driver_from_DB.getEmail());
        editor.putString("FName", MyApp.driver_from_DB.getFName());
        editor.putString("LName", MyApp.driver_from_DB.getLName());
        editor.putString("Phone", MyApp.driver_from_DB.getPhone());
        editor.putString("Password", MyApp.driver_from_DB.getPasswrod());
        editor.putString("Company", MyApp.driver_from_DB.getCompany());
        //editor.putInt("DayPrice", MyApp.driver_from_DB.getDayPrice());
        //editor.putInt("MonthPrice", MyApp.driver_from_DB.getMonthPrice());
        editor.putString("License", MyApp.driver_from_DB.getLicense());
        editor.putString("Nationality", MyApp.driver_from_DB.getNationality());
        editor.putString("FemaleCompanion", MyApp.driver_from_DB.getFemaleCompanion() + "");
        editor.putString("ID_Iqama", MyApp.driver_from_DB.getID_Iqama());
        editor.putString("Age", MyApp.driver_from_DB.getAge());
        editor.putFloat("Rating", Float.parseFloat(MyApp.driver_from_DB.getRating() + ""));
        editor.putString("Confirmed", MyApp.driver_from_DB.getConfirmed() + "");

        // ============================================  Car Info ===============
        // car photo.
        //Car car= new Car();
        editor.putString("Plate",MyApp.driver_from_DB.getCar().getPlate());
        editor.putString("Type",MyApp.driver_from_DB.getCar().getType());
        editor.putString("Model",MyApp.driver_from_DB.getCar().getModel());
        editor.putString("Color",MyApp.driver_from_DB.getCar().getColor());
        editor.putString("CarCompany",MyApp.driver_from_DB.getCar().getCompany());
        editor.putInt("Capacity",MyApp.driver_from_DB.getCar().getCapacity());
        editor.putInt("YearOfManufacture",MyApp.driver_from_DB.getCar().getYearOfManufacture());

        //MyApp.driver_from_DB.setCar(car);

        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+MyApp.driver_from_DB.toString());
        MyApp.driver_from_session=MyApp.driver_from_DB;
        //0MyApp.driver_from_session.setCar(car);
        editor.commit();

        System.out.println(" ################ --3-- IN createSession Method "+MyApp.driver_from_session.toString());
        //set MyApp.driver_from_session;

    }//create session



    //**************************************** SETTERS &&& GETTERS HERE ▼▼▼▼▼


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    /*public String getPasswrod() {
        return Passwrod;
    }

    public void setPasswrod(String passwrod) {
        Passwrod = passwrod;
    }
*/
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getLicense() {
        return License;
    }

    public void setLicense(String license) {
        License = license;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public char getFemaleCompanion() {
        return FemaleCompanion;
    }

    public void setFemaleCompanion(char femaleCompanion) {
        FemaleCompanion = femaleCompanion;
    }

    public String getID_Iqama() {
        return ID_Iqama;
    }

    public void setID_Iqama(String ID_Iqama) {
        this.ID_Iqama = ID_Iqama;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public char getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(char confirmed) {
        this.confirmed = confirmed;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getPasswrod() {
        return Passwrod;
    }

    public void setPasswrod(String passwrod) {
        Passwrod = passwrod;
    }

    public String toString()
    {
        return FName+" "+LName+" "+Email+" "+getCar().getModel()+" "+getCar().getPlate()+" "+getCar().getCompany()+" "+Passwrod;
    }
}
