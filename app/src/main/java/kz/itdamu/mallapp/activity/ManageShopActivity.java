package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.ManageShopAdapter;
import kz.itdamu.mallapp.entity.Shop;
import kz.itdamu.mallapp.entity.User;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.helper.SQLiteHandler;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.ShopApi;

public class ManageShopActivity extends BaseActivity {

    private Toolbar toolbar;
    private List<Shop> shops;
    private RecyclerView mRecyclerView;
    private ManageShopAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    private User user;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shop);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.listShop);
        db = new SQLiteHandler(getApplicationContext());
        shops = new ArrayList<>();
        handler = new Handler();
        if (toolbar != null) {
            toolbar.setTitle(R.string.drawer_item_my_shops);
            setSupportActionBar(toolbar);
            initDrawer(toolbar);
            makeNavigationBackButton();
        }
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        // create an Object for Adapter
        mAdapter = new ManageShopAdapter(shops, mRecyclerView, this);
        user = db.getUserDetails();
        new LoadShops().execute(String.valueOf(user.getId()));
    }

    class LoadShops extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            ShopApi client = ServiceGenerator.createService(ShopApi.class, Helper.API_URL);
            shops = client.getShopsByUser(params[0]);
            mAdapter = new ManageShopAdapter(shops, mRecyclerView, ManageShopActivity.this);
            return null;
        }

        protected void onPostExecute(String unused) {
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
        getMenuInflater().inflate(R.menu.menu_manage_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_shop) {
            Intent intent = new Intent(getApplicationContext(), AddShopActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
