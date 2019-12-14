package com.example.burnedjangwon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.burnedjangwon.Util.showToast;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;

    LinearLayout lMain;
    ImageView loading;
    ImageView mainTitle;
    ImageButton start;
    ImageButton login;
    ImageButton logout;
    ImageButton signup;
    ImageButton rank;

    Timer mTimer;
    TimerTask mTimerTask0, mTimerTask1, mTimerTask2, mTimerTask3;

    boolean firstPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        lMain = findViewById(R.id.main_layout);
        start = findViewById(R.id.startButton);
        login = findViewById(R.id.loginButton);
        logout = findViewById(R.id.logoutButton);
        signup = findViewById(R.id.signUpButton);
        rank = findViewById(R.id.rankingButton);
        mainTitle = findViewById(R.id.mainTitle);
        loading = findViewById(R.id.loading);

        login.setOnClickListener(onClickListener);
        logout.setOnClickListener(onClickListener);
        signup.setOnClickListener(onClickListener);
        rank.setOnClickListener(onClickListener);

        findViewById(R.id.startButton).setOnClickListener(onClickListener);
        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);

        mTimer = new Timer();
        //init();
        Intent in = new Intent(MainActivity.this, MusicService.class);
        startService(in);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
        final Animation alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        final Animation trans = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);

        mTimerTask0 = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lMain.setBackgroundResource(R.drawable.darkbackground);
                        loading.setImageResource(R.drawable.loading_text1);
                        loading.startAnimation(alpha);
                        start.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.INVISIBLE);
                        logout.setVisibility(View.GONE);
                        signup.setVisibility(View.INVISIBLE);
                        rank.setVisibility(View.INVISIBLE);
                        mainTitle.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };

        mTimerTask1 = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setImageResource(R.drawable.loading_text2);
                        loading.startAnimation(alpha);
                    }
                });
            }
        };

        mTimerTask2 = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lMain.setBackgroundResource(R.drawable.background);
                        loading.setVisibility(View.GONE);
                        mainTitle.setVisibility(View.VISIBLE);
                        mainTitle.startAnimation(trans);
                    }
                });
            }
        };

        mTimerTask3 = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        mainTitle.setBackgroundResource(0x00000000);
                        start.setVisibility(View.VISIBLE);
                        rank.setVisibility(View.VISIBLE);
                        if (firebaseUser == null) {
                            logout.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                            signup.setVisibility(View.VISIBLE);
                        }
                        else {
                            login.setVisibility(View.GONE);
                            logout.setVisibility(View.VISIBLE);
                            signup.setVisibility(View.GONE);
                        }
                        Glide.with(MainActivity.this).load(R.drawable.maintitle).into(mainTitle);
                    }
                });
            }
        };

        if (!firstPlay) {
            mTimer.schedule(mTimerTask0, 0);
            mTimer.schedule(mTimerTask1, 5000);
            mTimer.schedule(mTimerTask2, 12000);
            mTimer.schedule(mTimerTask3, 14000);
            firstPlay = true;
        }
        else {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser == null) {
                logout.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                signup.setVisibility(View.VISIBLE);
            }
            else {
                login.setVisibility(View.GONE);
                logout.setVisibility(View.VISIBLE);
                signup.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
    }

    private void init() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            myStartActivity(SignUpActivity.class);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startButton :
                    showToast(MainActivity.this, "미구현");
                    break;
                case R.id.logoutButton :
                    FirebaseAuth.getInstance().signOut();
                    showToast(MainActivity.this, "로그아웃 완료");
                    recreate();
                    break;
                case R.id.loginButton :
                    myStartActivity(LoginActivity.class);
                    break;
                case R.id.rankingButton :
                    myStartActivity(RankingActivity.class);
                    //showToast(MainActivity.this, "미구현");
                    break;
                case R.id.signUpButton :
                    myStartActivity(SignUpActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public static void restartActivity(Activity act){

        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        act.startActivity(intent);
        act.finish();

    }
}
