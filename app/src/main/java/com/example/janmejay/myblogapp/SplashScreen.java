package com.example.janmejay.myblogapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    ProgressBar mprogressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
if(FirebaseAuth.getInstance().getCurrentUser()==null) {

    Handler handler = new Handler();

    handler.postDelayed(new Runnable() {
        @Override
        public void run() {

            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();

        }
    }, 3000);
}
else
{
    Handler handler = new Handler();

    handler.postDelayed(new Runnable() {
        @Override
        public void run() {

            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();

        }
    }, 3000);
}
    }
}
