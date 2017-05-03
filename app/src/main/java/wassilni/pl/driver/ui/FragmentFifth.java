package wassilni.pl.driver.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import objects.MyApp;
import wassilni.pl.driver.R;

/**
 * Created by haya on 10/19/2016.
 */

public class FragmentFifth extends Fragment implements DatePickerDialog.OnDateSetListener,AdapterView.OnItemSelectedListener, View.OnClickListener, TextWatcher {
    Spinner capacitySpinner, CarTypeSpinner, carColorSpinner, yearOFManufactureSpinner;

    EditText et_fName, et_lName, et_email, et_currentPassword, et_password, et_checkPassword, et_phone, et_companyName,
            et_carType, et_carModel, et_carColor, et_carComp, et_yearOfmanufacture, et_PlateNum, et_capacityCar;
    String D_ID, fName, lName, email, password, phone, DOB, PlateNum, driverNum, identificationNum, carType,
            carModel, carColor, carComp, yearOfmanufacture, female, capacity, companyName, currentPassword;
    Button edit;
    RadioButton yesRadio, noRadio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fifth, container, false);

        edit = (Button) view.findViewById(R.id.editButton);
        edit.setOnClickListener(this);

        //et_driveNum = (EditText)view.findViewById(R.id.driveNumET);
        et_capacityCar = (EditText) view.findViewById(R.id.carCapacityET);
        et_carColor = (EditText) view.findViewById(R.id.colorCarET);
        et_carComp = (EditText) view.findViewById(R.id.carCompanyET);
        et_carType = (EditText) view.findViewById(R.id.carTypeET);
        et_carModel = (EditText) view.findViewById(R.id.carModelET);
        et_yearOfmanufacture = (EditText) view.findViewById(R.id.yearOfmanufactureET);
        et_PlateNum = (EditText) view.findViewById(R.id.PlateNumET);
        //et_yearOfmanufacture=(EditText)view.findViewById(R.id.yearOfmanufactureET);
        yesRadio = (RadioButton) view.findViewById(R.id.yesR);
        noRadio = (RadioButton) view.findViewById(R.id.noR);
        yesRadio.setOnClickListener(this);
        noRadio.setOnClickListener(this);
        capacitySpinner = (Spinner) view.findViewById(R.id.spinnerCapacity);
        capacitySpinner.setOnItemSelectedListener(this);
        CarTypeSpinner = (Spinner) view.findViewById(R.id.spinnerCarType);
        CarTypeSpinner.setOnItemSelectedListener(this);
        carColorSpinner = (Spinner) view.findViewById(R.id.spinnerCarColor);
        carColorSpinner.setOnItemSelectedListener(this);
        yearOFManufactureSpinner = (Spinner) view.findViewById(R.id.spinnerYearOfManufacture);
        yearOFManufactureSpinner.setOnItemSelectedListener(this);

        et_fName = (EditText) view.findViewById(R.id.firstNameET);
        et_lName = (EditText) view.findViewById(R.id.lastNameET);
        et_email = (EditText) view.findViewById(R.id.emailET);
        et_currentPassword = (EditText) view.findViewById(R.id.currentPasswordET);
        et_password = (EditText) view.findViewById(R.id.passwordET);
        et_checkPassword = (EditText) view.findViewById(R.id.passwordCheckET);
        et_phone = (EditText) view.findViewById(R.id.phoneET);
        et_companyName = (EditText) view.findViewById(R.id.companyET);
        et_carType = (EditText) view.findViewById(R.id.carTypeET);
        et_fName.addTextChangedListener(this);
        et_lName.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_checkPassword.addTextChangedListener(this);
        et_currentPassword.addTextChangedListener(this);
        et_phone.addTextChangedListener(this);
        et_yearOfmanufacture.addTextChangedListener(this);
        et_PlateNum.addTextChangedListener(this);
        et_carComp.addTextChangedListener(this);
        et_carModel.addTextChangedListener(this);

            et_password.setFocusableInTouchMode(false);
            et_checkPassword.setFocusableInTouchMode(false);

        female = "n";
        setFields();
        return view;

    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.yesR:
                if (checked)
                    female = "y";
                break;
            case R.id.noR:
                if (checked)
                    female = "n";
                break;
        }
    }

    //to set all the filed from the current information of the driver
    public void setFields() {
        currentPassword = MyApp.driver_from_session.getPasswrod();
        et_fName.setText(MyApp.driver_from_session.getFName());
        et_lName.setText(MyApp.driver_from_session.getLName());
        et_email.setText(MyApp.driver_from_session.getEmail());
        et_phone.setText(MyApp.driver_from_session.getPhone());

        et_companyName.setText(MyApp.driver_from_session.getCompany());
        et_carType.setText(MyApp.driver_from_session.getCar().getType());
        et_PlateNum.setText(MyApp.driver_from_session.getCar().getPlate());
        et_carColor.setText(MyApp.driver_from_session.getCar().getColor());
        et_carComp.setText(MyApp.driver_from_session.getCar().getCompany() + "");
        et_carModel.setText(MyApp.driver_from_session.getCar().getModel() + "");
        et_yearOfmanufacture.setText(MyApp.driver_from_session.getCar().getYearOfManufacture() + "");
        et_capacityCar.setText(MyApp.driver_from_session.getCar().getCapacity() + "");
        char c = MyApp.driver_from_session.getFemaleCompanion();
        if (c == 'y') {
            yesRadio.setChecked(true);
        } else
            noRadio.setChecked(true);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /* this method will send the input to the background task to send it the php file
    and edit driver info */
    public void userEdit() {
        initialization(); //set all the input fields in string

        Log.d("F5","carModel"+carModel+"\t company"+companyName);
        String method = "edit";//to detecting that we want editing
        backgroundTask backgroundTask = new backgroundTask(getActivity());
        backgroundTask.execute(method, fName, lName, email, password, phone,
                companyName, carType, carModel, carColor, carComp, female,
                capacity, PlateNum, yearOfmanufacture, D_ID);
    }


    /*
    * This method will set the item of the capacity in the car and car Type
    *  from the list view in String
    * */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        TextView myText = (TextView) view;


        if (spinner.getId() == R.id.spinnerCapacity) {
            capacity = (String) myText.getText();
            changeSpinnerTV(spinner, et_capacityCar, capacity);
            //  et_capacityCar.setText(capacity);
        } else if (spinner.getId() == R.id.spinnerCarType) {
            carType = (String) myText.getText();
            changeSpinnerTV(spinner, et_carType, carType);
            // et_carType.setText(carType);

        } else if (spinner.getId() == R.id.spinnerYearOfManufacture) {

            yearOfmanufacture = (String) myText.getText();
            changeSpinnerTV(spinner, et_yearOfmanufacture, yearOfmanufacture);
            //et_yearOfmanufacture.setText(yearOfmanufacture);
        } else if (spinner.getId() == R.id.spinnerCarColor) {
            carColor = (String) myText.getText();
            changeSpinnerTV(spinner, et_carColor, carColor);
            // et_carColor.setText(carColor);

        }
    }

    private void changeSpinnerTV(Spinner spinner, TextView textView, String str) {

        if (spinner.getSelectedItemId() != 0) {

            textView.setText(str);
        }
    }

    private void initialization() {

        carModel = et_carModel.getText().toString().trim();
        carColor = et_carColor.getText().toString().trim();
        carComp = et_carComp.getText().toString().trim();
        PlateNum = et_PlateNum.getText().toString().trim();
        yearOfmanufacture = et_yearOfmanufacture.getText().toString().trim();
        fName = et_fName.getText().toString().trim();
        lName = et_lName.getText().toString().trim();
        email = et_email.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        password = et_password.getText().toString().trim();
        companyName = et_companyName.getText().toString().trim();
        D_ID = "" + MyApp.driver_from_session.getID();

        System.out.println(carModel+" "+carColor+" ");

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);

        // setDate(calendar);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editButton) {
            boolean flage = true;
            String method = "checkEmail";
            backgroundTask backgroundTask = new backgroundTask(getActivity());
            try {
                if (!et_email.getText().toString().equals(MyApp.driver_from_session.getEmail())) {
                    String r;
                    r = backgroundTask.execute(method, et_email.getText().toString()).get();
                    if (r.equals("The email is taken")) {
                        et_email.setError("الايميل سبق التسجيل به ");
                        flage = false;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (validateInput() && flage) {


                new AlertDialog.Builder(getContext())
                        .setTitle("تأكيد تعديل البيانات")
                        .setMessage("هل أنت متأكد من تعديل البيانات؟\n في حال الموافقة, سيتم تسجيل خروجك تلقائياً.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getActivity().getApplicationContext(),"تم تعديل البيانات",Toast.LENGTH_SHORT).show();

                                userEdit();
                                SharedPreferences sp = getActivity().getSharedPreferences("session", Context.MODE_APPEND);
                                SharedPreferences.Editor editor = sp.edit();
                                editor = editor.clear();
                                editor.clear();
                                editor.commit();
                                MyApp.driver_from_session = null;
                                startActivity(new Intent(getActivity().getApplicationContext(),login.class));
                                getActivity().finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


                /*userEdit();
                SharedPreferences sp = getActivity().getSharedPreferences("session", Context.MODE_APPEND);
                SharedPreferences.Editor editor = sp.edit();
                editor = editor.clear();
                editor.clear();
                editor.commit();
                MyApp.driver_from_session = null;
                Toast.makeText(getActivity(), "تم تعديل البيانات, يتوجب عليك تسجيل الدخول مجددا.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), login.class));
                getActivity().finish();*/
            } else {
                Toast.makeText(getActivity(), "تأكد من تعديل المعلومات بشكل صحيح ", Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.yesR || v.getId() == R.id.noR) {
            onRadioButtonClicked(v);


        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
       // validateInput();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!et_currentPassword.getText().toString().trim().equals("")) {
            System.out.println("currentPassword= "+currentPassword);
            if(et_currentPassword.getText().toString().trim().equals(currentPassword)) {
                checkPassword();
            } else   et_currentPassword.setError("كلمة المرور الحالية غير متوافقة");
        } else {
            et_password.setFocusableInTouchMode(false);
            et_checkPassword.setFocusableInTouchMode(false);
        }
    }


    private boolean validateInput() {

        boolean result = true;

        if (et_fName.getText().toString().length() == 0)     //size as per your requirement
        {
            et_fName.setError("يجب عليك تعبية هذه الخانة ");
            result = false;

        }
        if (et_lName.getText().toString().length() == 0)     //size as per your requirement
        {
            et_lName.setError("يجب عليك تعبية هذه الخانة ");
            result = false;

        }
        if (et_email.getText().toString().length() == 0)     //size as per your requirement
        {
            et_email.setError("يجب عليك تعبية هذه الخانة ");
            result = false;

        }

        if (et_phone.getText().toString().length() != 10)     //size as per your requirement
        {
            et_phone.setError("رقم الجوال يجب ان يكون 10 خانات ");
            result = false;

        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches())     //size as per your requirement
        {
            et_email.setError("فورمات الايميل غير صحيح ");
            result = false;

        }


        if (et_PlateNum.getText().toString().length() == 0)     //size as per your requirement
        {
            et_PlateNum.setError("يجب عليك تعبية هذه الخانة ");
            result = false;

        }


        if (CarTypeSpinner.getSelectedItemId() == 0 && et_carType.getText().toString().trim().equals("")) {


            register.setSpinnerError(CarTypeSpinner, "يجب إختيار نوع المركبة ");
            result = false;

        }


        if (carColorSpinner.getSelectedItemId() == 0 && et_carColor.getText().toString().trim().equals("")) {


            register.setSpinnerError(carColorSpinner, "يجب إختيار لون المركبة ");
            result = false;

        }
        if (capacitySpinner.getSelectedItemId() == 0 && et_capacityCar.getText().toString().trim().equals("")) {


            register.setSpinnerError(capacitySpinner, "يجب إختيار عدد مقاعد المركبة");
            result = false;

        }


        return result;
    }

    public boolean checkPassword() {
        boolean r = true;
        et_password.setFocusableInTouchMode(true);
        et_checkPassword.setFocusableInTouchMode(true);

        if (et_password.getText().toString().length() == 0 )     //size as per your requirement
        {
            et_password.setError("يجب عليك تعبية هذه الخانة ");
            r = false;

        }
        if (et_checkPassword.getText().toString().length() == 0 )     //size as per your requirement
        {
            et_checkPassword.setError("يجب عليك تعبية هذه الخانة ");
            r = false;

        }

        if (et_password.getText().toString().length() < 6) {
            et_password.setError("الرقم السري يجب ان يكون أطول من 6 خانات");
            r = false;
        }
        if (et_checkPassword.getText().toString().length() < 6) {

            et_checkPassword.setError("الرقم السري يجب ان يكون أطول من 6 خانات ");
            r = false;

        }
        if (!et_password.getText().toString().equals(et_checkPassword.getText().toString())) {
            et_checkPassword.setError("التأكد من كلمة المرور يجب أن يكون مطابق لكلمة المرور ");
            r = false;

        }


        return r;

    }
}
