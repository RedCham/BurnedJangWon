package com.example.burnedjangwon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Point> chatList;
    private String nick = "default";


    private EditText pointText;
    private Button Button_send;
    private DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Button_send = findViewById(R.id.btnPost);
        pointText = findViewById(R.id.pointEdit);
        //IDText = findViewById(R.id.IDEdit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nick = user.getEmail();
        }

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String id = IDText.getText().toString();
                String point = pointText.getText().toString();

                if (point != null) {
                    Point p = new Point();
                    p.setPoint(point);
                    p.setid(nick);
                    myRef.push().setValue(p);
                }

            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new ListAdapter(chatList, RankingActivity.this, nick);

        mRecyclerView.setAdapter(mAdapter);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        //caution!!!
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                //Log.d("CHATCHAT", dataSnapshot.getValue().toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Point p = snapshot.getValue(Point.class);
                    ((ListAdapter) mAdapter).addPoint(p);
                }
                chatList.sort(new Comparator<Point>() {
                    @Override
                    public int compare(Point o1, Point o2) {
                        if (o1.getPointInt() == o2.getPointInt())
                            return 0;
                        else if (o1.getPointInt() > o2.getPointInt())
                            return -1;
                        else
                            return 1;
                    }
                });
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("RankingActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }
}

