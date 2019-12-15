package com.example.burnedjangwon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.NAJangwon.Kkamang.UnityPlayerActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private DatabaseReference myRef;

    long startTime, endTime;
    boolean endGame;

    boolean firstPlay;

    private String nick = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("main", "onCreate");
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

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mTimer = new Timer();

        endGame = false;

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
        Log.e("main", "onStart");
        SharedPreferences settings = getSharedPreferences( "myPref" , MODE_PRIVATE );
        firstPlay = settings.getBoolean("firstplay", false);
        final Animation alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        final Animation trans = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);

        mTimerTask0 = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lMain.setBackgroundResource(R.drawable.darkbackground);
                        loading.setVisibility(View.VISIBLE);
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
            mainTitle.setBackgroundResource(0x00000000);
            Glide.with(MainActivity.this).load(R.drawable.maintitle).into(mainTitle);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("main", "onResume");
        if(endGame) {
            endTime = System.currentTimeMillis();
            Log.e("endTime", Long.toString(endTime));
//            Log.e("score1", Long.toString(endTime - startTime));
//            Log.e("score2", Long.toString((endTime - startTime) / 100000));
//            Log.e("score3", Long.toString(Math.round((endTime - startTime) * 100000)));
            String point = Long.toString(endTime - startTime);
            Log.e("score", point);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                nick = user.getEmail();
            }

            if (point != null) {
                Point p = new Point();
                p.setPoint(point);
                p.setid(nick);
                myRef.push().setValue(p);
            }
            endGame = false;
            Intent in = new Intent(MainActivity.this, MusicService.class);
            startService(in);
        }
    }

    @Override
    protected void onStop() {
        Log.e("main", "onStop");
        super.onStop();
        SharedPreferences settings = getSharedPreferences( "myPref" , MODE_PRIVATE );
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstplay", firstPlay);
        editor.apply();

    }

    @Override
    protected void onDestroy() {
        Log.e("main", "onDestroy");
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
                    //showToast(MainActivity.this, "미구현");
                    Intent ins = new Intent(MainActivity.this, MusicService.class);
                    stopService(ins);
                    startTime = System.currentTimeMillis();
                    Log.e("startTime", Long.toString(endTime));
                    endGame = true;
                    Intent in = new Intent(MainActivity.this, com.NAJangwon.Kkamang.UnityPlayerActivity.class);
                    startActivity(in);
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
