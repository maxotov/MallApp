package kz.itdamu.mallapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import kz.itdamu.mallapp.entity.Mall;

/**
 * Created by Aibol on 28.03.2016.
 */
public class SpinMallAdapter extends ArrayAdapter {
    private Context context;
    private Mall[] values;

    public SpinMallAdapter(Context context, int textViewResourceId,
                       Mall[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.length;
    }

    public Mall getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.rgb(75, 180, 225));
        label.setText(values[position].getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.rgb(75, 180, 225));
        label.setText(values[position].getName());
        return label;
    }
}
