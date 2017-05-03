package wassilni.pl.driver.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import objects.MyApp;
import objects.Passenger;
import wassilni.pl.driver.R;

@TargetApi(Build.VERSION_CODES.N)
public class FragmentSixth extends Fragment {
    public static final String MY_JSON = "SendComplaint";//MY_JSON
    String url="http://wassilni.com/db/GetAllReqDriver.php";
    String[] name;
    String sJson;
    String ID_Passenger=null;
    String Complaint;
    EditText complaint;
    ArrayList<String> PassengerN;
    ArrayList<Passenger> passengerInfo;
    Passenger passengerObject;
    final Calendar calendar = Calendar.getInstance();
    private static final String JSON_ARRAY = "result";


    private int TRACK = 0;
    private JSONArray users = null;
    Spinner PassengersName;
    Button send;
    String D_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sixth, container, false);
        super.onCreate(savedInstanceState);

        PassengersName = (Spinner) view.findViewById(R.id.spinner);
        passengerInfo = new ArrayList<Passenger>();
        PassengerN = new ArrayList<String>();

        complaint = (EditText) view.findViewById(R.id.edit_texto);
        send = (Button) view.findViewById(R.id.send);
        D_ID= MyApp.driver_from_session.getID()+"";

        getJSON(url);
        return view;
    }


    private void showData() {
        try {
            JSONObject jsonObject;

            int counter = 1;
            name = new String[users.length() + 1];
            passengerObject = new Passenger(0, "شكوى", "عامة");
            passengerInfo.add(passengerObject);
            name[0] = "شكوى عامة";
            while (TRACK < users.length()) {
                jsonObject = users.getJSONObject(TRACK);
                String fName = jsonObject.getString("P_F_Name");
                String lName = jsonObject.getString("P_L_Name");
                int P_ID=Integer.parseInt(jsonObject.getString("P_ID"));
                name[counter] = fName + " " + lName;
                passengerObject = new Passenger(P_ID, fName, lName);
                passengerInfo.add(passengerObject);
                counter++;
                TRACK++;

            }
            for (int i = 0; i < name.length; i++) {
                //PassengerN.add(name[TRACK]);
                PassengerN.add(name[i]);

            }



            spinner_fn();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJSON() {
        try {
            JSONObject jsonObject = new JSONObject(sJson);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            showData();
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    private void spinner_fn() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, PassengerN);
        PassengersName.setAdapter(dataAdapter);
        PassengersName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PassengersName.setSelection(position);
                String driverSelected = parent.getItemAtPosition(position).toString();
                    System.out.println(driverSelected);

              if (!driverSelected.isEmpty()) {
                    for (int i = 0; i < passengerInfo.size(); i++) {
                        String passName =passengerInfo.get(i).getFName() +" "+passengerInfo.get(i).getLName();
                        if (passName.equals(driverSelected)) {

                            ID_Passenger = passengerInfo.get(i).getID() + "";
                            System.out.println(ID_Passenger);
                           // Complaint = complaint.getText().toString().trim();

                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Complaint = complaint.getText().toString().trim();
                                    if(Complaint.length()!=0){
                                        SendComplaint(ID_Passenger, Complaint);
                                        Toast.makeText(getActivity(), "تم إرسال الشكوى للإدارة ",Toast.LENGTH_SHORT).show();
                                        complaint.setText("");

                                    }
                                    else{
                                        Toast.makeText(getActivity(), "الرجاء كتابة الشكوى  ",Toast.LENGTH_SHORT).show();
                                        complaint.setError("الرجاء كتابة الشكوى ");
                                    }


                                }
                            });
                        }
                    }
                    // Complaint=complaint.getText().toString().trim();
                    // ID_Driver=null;
                    //SendComplaint(ID_Driver,Complaint);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
    }


    private void SendComplaint(String id, String comp) {
        final String methods = "sendComplaint";
        String time=setCurrentTime();
        String D_id=MyApp.driver_from_session.getID()+""; // login session
       // System.out.println("in fragment four : "+D_id + comp + id + time);

        backgroundTask b = new backgroundTask(getActivity());
        b.execute(methods, D_id, comp,id,time);

    }




     @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("D_ID","UTF-8")+"="+URLEncoder.encode(D_ID,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream Is=httpURLConnection.getInputStream();
                    bufferedReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                    String response="";
                    String line="";
                    while ((line=bufferedReader.readLine())!=null){
                        response+=line;
                    }
                    bufferedReader.close();
                    Is.close();
                    httpURLConnection.disconnect();
                    return response;

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                sJson=s;
               // Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                extractJSON();

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }//end getJson class

    private String setCurrentTime() {
        String timeFormat = "hh:mm:ss a";
        SimpleDateFormat sdf=new SimpleDateFormat(timeFormat,Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


}//end



   /* private void SendComplaint(String id, String comp) {
        final String methods = "sendComplaint";
        // String date = setCurrentDate();
        String time=setCurrentTime();
        String Pass_id="3"; // login session
        backgroundTask b = new backgroundTask(getActivity());
        b.execute(methods, id, comp,Pass_id,time);

    }

    private String setCurrentDate() {

        String dateFormat = "yyyy-mm-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());

        return sdf.format(calendar.getTime());
    }


    private String setCurrentTime() {
        String timeFormat = "hh:mm:ss a";
        SimpleDateFormat sdf=new SimpleDateFormat(timeFormat,Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

}*/
