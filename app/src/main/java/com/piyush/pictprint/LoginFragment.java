package com.piyush.pictprint;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.piyush.pictprint.Utils.ColorUtil;
import com.piyush.pictprint.api.LoginService;
import com.piyush.pictprint.listeners.onLoginListener;
import com.piyush.pictprint.model.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class LoginFragment extends Fragment implements LoginService.onLoginResponseListener{


    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.parent_container)
    ViewGroup parentContainer;

    @BindView(R.id.printit)
    TextView printit;

    @BindView(R.id.username)
    AutoCompleteTextView username;


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

    @BindView(R.id.progress)
    ProgressBar progressBar;

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
                String Password = password.getText().toString();
                if(Username.isEmpty())
                    username.setError("Username cannot be empty");
                if(Password.isEmpty())
                    password.setError("Password cannot be empty");
                setProgressBarGone(false);
                if(!Password.isEmpty()&&!Username.isEmpty())
                {
                    try {
                        service.login(Username,Password,LoginFragment.this);
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

    private void setProgressBarGone(boolean bool) {
        if (bool) {


            TransitionManager.beginDelayedTransition(parentContainer);
           container.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            TransitionManager.beginDelayedTransition(parentContainer);
            container.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onLoginResponse(Response<LoginResponse> response) {


        if(response.code()==200)
        {
            SharedPreferences.Editor editor =  PreferenceManager
                    .getDefaultSharedPreferences(getContext())
                    .edit();

                    editor.putString("Token",response.body().getToken());
                    editor.putString("Username", username.getText().toString());
                    editor.putBoolean("LoggedIn", true);
                    editor.apply();
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  makeToast();
                    getContext().startActivity(intent);
                    getActivity().finish();
        }
        else if(response.code()==400)
        {            Toast.makeText(getContext(), "User doesn't exist", Toast.LENGTH_SHORT).show();}
        setProgressBarGone(true);

    }

    public void makeToast()
    {
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        View v = getLayoutInflater().inflate(R.layout.logged_in_toast,null);
        ((TextView) v.findViewById(R.id.message)).setText("Logged in as");
        ((TextView) v.findViewById(R.id.name)).setText(username.getText());

        v.findViewById(R.id.scrim)
                .setBackground(ColorUtil
                        .makeCubicGradientScrimDrawable(getResources()
                                .getColor(R.color.scrim),
                                5,
                                Gravity.BOTTOM));
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onLoginFailed(String t) {
        Toast.makeText(getContext(), t, Toast.LENGTH_SHORT).show();
        setProgressBarGone(true);
    }
}
