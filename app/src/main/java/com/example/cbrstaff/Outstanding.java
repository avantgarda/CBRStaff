package com.example.cbrstaff;

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

    DatabaseReference databaseStaff;
    ArrayList<Staff> staffList;

    @Override
    protected void onStart() {
        super.onStart();

        databaseStaff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!staffList.isEmpty()){ staffList.clear(); }
                for(DataSnapshot staffSnapshot : dataSnapshot.getChildren()){
                    Staff staff = staffSnapshot.getValue(Staff.class);
                    staffList.add(staff);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FireBaseError", "onCancelled: Error in database.");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

        outstandingTitleText = findViewById(R.id.outstandingScreenTitle);
        mRecyclerView = findViewById(R.id.outstandingRecyclerView);

        databaseStaff = FirebaseDatabase.getInstance().getReference("staff");

        staffList = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StaffAdapter(staffList,this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
