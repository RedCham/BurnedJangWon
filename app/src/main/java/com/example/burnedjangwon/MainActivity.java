package com.example.burnedjangwon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.burnedjangwon.Util.showToast;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;

    Button login;
    Button logout;
    Button signup;
    Button rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        login = findViewById(R.id.loginButton);
        logout = findViewById(R.id.logoutButton);
        signup = findViewById(R.id.signUpButton);
        rank = findViewById(R.id.rankingButton);

        login.setOnClickListener(onClickListener);
        logout.setOnClickListener(onClickListener);
        signup.setOnClickListener(onClickListener);
        rank.setOnClickListener(onClickListener);

        findViewById(R.id.startButton).setOnClickListener(onClickListener);
        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);


        //init();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
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
