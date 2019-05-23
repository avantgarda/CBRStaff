package com.example.cbrstaff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Outstanding extends AppCompatActivity {

    TextView outstandingTitleText;
    RecyclerView mRecyclerView;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Staff> mStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

        outstandingTitleText = findViewById(R.id.outstandingScreenTitle);
        mRecyclerView = findViewById(R.id.outstandingRecyclerView);

        Intent intent = getIntent();
        mStaff = intent.getParcelableArrayListExtra(MainActivity.EXTRA_STAFF);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StaffAdapter(mStaff,this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
