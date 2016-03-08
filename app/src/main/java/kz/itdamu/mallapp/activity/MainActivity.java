package kz.itdamu.mallapp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.MallAdapter;
import kz.itdamu.mallapp.entity.Mall;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.rest.MallApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;


public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private List<Mall> malls;
    private RecyclerView mRecyclerView;
    private MallAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.listMall);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_loading);
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
