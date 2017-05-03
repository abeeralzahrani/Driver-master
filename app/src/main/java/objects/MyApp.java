package objects;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.net.InetAddress;

import modules.ConnectivityReceiver;
import wassilni.pl.driver.Manifest;
import wassilni.pl.driver.R;

/**
 * Created by Najla AlHazzani on 11/24/2016.
 */

/*
* This class will hold variables/methods that are needed within many classes.
* to reduce code redundancy.
* */
public class MyApp extends Application {
    //upload changes on new github repo

    public static int D_ID;
    public static Driver driver_from_session = null;// this will be retreived from XML.
    public static Driver driver_from_DB = null;// this will be used as reference for GetJSON class in Driver.
    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

/*This method is called to check that user's device is connected to the internet, returns true if connected, false otherwise
* RESOURCE: http://stackoverflow.com/questions/9570237/android-check-internet-connection
* */
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //for ping only
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
    /*
    * This method is used to convert location from MySQL format, to JAVA format.
    * */
    public static LatLng getLatlng(String point)
    {
        String [] sp;
        LatLng result;
        String latlng;
        latlng = point;
        latlng=latlng.substring(latlng.indexOf('(')+1, latlng.indexOf(')'));
        //System.out.println(latlng);
        sp=latlng.split("\\s+");
        result= new LatLng(Double.parseDouble(sp[0]),Double.parseDouble(sp[1]));
        return result;
    }
    public static void showFeedback(Activity act, String original,boolean onResuem)
    {
        //RESOURCE: http://www.androidhive.info/2012/07/android-detect-internet-connection-status/
        String msg="";
        if(ConnectivityReceiver.isConnected()) {
            if(onResuem) {
                msg = "تم الاتصال بالانترنت, يمكنك المتابعة";
                act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                act.setTitle(original);
                Toast.makeText(act.getApplicationContext(),msg , Toast.LENGTH_SHORT).show();
            }
        }
        else {
            msg="لا يوجد اتصال بالانترنت , تم تعطيل اللمس";
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            act.getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED,WindowManager.LayoutParams.ALPHA_CHANGED);
            act.setTitle("(غير متصل بالانترنت) وصّلني");
            Toast.makeText(act.getApplicationContext(),msg , Toast.LENGTH_SHORT).show();
        }


    }
}
