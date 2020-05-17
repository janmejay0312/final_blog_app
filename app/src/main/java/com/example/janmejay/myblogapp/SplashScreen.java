package com.example.janmejay.myblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    ProgressBar mprogressBar;
    private FirebaseAuth firebaseAuth;
    private LinearLayout rel;
    private LinearLayout lin;
    Animation animation1,animation2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        mprogressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        rel=findViewById(R.id.relative);
        lin=findViewById(R.id.l2);
        animation2=AnimationUtils.loadAnimation(this,R.anim.animation2);
        animation1=AnimationUtils.loadAnimation(this,R.anim.animation1);
        rel.setAnimation(animation1);
        lin.setAnimation(animation2);
     if(FirebaseAuth.getInstance().getCurrentUser()==null) {

    Handler handler = new Handler();

    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
new Thread(new Runnable() {
    @Override
    public void run() {
        doWork();
        startAppLogin();
        finish();

    }
}).start();

        }
    }, 3000);
}
else if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
{
    Handler handler = new Handler();

    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doWork();
                    startAppMain();
                    finish();
                }
            }).start();


        }
    },3000);
}
else{
         Handler handler = new Handler();

         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 new Thread(new Runnable() {
                     @Override
                     public void run() {
                         doWork();
                         startAppLogin();
                         finish();

                     }
                 }).start();

             }
         }, 3000);
     }
    }

    private void startAppLogin() {
        Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
        startActivity(intent);
    }

    private void startAppMain() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=50) {
            try {
                Thread.sleep(100);
                mprogressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

}
