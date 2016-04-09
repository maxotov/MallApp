package kz.itdamu.mallapp.rest;

import java.util.List;

import kz.itdamu.mallapp.entity.Goods;
import kz.itdamu.mallapp.entity.Message;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * Created by Берик on 10.04.2016.
 */
public interface GoodsApi {

    @GET("/findGoodsByField")
    List<Goods> getGoodsByShop(
            @Query("method") String method,
            @Query("val") String val);

    @Multipart
    @POST("/insertGoods")
    void create(@Part("shop_id") String shop_id,@Part("name") String name, @Part("price") String price,@Part("description") String description, Callback<Message> message);


}
