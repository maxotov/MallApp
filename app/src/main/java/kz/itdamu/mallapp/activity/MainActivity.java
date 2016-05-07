package kz.itdamu.mallapp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.MallAdapter;
import kz.itdamu.mallapp.entity.Mall;
import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.helper.SessionManager;
import kz.itdamu.mallapp.rest.MallApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.UserApi;
import retrofit.Callback;
import retrofit.RetrofitError;


public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private List<Mall> malls;
    private RecyclerView mRecyclerView;
    private MallAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    private ProgressBar progressBar;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleCloudMessaging gcmObj;
    private String regId = "";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.listMall);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_loading);
        sessionManager = new SessionManager(getApplicationContext());
        if (toolbar != null) {
            toolbar.setTitle("MallApp");
            setSupportActionBar(toolbar);
            initDrawer(toolbar);
        }
        malls = new ArrayList<>();
        handler = new Handler();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        // create an Object for Adapter
        mAdapter = new MallAdapter(malls, mRecyclerView, this);
        new LoadMalls().execute();
        if(Helper.isEmpty(sessionManager.getDeviceId())){
            registerInBackground();
        }
    }

    class LoadMalls extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {
            MallApi client = ServiceGenerator.createService(MallApi.class, Helper.API_URL);
            malls = client.getMalls();
            mAdapter = new MallAdapter(malls, mRecyclerView, MainActivity.this);
            return null;
        }

        protected void onPostExecute(String unused) {
            progressBar.setVisibility(View.GONE);
            mRecyclerView.setAdapter(mAdapter);
            if (malls.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    // AsyncTask to register Device in GCM Server
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(getApplicationContext());
                    }
                    regId = gcmObj.register(Helper.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!Helper.isEmpty(regId)) {
                    UserApi api = ServiceGenerator.createService(UserApi.class, Helper.API_URL);
                    api.registerBasicUser(regId, new Callback<Message>() {
                        @Override
                        public void success(Message message, retrofit.client.Response response) {
                            //Log.e("success", String.valueOf(message.isError());
                            Log.e("message", message.getMessage());
                            if (!message.isError()) {
                                sessionManager.setDevice(regId);
                                Toast.makeText(getApplicationContext(), "User Device successfully created", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
                    Toast.makeText(
                            getApplicationContext(),
                            "Registered with GCM Server successfully.nn"
                                    + msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
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
            /**Toast.makeText(
             getApplicationContext(),
             "This device supports Play services, App will work normally",
             Toast.LENGTH_LONG).show();*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
