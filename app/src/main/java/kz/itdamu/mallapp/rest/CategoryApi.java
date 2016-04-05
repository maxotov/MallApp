package kz.itdamu.mallapp.rest;

import java.util.List;

import kz.itdamu.mallapp.entity.Category;
import kz.itdamu.mallapp.entity.Mall;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Aibol on 30.03.2016.
 */
public interface CategoryApi {

    @GET("/categories")
    List<Category> getCategories();

    @GET("/getCategoryById")
    Category getCategory(@Query("category_id") String categoryId);
}
