package wassilni.pl.driver.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import modules.ConnectivityReceiver;
import objects.Driver;
import objects.MyApp;
import wassilni.pl.driver.R;
/*
*       ((**===     Session     ===**))
*   initially when the user login for the first time, or login after logging out
*       the session file will be empty, then the user MUST login,
*       after logging in the session file will be filled with his data
*       and a copy of this data will be stored in the global variable MyApp.driver_from_session
*       to be accessible from everywhere within this application.
*
*   the second case, if the user enters the application and he's already logged in.
*       the value of the session file will be copied to the global variable MyApp.driver_from_session.
*
* */

public class login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    EditText et_loginName ,et_loginPass;
    String login_name,login_pass;
    SharedPreferences spRetrieve;
    SharedPreferences spInsert;
    public SharedPreferences sharedPreferences;
    String original;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // call a method that retrieves the session!
        sharedPreferences = getSharedPreferences("session", Context.MODE_APPEND);
        original= (String) getTitle();
        //setTheme(R.style.Theme_Transparento);
        setContentView(R.layout.activity_login);

        Button login=(Button) findViewById(R.id.loginbutton);
        TextView register=(TextView) findViewById(R.id.signin) ;
        TextView recover=(TextView) findViewById(R.id.recoverdpass);
        et_loginName=(EditText) findViewById(R.id.userNameET);
        et_loginPass=(EditText) findViewById(R.id.passwordET);

        //this.getTheme().applyStyle(R.style.Theme_Transparento,false);
        //sharedPreferences = getSharedPreferences("session", Context.MODE_APPEND);
        //SessionSetup();
        // ====================== here will be the session retreival.

        // hhhhPostResponseAsyncTask task =new PostResponseAsyncTask((AsyncResponse) this);

            /*login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent i=new Intent(getApplicationContext(), MainActivity.class);
                    //startActivity(i);


                }
            });*/
           register.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent r=new Intent(getApplicationContext(), register.class);
                   startActivity(r);
               }
           });
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c=new Intent(getApplicationContext(),recoverdpass.class);
                startActivity(c);
            }
         });


    }//end of oncreat

    @Override
    protected void onStart() {
        super.onStart();
        MyApp.showFeedback(this, original,false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApp.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */

    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        MyApp.showFeedback(this,original,true);
    }

    public void userReg(View view ){

    }
    public void userLogin(View view) {
        //startActivity(new Intent(this , register.class));
        intitialize();
        try {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            String method = "login";
            backgroundTask bc = new backgroundTask(this);
            String result = bc.execute(method, login_name, login_pass).get();
            System.out.println(result);
          if (!result.toLowerCase().contains("failed")){
                createSession();
                moveNext();
            }
            else
                Toast.makeText(this, "فشل تسجيل الدخول, الرجاء التحقق من معلوماتك", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //Driver d = new Driver();
        //d.getDriverInfoFromDB("ali@gmail.com");// this will reset MyApp.driver_inserted_DB
        //String flag = d.getDriverInfoFromDB(1,this);
        //System.out.println("9899554864515468512"+flag);
        /*while (flag.equalsIgnoreCase("failed")){

        }*/
        //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + MyApp.driver_from_DB.getEmail());
        // moveNext

    }

    public void moveNext(){
        //call retreiveSession()
        Intent act = new Intent(login.this,MainActivity.class);
        startActivity(act);
        this.finish();

    }

    private void intitialize(){
        login_name =et_loginName.getText().toString().trim();
        login_pass =et_loginPass.getText().toString().trim();
    }

   /* private void setDate(final Calendar calendar){

        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.datepickerTV)).setText(dateFormat.format(calendar.getTime()));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar= new GregorianCalendar(year,month,day);
        setDate(calendar);
    }*/



    /*public void SessionSetup() {


        class Task extends AsyncTask<String, Void, String> {
            ProgressDialog loggingin;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loggingin= ProgressDialog.show(getApplicationContext(),"ارجو الإنتظار...","جاري استرجاع المعلومات إن وجدت");
            }

            @Override
            protected String doInBackground(String... params) {
                String flag="";
                if (sharedPreferences.contains("ID"))//means that the XML file isn't empty.
                {//retrieve session , and set the variable MyApp.driver_from_session
                    Driver.retrieveSession(sharedPreferences);
                    flag="succeed";
                } else // there's no session saved, then create one!
                {
                    flag="failed";
                    // 1.prompt for login, using Toast
                    //createSession();
                }
                return flag;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loggingin.dismiss();
                if(s.equalsIgnoreCase("succeed")){
                    Toast.makeText(login.this,"مرحبا مجدداً!",Toast.LENGTH_LONG).show();
                    System.out.println("@@@@@@@@@@@\t "+MyApp.driver_from_session.getEmail());
                    moveNext();
                    //direct user to next page.
                }
                else{
                    //prompt to logiin
                    Toast.makeText(login.this,"لا يوجد ملف تعريف, الرجاء تسجيل الدخول",Toast.LENGTH_SHORT).show();
                }

            }

        }
        Task t=new Task();
        //t.execute(id+"");
        t.execute();
    }*/

    public void createSession()
    {
             /*
            * 1. Prompt user to login.
            * 2. restore the data from the DB.
            * 3. and insert it in a session.
            * 4. set data in MyApp.driver_from_session
            * */
        Driver d = new Driver();
        //Email entered by the user, since the authentication succeed, it's guarnteed that the email valid//step 2
        d.getDriverInfoFromDB(login_name,this);//step 2
        Driver.createSession(sharedPreferences);//step 3,4
    }
}