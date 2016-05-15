package kz.itdamu.mallapp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aibol on 05.04.2016.
 */
public class Goods implements Serializable {
    public static final String PATH = "http://www.villopim.com.br/android/glide/__w-395-593-790-1185__/";
    public static final String SER_KEY = "GoodsSer";
    private int id;
    private String name;
    private double price;
    private String description;
    private int shop_id;
    private String c_date;
    private String m_date;
    private List<Photo> photoList;
    private int view;
    private int share;
    private int discount;
    public Goods(){}

    public Goods(String name,int id) {
        this.name = name;
        this.id = id;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
