package wassilni.pl.driver.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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
import java.util.concurrent.ExecutionException;

import objects.MyApp;
import wassilni.pl.driver.R;

public class FragmentTwo extends Fragment implements View.OnClickListener {

       // @InjectView(R.id.circleLayout)
        //LinearLayout circleLayout;

        public static final String MY_JSON ="GetReqPas";//MY_JSON
        String url="http://wassilni.com/db/GetReqPas.php";
        String sJson;
        TextView name , startD,endD,picupL ,dropOffL,time;
    // the string must be the same as the key name in the php file
        private static final String JSON_ARRAY ="result";
        private static final String FName = "P_F_Name";
        private static final String LName= "P_L_Name";
        private static final String startDate = "startD";
        private static final String endDate  = "endD";
        private static final String picupLoc= "picL";
        private static final String dropoffLoc = "dropL";
        private static final String picupAddress = "picAdd";
        private static final String dropAdress = "dropAdd";
        private static final String timeR= "time";
        private static final String RID= "R_ID";
        private static final String dayPrice= "dayPrice";
        private static final String monthPrice= "monthPrice";
        private static final String S_ID= "S_ID";
        private static final String bookedSeat= "BookedSeat";
        private static final String DESTINATION= "destination";

    String sid,bookedseats,dayp,monthp,dest,pick,drop,timee;
        private int TRACK = 0;
    String R_ID,D_ID;
    LinearLayout linearLayout;

        private JSONArray users = null;
        ImageButton btnprev,btnnext;
        TextView scheduleInfo;
        Button edit,acc;
        TextView title ;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_two, containter, false);
            btnnext=(ImageButton) view.findViewById(R.id.btnNext);
            btnnext.setOnClickListener(this);
            btnprev=(ImageButton) view.findViewById(R.id.btnPrev);
            btnprev.setOnClickListener(this);
            edit = (Button) view.findViewById(R.id.rejectB);
            acc = (Button) view.findViewById(R.id.acceptB);
            edit.setOnClickListener(this);
            acc.setOnClickListener(this);
            name=(TextView)view.findViewById(R.id.NameTV);
            startD=(TextView)view.findViewById(R.id.startDateTV);
            endD=(TextView)view.findViewById(R.id.endDateTV);
            picupL=(TextView)view.findViewById(R.id.pickUpLTV);
            dropOffL=(TextView)view.findViewById(R.id.dropOffLTV);
            time=(TextView)view.findViewById(R.id.timeTV);
            title=(TextView)view.findViewById(R.id.confirmRequestTV);
            D_ID= MyApp.driver_from_session.getID()+"";
            scheduleInfo=(TextView) view.findViewById(R.id.scheduleButton);
            linearLayout=(LinearLayout) view.findViewById(R.id.linerLayout1);

            scheduleInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    putExtras();
                }

            });

            getJSON(url);

            return view;
        }


        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rejectB:
                    rejectReq();
                    getJSON(url);
                    /*getActivity().finish();
                    startActivity(getActivity().getIntent());*/
                    break;
                case R.id.acceptB:
                    confirm();
                    getJSON(url);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    break;
                case R.id.btnNext:

                    moveNext();
                    break;
                case R.id.btnPrev:

                    movePrev();
                    break;
            }
        }
    //to move to next object in database
        private void moveNext(){
            if(TRACK<users.length()-1){
                TRACK++;
            }
            showData();
        }
    //to move to prev object in database
        private void movePrev(){
            if(TRACK>0){
                TRACK--;
            }
            showData();
        }

        public void confirm(){
            String method="confirm";
            backgroundTask backgroundTask = new backgroundTask(getActivity());
            try {
                String result= backgroundTask.execute(method,R_ID,sid).get();
                System.out.println(result);
                if(result.contains("the car capacity in full")){
                    Toast.makeText(getActivity(),"لايمكنك تأكيد الطلب سعة المركبة غير كافية ",Toast.LENGTH_SHORT).show();
                }
                else if(result.contains("Request Updated Successfully")){

                    Toast.makeText(getActivity(),"تم تأكيد الطلب بنجاح ",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getActivity(),"حدث خطأ أثناء إضافة الطلب  ",Toast.LENGTH_SHORT).show();

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    //to reject the request
        public void rejectReq() {





            new AlertDialog.Builder(getContext())
                    .setTitle("تأكيد رفض الطلب")
                    .setMessage("هل أنت متأكد من رفض الطلب؟")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                               /*
                               * Here put the code for deleting the request !!!!!!!!!!!!
                               * */

                           String method = "deleteReq";
                            backgroundTask backgroundTask = new backgroundTask(getActivity());
                            String res;
                            try {

                                res = backgroundTask.execute(method, R_ID, sid).get();
                                System.out.println("R_ID= "+R_ID +"  SID= "+sid);

                            if(res.contains("request Deleted Successfully")){
                                Toast.makeText(getContext(),"تم رفض الطلب",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(),"حدث خطأ أثناء رفض الطلب , الرجاء المحاولة في وقت أخر",Toast.LENGTH_SHORT).show();

                            } }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }})
                    .setNegativeButton(android.R.string.no, null).show();






        }

    JSONObject jsonObject;
    //show the one row from database
        private void showData(){
            try {

                int num= TRACK;

                title.setText("قائمة الطالبات "+(num+1)+"/"+users.length());
                jsonObject = users.getJSONObject(TRACK);
                String fullName = jsonObject.getString(FName)+" "+jsonObject.getString(LName);

                R_ID=jsonObject.getString(RID);
                name.setText(fullName);
                picupL.setText(jsonObject.getString(picupAddress));
                dropOffL.setText(jsonObject.getString(dropAdress));
                startD.setText(jsonObject.getString(startDate));
                endD.setText(jsonObject.getString(endDate));
                time.setText(jsonObject.getString(timeR));
                sid=jsonObject.getString(S_ID);
                bookedseats=jsonObject.getString(bookedSeat);
                dayp=jsonObject.getString(dayPrice);
                monthp=jsonObject.getString(monthPrice);
                dest=jsonObject.getString(DESTINATION);
                pick=jsonObject.getString(picupLoc);
                drop=jsonObject.getString(dropoffLoc);
                timee=jsonObject.getString(timeR);
                System.out.println( sid+"  schedule id in framnet two   and directly from json is   "+jsonObject.getString(S_ID));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    //extract the jason object
        private void extractJSON(){

            System.out.println(sJson);
            try {
                JSONObject jsonObject = new JSONObject(sJson);
                users = jsonObject.getJSONArray(JSON_ARRAY);
                if(users.length()!=0){
                    LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.linerLayout1);

                    linearLayout.setVisibility(View.VISIBLE);
                title.setText("قائمة الطالبات 1/"+users.length());
                showData();}
                else {
                    title.setText("قائمة الطالبات فارغة "+users.length());
                    linearLayout.setVisibility(LinearLayout.GONE);
                    Toast.makeText(getActivity(),"لا يوجد طلبات حالية ",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    Log.d("F2"," getReqPass.php \t"+s);
                    //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                    extractJSON();

                }
            }
            GetJSON gj = new GetJSON();
            gj.execute(url);
        }//end getJson class


    public void putExtras()
    {
        Intent i=new Intent(getActivity(),scheduleInfo.class);
            i.putExtra("S_ID", sid);
            i.putExtra("bookedSeat", bookedseats );
            i.putExtra("dayPrice", dayp);
            i.putExtra("monthPrice", monthp);
            i.putExtra("destination",dest);
            i.putExtra("pickup", pick);
            i.putExtra("dropoff", drop);
            i.putExtra("time", timee);
            startActivity(i);
    }


}//end
