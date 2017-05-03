package wassilni.pl.driver.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import android.location.LocationListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import modules.ConnectivityReceiver;
import modules.DirectionFinder;
import modules.DirectionFinderListener;
import modules.Route;
import objects.MyApp;
import objects.Passenger;
import wassilni.pl.driver.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener , ConnectivityReceiver.ConnectivityReceiverListener {
        String original;

    /*
    *   -retrieve locations of passengers who assigned to a certain SCHEDULE for a certain DRIVER, and represent an optimized path.
    *   -or a simulated path to help the driver to decide if he want to accept/reject the passenger base on it's location.
    * */
    private GoogleMap mMap;
    private Button btnFindPath;
    private TextView tvPath;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;


    // ********************************************************************
    public DirectionFinder directionFinder;
    public LatLng a = new LatLng(24.688857, 46.711454);
    public LatLng d = new LatLng(24.751130, 46.668221);
    public LatLng c = new LatLng(24.770821, 46.716369);
    public LatLng b = new LatLng(24.725910, 46.752387);
    public LatLng userLocation;
    public LatLng destLocation;
    public String extractPoint;
    // ********************************************************************
    public ArrayList<Passenger> passengers = new ArrayList<Passenger>();
    public ArrayList<LatLng> pickupLocations =new ArrayList<LatLng>();
    public ArrayList<LatLng> dropoffLocations =new ArrayList<LatLng>();

    private LocationManager locationManager;
    private LocationListener listener;
    String flag;
    LatLng simDropoff;
    LatLng simPickup;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        original= (String) getTitle();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tvPath=(TextView)findViewById(R.id.tvPath);
        //get data sent from the previous Activity
        Intent i= getIntent();
        flag=i.getStringExtra("flag");

        if( flag.equals("real")) {
            String json = i.getStringExtra("json");
            String time = i.getStringExtra("time");
            //extractPoint = i.getStringExtra("destination");
            //System.out.println("####@@@@@@@@#########@@@@@@@@@######@@@@@@@#########@@##### real \t"+extractPoint);
            //destLocation = MyApp.getLatlng(extractPoint);
            parseJSON(json);
            tvPath.setText(" مسار فترة الساعة "+time);
        }
        else //flag= simulation
        {
            String json = i.getStringExtra("json");
            String time = i.getStringExtra("time");
            extractPoint = i.getStringExtra("destination");
            System.out.println("####@@@@@@@@#########@@@@@@@@@######@@@@@@@#########@@##### simulation \t"+extractPoint);
            destLocation = MyApp.getLatlng(extractPoint);
            simPickup=MyApp.getLatlng(i.getStringExtra("pickup"));
            simDropoff=MyApp.getLatlng(i.getStringExtra("dropoff"));
            parseJSON(json);
            tvPath.setText("محاكاة مسار فترة الساعة "+time);
        }

        // button to trigger searching for a path.
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //configure locationListner and locationManager to get the user's current location
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("Maps","userLocation have changed \t"+userLocation.toString());
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) { // prompt the user to turn on GPS service.
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }//finish onCreate() *******

    private void sendRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //prompt the user to make Wassilni application grant location from the GPS.
            Toast.makeText(getApplicationContext(),"الرجاء تفعيل أذونات الموقع من الإعدادات",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION },0 );//*/
            Log.d("MAP", "PERMISSION ISSUE");
            return;
        }

        //  this triggered by location changes to get user's current location
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.listener);
        Log.d("Maps","after requestLocationUpdates");

        try {
            //to avoid null pointer exceptions check if userLocation != null.
            if (userLocation != null) {
                directionFinder = new DirectionFinder(getApplicationContext(),this,destLocation,userLocation,pickupLocations,dropoffLocations);
                directionFinder.execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "عفواً, لم يتم توصيل GPS حاول بعد ثواني", Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // initially the focus will be on Riyadh city.
        LatLng riyadh = new LatLng(24.6796205, 46.6981272);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(riyadh, 10));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // set my location enabled to assign value to userLocation variable.
        mMap.setMyLocationEnabled(true);

    }


    @Override
    public void onDirectionFinderStart() {
        // This will display a progressDialog to the user until the requested path is ready.
        progressDialog = ProgressDialog.show(this, "ارجو الإنتظار",
                "جاري البحث عن المسار المناسب.", true);
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        /* all passengers, pickuplocations and dropofflocation all indeceis coresspond to the same passengers' info
        * this loop will represent the polyline on the map and the pickup/dropoff markers.
        * it'll also provide info about the total distance and total duration.*/
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 10));
            ((TextView) findViewById(R.id.tvDuration)).setText(" دقائق "+route.duration);
            ((TextView) findViewById(R.id.tvDistance)).setText(" كم " +Math.round(route.distance));

            int sizeOfList=pickupLocations.size();
            // consider these points are retreived from a database. it must be represented on the map as MARKERS.
            if(flag.equals("simulation"))
                sizeOfList--;// decrement the simulated points, because it'll be represented using different marker
            for (int i=0; i< sizeOfList; i++) {
                /* represent pickup & dropoff locations of each passenger and display her name & phone.*/
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pushpin2))
                        .title(passengers.get(i).getFName()+" "+passengers.get(i).getLName())
                        .snippet(" هاتف :"+passengers.get(i).getPhone())
                        .position(pickupLocations.get(i)));

                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.destpushpin2))
                        .position(dropoffLocations.get(i))
                        .title(passengers.get(i).getFName()+" "+passengers.get(i).getLName())
                        .snippet(" هاتف :"+passengers.get(i).getPhone()));
            }
            if(flag.equals("simulation"))
            {
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pushpinsim))
                        .title("محاكاة نقطة انطلاق الراكب")
                        .position(pickupLocations.get(sizeOfList)));

                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pushpinsim))
                        .title("محاكاة نقطة وصول الراكب")
                        .position(dropoffLocations.get(sizeOfList)));
            }
            // to represent the polyline (path) on the map
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(8);
            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));
            }

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //prompt the user to make Wassilni application grant location from the GPS.
            Toast.makeText(getApplicationContext(),"الرجاء تفعيل أذونات الموقع من الإعدادات",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION },0 );//*/
            Log.d("MAP", "PERMISSION ISSUE");
            return;
        }

        //  this triggered by location changes to get user's current location
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.listener);
        Log.d("Maps","after requestLocationUpdates");
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void parseJSON(String s)
    {
        String picloc,droploc;//POINT(24.844278 46.780666) PHP format of a point
        String[] sp;
        LatLng pickup,dropoff;

        try {

            JSONArray auser= null;
            JSONObject j = new JSONObject(s);
            auser=j.getJSONArray("result");
        for (int i = 0 ; i <auser.length() ; i++) {
            JSONObject jsonObject=auser.getJSONObject(i);
            String JSON_ARRAY = "result";
            String p_id = "P_ID";
            String FName = "P_F_Name";
            String LName = "P_L_Name";
            String phone = "P_phone";
            String email = "P_email";
            String school = "school";

            String R_ID = "R_ID";
            String R_pickup = "R_pickup_loc";
            String R_dropoff = "R_dropoff_loc";

            Passenger p=new Passenger();
            p.setID(jsonObject.getInt(p_id));
            p.setFName(jsonObject.getString(FName));
            p.setLName(jsonObject.getString(LName));
            p.setPhone(jsonObject.getString(phone));
            p.setEmail(jsonObject.getString(email));
            p.setSchool(jsonObject.getString(school));

            passengers.add(p);

            picloc = jsonObject.getString(R_pickup);
            pickup= MyApp.getLatlng(picloc);
            droploc = jsonObject.getString(R_dropoff);
            dropoff= MyApp.getLatlng(droploc);

            if(flag.equals("real"))
            destLocation=MyApp.getLatlng(jsonObject.getString("destination"));

            pickupLocations.add(pickup);
            dropoffLocations.add(dropoff);
        }//end for auser.length
            if(flag.equals("simulation"))
            {
                pickupLocations.add(simPickup);
                dropoffLocations.add(simDropoff);
            }
            System.out.println(passengers.toString()+" LatLng : "+pickupLocations.toString());
        } catch (JSONException e) {
            e.printStackTrace();
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