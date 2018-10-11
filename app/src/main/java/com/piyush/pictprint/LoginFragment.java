package com.piyush.pictprint;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.piyush.pictprint.api.LoginService;
import com.piyush.pictprint.model.LoginRequest;
import com.piyush.pictprint.model.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment implements LoginService.onLoginResponseListener{


    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.printit)
    TextView printit;

    @BindView(R.id.username)
    AutoCompleteTextView username;

    LoginService loginService;

    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.signup)
    Button signUp;
    @BindView(R.id.login)
    Button login;
    private onLoginListener listener;
    private ValueAnimator animator;
    String text = "PrintIt.";
    LoginService service;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = ((onLoginListener) getActivity());
    }

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
        View v = inflater.inflate(R.layout.layout_login,
                container,
                false);
        ButterKnife.bind(this, v);

        animator = ValueAnimator.ofInt(0, text.length());
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.setDuration(text.length() * 500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                printit.setText(text.substring(0, ((int) animation.getAnimatedValue())));
            }
        });
        animator.start();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSignUpClicked();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = username.getText().toString();
                Toast.makeText(getContext(),    Username, Toast.LENGTH_SHORT).show();
                String Password = password.getText().toString();
                if(Username.isEmpty())
                    username.setError("Username cannot be empty");
                if(Password.isEmpty())
                    password.setError("Password cannot be empty");
                if(!Password.isEmpty()&&!Username.isEmpty())
                {
                    try {
                        loginService.login(Username,Password,LoginFragment.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        animator.end();
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
