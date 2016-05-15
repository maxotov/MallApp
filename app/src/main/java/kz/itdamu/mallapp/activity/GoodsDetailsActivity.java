package kz.itdamu.mallapp.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.GoodsPhotoSwipeAdapter;
import kz.itdamu.mallapp.entity.Goods;
import kz.itdamu.mallapp.entity.Photo;

public class GoodsDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Goods goods;
    private TextView tvName, tvDescription;
    private ViewGroup mRoot;
    String id, name, description;
    ViewPager viewPager;
    GoodsPhotoSwipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);

        Fresco.initialize(this);
        setContentView(R.layout.activity_goods_details);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(mToolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        Goods goods = (Goods) getIntent().getSerializableExtra(Goods.SER_KEY);

        mCollapsingToolbarLayout.setTitle(goods.getName());
        tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(goods.getDescription());


        List<Photo> photoList = goods.getPhotoList();
        String str="";
        for(int i=0;i<photoList.size();i++) {
            str += photoList.get(i).getDescription();
            if (i != photoList.size() - 1)
                str += ",";
        }

        Log.d("PhoyoList",str);

        viewPager=(ViewPager)findViewById(R.id.view_pager);
        adapter=new GoodsPhotoSwipeAdapter(this,photoList);
        viewPager.setAdapter(adapter);
    }
}
