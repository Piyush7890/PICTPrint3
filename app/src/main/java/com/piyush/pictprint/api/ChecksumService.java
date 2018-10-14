package com.piyush.pictprint.api;

import android.util.Log;

import com.piyush.pictprint.model.Checksum;
import com.piyush.pictprint.Utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChecksumService {


    private static ChecksumApi api;

    private static ChecksumApi buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ChecksumApi.class);
        }
        return api;
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
