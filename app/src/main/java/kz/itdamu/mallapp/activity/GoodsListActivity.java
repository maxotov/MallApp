package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.GoodsAdapter;
import kz.itdamu.mallapp.entity.Goods;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.rest.GoodsApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;


public class GoodsListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<Goods> goodsList;
    private RecyclerView mRecyclerView;
    private GoodsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private String shopId;
    private String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        mRecyclerView=(RecyclerView)findViewById(R.id.listGoods);
        progressBar=(ProgressBar)findViewById(R.id.progressbar_loading);

        Intent intent=getIntent();
        shopId=intent.getStringExtra("shopId");
        shopName=intent.getStringExtra("shopName");

        goodsList=new ArrayList<>();
        if(toolbar!=null){
            toolbar.setTitle(shopName);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new GoodsAdapter(goodsList,mRecyclerView,this);
        new LoadShops().execute(shopId);
    }

    class LoadShops extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {
            GoodsApi client = ServiceGenerator.createService(GoodsApi.class, Helper.API_URL2);
            goodsList = client.getGoodsByShop("fieldByShopId", params[0]);
            mAdapter = new GoodsAdapter(goodsList, mRecyclerView, GoodsListActivity.this);
            return null;
        }

        protected void onPostExecute(String unused) {
            progressBar.setVisibility(View.GONE);

            mRecyclerView.setAdapter(mAdapter);
            if (goodsList.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
            }

        }
    }
}