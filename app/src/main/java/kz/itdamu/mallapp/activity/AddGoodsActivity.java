package kz.itdamu.mallapp.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.PhotosAdapter;
import kz.itdamu.mallapp.entity.CustomPhotos;
import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.rest.GoodsApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import retrofit.Callback;
import retrofit.RetrofitError;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class AddGoodsActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextInputLayout goodsNameLayout;
    private TextInputLayout goodsDesciptionLayout;

    private EditText goodsName;
    private EditText goodsPrice;
    private EditText goodsDescription;

    private Button btnAddGoods;
    private Button btnPhotosPickMul;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;

    GridView gridGallery;
    Handler handler;
    PhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_goods);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle("Добавление товара");
            setSupportActionBar(toolbar);
            initToolbar(toolbar);
            makeNavigationBackButton();
        }

        goodsNameLayout = (TextInputLayout)findViewById(R.id.goods_name_layout);
        goodsDesciptionLayout = (TextInputLayout)findViewById(R.id.goods_description_layout);

        goodsName = (EditText)findViewById(R.id.goods_name);
        goodsPrice=(EditText)findViewById(R.id.goods_price);
        goodsDescription=(EditText)findViewById(R.id.goods_description);

        btnAddGoods = (Button)findViewById(R.id.btnAddGoods);

        btnAddGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopId = "";
                String name = goodsName.getText().toString();
                String price = goodsPrice.getText().toString();
                String description = goodsDescription.getText().toString();

                if (validateData(name)) {
                    GoodsApi api = ServiceGenerator.createService(GoodsApi.class, Helper.API_URL2);

                    api.create(shopId, name, price, description, new Callback<Message>() {
                        @Override
                        public void success(Message message, retrofit.client.Response response) {
                            Log.e("message", message.getMessage());
                            if (!message.isError()) {
                                Toast.makeText(getApplicationContext(), "Успешно добавлено!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(
                                        AddGoodsActivity.this,
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

        //-----
        initImageLoader();
        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new PhotosAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        btnPhotosPickMul = (Button) findViewById(R.id.btnPhotosPickMul);
        btnPhotosPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Helper.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });

    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private boolean validateData(String name) {
        boolean result = true;
        if (Helper.isEmpty(name)) {
            // We set the error message
            goodsNameLayout.setError(getString(R.string.error_empty_title));
            result = false;
        } else // We remove error messages
            goodsNameLayout.setErrorEnabled(false);
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomPhotos> dataT = new ArrayList<CustomPhotos>();
            String buffer="";
            for (String string : all_path) {
                CustomPhotos item = new CustomPhotos();
                item.sdcardPath = string;
                dataT.add(item);

                buffer += string;
            }

            Log.d("MyPath",buffer);
            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
        }
    }
}
