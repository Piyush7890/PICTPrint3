package com.piyush.pictprint.api;


import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class Interceptor implements okhttp3.Interceptor {

    private final String token;

    public Interceptor(String token)
    {
        this.token=token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request().newBuilder().addHeader("Authorization","Bearer "+token).build();


        return chain.proceed(request);
    }
}
