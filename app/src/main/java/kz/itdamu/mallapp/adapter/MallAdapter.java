package kz.itdamu.mallapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.activity.MallMapActivity;
import kz.itdamu.mallapp.activity.ShopActivity;
import kz.itdamu.mallapp.entity.Mall;

/**
 * Created by Aibol on 08.03.2016.
 */
public class MallAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Mall> mallList;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private Activity activity;
    private OnLoadMoreListener onLoadMoreListener;

    public MallAdapter(List<Mall> rows, RecyclerView recyclerView, Activity act) {
        mallList = rows;
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
        return mallList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.mall_item, parent, false);

            vh = new MallViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MallViewHolder) {

            Mall mall = (Mall) mallList.get(position);

            ((MallViewHolder) holder).name.setText(mall.getName());
            ((MallViewHolder) holder).mallId.setText(String.valueOf(mall.getId()));
            ((MallViewHolder) holder).mall_address.setText(mall.getAddress());
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return mallList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class MallViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView mallId;
        public TextView mall_address;
        public TextView display_map;

        public MallViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.mall_name);
            mallId = (TextView) v.findViewById(R.id.mall_id);
            mall_address = (TextView)v.findViewById(R.id.mall_address);
            display_map = (TextView)v.findViewById(R.id.show_map);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Mall id = ", mallId.getText().toString());
                     Intent intent = new Intent(activity, ShopActivity.class);
                     intent.putExtra("id", mallId.getText().toString());
                     intent.putExtra("title", name.getText().toString());
                     activity.startActivity(intent);
                }
            });

            display_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, MallMapActivity.class);
                    intent.putExtra("mall_name", name.getText().toString());
                    Mall m = getMallById(Integer.parseInt(mallId.getText().toString()));
                    intent.putExtra("mall_lat", m.getLat());
                    intent.putExtra("mall_lng", m.getLng());
                    intent.putExtra("address", mall_address.getText().toString());
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

    private Mall getMallById(int id){
        Mall mall = null;
        for(Mall m: mallList){
            if(m.getId()==id) {mall = m; break;}
        }
        return mall;
    }
}
