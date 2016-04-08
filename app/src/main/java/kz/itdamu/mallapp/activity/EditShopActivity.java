package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.custom.ClickToSelectEditText;
import kz.itdamu.mallapp.entity.Category;
import kz.itdamu.mallapp.entity.Mall;
import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.entity.Shop;
import kz.itdamu.mallapp.entity.User;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.helper.SQLiteHandler;
import kz.itdamu.mallapp.rest.CategoryApi;
import kz.itdamu.mallapp.rest.MallApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.ShopApi;
import retrofit.Callback;
import retrofit.RetrofitError;

public class EditShopActivity extends BaseActivity {

    private Toolbar toolbar;
    private List<Mall> malls;
    private List<Category> categories;
    String[] mallNames;
    String[] categoryNames;
    private int selectedMallIndex = -1;
    private int selectedCategoryIndex = -1;

    private TextInputLayout shopTitleLayout;
    private TextInputLayout shopPhoneLayout;
    private TextInputLayout shopDescriptionLayout;
    private TextInputLayout shopMallLayout;
    private TextInputLayout shopCategoryLayout;

    private EditText shopTitle;
    private EditText shopNumber;
    private EditText shopMainPhone;
    private EditText shopExtraPhone;
    private EditText shopSite;
    private EditText shopDescription;
    ClickToSelectEditText<Mall> editTextSelectMall;
    ClickToSelectEditText<Category> editTextSelectCategory;
    private Button btnEditShop;
    private ProgressBar progressBar;
    private LinearLayout editShopLayout;

    private Shop shop;
    private String shopId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);
        Intent intent = getIntent();
        if(intent != null){
            shopId = intent.getStringExtra("shopId");
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Редактирование магазина");
            setSupportActionBar(toolbar);
            initToolbar(toolbar);
            makeNavigationBackButton();
        }
        shopTitleLayout = (TextInputLayout)findViewById(R.id.shop_title_layout);
        shopPhoneLayout = (TextInputLayout)findViewById(R.id.shop_phone_layout);
        shopDescriptionLayout = (TextInputLayout)findViewById(R.id.shop_description_layout);
        shopMallLayout = (TextInputLayout)findViewById(R.id.shop_mall_layout);
        shopCategoryLayout = (TextInputLayout)findViewById(R.id.shop_category_layout);

        progressBar = (ProgressBar)findViewById(R.id.progressbar_loading);
        editShopLayout = (LinearLayout) findViewById(R.id.editShopLayout);
        shopTitle = (EditText)findViewById(R.id.shop_title);
        shopNumber = (EditText)findViewById(R.id.shop_number);
        shopMainPhone = (EditText)findViewById(R.id.shop_main_phone);
        shopExtraPhone = (EditText)findViewById(R.id.shop_extra_phone);
        shopSite = (EditText)findViewById(R.id.shop_site);
        shopDescription = (EditText)findViewById(R.id.shop_description);
        editTextSelectMall = (ClickToSelectEditText<Mall>) findViewById(R.id.selectMallName);
        editTextSelectCategory = (ClickToSelectEditText<Category>) findViewById(R.id.selectCategoryName);
        btnEditShop = (Button)findViewById(R.id.btnEditShop);
        new LoadShop().execute();
        editTextSelectMall.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<Mall>() {
            @Override
            public void onItemSelectedListener(Mall item, int selectedIndex) {
                selectedMallIndex = selectedIndex;
            }
        });
        editTextSelectCategory.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<Category>() {
            @Override
            public void onItemSelectedListener(Category item, int selectedIndex) {
                selectedCategoryIndex = selectedIndex;
            }
        });
        btnEditShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mallId = getMallId(editTextSelectMall.getText().toString());
                String categoryId = getCategoryId(editTextSelectCategory.getText().toString());
                String title = shopTitle.getText().toString();
                String mainPhone = shopMainPhone.getText().toString();
                String extraPhone = shopExtraPhone.getText().toString();
                String number = shopNumber.getText().toString();
                String site = shopSite.getText().toString();
                String desc = shopDescription.getText().toString();
                if(validateData(title, mainPhone, desc, mallId, categoryId)){
                    ShopApi api = ServiceGenerator.createService(ShopApi.class, Helper.API_URL);
                    api.update(shopId, title, number, mainPhone, extraPhone, site, desc, categoryId, mallId, new Callback<Message>() {
                        @Override
                        public void success(Message message, retrofit.client.Response response) {
                            Log.e("message", message.getMessage());
                            if (!message.isError()) {
                                Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(
                                        EditShopActivity.this,
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

    private boolean validateData(String title, String phone, String desc, String mallId, String categoryId) {
        boolean result = true;
        if (Helper.isEmpty(title)) {
            shopTitleLayout.setError(getString(R.string.error_empty_title));
            result = false;
        }
        else shopTitleLayout.setErrorEnabled(false);

        if (Helper.isEmpty(phone)) {
            shopPhoneLayout.setError(getString(R.string.error_empty_phone));
            result = false;
        }
        else shopPhoneLayout.setErrorEnabled(false);

        if (Helper.isEmpty(desc)) {
            shopDescriptionLayout.setError(getString(R.string.error_empty_description));
            result = false;
        }
        else shopDescriptionLayout.setErrorEnabled(false);

        if(mallId==null || mallId.equals("")){
            shopMallLayout.setError(getString(R.string.error_empty_mall));
            result = false;
        } else shopMallLayout.setErrorEnabled(false);

        if(categoryId==null || categoryId.equals("")){
            shopCategoryLayout.setError(getString(R.string.error_empty_category));
            result = false;
        } else shopCategoryLayout.setErrorEnabled(false);

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_shop, menu);
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

    /**private void selectMallDialog(String[] mallNames, final int selectedIndex) {
        new MaterialDialog.Builder(EditShopActivity.this)
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
        new MaterialDialog.Builder(EditShopActivity.this)
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
    }*/

    class LoadShop extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {
            MallApi mallClient = ServiceGenerator.createService(MallApi.class, Helper.API_URL);
            malls = mallClient.getMalls();
            CategoryApi categoryClient = ServiceGenerator.createService(CategoryApi.class, Helper.API_URL);
            categories = categoryClient.getCategories();
            ShopApi client = ServiceGenerator.createService(ShopApi.class, Helper.API_URL);
            shop = client.getShopById(shopId);
            return null;
        }
        protected void onPostExecute(String unused) {
            if (!malls.isEmpty()) {
                editTextSelectMall.setItems(malls);
            }
            if (!categories.isEmpty()) {
                editTextSelectCategory.setItems(categories);
            }
            if (shop != null) {
                shopTitle.setText(shop.getTitle());
                shopNumber.setText(shop.getNumber_shop());
                shopMainPhone.setText(shop.getMain_phone());
                shopExtraPhone.setText(shop.getExtra_phone());
                shopSite.setText(shop.getSite());
                shopDescription.setText(shop.getDescription());
                editTextSelectMall.setText(getMallName(shop.getMall_id()));
                editTextSelectCategory.setText(getCategoryTitle(shop.getCategory_id()));
                /**for(int i=0; i<malls.size(); i++){
                    if(malls.get(i).getId()==shop.getMall_id()) selectedMallIndex = i;
                }
                for (int i=0; i<categories.size(); i++){
                    if(categories.get(i).getId()==shop.getCategory_id()) selectedCategoryIndex = i;
                }*/
            }
            progressBar.setVisibility(View.GONE);
            editShopLayout.setVisibility(View.VISIBLE);
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

    private String getMallName(int mall_id){
        String res = "";
        for(Mall m: malls){
            if(m.getId()==mall_id) {
                res = m.getName();
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

    private String getCategoryTitle(int cat_id){
        String res = "";
        for(Category m: categories){
            if(m.getId()==cat_id) {
                res = m.getTitle();
                break;
            }
        }
        return res;
    }
}
