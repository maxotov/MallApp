package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.rest.GoodsApi;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.ShopApi;
import retrofit.Callback;
import retrofit.RetrofitError;

public class AddGoodsActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextInputLayout goodsNameLayout;
    private TextInputLayout goodsDesciptionLayout;

    private EditText goodsName;
    private EditText goodsPrice;
    private EditText goodsDescription;

    private Button btnAddGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Добавление товар");
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
                String shopId="";
                String name=goodsName.getText().toString();
                String price=goodsPrice.getText().toString();
                String description=goodsDescription.getText().toString();

                if(validateData(name)) {
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
}
