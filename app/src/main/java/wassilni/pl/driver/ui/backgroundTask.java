package wassilni.pl.driver.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hp on 10/11/16.
 */

public class backgroundTask extends AsyncTask<String ,Void,String> {
    AppCompatActivity appCompatActivity;
    Context ctx;
    AlertDialog alertDialog;
    backgroundTask(Context ctx){
        this.ctx=ctx;
        appCompatActivity= (AppCompatActivity)new AppCompatActivity();
    }
    @Override
    protected void onPreExecute() {
        alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information");
    }

    @Override
    protected String doInBackground(String... voids) {
        String req_url= "http://wassilni.com/db/RegisterDriv.php";//register url
        String checkEmail_url="http://wassilni.com/db/checkEmail.php";//login url
        String sendComplaint_url="http://wassilni.com/db/addComplaint.php";
        String login_url="http://wassilni.com/db/login.php";
        String delete_url="http://wassilni.com/db/DelDri.php";//delete url
        String confirm_url="http://wassilni.com/db/confirmReq.php";//delete url
        String deleteReq_url="http://wassilni.com/db/DelReq.php";//delete url
        String edti_url="http://wassilni.com/db/UpdateDriv.php";
        String addSchedule_url="http://wassilni.com/db/addSchedule.php";
        String RetrieveSchedule_url="http://wassilni.com/db/RetrieveDrivSchedule.php";
        String recoverPass_url="http://wassilni.com/db/recoverPass.php";
        String updateAbsent_url="http://wassilni.com/db/updateAbsent.php";
        String confirmDriver_url="http://wassilni.com/db/getDriverConfirm.php";
        String D_ID,fName ,lName , email , password , phone , DOB ,PlateNum,identificationNum, nationality , driverNum,compName, carType, carModel, carColor, carComp, female,capacity,yearOfmanufacture;
        String method = voids[0];
        if(method.equals("register")||method.equals("edit"))
        {

            DOB ="";
            identificationNum ="";
            nationality ="";
            driverNum ="";
            fName =voids[1];
            lName =voids[2];
            email =voids[3];
            password =voids[4];
            phone =voids[5];
            compName =voids[6];
            carType =voids[7];
            carModel =voids[8];
            carColor =voids[9];
            carComp =voids[10];
            female =voids[11];
            capacity =voids[12];
            PlateNum=voids[13];
            yearOfmanufacture=voids[14];
            D_ID=voids[15];
            try {
                URL url=new URL(edti_url);
                if(method.equals("register")){
                    url = new URL(req_url);
                    DOB =voids[16];
                    driverNum =voids[17];
                    identificationNum =voids[18];
                    nationality =voids[19];
                }
                else if(method.equals("edit")){
                    url = new URL(edti_url);
                }
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data = URLEncoder.encode("D_ID","UTF-8")+"="+URLEncoder.encode(D_ID,"UTF-8")+"&"+
                        URLEncoder.encode("fName","UTF-8")+"="+URLEncoder.encode(fName,"UTF-8")+"&"+
                        URLEncoder.encode("lName","UTF-8")+"="+URLEncoder.encode(lName,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                        URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                        URLEncoder.encode("DOB","UTF-8")+"="+URLEncoder.encode(DOB,"UTF-8")+"&"+
                        URLEncoder.encode("identificationNum","UTF-8")+"="+URLEncoder.encode(identificationNum,"UTF-8")+"&"+
                        URLEncoder.encode("nationality","UTF-8")+"="+URLEncoder.encode(nationality,"UTF-8")+"&"+
                        URLEncoder.encode("driverNum","UTF-8")+"="+URLEncoder.encode(driverNum,"UTF-8")+"&"+
                        URLEncoder.encode("compName","UTF-8")+"="+URLEncoder.encode(compName,"UTF-8")+"&"+
                        URLEncoder.encode("carType","UTF-8")+"="+URLEncoder.encode(carType,"UTF-8")+"&"+
                        URLEncoder.encode("carModel","UTF-8")+"="+URLEncoder.encode(carModel,"UTF-8")+"&"+
                        URLEncoder.encode("carColor","UTF-8")+"="+URLEncoder.encode(carColor,"UTF-8")+"&"+
                        URLEncoder.encode("carComp","UTF-8")+"="+URLEncoder.encode(carComp,"UTF-8")+"&"+
                        URLEncoder.encode("female","UTF-8")+"="+URLEncoder.encode(female,"UTF-8")+"&"+
                        URLEncoder.encode("capacity","UTF-8")+"="+URLEncoder.encode(capacity,"UTF-8")+"&"+
                        URLEncoder.encode("PlateNum","UTF-8")+"="+URLEncoder.encode(PlateNum,"UTF-8")+"&"+
                        URLEncoder.encode("yearOfmanufacture","UTF-8")+"="+URLEncoder.encode(yearOfmanufacture,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream Is=httpURLConnection.getInputStream();
                BufferedReader buffredReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                String response="";
                String line="";
                while ((line=buffredReader.readLine())!=null){
                    response+=line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
                //return "successful Register ";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (method.equals("login")){


            String login_name=voids[1];
            String login_pass=voids[2];
            try{
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter (outputStream,"UTF-8"));
                String data=URLEncoder.encode("login_name","UTF-8")+"="+URLEncoder.encode(login_name,"UTF-8")+"&"+
                        URLEncoder.encode("login_pass","UTF-8")+"="+URLEncoder.encode(login_pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is=httpURLConnection.getInputStream();
                BufferedReader buffredReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                String response="";
                String line="";
                while ((line=buffredReader.readLine())!=null){
                    response+=line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//end login if

//to delete the driver from the database
        else if (method.equals("delete")) {

            try{
                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter (outputStream,"UTF-8"));
                String data=URLEncoder.encode("DriverID","UTF-8")+"="+URLEncoder.encode("92","UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                String response="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    response+=line;
                }
                bufferedReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//end delete if

        else if (method.equals("confirm")) {
            String R_ID=voids[1];
            String S_ID=voids[2];
            try{
                URL url = new URL(confirm_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter (outputStream,"UTF-8"));
                String data= URLEncoder.encode("R_ID","UTF-8")+"="+URLEncoder.encode(R_ID,"UTF-8")+"&"+
                        URLEncoder.encode("S_ID","UTF-8")+"="+URLEncoder.encode(S_ID,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                String response="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    response+=line;
                }
                bufferedReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//end confirm if
        else if (method.equals("deleteReq")) {
            String R_ID=voids[1];
            String S_ID=voids[2];
            System.out.println("Requset id = "+R_ID+" sechedule id ="+S_ID);
            try{
                URL url = new URL(deleteReq_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter (outputStream,"UTF-8"));
                String data= URLEncoder.encode("R_ID","UTF-8")+"="+URLEncoder.encode(R_ID,"UTF-8")+"&"+
                        URLEncoder.encode("S_ID","UTF-8")+"="+URLEncoder.encode(S_ID,"UTF-8")+"&"+
                        URLEncoder.encode("statues","UTF-8")+"="+URLEncoder.encode("reject","UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                String response="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    response+=line;
                }
                bufferedReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//end confirm if
        else if(method.equals("addWorkTime")){
            String startDate,endDate, bookedSeat,pickupL,dropoffL,monthP,dayP,time;

            D_ID=voids[1];
            startDate =voids[2];
            endDate=voids[3];
            pickupL =voids[4];
            dropoffL =voids[5];
            time =voids[6];
            bookedSeat =voids[7];
            monthP =voids[8];
            dayP =voids[9];

            try {
                URL url = new URL(addSchedule_url);

                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data = URLEncoder.encode("D_ID","UTF-8")+"="+URLEncoder.encode(D_ID,"UTF-8")+"&"+
                        URLEncoder.encode("startDate","UTF-8")+"="+URLEncoder.encode(startDate,"UTF-8")+"&"+
                        URLEncoder.encode("endDate","UTF-8")+"="+URLEncoder.encode(endDate,"UTF-8")+"&"+
                        URLEncoder.encode("pickupL","UTF-8")+"="+URLEncoder.encode(pickupL,"UTF-8")+"&"+
                        URLEncoder.encode("dropL","UTF-8")+"="+URLEncoder.encode(dropoffL,"UTF-8")+"&"+
                        URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8")+"&"+
                        URLEncoder.encode("bookedSeat","UTF-8")+"="+URLEncoder.encode(bookedSeat,"UTF-8")+"&"+
                        URLEncoder.encode("monthP","UTF-8")+"="+URLEncoder.encode(monthP,"UTF-8")+"&"+
                        URLEncoder.encode("dayP","UTF-8")+"="+URLEncoder.encode(dayP,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream Is=httpURLConnection.getInputStream();
                BufferedReader buffredReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                String response="";
                String line="";
                while ((line=buffredReader.readLine())!=null){
                    response+=line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
                //return "successful Register ";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//end if of addWorkTiem

        else if(method.equals("sendComplaint")) {
            String P_ID = voids[1]; //driver id
            String complaint = voids[2];
            String passenger_ID = voids[3];
            String time = voids[4];
            String sender="D";
            System.out.println("in background : "+P_ID + complaint + passenger_ID + time);

            try {
                URL url = new URL(sendComplaint_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("D_ID", "UTF-8") + "=" + URLEncoder.encode(P_ID, "UTF-8") + "&" +
                        URLEncoder.encode("passenger_ID", "UTF-8") + "=" + URLEncoder.encode(passenger_ID, "UTF-8") + "&" +
                         URLEncoder.encode("sender","UTF-8")+"="+URLEncoder.encode(sender,"UTF-8")+"&"+
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&" +
                        URLEncoder.encode("complaint", "UTF-8") + "=" + URLEncoder.encode(complaint, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is = httpURLConnection.getInputStream();
                BufferedReader buffredReader = new BufferedReader(new InputStreamReader(Is, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = buffredReader.readLine()) != null) {
                    response += line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if(method.equals("checkEmail")) {
            String D_Email = voids[1];
            String Driver="Driver";
            System.out.println(D_Email);

            try {
                URL url = new URL(checkEmail_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");//use the POST method to send the data to php file
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //send the date to php file
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(D_Email, "UTF-8") + "&" +
                        URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(Driver, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is = httpURLConnection.getInputStream();
                BufferedReader buffredReader = new BufferedReader(new InputStreamReader(Is, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = buffredReader.readLine()) != null) {
                    response += line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(method.equals("recoverPass")){

            String D_Email = voids[1];
            String Driver="Driver";
            System.out.println(D_Email);

            try {
                URL url = new URL(recoverPass_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");//use the POST method to send the data to php file
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //send the date to php file
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(D_Email, "UTF-8") + "&" +
                        URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(Driver, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is = httpURLConnection.getInputStream();
                BufferedReader buffredReader = new BufferedReader(new InputStreamReader(Is, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = buffredReader.readLine()) != null) {
                    response += line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    else if(method.equals("checkAbsent")) {

            D_ID = voids[1];

            try {
                URL url = new URL(updateAbsent_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("D_ID", "UTF-8") + "=" + URLEncoder.encode(D_ID, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is = httpURLConnection.getInputStream();
                BufferedReader buffredReader = new BufferedReader(new InputStreamReader(Is, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = buffredReader.readLine()) != null) {
                    response += line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(method.equals("driverConfirm")) {

            D_ID = voids[1];

            try {
                URL url = new URL(confirmDriver_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("D_ID", "UTF-8") + "=" + URLEncoder.encode(D_ID, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream Is = httpURLConnection.getInputStream();
                BufferedReader buffredReader = new BufferedReader(new InputStreamReader(Is, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = buffredReader.readLine()) != null) {
                    response += line;
                }
                buffredReader.close();
                Is.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;}
    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("The email is taken")||result.equals("The email is free")){

            return;
        }
        else {
           //Toast.makeText(ctx, result, Toast.LENGTH_LONG).show(); // no need for this line I added it in login class.
        }

            //Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
           /* if(result.equals("Login success ... welcome ")){

                Intent i=new Intent(appCompatActivity.getApplicationContext(), MainActivity.class);
                appCompatActivity.startActivity(i);
            }*/
        }



}
