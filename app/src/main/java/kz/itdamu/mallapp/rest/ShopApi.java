package kz.itdamu.mallapp.rest;

import java.util.List;

import kz.itdamu.mallapp.entity.Shop;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Aibol on 09.03.2016.
 */
public interface ShopApi {

    @GET("/findShopByMall")
    List<Shop> getShopsByMall(@Query("mall_id") String mallId);
}
