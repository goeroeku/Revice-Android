package com.linkensky.revice.api;

import com.linkensky.revice.api.model.Auth;
import com.linkensky.revice.api.model.ClosestServices;
import com.linkensky.revice.api.model.Credentials;
import com.linkensky.revice.api.model.LocationRequest;
import com.linkensky.revice.api.model.Order;
import com.linkensky.revice.api.model.OrderRequest;
import com.linkensky.revice.api.model.Service;
import com.linkensky.revice.api.model.ServiceItem;
import com.linkensky.revice.api.model.StatusRequest;
import com.linkensky.revice.api.model.User;
import com.linkensky.revice.api.model.UserItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by setyo on 03/02/16.
 */
public interface ReviceApi {

    /*
    Authentication module
     */
    // Authenticate UserItem
    @POST("auth")
    Call<Auth> authUser(@Body Credentials credentials);

    /*
    UserItem Module
     */
    //Get UserItem Info
    @GET("user/{id}")
    Call<User> getUsers(@Path("id") String id);

    //Get Current UserItem Info
    @GET("user/current")
    Call<User> getCurrentUser();

    //Edit Current UserItem
    @PUT("user/current")
    Call<User> editCurrentUser(@Body UserItem user);

    //Create New UserItem
    @POST("user/create")
    Call<User> createUser(@Body UserItem user);

    //Set Token
    @FormUrlEncoded
    @PUT("user/token")
    Call<User> setToken(@Field("token") String token);
    /*
    Service Module
     */
    //Find Closest ServiceItem Location
    @GET("service/closest")
    Call<ClosestServices> closestService(@Query("lat") Double lat, @Query("lng") Double lng, @Query("dist") Double dist, @Query("max") Integer max);

    //Get ServiceItem Info
    @GET("service/{id}")
    Call<Service> getService(@Path("id") String id);

    /*
    Order Module
     */
    @GET("order/{id}")
    Call<Order> getOrder(@Path("id") String id);

    @POST("order/create")
    Call<Order> createOrder(@Body OrderRequest orderRequest);

    @PUT("order/status/{id}")
    Call<Order> editStatus(@Path("id") String id, @Body StatusRequest statusRequest);

    /*
    Notifier Module
     */
}
