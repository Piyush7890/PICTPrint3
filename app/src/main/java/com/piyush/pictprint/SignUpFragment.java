package com.piyush.pictprint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.piyush.pictprint.Utils.Utils;
import com.piyush.pictprint.api.LoginService;
import com.piyush.pictprint.model.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SignUpFragment  extends Fragment implements LoginService.onLoginResponseListener{

    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.parent_container)
    ViewGroup parentContainer;

    @BindView(R.id.username)
    AutoCompleteTextView username;

    @BindView(R.id.email)
    AutoCompleteTextView email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.confirm_password)
    EditText comfirmPassword;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.signup)
    Button signup;

    LoginService service;

    @BindView(R.id.progress)
    ProgressBar progressBar ;
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
                String Email = email.getText().toString();
                String ConfirmPassword = comfirmPassword.getText().toString();
                if(Username.isEmpty()||Password.isEmpty()||ConfirmPassword.isEmpty()|| Email.isEmpty())
                {
                    if(Username.isEmpty())
                        username.setError("Username can't be empty");
                    if(Password.isEmpty())
                        password.setError("Password can't be empty");
                    if(ConfirmPassword.isEmpty())
                        comfirmPassword.setError("Password can't be empty");
                    if(Email.isEmpty())
                        email.setError("Email can't be empty");
                    return;
                }
                if(!Password.equals(ConfirmPassword))
                {
                    Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utils.isEmailValid(Email))
                {
                    Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_SHORT).show();
                return;
                }
                setProgressBarGone(true);
                service.onSignUp(Email,Username,Password,SignUpFragment.this);
            }
        });
        return v;

    }

    private void setProgressBarGone(boolean bool) {
        if (!bool) {


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
        if(response.body()!=null) {
            if (response.code() == 400) {
                Toast.makeText(getContext(), "User already exists", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.code()==200) {

                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(getContext())
                        .edit();
                editor.putString("Token", response.body().getToken());
                editor.putString("Username",username.getText().toString());
                editor.putBoolean("LoggedIn", true);
                editor.apply();
                makeToast();
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(i);
                getActivity().finish();

            } else{
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        else {
            Toast.makeText(getContext(), "Some error occured!", Toast.LENGTH_SHORT).show();
        }
        setProgressBarGone(false);
    }

    public void makeToast()
    {
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        View v = getLayoutInflater().inflate(R.layout.logged_in_toast,null);
        ((TextView) v.findViewById(R.id.message)).setText("Signed up as");
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
        setProgressBarGone(false);

//        Toast.makeText(getContext(), t, Toast.LENGTH_SHORT).show();
    }
}
