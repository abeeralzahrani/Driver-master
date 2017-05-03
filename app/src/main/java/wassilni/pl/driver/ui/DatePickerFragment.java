package wassilni.pl.driver.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hp on 30/10/16.
 */
public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener  {
    /**
     * Created by hp on 07/12/16.
     */
    private Button btn;
    private int year, month, day;
    String date;
    static boolean f= false;
    @Override
        public Dialog onCreateDialog(Bundle saveInstanceState){
        f= false;
            final Calendar c=Calendar.getInstance();
            int year =c.get(Calendar.YEAR);
            int month =c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
           // return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(),year,month,day);

        }
    public void setObject(Button v) {
        this.btn = v;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        populateSetDate(calendar);

    }

    public void populateSetDate(final Calendar calendar) {
        Date newDate = calendar.getTime();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-dd-MM");
        date = spf.format(newDate);
        f=true;


    }
    public String getDate(){

        return date;
    }
}
