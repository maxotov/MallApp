package kz.itdamu.mallapp.rest;

import java.util.List;

import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.entity.Shop;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * Created by Aibol on 09.03.2016.
 */
public interface ShopApi {

    @GET("/findShopByMall")
    List<Shop> getShopsByMall(@Query("mall_id") String mallId);

    @GET("/findShopByUser")
    List<Shop> getShopsByUser(@Query("user_id") String userId);

    @GET("/getShopById")
    Shop getShopById(@Query("shop_id") String shopId);

    @Multipart
    @POST("/createShop")
    void create(@Part("title") String title, @Part("number_shop") String number_shop, @Part("main_phone") String phone, @Part("extra_phone") String extra_phone, @Part("site") String site, @Part("description") String desc, @Part("user_id") String userId, @Part("category_ids") String category_ids, @Part("mall_id") String mall_id, Callback<Message> message);

    @Multipart
    @POST("/updateShop")
    void update(@Part("shop_id") String shop_id, @Part("title") String title, @Part("number_shop") String number_shop, @Part("main_phone") String phone, @Part("extra_phone") String extra_phone, @Part("site") String site, @Part("description") String desc, @Part("category_ids") String category_ids, @Part("mall_id") String mall_id, Callback<Message> message);
}
