package kz.itdamu.mallapp.custom;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.adapter.CategoryAdapter;
import kz.itdamu.mallapp.entity.Category;

/**
 * Created by Aibol on 06.05.2016.
 */
public class CategoryDialogFragment extends DialogFragment {

    ArrayList<Category> categories;
    static EditText editText;

    public static CategoryDialogFragment newInstance(ArrayList<Category> categories, EditText customEditText) {
        CategoryDialogFragment frag = new CategoryDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("categories", categories);
        frag.setArguments(args);
        editText = customEditText;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        categories = getArguments().getParcelableArrayList("categories");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.select_category_layout, null);
        ExpandableListView expandableListView = (ExpandableListView) v.findViewById(R.id.list);
        final CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);
        expandableListView.setAdapter(categoryAdapter);

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.shop_category)
                .customView(v, false)
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
                        editText.setText(selectedText);
                        materialDialog.dismiss();
                    }
                })
                .build();

        return dialog;
    }
}
