package wassilni.pl.driver.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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

import wassilni.pl.driver.R;
import objects.MyApp;


public class FragmentOne extends Fragment implements View.OnClickListener{

   // @InjectView(R.id.circleLayout)
   // LinearLayout circleLayout;

    public static final String MY_JSON ="GetDriv";//MY_JSON
    TextView TV_NumberOfRater, TV_Name, TV_phoneNum,TV_model, TV_age, TV_nationality, TV_companyNam,TV_carColor, TV_carType, TV_carComp, TV_plateNum, TV_capacity, TV_manufactured, TV_female;
    /*String url="http://192.168.27.2/wasilny/GetDriv.php";
    String sJson;
    backgroundTask b;
    private static final String JSON_ARRAY ="result";// the string must be the same as the name of the json object in the php file
    private static final String FName = "D_F_Name";// the string must be the same as the key name in the php file
    private static final String LName= "D_L_Name";// the string must be the same as the key name in the php file
    private static final String phoneNum = "D_Phone";// the string must be the same as the key name in the php file
    private static final String age = "age";// the string must be the same as the key name in the php file
    private static final String nationality= "nationality";// the string must be the same as the key name in the php file
    private static final String compmanyName= "compName";// the string must be the same as the key name in the php file
    private static final String female = "female_companion";// the string must be the same as the key name in the php file
    private static final String carType= "carType";// the string must be the same as the key name in the php file
    private static final String carComp= "carComp";// the string must be the same as the key name in the php file
    private static final String carModel= "carModel";// the string must be the same as the key name in the php file
    private static final String carColor= "carColor";// the string must be the same as the key name in the php file
    private static final String plateNum= "plateNum";// the string must be the same as the key name in the php file
    private static final String capacity= "capacity";// the string must be the same as the key name in the php file
    private static final String seatAval= "seatAval";// the string must be the same as the key name in the php file
*/

    private int TRACK = 0;
    private JSONArray users = null;
    String url ="http://wassilni.com/db/getRating.php";
    String sJson;
    private static final String JSON_ARRAY ="result";// the string must be the same as the name of the json object in the php file
    private static final String d_ID="D_ID";
    private static final String Rating="Ratting";
    private static final String numberRater="numberOfRetter";
    RatingBar ratingBar;
    private JSONArray rating = null;
    Typeface tf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
            Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_one, containter, false);
      // ButterKnife.inject(this, view);
       // ((GradientDrawable) circleLayout.getBackground())
              //  .setColor(getResources().getColor(R.color.material_blue));
        getActivity().setTitle("SDFFFFFFFFFFFFFFFFFFFFFFFF");
        //tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gs-Light.ttf");


        TV_Name=(TextView)view.findViewById(R.id.driveNameET);
        TV_phoneNum=(TextView)view.findViewById(R.id.driverNumET);
        TV_age=(TextView)view.findViewById(R.id.diverAgeET);
        TV_nationality=(TextView)view.findViewById(R.id.nationalityET);
        TV_companyNam=(TextView)view.findViewById(R.id.companyName);
        TV_carType=(TextView)view.findViewById(R.id.carType);
        TV_carComp =(TextView)view.findViewById(R.id.carCompanyET);
        TV_plateNum=(TextView)view.findViewById(R.id.PlateNumET);
        TV_capacity=(TextView)view.findViewById(R.id.capacityTV);
        TV_manufactured=(TextView)view.findViewById(R.id.manufacturedTV);
        TV_female=(TextView)view.findViewById(R.id.femaleTV);
        TV_model=(TextView)view.findViewById(R.id.modelNum);
        TV_carColor=(TextView)view.findViewById(R.id.carColorET);
        ratingBar= (RatingBar)view.findViewById(R.id.ratingBar);
        ratingBar.setClickable(false);
        TV_NumberOfRater=(TextView)view.findViewById(R.id.numberOfRaeter);
        setFont(TV_Name);
        setFont(TV_phoneNum);
        setFont(TV_age);
        setFont(TV_nationality);
        setFont(TV_companyNam);
        setFont(TV_carType);
        setFont(TV_carComp);
        setFont(TV_carColor);
        setFont(TV_manufactured);
        setFont(TV_capacity);
        setFont(TV_plateNum);

if (MyApp.driver_from_session != null){// if there is data in the session
        showData();
    getJSON(url);
System.out.println((MyApp.driver_from_session!=null)? "********************* not null" : "************** null!");
}
        else {
    System.out.println((MyApp.driver_from_session != null) ? "ee********************* not null" : "ee************** null!");
    startActivity(new Intent(getActivity(), login.class));

        //getActivity().finish();
}
        return view;
    }

//change the font of the text
    public void setFont (TextView tv){

        tv.setTypeface(tf);
    }


    /*
    * this method to show the data from the json ()
    *
    *
    *
    *
    * */
    private void showData(){
                /*
        * Since session already retrieved from login class,
        * no need to retrieve the data from the DB, instead get it from session.
        * using the global variable MyApp.driver_form_session
        * */


        /*try {
            JSONObject jsonObject = users.getJSONObject(TRACK);
            String fullName = jsonObject.getString(FName)+" "+jsonObject.getString(LName);
*/
        System.out.println("%%%%%%%%%%%%%%%%%%% "+MyApp.driver_from_session.toString());
            TV_Name.setText(MyApp.driver_from_session.getFName()+" "+MyApp.driver_from_session.getLName());
            TV_phoneNum.setText(MyApp.driver_from_session.getPhone());
            TV_age.setText(MyApp.driver_from_session.getAge());
            TV_nationality.setText(MyApp.driver_from_session.getNationality());
        if (MyApp.driver_from_session.getFemaleCompanion() == 'y') {
            TV_female.setText("نعم");
        }
      else
            TV_female.setText("لا");

        TV_companyNam.setText(MyApp.driver_from_session.getCompany());

        TV_carType.setText(MyApp.driver_from_session.getCar().getType());
        TV_model.setText(MyApp.driver_from_session.getCar().getModel()+"");
        TV_carColor.setText(MyApp.driver_from_session.getCar().getColor());
        TV_carComp.setText(MyApp.driver_from_session.getCar().getCompany());
        TV_plateNum.setText(MyApp.driver_from_session.getCar().getPlate());
        TV_capacity.setText(MyApp.driver_from_session.getCar().getCapacity()+"");
        TV_manufactured.setText(MyApp.driver_from_session.getCar().getYearOfManufacture()+"");

        /*} catch (JSONException e) {
            e.printStackTrace();
        }*/


    }

    private void extractJSON(){

        try {
            JSONObject jsonObject = new JSONObject(sJson);
            rating = jsonObject.getJSONArray(JSON_ARRAY);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Float RatingV;
        JSONObject jsonObject;
        try {
        for (int i = 0; i < rating.length(); i++) {


            jsonObject = rating.getJSONObject(0);


            RatingV = Float.parseFloat(jsonObject.getString(Rating));


            ratingBar.setRating(RatingV);
            TV_NumberOfRater.setText(TV_NumberOfRater.getText() + jsonObject.getString(numberRater));

        }}
             catch (JSONException e) {
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
                loading = ProgressDialog.show(getActivity(), "Please Wait...",null,true,true);// CODE STUCK HERE !
                System.out.println("########################## F1 , 1111");
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String D_ID = MyApp.driver_from_session.getID()+"";
                System.out.println(D_ID+" $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  drrriver ID");
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);//take the url for the php in case we want to retrives the driver schedule
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("D_ID", "UTF-8") + "=" + URLEncoder.encode(D_ID, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream Is = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(Is, "iso-8859-1"));
                    String response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    Is.close();
                    httpURLConnection.disconnect();
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                sJson=s;
               extractJSON();





            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }//end getJson class


    @Override
    public void onClick(View v) {

    }
}
















