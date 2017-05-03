package wassilni.pl.driver.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import modules.ConnectivityReceiver;
import objects.MyApp;
import objects.Passenger;
import wassilni.pl.driver.R;

public class passenger extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener {
    String original;



    String url="http://wassilni.com/db/getAllpassToSchedule.php";
    String sJson;
    private static final String JSON_ARRAY ="result";// the string must be the same as the name of the json object in the php file
    private static final String P_F_name= "P_F_Name";// the string must be the same as the key name in the php file
    private static final String P_L_name= "P_L_Name";
    private static final String P_phone = "P_Phone";
    private static final String P_absent ="absent";
    private int TRACK = 0;
    private JSONArray users = null;
    ListView listView;
    String S_ID="14";
    ArrayList<Passenger> passengersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        original= (String) getTitle();
        setContentView(R.layout.passengers_layout);
        passengersArrayList = new ArrayList<Passenger>();
        listView=(ListView)findViewById(R.id.listPassengers);

        Intent in = getIntent();
        S_ID = in.getStringExtra("S_ID");
        System.out.println(S_ID+" from passenger activity ");
        getJSON(url);

    }


    private void showData(){
        try {

            JSONObject jsonObject;

          // String id[] = new String[users.length()];
            String passenger_F_Name,passenger_L_Name,passengerPhone ,passengerAbsent;
            int i_bookedSeat, i_monthPrice, i_dayPrice;
            for (int i = 0; i < users.length(); i++) {
                jsonObject = users.getJSONObject(TRACK);
                passenger_F_Name = jsonObject.getString(P_F_name);
                //take the passenger info from the json object
                passenger_L_Name=jsonObject.getString(P_L_name);
                passengerPhone = jsonObject.getString(P_phone);
                passengerAbsent = jsonObject.getString(P_absent);
                Passenger p = new Passenger(passenger_F_Name,passenger_L_Name, passengerPhone, passengerAbsent);
               passengersArrayList.add(p);//add to the array list

                TRACK++;//increment to move to next passenger in jason object
            }
           //set the array list of all passenger that corresponding to specific schedule then view it in list view
            listView.setAdapter(new myList(getApplicationContext() ,  passengersArrayList));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //extract the jason object
    private void extractJSON(){
        try {
            JSONObject jsonObject = new JSONObject(sJson);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            if(users.length()!=0)
                showData();
            else
            {
                Toast.makeText(this, "لا يوجد ركاب مسجلين   ", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getApplicationContext(), "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    String D_ID = MyApp.driver_from_session.getID()+"";
                    URL url = new URL(uri);//take the url for the php in case we want to retrives the driver schedule
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter (outputStream,"UTF-8"));
                    String data= URLEncoder.encode("S_ID","UTF-8")+"="+URLEncoder.encode(S_ID,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream Is=httpURLConnection.getInputStream();
                    bufferedReader=new BufferedReader(new InputStreamReader(Is,"iso-8859-1"));
                    String response="";
                    String line="";
                    while ((line=bufferedReader.readLine())!=null)
                    {
                        response+=line;
                    }
                    bufferedReader.close();
                    Is.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              //  loading.dismiss();
                sJson=s;
                System.out.println(s+" jason oject ");
               extractJSON();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }//end getJson class

    class myList extends BaseAdapter {
        ArrayList<Passenger> items;
        Context context;
        Passenger temp;
        public myList(Context context,ArrayList<Passenger> Listitem) {
            this.context=context;
            items=Listitem;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View row = inflater.inflate(R.layout.passenger, parent, false);
            TextView PassN = (TextView) row.findViewById(R.id.nameTV);
            TextView PassPhone = (TextView) row.findViewById(R.id.phoneTV);
            TextView PassAbsent = (TextView) row.findViewById(R.id.absentTV);
            LinearLayout linearLayout=(LinearLayout) row.findViewById(R.id.linearLayout);
            if(position%2==0)
            {
                linearLayout.setBackgroundColor(Color.argb(15,10,15,10));
            }
            temp = items.get(position);
            PassN.setText(temp.getFName()+" "+temp.getLName());
            PassPhone.setText(temp.getPhone());
            if(temp.getAbsent().equals("0")){
            PassAbsent.setText("ليست غائبة ");
            PassAbsent.setTextColor(Color.argb(120,4,204,4));}//alpha , red , green , blue
            else if(temp.getAbsent().equals("1")){

                PassAbsent.setText("غائبة");
                PassAbsent.setTextColor(Color.RED);
            }

            return row;
        }}


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
