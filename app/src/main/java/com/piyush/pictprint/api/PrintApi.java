package com.piyush.pictprint.api;

import com.piyush.pictprint.model.JobsListWrapper;
import com.piyush.pictprint.model.SubmitResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PrintApi {

    @Multipart
    @POST("submit")
    Call<SubmitResponse> submit(@Part MultipartBody.Part file, @Query("ticket") String ticket);


    @GET("logs")
    Call<JobsListWrapper> fetchJobs();

}
