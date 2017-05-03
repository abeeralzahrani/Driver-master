package wassilni.pl.driver.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modules.ConnectivityReceiver;
import objects.MyApp;
import wassilni.pl.driver.R;

import static wassilni.pl.driver.ui.FragmentThree.date;

public class register extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, TextWatcher, View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener {
    String original;

    Spinner spinNationality;
    static EditText  et_DOB;
    EditText et_fName, et_lName, et_email, et_password, et_checkPassword, et_phone, et_companyName, et_identificationNum;
    String fName, lName, email, password, passwordCheck, phone, DOB, nationality, identificationNum, companyName;
    Button B_Date;
    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN ="yyyy-dd-MM";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    boolean validate ;
    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        original= (String) getTitle();
        setContentView(R.layout.registermore);

        setContentView(R.layout.activity_register);
         Button B_Date=(Button) findViewById(R.id.datepickerB);
        B_Date.setOnClickListener(this);
        spinNationality = (Spinner) findViewById(R.id.spinner);
        spinNationality.setOnItemSelectedListener(this);

        et_fName = (EditText) findViewById(R.id.fistNameET);
        et_lName = (EditText) findViewById(R.id.lastNameET);
        et_email = (EditText) findViewById(R.id.emailET);
        et_password = (EditText) findViewById(R.id.passwordET);
        et_checkPassword = (EditText) findViewById(R.id.passwordCheckET);
        et_phone = (EditText) findViewById(R.id.phoneET);
        et_DOB = (EditText) findViewById(R.id.datepickerET);
        et_identificationNum = (EditText) findViewById(R.id.identificationNumET);
        et_companyName = (EditText) findViewById(R.id.companyET);
        et_fName.addTextChangedListener(this);
        et_lName.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_checkPassword.addTextChangedListener(this);
        et_phone.addTextChangedListener(this);
        et_DOB.addTextChangedListener(this);
        et_identificationNum.addTextChangedListener(this);

        //ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }//end onCreate method


    public void userReg(View view) {
        intitialize();
        boolean flage=true;
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches())
        {


            String method="checkEmail";
            backgroundTask backgroundTask = new backgroundTask(this);
            try {
                String r ;
                r= backgroundTask.execute(method,et_email.getText().toString()).get();
                if(r.equals("The email is taken")){
                    et_email.setError("الايميل سبق التسجيل به ");
                    flage=false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if(!validateInput()&&!flage)
        {

            Toast.makeText(this,"خطأ في التسجيل",Toast.LENGTH_SHORT).show();
        }
        else{
        Intent i = new Intent(register.this, registermore.class);
        i.putExtra("fName", fName);
        i.putExtra("lName", lName);
        i.putExtra("email", email);
        i.putExtra("password", password);
        i.putExtra("phone", phone);
        i.putExtra("DOB", DOB);
        System.out.println(identificationNum + "تعررررررررريف السائق ");
        i.putExtra("identificationNum", identificationNum);// take the identification for the saudi and not saudi driver
        i.putExtra("nationality", nationality);
        i.putExtra("companyName", companyName);
        // i.putExtra("iqama",iqama);
        startActivity(i);}
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.datepickerB:

                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");

                break;



        }
    }




    //this method to initionalize  all the input from user to string
    private void intitialize() {
        fName = et_fName.getText().toString().trim();
        lName = et_lName.getText().toString().trim();
        email = et_email.getText().toString().trim().toLowerCase();
        phone = et_phone.getText().toString().trim();
        DOB = et_DOB.getText().toString().trim();
        password = et_password.getText().toString().trim();
        passwordCheck = et_checkPassword.getText().toString().trim();
        companyName = et_companyName.getText().toString().trim();
        identificationNum=et_identificationNum.getText().toString().trim();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView myText = (TextView) view;
        nationality = (String) myText.getText().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




    private boolean validateInput(){

        boolean result =true;

        if(et_fName.getText().toString().length()==0)     //size as per your requirement
        {
            et_fName.setError("يجب عليك تعبية هذه الخانة ");
            result=false;

        }
        if(et_lName.getText().toString().length()==0)     //size as per your requirement
        {
            et_lName.setError("يجب عليك تعبية هذه الخانة ");
            result=false;

        }
        if(et_email.getText().toString().length()==0)     //size as per your requirement
        {
            et_email.setError("يجب عليك تعبية هذه الخانة ");
            result=false;

        }
        if(et_password.getText().toString().length()==0)     //size as per your requirement
        {
            et_password.setError("يجب عليك تعبية هذه الخانة ");
            result=false;

        }
        if(et_checkPassword.getText().toString().length()==0)     //size as per your requirement
        {
            et_checkPassword.setError("يجب عليك تعبية هذه الخانة ");
            result=false;

        }
        if(et_phone.getText().toString().length()!=10)     //size as per your requirement
        {
            et_phone.setError("رقم الجوال يجب ان يكون 10 خانات ");
            result=false;

        }
        if(et_DOB.getText().toString().length()==0)     //size as per your requirement
        {
            et_DOB.setError("يجب عليك إدخال تاريخ ميلادك ");
            result=false;

        }
        if(et_identificationNum.getText().toString().length()==0)     //size as per your requirement
        {
            et_identificationNum.setError("يجب عليك تعبية هذه الخانة ");
            result=false;

        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches())     //size as per your requirement
        {
            et_email.setError("صياغة البريد الإلكتروني غير صحيحة  ");
            result=false;

        }


        if(et_password.getText().toString().length()<6 &&  et_password.getText().toString().length()>14)
        {
            et_password.setError("الرقم السري يجب ان يكون أطول من 6 خانات وأقل من 14 ");
            result=false;
        }
        if(et_checkPassword.getText().toString().length()<6&&et_checkPassword.getText().toString().length()>6){

            et_checkPassword.setError("الرقم السري يجب ان يكون أطول من 6 خانات وأقل من 14");
            result=false;

        }
        else if (!et_checkPassword.getText().toString().equals(et_password.getText().toString())){
            et_checkPassword.setError("كلمة المرور لا تتطابق ");
            result=false;

        }

        if(et_identificationNum.getText().toString().length()!=10)
        {
            et_identificationNum.setError("الخانة يجب أن تكون 10 أرقام ");
            result=false;

        }
        if(et_DOB.getText().toString().length()!=10) {
          //  matcher = Pattern.compile(DATE_PATTERN).matcher(et_DOB.getText().toString());
            Date d=null;
            String value = et_DOB.getText().toString().trim();
            if (value.length() != 0) {
            try {


                    d = spf.parse(value);
                    if (!value.equals(spf.format(d))) {//if the date not match the date format
                        d = null;
                    }
                }catch(ParseException ex){
                    ex.printStackTrace();
                }
                if (d == null) {
                    // Invalid date format
                    et_DOB.setError("التنسيق اليوم غير صحيح يجب ان يكون 1994-29-12");
                    result = false;
                }
            }
            }

        if(spinNationality.getSelectedItemId()==0 ){

            spinNationality.setPrompt("يجب إختيار جنسيتك ");
            setSpinnerError(spinNationality,"يجب إختيار جنسيتك ");
            result=false;

        }

        return result;
    }

    static public void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(error);
        }
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        MyApp.showFeedback(this, original,false);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        validateInput();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       validateInput();
    }

    @Override
    public void afterTextChanged(Editable s) {
       // System.out.println(s.toString());
        validateInput();


    }




    /**
     * Validate date format with regular expression

     * @return true valid date format, false invalid date format
     */



    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        int yy, mm, dd;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
            yy = calendar.get(android.icu.util.Calendar.YEAR);
            mm = calendar.get(android.icu.util.Calendar.MONTH);
            dd = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);


            populateSetDate(calendar);
        }

        public void populateSetDate(final Calendar calendar) {
            Date newDate = calendar.getTime();
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            date = spf.format(newDate);
            et_DOB.setText(date);
        }
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


}
