package kz.itdamu.mallapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.activity.GoodsListActivity;
import kz.itdamu.mallapp.entity.Shop;

/**
 * Created by Aibol on 09.03.2016.
 */
public class ShopAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Shop> shopList;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private Activity activity;
    private OnLoadMoreListener onLoadMoreListener;
    private String IMG_URL = "http://itdamu.kz/MallBackend/images/icon/shop";

    public ShopAdapter(List<Shop> rows, RecyclerView recyclerView, Activity act) {
        shopList = rows;
        activity = act;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return shopList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.shop_item, parent, false);

            vh = new ShopViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShopViewHolder) {

            Shop shop = (Shop) shopList.get(position);

            ((ShopViewHolder) holder).name.setText(shop.getTitle());
            ((ShopViewHolder) holder).shopId.setText(String.valueOf(shop.getId()));
            if(shop.getCategories().get(0).getParent()==0){
                Picasso.with(activity).load(IMG_URL+shop.getCategories().get(0).getId()+".png").into(((ShopViewHolder) holder).shopIcon);
            } else {
                Picasso.with(activity).load(IMG_URL+shop.getCategories().get(0).getParent()+".png").into(((ShopViewHolder) holder).shopIcon);
            }
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView shopId;
        public ImageView shopIcon;

        public ShopViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.shop_name);
            shopId = (TextView) v.findViewById(R.id.shop_id);
            shopIcon = (ImageView)v.findViewById(R.id.shop_icon);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Mall id = ", shopId.getText().toString());
                    Intent intent = new Intent(activity, GoodsListActivity.class);
                    intent.putExtra("shopId", shopId.getText().toString());
                    intent.putExtra("shopName", name.getText().toString());
                    activity.startActivity(intent);
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
