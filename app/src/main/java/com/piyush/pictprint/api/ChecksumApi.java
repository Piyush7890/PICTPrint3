package com.piyush.pictprint.api;

import com.piyush.pictprint.model.Checksum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChecksumApi {

    @GET("checksum")
    Call<Checksum> getChecksum(@Query("token")String token,@Query("orderid")String orderid, @Query("price")String price);
}
