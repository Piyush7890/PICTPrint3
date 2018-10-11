package com.piyush.pictprint;

import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.piyush.pictprint.api.LoginService;
import com.piyush.pictprint.model.LoginResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements onLoginListener, LoginService.onLoginResponseListener {

@BindView(R.id.parent)
    ViewGroup parent;

@BindView(R.id.viewpager)
    ViewPager viewPager;
    LoginService service;

    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        service = new LoginService();
        animationDrawable = ((AnimationDrawable) parent.getBackground());
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        List<Fragment> list = new ArrayList<>();
        list.add(new LoginFragment());
        list.add(new SignUpFragment());
        LoginPagerAdapter adapter = new LoginPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        animationDrawable.stop();
    }

    @Override
    public void onSignUpClicked() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onLogin(String username, String password) {
        try {
            service.login(username,password, this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to encrypt string", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginResponse(LoginResponse response) {

    }

    @Override
    public void onLoginFailed(String t) {
        Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
    }
}
