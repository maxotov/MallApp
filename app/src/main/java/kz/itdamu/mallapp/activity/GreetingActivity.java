package kz.itdamu.mallapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import kz.itdamu.mallapp.R;

public class GreetingActivity extends BaseActivity {

    private Toolbar toolbar;
    TextView emailET;
    ProgressDialog prgDialog;
    ImageView greetingImage;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Сообщение");
            setSupportActionBar(toolbar);
            initDrawer(toolbar);
            makeNavigationBackButton();
        }
        String json = getIntent().getStringExtra("greetjson");
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        emailET = (TextView) findViewById(R.id.greetingmsg);
        greetingImage = (ImageView)findViewById(R.id.greetimg);

        // Check if Google Play Service is installed in Device
        // Play services is needed to handle GCM stuffs
        if (!checkPlayServices()) {
            Toast.makeText(
                    getApplicationContext(),
                    "This device doesn't support Play services, App will not work normally",
                    Toast.LENGTH_LONG).show();
        }
        // When json is not null
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("greetimgurl", jsonObj.getString("greetImgURL"));
                editor.putString("greetmsg", jsonObj.getString("greetMsg"));
                editor.commit();
                emailET.setText(prefs.getString("greetmsg", ""));
                // Render Image read from Image URL using aquery 'image' method
                //Log.i("IMG URL == ", prefs.getString("greetimgurl", ""));
                Picasso.with(GreetingActivity.this).load(prefs.getString("greetimgurl", "")).placeholder(R.drawable.app_icon).into(greetingImage);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // When Json is null
        else if (!"".equals(prefs.getString("greetimgurl", ""))	&& !"".equals(prefs.getString("greetmsg", "") != null)) {
            emailET.setText(prefs.getString("greetmsg", ""));
            Picasso.with(GreetingActivity.this).load(prefs.getString("greetimgurl", "")).placeholder(R.drawable.app_icon).into(greetingImage);
        }
    }

    // Check if Google Playservices is installed in Device or not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    // When Application is resumed, check for Play services support to make sure
    // app will be running normally
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
}
