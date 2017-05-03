package wassilni.pl.driver.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import modules.ConnectivityReceiver;
import objects.MyApp;
import wassilni.pl.driver.R;

/**
 * Created by hp on 29/10/16.
 */

public class registermore extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemSelectedListener, TextWatcher, ConnectivityReceiver.ConnectivityReceiverListener {
        String original;

//Declare variable
    Spinner capacitySpinner,CarTypeSpinner,carColorSpinner , yearOFManufactureSpinner;
    EditText et_driveNum ,et_carModel , et_carComp ,et_PlateNum;
    String fName, lName, email, password, phone, DOB,PlateNum, driverNum,identificationNum, nationality,compName, carType,
            carModel, carColor, carComp, yearOfmanufacture, female,capacity,companyName,D_ID;
    Button register;
    RadioButton yesRadio, noRadio;
    private static final String PlateNum_PATTERN ="[A-Z][A-Z]([A-Z]|\\d)\\d\\d";
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        original= (String) getTitle();
        setContentView(R.layout.registermore);
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(this);
        et_driveNum = (EditText)findViewById(R.id.driveNumET);

        et_carComp=(EditText)findViewById(R.id.carCompanyET);
   //     et_carType=(EditText)findViewById(R.id.carTypeET);
        et_carModel=(EditText)findViewById(R.id.carModelET);

        et_PlateNum=(EditText)findViewById(R.id.PlateNumET);
        yesRadio=(RadioButton)findViewById(R.id.yesR);
        noRadio=(RadioButton)findViewById(R.id.noR);
        yesRadio.setChecked(true);
        female="y";
        yesRadio.setOnClickListener(this);
        noRadio.setOnClickListener(this);
        capacitySpinner = (Spinner) findViewById(R.id.spinnerCapacity);
        capacitySpinner.setOnItemSelectedListener(this);
        CarTypeSpinner=(Spinner)findViewById(R.id.spinnerCarType);
        CarTypeSpinner.setOnItemSelectedListener(this);
        carColorSpinner=(Spinner) findViewById(R.id.spinnerCarColor);
        carColorSpinner.setOnItemSelectedListener(this);
        yearOFManufactureSpinner=(Spinner) findViewById(R.id.spinnerYearOfManufacture);
        yearOFManufactureSpinner.setOnItemSelectedListener(this);
        et_driveNum.addTextChangedListener(this);
        et_PlateNum.addTextChangedListener(this);
        et_carComp.addTextChangedListener(this);
        et_carModel.addTextChangedListener(this);



    }//end on create method

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.yesR:
                if (checked)
                    female="y";
                break;
            case R.id.noR:
                if (checked)
                    female="n";
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void userReg(){
        intitialize();
        if(!validateInput()){
            Toast.makeText(this,"خطأ في التسجيل",Toast.LENGTH_SHORT).show();
        }
        else{
        Intent in = getIntent();
        fName = in.getStringExtra("fName");
        lName = in.getStringExtra("lName");
        email = in.getStringExtra("email");
        password = in.getStringExtra("password");
        phone = in.getStringExtra("phone");
        DOB = in.getStringExtra("DOB");
        identificationNum = in.getStringExtra("identificationNum");
        nationality = in.getStringExtra("nationality");
        companyName = in.getStringExtra("companyName");
        String method="register";
        backgroundTask backgroundTask = new backgroundTask(this);
            String result;//to take the result form the php and check if it register or no
            try {
                result=backgroundTask.execute(method,  fName, lName, email, password, phone,
                        companyName, carType, carModel, carColor, carComp,female,
                        capacity,PlateNum,yearOfmanufacture,D_ID, DOB,driverNum, identificationNum,nationality
                        ).get();
                if(result.contains("Driver insert Success")){
                    Toast.makeText(this," تم التسجيل بنجاح",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(registermore.this,login.class);
                    startActivity(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void intitialize(){
        driverNum =et_driveNum.getText().toString().trim();
        carModel =et_carModel.getText().toString().trim();

        carComp =et_carComp.getText().toString().trim();
     //   carType=et_carType.getText().toString().trim();
        PlateNum =et_PlateNum.getText().toString().trim();

        D_ID="";
    }


    private boolean validateInput(){

        boolean result =true;

        if(et_driveNum.getText().toString().length()==0||et_driveNum.getText().toString().length()!=10)     //size as per your requirement
        {
            et_driveNum.setError("يجب عليك تعبية هذه الخانة ب10 أرقام");
            result=false;

        }
        if(et_PlateNum.getText().toString().length()==0)     //size as per your requirement
        {
            et_PlateNum.setError("يجب عليك تعبية هذه الخانة ");
            result=false;

        }


        if(CarTypeSpinner.getSelectedItemId()==0 ){


            setSpinnerError(CarTypeSpinner,"يجب إختيار نوع السيارة ");
            result=false;

        }

        if(yearOFManufactureSpinner.getSelectedItemId()==0 ){


            setSpinnerError(yearOFManufactureSpinner,"يجب إختيار سنة تصنيع السيارة ");
            result=false;

        }

        if(carColorSpinner.getSelectedItemId()==0 ){


            setSpinnerError(carColorSpinner,"يجب إختيار لون السيارة ");
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
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        userReg();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Spinner spinner = (Spinner) adapterView;
        TextView myText = (TextView) view;


        if(spinner.getId()==R.id.spinnerCapacity)
        {
            capacity = (String) myText.getText();
        }
        else if (spinner.getId() == R.id.spinnerCarType)
        {
            carType = (String) myText.getText();
        }
        else if(spinner.getId()==R.id.spinnerCarColor){

            carColor=(String) myText.getText();
        }
        else if(spinner.getId()==R.id.spinnerYearOfManufacture){

            yearOfmanufacture=(String) myText.getText();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        validateInput();
    }

    @Override
    public void afterTextChanged(Editable s) {
        validateInput();
    }

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
}
