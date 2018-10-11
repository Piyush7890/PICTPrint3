package com.piyush.pictprint;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.piyush.pictprint.api.LoginService;
import com.piyush.pictprint.model.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFragment  extends Fragment implements LoginService.onLoginResponseListener{

    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.username)
    AutoCompleteTextView username;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.confirm_password)
    EditText comfirmPassword;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.signup)
    Button signup;

    LoginService service;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = LoginService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_signup,container,false);
        ButterKnife.bind(this,v);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = comfirmPassword.getText().toString();
                if(Username.isEmpty()||Password.isEmpty()||ConfirmPassword.isEmpty())
                {
                    if(Username.isEmpty())
                        username.setError("Username can't be empty");
                    if(Password.isEmpty())
                        password.setError("Password can't be empty");
                    if(ConfirmPassword.isEmpty())
                        comfirmPassword.setError("Password can't be empty");
                    return;
                }
                if(!Password.equals(ConfirmPassword))
                {
                    Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                service.onSignUp(Username,Password,SignUpFragment.this);
            }
        });
        return v;

    }


    @Override
    public void onLoginResponse(LoginResponse response) {
        if(response.getSuccess())
        {
            PreferenceManager
                    .getDefaultSharedPreferences(getContext())
                    .edit()
                    .putString("Token",response.getToken()).apply();
            getContext().startActivity(new Intent(getActivity(),MainActivity.class));
        }
        else Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailed(String t) {

        Toast.makeText(getContext(), t, Toast.LENGTH_SHORT).show();
    }
}
