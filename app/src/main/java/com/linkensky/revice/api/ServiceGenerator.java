package com.linkensky.revice.api;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    public static final String API_BASE_URL = "http://192.168.58.1:8000/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());

                    if(authToken != null){
                        requestBuilder.addHeader("Authorization", "bearer " + authToken);
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

        // Debugging Purpose
        httpClient.addNetworkInterceptor(new StethoInterceptor());

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}