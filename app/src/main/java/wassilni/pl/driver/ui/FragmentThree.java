package wassilni.pl.driver.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;

import objects.MyApp;
import wassilni.pl.driver.R;
import wassilni.pl.driver.data.Fragments;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;


public class FragmentThree extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener, TextWatcher {
    Spinner hourSpinner, mintSpinner, bookedSpinner;
    static EditText pickupET,dropoffET,startDateET, endDateET ;
    EditText monthPriceET,dayPriceET;
    static boolean CheckButton;
    String D_ID,startDate,endDate, hourS, mintS,bookedSeat,pickupL,dropoffL,monthP,dayP,time;
    View view;
    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
    static  String date;
    ImageButton ButtonDate1, ButtonDate2,pickupB,dropoffB;
    Button saveB;
    public static Date start,end;
    Date currentdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_three, containter, false);
        // ButterKnife.inject(this, view);
        // ((GradientDrawable) circleLayout.getBackground())
        //  .setColor(getResources().getColor(R.color.material_purple));
        hourSpinner = (Spinner) view.findViewById(R.id.spinnerHour);
        hourSpinner.setOnItemSelectedListener(this);
        mintSpinner = (Spinner) view.findViewById(R.id.spinnerMint);
        mintSpinner.setOnItemSelectedListener(this);
        ButtonDate1 = (ImageButton) view.findViewById(R.id.datepickerB);
        ButtonDate1.setOnClickListener(this);
        ButtonDate2 = (ImageButton) view.findViewById(R.id.datepickerB1);
        ButtonDate2.setOnClickListener(this);
        pickupB = (ImageButton) view.findViewById(R.id.pickupB);
        pickupB.setOnClickListener(this);
        dropoffB = (ImageButton) view.findViewById(R.id.dropoffB);
        dropoffB.setOnClickListener(this);
        saveB = (Button) view.findViewById(R.id.save);
        saveB.setOnClickListener(this);
        bookedSpinner = (Spinner) view.findViewById(R.id.spinnerBookedSeat);
        bookedSpinner.setOnItemSelectedListener(this);
        startDateET = (EditText) view.findViewById(R.id.StringDateET);
        endDateET = (EditText) view.findViewById(R.id.endingDateTE);
        pickupET =(EditText) view.findViewById(R.id.pickupET);
        pickupET.setEnabled(false);
        dropoffET=(EditText) view.findViewById(R.id.dropOffLET);
        dropoffET.setEnabled(false);
        monthPriceET=(EditText) view.findViewById(R.id.monthPriceET);
        dayPriceET=(EditText) view.findViewById(R.id.dayPriceET);
        monthPriceET.addTextChangedListener(this);
        dayPriceET.addTextChangedListener(this);
        startDateET.addTextChangedListener(this);
        endDateET.addTextChangedListener(this);


        String date =spf.format(new Date()) ;
        //System.out.println(spf.format(date));
        try {
            currentdate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //  ButterKnife.reset(this);
    }

    private void save(){
        intitialize();

        String method="addWorkTime";
        backgroundTask backgroundTask = new backgroundTask(getActivity());
        try {
            String result= backgroundTask.execute(method,D_ID,startDate,endDate,pickupL,dropoffL,time,bookedSeat,monthP,dayP).get();
      System.out.println(result+"88888888888dddddddd");
       if(result.contains("schedule Added Successfully")){

           Toast.makeText(getActivity(), "تم إضافة الفترة بنجاح", Toast.LENGTH_LONG).show();
           // Reload current fragment
        Intent i =new Intent(this.getActivity(),MainActivity.class);
           startActivity(i);

       }
            else {
           Toast.makeText(getActivity(), "حدث خطأ أثناء إضافة الفترة", Toast.LENGTH_LONG).show();

       }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void intitialize(){
        D_ID=""+ MyApp.driver_from_session.getID();
        startDate=startDateET.getText().toString().trim();
        endDate=endDateET.getText().toString().trim();
        pickupL=pickupET.getText().toString().trim();
        dropoffL=dropoffET.getText().toString().trim();
        time=hourS+":"+mintS+":"+"00";
        monthP=monthPriceET.getText().toString().trim();
        dayP=dayPriceET.getText().toString().trim();


    }


    private boolean validateInput(){

        boolean result =true;

        if(pickupET.getText().toString().length()==0)     //size as per your requirement
        {
            pickupET.setError("يجب عليك إختيار منطقة الإنطلاق ");
            result=false;

        }
        if(dropoffET.getText().toString().length()==0)     //size as per your requirement
        {
            dropoffET.setError("يجب عليك  إختيار منطقة الوجهه ");
            result=false;

        }
        if(dayPriceET.getText().toString().length()==0)     //size as per your requirement
        {
            dayPriceET.setError("يجب عليك تحديد السعر اليومي ");
            result=false;

        }
        if(monthPriceET.getText().toString().length()==0)     //size as per your requirement
        {
            monthPriceET.setError("يجب عليك تحديد السعر الشهري ");
            result=false;

        }

        if(startDateET.getText().toString().length()==0)     //size as per your requirement
        {
            startDateET.setError("يجب عليك إدخال تاريخ البداية ");
            result=false;

        }
        if(endDateET.getText().toString().length()==0)     //size as per your requirement
        {
            endDateET.setError("يجب عليك إدخال تاريخ النهاية  ");
            result=false;

        }

        if(endDateET.getText().toString().length()!=10) {

          result =ValidateDate(endDateET);
        }
        if(startDateET.getText().toString().length()!=10) {

            result =ValidateDate(startDateET);
        }
        if(hourSpinner.getSelectedItemId()==0 ){


            register.setSpinnerError(hourSpinner,"يجب إختيار ساعة الرحلة ");
            result=false;

        }
        if(mintSpinner.getSelectedItemId()==0 ){


            register.setSpinnerError(mintSpinner,"يجب إختيار الدقائق  ");
            result=false;

        }

        return result;
    }

    public boolean ValidateDate(TextView t){

        boolean r=true;
        Date d=null;
        String value = t.getText().toString().trim();
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
                t.setError("التنسيق اليوم غير صحيح يجب ان يكون 1994-29-12");
                r = false;
            }
        }

        return r;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.datepickerB || v.getId() == R.id.datepickerB1) {

            if (v.getId() == R.id.datepickerB) {
                CheckButton = true;
                System.out.println(CheckButton);
            } else if (v.getId() == R.id.datepickerB1) {
                CheckButton = false;
                System.out.println(CheckButton);
            }
            DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getFragmentManager(), "DatePicker");


        }
        else if(v.getId() == R.id.save){

            boolean check = true;
            if(startDateET.getText().length()!=0){
                if (start.before(currentdate)){
                    startDateET.setError("يجب أن يكون بعد اليوم الحالي ");
                    check=false;
                }}
            if(endDateET.getText().length()!=0){
                if(end.before(currentdate)){
                    endDateET.setError("يجب أن يكون بعد اليوم الحالي ");
                    check=false;

                }
                if(end.after(start))
                {
                    endDateET.setError("تاريخ إنتهاء يجب أن يكون  بعد تاريخ البدء");
                    check=false;

                }}
        if(validateInput())
        {
            save();
        }
        }
        else if (v.getId() == R.id.pickupB)
        {
            Intent i=new Intent(this.getActivity(),workArea.class);
            i.putExtra("Type","pickup");
            startActivity(i);
        }
        else if (v.getId() == R.id.dropoffB)
        {
            Intent i=new Intent(this.getActivity(),workArea.class);
            i.putExtra("Type","dropoff");
            startActivity(i);
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Spinner spinner = (Spinner) adapterView;
        TextView myText = (TextView) view;
      /*  ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
        ((TextView) adapterView.getChildAt(0)).setTextSize(18);*/
        if(spinner.getId()==R.id.spinnerHour){
            hourS = (String) myText.getText();
        }
        else if(spinner.getId()==R.id.spinnerMint){
            mintS = (String) myText.getText();
        }

        else if (spinner.getId()==R.id.spinnerBookedSeat){
            bookedSeat=(String) myText.getText();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        validateInput();
    }

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
            try {
            if (CheckButton == true){
                startDateET.setText(date);


                start = spf.parse(startDateET.getText().toString());}
            else{

                endDateET.setText(date);

                    end = spf.parse(endDateET.getText().toString());

            }} catch (ParseException e) {
            e.printStackTrace();
        }

        }



        }



}
