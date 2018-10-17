package com.piyush.pictprint.api;

import android.util.Log;

import com.piyush.pictprint.model.Checksum;
import com.piyush.pictprint.Utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChecksumService {


    private static OkHttpClient client;
    private static String token;

    public ChecksumService(String token) {
        ChecksumService.token = token;
    }

    private static ChecksumApi api;

    private static ChecksumApi buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build()
                .create(ChecksumApi.class);
        }
        return api;
    }


    private static OkHttpClient buildClient()
    {
        if(client==null)
            client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor(token))
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS).build();

        return client;
    }
    public void getChecksum(final String token, String amount, String orderid, final onChecksumGenerated onChecksumGenerated)
    {

        Log.d("INCHECKSUM", "CHECKSUM");
        buildApi().getChecksum(token,orderid,amount).enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {
                onChecksumGenerated.onSuccess(response.body(), token);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                onChecksumGenerated.onFailure(t.toString(), token);
            }
        });
    }

    public interface onChecksumGenerated
    {
         void onSuccess(Checksum checksum, String token);
         void onFailure(String t, String token);
    }
}
