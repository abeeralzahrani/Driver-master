package wassilni.pl.driver.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import objects.MyApp;
import objects.schedule;
import wassilni.pl.driver.R;


/**
 * Created by haya on 10/12/2016.
 */
/*
* Here there will be a query to find registered passengers, and parse the result in a certain SCHEDULE and the LatLng values will be passed
* to the next activity (map) to represent passengers' locations on the map, and their info as snippet
* */

public class FragmentFour extends Fragment implements View.OnClickListener {


   // @InjectView(R.id.circleLayout)
   // LinearLayout circleLayout;
    public String S_dropoff;

    public static final String MY_JSON ="GetReqPas";//MY_JSON
    String url="http://wassilni.com/db/RetriveDrivSchedule.php";
    String sJson;
    String pathurl="http://wassilni.comdbdriverGetPassengers.php";
    private static final String JSON_ARRAY ="result";// the string must be the same as the name of the json object in the php file
    private static final String S_ID = "S_ID";// the string must be the same as the key name in the php file
    private static final String time= "time";// the string must be the same as the key name in the php file
    private static final String endDate = "endDate";
    private static final String startDate ="startDate";
    private static final String bookedSeat="bookedSeat";
    private static final String monthPrice="monthPrice";
    private static final String dayPrice="dayPrice";
    private int TRACK = 0;
    private JSONArray users = null;
    String SID;
    ListView listView;
    int ids[];
    String times[];
    ArrayList<schedule> scheduleArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        scheduleArrayList = new ArrayList<schedule>();//array to save the schedule from the driver
        listView=(ListView)view.findViewById(R.id.list1);
        checkAbsent();
        getJSON(url); //to call the data from database

        return view;
    }

    @Override
    public void onClick(View v) {

    }
   public void checkAbsent(){
    String method="checkAbsent";
    String D_ID = MyApp.driver_from_session.getID()+"";
    backgroundTask b=new backgroundTask(getActivity());
    b.execute(method,D_ID);
}


           /* public void onClick(View v) {
                // query and parseJSON, then go to mapActivity.
                int sched_id=2;
                Background b= new Background();
                try {
                    String result=b.execute(sched_id+"").get();
                    if(!result.equalsIgnoreCase("InternetFailed")){
                    Intent i=new Intent(getActivity(), MapsActivity.class);
                    i.putExtra("json",result);//json will be parsed in the map activity.
                    startActivity(i);
                    //this.finish();
                    }
                    else
                        Toast.makeText(getActivity(),"ارجو التأكد من توصيل الانترنت",Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });*/


   /* @Override
    public void onDestroyView() {
        super.onDestroyView();
       // ButterKnife.reset(this);
    }
*/

    int i_ID;
    //show the one row from database
    private void showData(){
        try {

            JSONObject jsonObject;
          /*  String fullName = jsonObject.getString(FName)+" "+jsonObject.getString(LName);*/
            String id[] = new String[users.length()];
            String S_time, S_startDate, S_endDate;
            int i_bookedSeat, i_monthPrice, i_dayPrice;
            for (int i = 0; i < users.length(); i++) {  //to go over all the
                jsonObject = users.getJSONObject(TRACK);
                /*
                * take the json object as string
                * */
                i_ID = Integer.parseInt(jsonObject.getString(S_ID));
                S_startDate = jsonObject.getString(startDate);
                S_endDate = jsonObject.getString(endDate);
                S_time = jsonObject.getString(time);
                i_bookedSeat = Integer.parseInt(jsonObject.getString(bookedSeat));
                i_monthPrice = Integer.parseInt(jsonObject.getString(monthPrice));
                i_dayPrice = Integer.parseInt(jsonObject.getString(dayPrice));
                S_dropoff = jsonObject.getString("dropoff");
                id[i] = jsonObject.getString(S_ID);
                schedule s = new schedule(i_ID, S_time, S_startDate, S_endDate, i_bookedSeat, i_monthPrice, i_dayPrice);
                scheduleArrayList.add(s);//add the object to array list
                TRACK++;
            }
            custmerAdapter adapter = new custmerAdapter(getActivity(), scheduleArrayList);
            ids=new int [TRACK];
            times=new String [TRACK];
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, id);
            listView.setAdapter(new myList(getActivity(),  scheduleArrayList));
         /*   listView.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                {
                    // query and parseJSON, then go to mapActivity.
                    int sched_id=i_ID;
                    Background b= new Background();
                    try {
                        String result=b.execute(sched_id+"").get();
                        if(!result.equalsIgnoreCase("InternetFailed")){
                            Intent i=new Intent(getActivity(), MapsActivity.class);
                            i.putExtra("json",result);//json will be parsed in the map activity.
                            startActivity(i);
                            //this.finish();
                        }
                        else
                            Toast.makeText(getActivity(),"ارجو التأكد من توصيل الانترنت",Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            });*/

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
                Toast.makeText(getActivity(), "لا يوجد جداول مسجلة  ", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class Background extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Please Wait...",null,true,true);
        }

        @Override
        protected String doInBackground(String... params)
        {
            String uri="http://wassilni.com/db/driverGetPassengers.php";

//the data will be parsed twice, once in MapActivity and once in "passengers registered"
            if(MyApp.isInternetAvailable()) {
                BufferedReader bufferedReader = null;
                try {
                 //   System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ params[0]:"+params[0]);
                    URL url = new URL(uri);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    //[0]type  [1]value
                    String data;
                    //if(params[0].equalsIgnoreCase("ID"))
                    data = URLEncoder.encode("S_ID", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                    //Log.d("F1",data);
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();
                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ in backgroundTask 222222");
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    InputStream Is = httpURLConnection.getInputStream();
                    Is.close();
                    httpURLConnection.disconnect();
                    String result = sb.toString().trim();
                    //System.out.println(result);
                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ in backgroundTask after parsing data");
                    Log.d("F4","DriverGetPassenger.php \t"+result);
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            System.out.println("######################################### Intermetttttttt");
            return "InternetFailed";
        }

        @Override
        protected void onPostExecute(String a )
        {
            super.onPreExecute();
        }

    }// end class AsyncTask





    public void getJSON(String url) {
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
                    String D_ID = MyApp.driver_from_session.getID()+"";
                    URL url = new URL(uri);//take the url for the php in case we want to retrives the driver schedule
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter (outputStream,"UTF-8"));
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
                    e.printStackTrace();
                }
        return null;
                    }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                sJson=s;
                Log.d("F4"," retreiveDriverSchedule.php \t"+s);
            //    Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                extractJSON();

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }//end getJson class

    class myList extends BaseAdapter  {
        ArrayList<schedule> items;
        Context context;
        schedule temp;
        public myList(Context context,ArrayList<schedule> Listitem) {
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
        int sched_id;
        String specificTime;

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {

            LayoutInflater inflater= LayoutInflater.from(context);
            View row=inflater.inflate(R.layout.worktime,parent,false);
            TextView S_id=(TextView) row.findViewById(R.id.S_ID);
            TextView workTime=(TextView) row.findViewById(R.id.TimeTV);
            LinearLayout linearLayout=(LinearLayout) row.findViewById(R.id.linerLayout);
            if(position%2==0)
            {
                linearLayout.setBackgroundColor(Color.argb(15,10,15,10));
            }
            ImageButton passenger=(ImageButton) row.findViewById(R.id.passengersButton);
            ImageButton path=(ImageButton) row.findViewById(R.id.pathButton);

            temp=items.get(position);
             sched_id=temp.getS_ID();
            // specificTime=temp.getTime();
           // System.out.println("-------------------------------------------------------------------------------------------------------------------------"+specificTime);

            ids[position]=temp.getS_ID();//to take the id for the schedule
            times[position]=temp.getTime();


           passenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context,"Passenger Info",Toast.LENGTH_SHORT).show(); //get request id to send absent to her driver
                    Intent i=new Intent(getActivity(), passenger.class);
                    String sID=ids[position]+"";
                   i.putExtra("S_ID",sID);//json will be parsed in the map activity.
                    startActivity(i);

                   // System.out.println("id in saidd passenger "+ids[position]);
                }
            });
            path.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"Path",Toast.LENGTH_SHORT).show(); //get request id to send unSubscribe to her driver
                    // query and parseJSON, then go to mapActivity.

                    System.out.println("schedula id "+ids[position]);
                    Background b= new Background();
                    try {
                        String result=b.execute(ids[position]+"").get();
                        System.out.println("#$#$$##$#$##$#$#$#$##$#$#$#$#$#$#$#$#$#\t"+ids[position]);
                        if(!result.equalsIgnoreCase("InternetFailed")){
                            Intent i=new Intent(getActivity(), MapsActivity.class);
                            i.putExtra("flag","real");
                            i.putExtra("json",result);//json will be parsed in the map activity.
                            i.putExtra("destination",S_dropoff);
                            i.putExtra("time",times[position]);
                            startActivity(i);
                            //this.finish();
                        }
                        else
                            Toast.makeText(getActivity(),"ارجو التأكد من توصيل الانترنت",Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            });



            S_id.setText(temp.getS_ID()+"");
            workTime.setText(temp.getTime());


            return row;
        }


    }

    public String query(String SID)
    {
        String json="nothing";
        Background b=new Background();
        try {
            json= b.execute(SID).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return json;
    }


}