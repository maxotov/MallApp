package kz.itdamu.mallapp.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aibol on 09.03.2016.
 */
public class Shop {
    private int id;
    private String title;
    private String number_shop;
    private String main_phone;
    private String extra_phone;
    private String site;
    private String description;
    private int user_id;
    private int mall_id;
    private String c_date;
    private String m_date;
    private int view;
    private List<Category> categories = new ArrayList<>();

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber_shop() {
        return number_shop;
    }

    public void setNumber_shop(String number_shop) {
        this.number_shop = number_shop;
    }

    public String getMain_phone() {
        return main_phone;
    }

    public void setMain_phone(String main_phone) {
        this.main_phone = main_phone;
    }

    public String getExtra_phone() {
        return extra_phone;
    }

    public void setExtra_phone(String extra_phone) {
        this.extra_phone = extra_phone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMall_id() {
        return mall_id;
    }

    public void setMall_id(int mall_id) {
        this.mall_id = mall_id;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
