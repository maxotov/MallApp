package kz.itdamu.mallapp.adapter;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.entity.Photo;
import kz.itdamu.mallapp.helper.DataUrl;

/**
 * Created by Берик on 11.05.2016.
 */
public class GoodsPhotoSwipeAdapter extends PagerAdapter {

    private List<Photo> photoList;
    private Context ctx;
    private LayoutInflater layoutInflater;

    public GoodsPhotoSwipeAdapter(Context ctx,List<Photo> photoList ){
        this.ctx=ctx;
        this.photoList=photoList;

    }


    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.goods_photo_view,container,false);
        //ImageView imageView=(ImageView)item_view.findViewById(R.id.image_view);
        //imageView.setImageResource(image_resources[position]);

        SimpleDraweeView ivGoods = (SimpleDraweeView)item_view.findViewById(R.id.iv_goods);
        WindowManager window = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w;

        try {
            w = size.x;
        } catch (Exception e) {
            w = display.getWidth();
        }

        Uri uri = Uri.parse(DataUrl.getUrlCustom(photoList.get(position).getDescription(), w));
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .setOldController(ivGoods.getController())
                .build();

        ivGoods.setController(dc);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object );
    }
}
