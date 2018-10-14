package com.piyush.pictprint;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i;
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("LoggedIn",false))
        {
          i  = new Intent(new Intent(this, MainActivity.class));
        }
        else {
            i = new Intent(new Intent(this, LoginActivity.class));
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
                //  i  = new Intent(new Intent(this, MainActivity.class));
//startActivity(i);

    }

}
