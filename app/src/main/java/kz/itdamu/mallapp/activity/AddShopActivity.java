package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.MallAdapter;
import kz.itdamu.mallapp.adapter.SpinMallAdapter;
import kz.itdamu.mallapp.entity.Category;
import kz.itdamu.mallapp.entity.Mall;
import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.entity.User;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.helper.SQLiteHandler;
import kz.itdamu.mallapp.rest.CategoryApi;
import kz.itdamu.mallapp.rest.MallApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.ShopApi;
import retrofit.Callback;
import retrofit.RetrofitError;

public class AddShopActivity extends BaseActivity {

    private Toolbar toolbar;
    private User user;
    private SQLiteHandler db;
    private List<Mall> malls;
    private List<Category> categories;
    String[] mallNames;
    String[] categoryNames;
    private int selectedMallIndex = -1;
    private int selectedCategoryIndex = -1;

    private EditText shopTitle;
    private EditText shopNumber;
    private EditText shopMainPhone;
    private EditText shopExtraPhone;
    private EditText shopSite;
    private EditText shopDescription;
    private TextView txtSelectedMall;
    private TextView txtSelectedCategory;
    private Button btnAddShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Добавление магазина");
            setSupportActionBar(toolbar);
            initToolbar(toolbar);
            makeNavigationBackButton();
        }
        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        shopTitle = (EditText)findViewById(R.id.shop_title);
        shopNumber = (EditText)findViewById(R.id.shop_number);
        shopMainPhone = (EditText)findViewById(R.id.shop_main_phone);
        shopExtraPhone = (EditText)findViewById(R.id.shop_extra_phone);
        shopSite = (EditText)findViewById(R.id.shop_site);
        shopDescription = (EditText)findViewById(R.id.shop_description);
        txtSelectedMall = (TextView)findViewById(R.id.selectMallName);
        txtSelectedCategory = (TextView)findViewById(R.id.selectCategoryName);
        btnAddShop = (Button)findViewById(R.id.btnAddShop);
        new LoadMalls().execute();
        new LoadCategories().execute();
        txtSelectedMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMallDialog(mallNames, selectedMallIndex);
            }
        });
        txtSelectedCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategoryDialog(categoryNames, selectedCategoryIndex);
            }
        });
        btnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mallId = getMallId(txtSelectedMall.getText().toString());
                String categoryId = getCategoryId(txtSelectedCategory.getText().toString());
                String title = shopTitle.getText().toString();
                String mainPhone = shopMainPhone.getText().toString();
                String extraPhone = shopExtraPhone.getText().toString();
                String number = shopNumber.getText().toString();
                String site = shopSite.getText().toString();
                String desc = shopDescription.getText().toString();
                if(!mallId.equals("") && !categoryId.equals("") && Helper.validateParams(title, mainPhone, desc)){
                    ShopApi api = ServiceGenerator.createService(ShopApi.class, Helper.API_URL);
                    api.create(title, number, mainPhone, extraPhone, site, desc, String.valueOf(user.getId()), categoryId, mallId, new Callback<Message>() {
                        @Override
                        public void success(Message message, retrofit.client.Response response) {
                            Log.e("message", message.getMessage());
                            if (!message.isError()) {
                                Toast.makeText(getApplicationContext(), "Shop successfully added", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(
                                        AddShopActivity.this,
                                        ManageShopActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
                }
            }
        });

    }

    private void selectMallDialog(String[] mallNames, final int selectedIndex) {
        new MaterialDialog.Builder(AddShopActivity.this)
                .title(R.string.shop_mall)
                .items(mallNames)
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        selectedMallIndex = which;
                        txtSelectedMall.setText(text);
                        return true;
                    }
                })
                .itemColorRes(R.color.colorPrimary)
                .negativeText("Отмена")
                .show();
    }

    private void selectCategoryDialog(String[] categoryNames, final int selectedIndex) {
        new MaterialDialog.Builder(AddShopActivity.this)
                .title(R.string.shop_category)
                .items(categoryNames)
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        selectedCategoryIndex = which;
                        txtSelectedCategory.setText(text);
                        return true;
                    }
                })
                .itemColorRes(R.color.colorPrimary)
                .negativeText("Отмена")
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadMalls extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            MallApi client = ServiceGenerator.createService(MallApi.class, Helper.API_URL);
            malls = client.getMalls();
            return null;
        }
        protected void onPostExecute(String unused) {
            if (!malls.isEmpty()) {
                mallNames = new String[malls.size()];
                for(int i=0; i<malls.size(); i++){
                    mallNames[i] = malls.get(i).getName();
                }
            }
        }
    }

    class LoadCategories extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            CategoryApi client = ServiceGenerator.createService(CategoryApi.class, Helper.API_URL);
            categories = client.getCategories();
            return null;
        }
        protected void onPostExecute(String unused) {
            if (!categories.isEmpty()) {
                categoryNames = new String[categories.size()];
                for(int i=0; i<categories.size(); i++){
                    categoryNames[i] = categories.get(i).getTitle();
                }
            }
        }
    }

    private String getMallId(String title){
        String res = "";
        for(Mall m: malls){
            if(m.getName().equals(title)) {
                res = m.getId()+"";
                break;
            }
        }
        return res;
    }

    private String getCategoryId(String title){
        String res = "";
        for(Category m: categories){
            if(m.getTitle().equals(title)) {
                res = m.getId()+"";
                break;
            }
        }
        return res;
    }
}
