package modules;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/*
* This class used to create a path.
*       in this class the values passed to the constructor are LatLng of the:
*               - user's current location.
*               - pickup/dropoff location of each passenger registered in this schedule.
*               - the final destination specified by the driver.
*
*       a request will be sent to google direction API
*       and then the response will be parsed from json and manipulated.
*
* RESOURCE: https://github.com/hiepxuan2008/GoogleMapDirectionSimple/
* */
public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyCpZJ38Qiq-ZTHtV_lcf8UDj5M32UVKnsI";
    private DirectionFinderListener listener;
    private LatLng origin;
    private ArrayList<LatLng> pickups;
    private ArrayList<LatLng> dropoffs;
    private LatLng destination;
    Context ctx;

    public DirectionFinder(Context c ,DirectionFinderListener listener, LatLng destination, LatLng origin, ArrayList<LatLng> picks, ArrayList<LatLng> drops) {
        this.listener = listener;
        this.pickups =/*(ArrayList<LatLng>)*/ picks;// reference.
        this.dropoffs =drops;
        this.destination = destination;
        this.origin = origin;
        ctx=c;
        Log.d("DirectionFinder","an object of class Direction finder been created!!! *******************");
    }

    public void execute() throws UnsupportedEncodingException {
        if(this.pickups.size()>0) {
            listener.onDirectionFinderStart();
            new DownloadRawData().execute(createUrl());
        }
        else
            Toast.makeText(ctx,"لا يوجد ركاب مسجلين في هذه الفترة",Toast.LENGTH_LONG).show();
    }

    private String createUrl() throws UnsupportedEncodingException {
        /*
        * loop through the array and encode every point.*/
        String appendedPoints="",appendedDrops="";
        for (LatLng point : pickups){
        appendedPoints+=extractLatLng(point)+"|";
            Log.d("DirectionFinder","appendedPoints value is: "+appendedPoints);
        }

        for (LatLng point : dropoffs){
            appendedDrops+=extractLatLng(point)+"|";
        }
        // length()-1 to get rid of the | symbol at the end.
        String urlPoints = URLEncoder.encode(String.valueOf(appendedPoints.substring(0,(appendedPoints.length())-1)), "utf-8");
        String urldrops = URLEncoder.encode(String.valueOf(appendedDrops.substring(0,(appendedDrops.length())-1)), "utf-8");
        String urlOrigin = URLEncoder.encode(extractLatLng(origin), "utf-8");
        String urlDestination = URLEncoder.encode(extractLatLng(destination), "utf-8");

        String myURL= DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination +"&departure_time=now&waypoints=optimize:true|"+ urlPoints+"|"+appendedDrops+"&traffic_model=pessimistic"+"&key=" + GOOGLE_API_KEY;
        Log.d("DirectionFinder",myURL);
        return myURL;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            //if(MyApp.isInternetAvailable()){
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("DirectionFinder","doInBackground finished!!! *******************");
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("DirectionFinder","onPostExecute finished!!! *******************");
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null) {
            Log.d("DirectionFinder","data variable is empty!!! *******************");
            return;
        }
        //System.out.println("@@@@@@"+data);
        //if(!data.contains("ZERO_RESULTS")) {
            List<Route> routes = new ArrayList<Route>();
            JSONObject jsonData = new JSONObject(data);
            JSONArray jsonRoutes = jsonData.getJSONArray("routes");
            //for (int i = 0; i < jsonRoutes.length(); i++) {
            //Log.d("#################################","Routes Length = "+jsonRoutes.length());
            JSONObject jsonRoute = jsonRoutes.getJSONObject(0);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            //JSONObject jsonLeg = jsonLegs.getJSONObject(0);// was 0 instead of i
            JSONObject jsonLeg = new JSONObject();
            JSONObject jsonDuration = new JSONObject();
            JSONObject jsonDistance = new JSONObject();
            int durVal = 0;
            double disVal = 0;
            for (int j = 0; j < jsonLegs.length(); j++) {
                Log.d("#########", "Legs Length = " + jsonLegs.length());
                jsonLeg = jsonLegs.getJSONObject(j);// was 0 instead of i
                jsonDuration = jsonLeg.getJSONObject("duration");
                String str = jsonDuration.getString("text");
                durVal += Integer.parseInt(str.substring(0, str.indexOf('m') - 1));
                jsonDistance = jsonLeg.getJSONObject("distance");
                String str2 = jsonDistance.getString("text");
                disVal += Double.parseDouble(str2.substring(0, str2.indexOf(' ')));
                Log.d("###################", jsonDuration.getString("text"));
            }
            //jsonDistance = jsonLeg.getJSONObject("distance");
            //JSONObject jsonDuration = jsonLeg.getJSONObject("duration");


            //Log.d("#################################"," "+val);
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            route.distance = disVal;//new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = durVal;//new Duration(jsonDuration.getString("text"), val);
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            route.points = decodePolyLine(overview_polylineJson.getString("points"));

            routes.add(route);
            //}
            Log.d("DirectionFinder", "parseJSON works ,, calling onDirectionSuccess!!! *******************");
            listener.onDirectionFinderSuccess(routes);
        //}//end if zero results
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
/*
* This method converts LatLng object into String of the format "Lat,Lng"
* */
    public static String extractLatLng(LatLng s){
        String str=s.toString().substring(s.toString().indexOf('(',0)+1,s.toString().indexOf(')',0));
        str= str.replaceAll("\\s+","");

        return str;
    }

    public ArrayList<LatLng> getPoints() {
        return pickups;
    }

    public void setPoints(ArrayList<LatLng> points) {
        this.pickups = points;
    }
}
