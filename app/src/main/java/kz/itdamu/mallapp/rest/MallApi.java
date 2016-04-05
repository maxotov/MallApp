package kz.itdamu.mallapp.rest;

import java.util.List;

import kz.itdamu.mallapp.entity.Mall;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Aibol on 08.03.2016.
 */
public interface MallApi {

    @GET("/malls")
    List<Mall> getMalls();

    @GET("/getMallById")
    Mall getMall(@Query("mall_id") String mallId);
}
