package wassilni.pl.driver.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import modules.ConnectivityReceiver;
import modules.DirectionFinder;
import objects.MyApp;
import wassilni.pl.driver.R;

public class workArea extends FragmentActivity implements OnMapReadyCallback , ConnectivityReceiver.ConnectivityReceiverListener {
    String original;

    private GoogleMap mMap;
    private LatLng center;
    private Circle circle;
    private Marker marker;
public TextView etInfo;
    public String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        original= (String) getTitle();
        setContentView(R.layout.activity_work_area);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etInfo=(TextView)findViewById(R.id.etInfo);

        Intent i= getIntent();
        type=i.getStringExtra("Type");
        if(type.equalsIgnoreCase("pickup"))
        {
            etInfo.setText("الرجاء تحديد مكان البدء");
        }
        else
            etInfo.setText("الرجاء تحديد مكان الوصول");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng Riyadh = new LatLng(24.6796205, 46.6981272);
        //mMap.addMarker(new MarkerOptions().position(Riyadh).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Riyadh, 10));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //Log.d("WorkArea*******","in map onCLICKLISTER @@@@@@@@@@@@@@");
                // TODO Auto-generated method stub
                double lat = point.latitude;
                double lng = point.longitude;
                center = null;
                if(circle !=null) {
                    circle.remove();
                    marker.remove();
                }
                //center = new LatLng(lat,lng);// PARAMETER point;
                center = point;

                if (center != null) {
                    circle = mMap.addCircle(
                            new CircleOptions()
                                    .center(center)
                                    .radius(2500)
                                    .fillColor(Color.argb(50, 255, 180, 0))
                                    .strokeColor(Color.GRAY)
                                    .strokeWidth(5f)
                    );
                    marker=mMap.addMarker(new MarkerOptions().position(center).icon(BitmapDescriptorFactory.fromResource(R.drawable.pushpin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12));
                    if(type.equalsIgnoreCase("pickup"))
                    {
                        FragmentThree.pickupET.setText(DirectionFinder.extractLatLng(center));
                        Toast.makeText(getApplicationContext(),"تم تحديد مكان البدء",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        FragmentThree.dropoffET.setText(DirectionFinder.extractLatLng(center));
                        Toast.makeText(getApplicationContext(),"تم تحديد مكان الوصول",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }


            }
        });


        // for testing only!
        //LatLng within = new LatLng(24.686453, 46.586021);
        //LatLng without = new LatLng(24.695055, 46.676377);
        //mMap.addMarker(new MarkerOptions().position(within));
        //mMap.addMarker(new MarkerOptions().position(without));
        //Log.d("WorkArea*********","point within equals: "+checkDistance(Riyadh,within)+"\t point without equlas: "+checkDistance(Riyadh,without)+"  meters");

    }
/*
* This method TAKES two LatLng and RETURNS the distance between them in meters.
* */
    /*public float checkDistance(LatLng first, LatLng second){

        Location a,b;
        a= new Location("");
        a.setLatitude(first.latitude);
        a.setLongitude(first.longitude);

        b= new Location("");
        b.setLatitude(second.latitude);
        b.setLongitude(second.longitude);

        return a.distanceTo(b);
    }*/

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
