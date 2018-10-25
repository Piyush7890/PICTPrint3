package com.piyush.pictprint.api;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.piyush.pictprint.Utils.Constants;
import com.piyush.pictprint.model.Document;
import com.piyush.pictprint.model.JobsListWrapper;
import com.piyush.pictprint.model.SubmitResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubmitJobService {

    private static PrintApi api;
    private static OkHttpClient client;
    private Call<SubmitResponse> call;
    private static String token;
    NotificationChannel channel;

    public SubmitJobService(String token)
    {
        SubmitJobService.token = token;

    }

    private static OkHttpClient  buildClient()
    {
        if(client==null)
            client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor(token))
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS).build();

        return client;
    }

    private static PrintApi buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build()
                .create(PrintApi.class);
        }
        return api;
    }

    public  Call<SubmitResponse> submitJob(MultipartBody.Part file, final Document document)
    {
//        call = buildApi().submit(file, document.getCloudJobTicket());
//        call.enqueue(new Callback<SubmitResponse>() {
//            @Override
//            public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {
//                //if(response.code()==200)
//                    listener.success(response.body(),document);
//            }
//
//            @Override
//            public void onFailure(Call<SubmitResponse> call, Throwable t) {
//                listener.failure(t, document);
//
//            }
//        });
        return buildApi().submit(file,document.getCloudJobTicket());
    }




    public void fetchJobs(final JobsFetchedListener listener)
    {
        buildApi().fetchJobs().enqueue(new Callback<JobsListWrapper>() {
            @Override
            public void onResponse(Call<JobsListWrapper> call, Response<JobsListWrapper> response) {
                if(response.code()==200)
                {
                    listener.onJobsFetched(response.body());
                }
                else listener.onJobsFetchFailed("An error occured while fetching jobs.");
            }

            @Override
            public void onFailure(Call<JobsListWrapper> call, Throwable t) {

                listener.onJobsFetchFailed(t.toString());
            }
        });
    }


    public interface JobsFetchedListener
    {
        void onJobsFetched(JobsListWrapper jobsListWrapper);
        void onJobsFetchFailed(String error);
    }




    public  void submitJobAsync(MultipartBody.Part file, final Document document, final Listener listener)
    {
        call = buildApi().submit(file, document.getCloudJobTicket());
        call.enqueue(new Callback<SubmitResponse>() {
            @Override
            public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {
                //if(response.code()==200)
                    listener.success(response.body(),document);
            }

            @Override
            public void onFailure(Call<SubmitResponse> call, Throwable t) {
                listener.failure(t, document);

            }
        });
    }

    public void cancel()
    {
        if(call!=null)
            call.cancel();
    }

    public interface Listener
    {
        void success(SubmitResponse response, Document document);
        void failure(Throwable t, Document document);
    }

}
