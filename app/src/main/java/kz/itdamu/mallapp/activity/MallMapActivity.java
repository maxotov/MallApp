package kz.itdamu.mallapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.helper.DirectionsJSONParser;
import kz.itdamu.mallapp.helper.GPSTracker;
import kz.itdamu.mallapp.helper.Helper;

public class MallMapActivity extends BaseActivity {

    private Toolbar toolbar;
    private GoogleMap googleMap;
    private GPSTracker gpsTracker;
    private Button btnPutRoute;
    double latitude = 0;
    double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnPutRoute = (Button)findViewById(R.id.putRoute);
        Intent intent = getIntent();
        String mallName = intent.getStringExtra("mall_name");
        String lat = intent.getStringExtra("mall_lat");
        String lng = intent.getStringExtra("mall_lng");
        String address = intent.getStringExtra("address");
        if (toolbar != null) {
            toolbar.setTitle(mallName);
            setSupportActionBar(toolbar);
            initDrawer(toolbar);
            makeNavigationBackButton();
        }
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lng);
        try {
            // Loading map
            initilizeMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(mallName).snippet(address);
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(15).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } catch (Exception e) {
            e.printStackTrace();
        }
        btnPutRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE );
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (statusOfGPS)
                {
                    gpsTracker = new GPSTracker(MallMapActivity.this);
                    if(gpsTracker.canGetLocation()) {
                        LatLng origin = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                        LatLng dest = new LatLng(latitude, longitude);
                        String url = getDirectionsUrl(origin, dest);
                        DownloadTask downloadTask = new DownloadTask();
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                        boundsBuilder.include(origin);
                        boundsBuilder.include(dest);
                        LatLngBounds bounds = boundsBuilder.build();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                    }
                } else {
                    Helper.showSettingsGPS(MallMapActivity.this);
                }
            }
        });
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d("Exception url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }
            btnPutRoute.setText(distance + ", "+duration);
            btnPutRoute.setTextColor(Color.WHITE);
            btnPutRoute.setEnabled(false);
            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
}
