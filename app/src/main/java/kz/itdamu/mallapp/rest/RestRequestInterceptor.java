package kz.itdamu.mallapp.rest;

import retrofit.RequestInterceptor;

/**
 * Created by Aibol on 08.03.2016.
 */
public class RestRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Content-Type", "application/json");
    }
}
