package kz.itdamu.mallapp.custom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.CategoryAdapter;
import kz.itdamu.mallapp.entity.Category;

/**
 * Created by Aibol on 29.04.2016.
 */
public class CategoryEditText<T extends Listable> extends EditText {
    List<T> mItems;
    String[] mListableItems;
    CharSequence mHint;
    OnItemSelectedListener<T> onItemSelectedListener;
    /**ExpandableListView expandableListView;
    View customView;
    CategoryAdapter categoryAdapter;*/
    DialogFragment newFragment;

    public CategoryEditText(Context context) {
        super(context);
        mHint = getHint();
    }

    public CategoryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHint = getHint();
    }

    public CategoryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHint = getHint();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoryEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mHint = getHint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
    }

    public void setItems(List<T> items) {
        this.mItems = items;
        /**customView = LayoutInflater.from(getContext()).inflate(R.layout.select_category_layout, null);
        expandableListView = (ExpandableListView) customView.findViewById(R.id.list);
        categoryAdapter = new CategoryAdapter(getContext(), (ArrayList<Category>) mItems);
        expandableListView.setAdapter(categoryAdapter);*/
        newFragment = CategoryDialogFragment.newInstance((ArrayList<Category>)mItems, this);
        this.mListableItems = new String[items.size()];

        int i = 0;

        for (T item : mItems) {
            mListableItems[i++] = item.getLabel();
        }

        configureOnClickListener();
    }

    private void configureOnClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = getContext();
                FragmentManager fm = ((Activity) context).getFragmentManager();
                newFragment.show(fm, "dialog");
                /**MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .title(R.string.shop_category)
                        .customView(customView, false)
                        .positiveText(R.string.select)
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                ArrayList<Category> cats = categoryAdapter.getSelectedCats();
                                String selectedText = "";
                                for(int i=0; i<cats.size(); i++){
                                    selectedText += cats.get(i).getTitle();
                                    if(i+1 < cats.size()){
                                        selectedText += " | ";
                                    }
                                }
                                setText(selectedText);
                            }
                        })
                        .build();
                dialog.show();*/
                /**final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.select_category_layout);
                dialog.setTitle(R.string.shop_category);

                ExpandableListView expandableListView = (ExpandableListView) dialog.findViewById(R.id.list);
                final CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), (ArrayList<Category>) mItems);
                expandableListView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
                Button dialogButton = (Button) dialog.findViewById(R.id.btnSelectCategory);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Category> cats = categoryAdapter.getSelectedCats();
                        String selectedText = "";
                        for(int i=0; i<cats.size(); i++){
                            selectedText += cats.get(i).getTitle();
                            if(i+1 < cats.size()){
                                selectedText += " | ";
                            }
                        }
                        setText(selectedText);
                        dialog.dismiss();
                    }
                });
                dialog.show();*/
            }
        });
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelectedListener(T item, int selectedIndex);
    }
}
