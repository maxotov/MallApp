package kz.itdamu.mallapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.entity.Category;

/**
 * Created by Aibol on 24.04.2016.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Category> categories;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context,
                           ArrayList<Category> categoryList) {
        this.context = context;
        this.categories = categoryList;
        inflater = LayoutInflater.from(context);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getSub_categories().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long)( groupPosition*1024+childPosition );  // Max 1024 children per group
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.category_child_row, parent, false);
        final Category c = (Category) getChild( groupPosition, childPosition );
        TextView title = (TextView)v.findViewById(R.id.title);
        title.setText(c.getTitle());
        CheckBox cb = (CheckBox)v.findViewById(R.id.check);
        if(categories.get(groupPosition).getSub_categories().get(childPosition).isChecked()){
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        cb.setOnCheckedChangeListener(new CheckchangeChildListener(groupPosition, childPosition));
        return v;
    }

    class CheckchangeChildListener implements CompoundButton.OnCheckedChangeListener {
        private int position;
        private int childPosition;

        public CheckchangeChildListener(int position, int childPosition) {
            this.position= position;
            this.childPosition = childPosition;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                categories.get(position).getSub_categories().get(childPosition).setChecked(true);
            } else {
                categories.get(position).getSub_categories().get(childPosition).setChecked(false);
            }
        }
    }

    public int getChildrenCount(int groupPosition) {
        return categories.get(groupPosition).getSub_categories().size();
    }

    public Object getGroup(int groupPosition) {
        return categories.get( groupPosition );
    }

    public int getGroupCount() {
        return categories.size();
    }

    public long getGroupId(int groupPosition) {
        return (long)( groupPosition*1024 );  // To be consistent with getChildId
    }

    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.category_parent_row, parent, false);
        final Category category = (Category)getGroup(groupPosition);
        TextView title = (TextView)v.findViewById(R.id.title);
        CheckBox cb = (CheckBox)v.findViewById(R.id.check);
        ImageView image = (ImageView)v.findViewById(R.id.arrow);
        title.setText(category.getTitle());
        if(getChildrenCount(groupPosition) == 0){
            image.setVisibility(View.INVISIBLE);
            cb.setVisibility(View.VISIBLE);
            if(categories.get(groupPosition).isChecked()){
                cb.setChecked(true);
            } else{
                cb.setChecked(false);
            }
            cb.setOnCheckedChangeListener(new CheckchangeGroupListener(groupPosition));
        } else {
            image.setVisibility(View.VISIBLE);
            cb.setVisibility(View.INVISIBLE);
            image.setImageResource(isExpanded ? R.drawable.ic_chevron_down : R.drawable.ic_chevron_right);
        }
        return v;
    }

    class CheckchangeGroupListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        public CheckchangeGroupListener(int position) {
            this.position= position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                categories.get(position).setChecked(true);
            } else {
                categories.get(position).setChecked(false);
            }
        }
    }


    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void onGroupCollapsed (int groupPosition) {}
    public void onGroupExpanded(int groupPosition) {}

    public ArrayList<Category> getSelectedCats(){
        ArrayList<Category> checkedCategories = new ArrayList<>();
        for(Category c: categories){
            if(c.isChecked()) checkedCategories.add(c);
            if(!c.getSub_categories().isEmpty()){
                for(Category category: c.getSub_categories()) {
                    if (category.isChecked()) checkedCategories.add(category);
                }
            }
        }
        return checkedCategories;
    }

}
