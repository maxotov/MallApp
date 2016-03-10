package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import kz.itdamu.mallapp.adapter.ShopAdapter;
import kz.itdamu.mallapp.entity.Mall;
import kz.itdamu.mallapp.entity.Shop;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.ShopApi;

public class ShopActivity extends BaseActivity {

    private Toolbar toolbar;
    private List<Shop> shops;
    private RecyclerView mRecyclerView;
    private ShopAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    private ProgressBar progressBar;
    private String mallId;
    private String mallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.listShop);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_loading);
        Intent intent = getIntent();
        mallId = intent.getStringExtra("id");
        mallName = intent.getStringExtra("title");
        shops = new ArrayList<>();
        handler = new Handler();
        if (toolbar != null) {
            toolbar.setTitle(mallName);
            setSupportActionBar(toolbar);
            initDrawer(toolbar);
            makeNavigationBackButton();
        }
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        // create an Object for Adapter
        mAdapter = new ShopAdapter(shops, mRecyclerView, this);
        new LoadShops().execute(mallId);
    }

    class LoadShops extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {
            ShopApi client = ServiceGenerator.createService(ShopApi.class, Helper.API_URL);
            shops = client.getShopsByMall(params[0]);
            mAdapter = new ShopAdapter(shops, mRecyclerView, ShopActivity.this);
            return null;
        }

        protected void onPostExecute(String unused) {
            progressBar.setVisibility(View.GONE);
            mRecyclerView.setAdapter(mAdapter);
            if (shops.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop, menu);
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
