package kz.itdamu.mallapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.activity.GoodsDetailsActivity;
import kz.itdamu.mallapp.entity.Goods;

/**
 * Created by Берик on 09.04.2016.
 */

public class GoodsAdapter extends RecyclerView.Adapter {

    public  final static String SER_KEY = "GoodsSer";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private Activity activity;
    private List<Goods> goodsList;
    private int totalItemCount, lastVisibleItem;
    private boolean loading;
    private int visibleThreshold = 5;
    private OnLoadMoreListener onLoadMoreListener;

    public GoodsAdapter(List<Goods> goodsList, RecyclerView recyclerView, Activity activity) {
        this.goodsList = goodsList;
        this.activity = activity;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
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
        return goodsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.goods_item, parent, false);

            vh = new GoodsViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GoodsViewHolder) {

            Goods goods = (Goods) goodsList.get(position);
            ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;

            ((GoodsViewHolder) holder).name.setText(goods.getName());
            ((GoodsViewHolder) holder).description.setText(goods.getDescription());
            ((GoodsViewHolder) holder).price.setText(String.valueOf(goods.getPrice())+".тг");
            ((GoodsViewHolder) holder).name.setText(goods.getName());

            TextDrawable mDrawableBuilder;
            String letter = "A";
            if(goods.getName() != null && !goods.getName().isEmpty()) {
                letter = goods.getName().substring(0, 1);
            }
            int color = mColorGenerator.getRandomColor();
            mDrawableBuilder = TextDrawable.builder()
                    .buildRound(letter, color);
            ((GoodsViewHolder) holder).mThumbnailImage.setImageDrawable(mDrawableBuilder);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public class GoodsViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;
        public TextView price;
        private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
        public ImageView mThumbnailImage;

        public GoodsViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.name);
            description=(TextView)v.findViewById(R.id.description);
            price=(TextView)v.findViewById(R.id.price);
            mThumbnailImage=(ImageView)itemView.findViewById(R.id.thumbnail_image);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void
                onClick(View v) {
                    try {
                        int position = getPosition();
                        Goods goods = goodsList.get(position);
                        SerializeMethod(position);
                    }catch (Exception ex){
                        Toast.makeText(activity, "error:" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        void SerializeMethod(int position){
            //goodId
            Intent intent = new Intent(activity, GoodsDetailsActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable(SER_KEY, "");
            intent.putExtra("goodsName", name.getText().toString());
            Goods goods=goodsList.get(position);
            mBundle.putSerializable(SER_KEY, goods);
            intent.putExtras(mBundle);
            activity.startActivity(intent);
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
