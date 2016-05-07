package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.entity.Category;
import kz.itdamu.mallapp.entity.Mall;
import kz.itdamu.mallapp.entity.Shop;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.rest.CategoryApi;
import kz.itdamu.mallapp.rest.MallApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.ShopApi;

public class ManageGoodsActivity extends BaseActivity {

    private Toolbar toolbar;
    private FloatingActionButton btnAddGoods;
    private TextView txtShopDescription;
    private TextView txtShopNumber;
    private TextView txtShopPhone;
    private TextView txtShopSite;
    private TextView txtShopCategory;
    private TextView txtShopMall;
    private TextView txtControlDisplay;
    private CardView shopCardView;

    private String shopId;

    private Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_goods);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            initToolbar(toolbar);
            makeNavigationBackButton();
        }
        Intent intent = getIntent();
        if(intent!=null){
            shopId = intent.getStringExtra("shopId");
        }
        btnAddGoods = (FloatingActionButton)findViewById(R.id.fab);
        txtShopDescription = (TextView)findViewById(R.id.shop_description);
        txtShopNumber = (TextView)findViewById(R.id.shop_number);
        txtShopPhone = (TextView)findViewById(R.id.shop_phone);
        txtShopSite = (TextView)findViewById(R.id.shop_site);
        txtShopCategory = (TextView)findViewById(R.id.shop_category);
        txtShopMall = (TextView)findViewById(R.id.shop_mall);
        txtControlDisplay = (TextView)findViewById(R.id.controlShopDisplay);
        shopCardView = (CardView)findViewById(R.id.shop_card_view);
        new LoadShop().execute(shopId);

        txtControlDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopCardView.getVisibility()==View.GONE){
                    shopCardView.setVisibility(View.VISIBLE);
                } else {
                    shopCardView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_goods, menu);
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

    class LoadShop extends AsyncTask<String, String, String> {
        Mall mall;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... params) {
            ShopApi client = ServiceGenerator.createService(ShopApi.class, Helper.API_URL);
            shop = client.getShopById(shopId);
            MallApi clientMall = ServiceGenerator.createService(MallApi.class, Helper.API_URL);
            mall = clientMall.getMall(shop.getMall_id()+"");
            return null;
        }
        protected void onPostExecute(String unused) {
            if (shop != null) {
                toolbar.setTitle(shop.getTitle());
                txtShopDescription.setText(shop.getDescription());
                txtShopNumber.setText(shop.getNumber_shop());
                String phoneStr = shop.getMain_phone();
                if(!Helper.isEmpty(shop.getExtra_phone())) phoneStr += "\n"+shop.getExtra_phone();
                txtShopPhone.setText(phoneStr);
                txtShopSite.setText(shop.getSite());
                String shopCategoryNames = "";
                for(int i=0; i<shop.getCategories().size(); i++){
                    shopCategoryNames += shop.getCategories().get(i).getTitle();
                    if(i+1 < shop.getCategories().size()){
                        shopCategoryNames += " | ";
                    }
                }
                txtShopCategory.setText(shopCategoryNames);
                txtShopMall.setText(mall.getName());
            }
        }
    }
}
