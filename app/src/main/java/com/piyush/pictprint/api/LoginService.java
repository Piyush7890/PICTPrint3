package com.piyush.pictprint.api;

import android.util.Log;

import com.google.gson.Gson;
import com.piyush.pictprint.Utils.Constants;
import com.piyush.pictprint.Utils.EncryptDecrypt;
import com.piyush.pictprint.model.LoginRequest;
import com.piyush.pictprint.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginService {

    private static volatile LoginService instance;
    private static final Object mutex = new Object();


    public static LoginService getInstance() {
        LoginService result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new LoginService();
            }
        }
        return result;
    }
    private static LoginApi api;


    private static LoginApi buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginApi.class);
        }
        return api;
    }


    public  void login(String username,
                                   String password,
                                   final onLoginResponseListener loginListener) throws Exception {
        String json = new Gson().toJson(new LoginRequest("",username,password));
        String credential = EncryptDecrypt.encrypt(json);
        buildApi().login(credential).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginListener.onLoginResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginListener.onLoginFailed(t.toString());
            }
        });

    }
    public synchronized void onSignUp(String email, String username, String password, final onLoginResponseListener loginResponseListener)
    {
        String credential = EncryptDecrypt.encrypt(new Gson().toJson(new LoginRequest(email, username,password)));
        buildApi().signUp(credential).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginResponseListener.onLoginResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponseListener.onLoginFailed(t.toString());
            }
        });
    }


    public interface onLoginResponseListener
    {
        void onLoginResponse(Response<LoginResponse> response);
        void onLoginFailed(String t);
    }

}
