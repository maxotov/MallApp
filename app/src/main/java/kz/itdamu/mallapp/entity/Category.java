package kz.itdamu.mallapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kz.itdamu.mallapp.custom.Listable;

/**
 * Created by Aibol on 27.03.2016.
 */
public class Category implements Listable, Parcelable, Serializable{
    private static final long serialVersionUID = 2070450081971040619L;
    private int id;
    private String title;
    private String c_date;
    private List<Category> sub_categories = new ArrayList<>();
    private boolean isChecked;
    private int parent;

    protected Category(Parcel in) {
        id = in.readInt();
        title = in.readString();
        c_date = in.readString();
        sub_categories = in.createTypedArrayList(Category.CREATOR);
        isChecked = in.readByte() != 0;
        parent = in.readInt();
        m_date = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

    public String getM_date() {
        return m_date;
    }

    public void setM_date(String m_date) {
        this.m_date = m_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String m_date;

    @Override
    public String getLabel() {
        return title;
    }

    public List<Category> getSub_categories() {
        return sub_categories;
    }

    public void setSub_categories(List<Category> sub_categories) {
        this.sub_categories = sub_categories;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(c_date);
        parcel.writeTypedList(sub_categories);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
        parcel.writeInt(parent);
        parcel.writeString(m_date);
    }
}
