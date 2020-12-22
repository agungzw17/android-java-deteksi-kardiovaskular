package com.agung.deteksikardiovaskular.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.ui.login.LoginActivity;
import com.agung.deteksikardiovaskular.ui.menu.MenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        TextView title1 = findViewById(R.id.title1);
        TextView title2 = findViewById(R.id.title2);

        Typeface customFont = Typeface.createFromAsset(getAssets(),"font/kuebeg.personal-use.otf");
        title1.setTypeface(customFont);
        title2.setTypeface(customFont);

        Timer timer = new Timer();
        timer.schedule((TimerTask)(new TimerTask() {
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    Intent menuActivity = new Intent((Context)MainActivity.this, MenuActivity.class);
                    MainActivity.this.startActivity(menuActivity);
                    MainActivity.this.finish();
                }
                if(user == null) {
                    Intent loginActivity = new Intent((Context)MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(loginActivity);
                    MainActivity.this.finish();
                }
            }
        }), 3000L);
    }
}