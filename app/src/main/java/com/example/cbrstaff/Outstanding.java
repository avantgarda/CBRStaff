package com.example.cbrstaff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Outstanding extends AppCompatActivity {

    public static final String EXTRA_CURRENCY = "com.example.cbrstaff.EXTRA_CURRENCY";
    public static final int MAX_CRUISES = 3;

    TextView outstandingTitleText;
    RecyclerView mRecyclerView;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Staff> mStaff;
    ArrayList<AdapterItem> mItem;
    Currency mCurrency;
    boolean hideCheckboxes;

    DatabaseReference databaseStaff;

    @Override
    protected void onStart() {
        super.onStart();

        /*databaseStaff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!mStaff.isEmpty()){ mStaff.clear(); }
                for(DataSnapshot staffSnapshot : dataSnapshot.getChildren()){
                    Staff staff = staffSnapshot.getValue(Staff.class);
                    mStaff.add(staff);
                }
                // Update total outstanding balance
                updateOutstanding();
                // Refresh list
                mAdapter.notifyDataSetChanged();
                Log.i("TESTING", "onDataChange: " + mStaff.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FireBaseError", "onCancelled: Error in database.");
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mCurrency = data.getParcelableExtra(EXTRA_CURRENCY);
                hideCheckboxes = false;
//                Staff newStaff = new Staff("Tim", new Currency(5,2,1));
//                mStaff.add(newStaff);
//                mItem.add(new AdapterItem(newStaff, hideCheckboxes));
//                mStaff.get(0).setName("Peter");
                for(AdapterItem item : mItem){ item.setHideCheckbox(hideCheckboxes); }
                mAdapter.notifyDataSetChanged();
            }
            else { Log.i("IntentError", "onActivityResult: RESULT_OK not received [" + resultCode + "]"); }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

        outstandingTitleText = findViewById(R.id.outstandingScreenTitle);
        mRecyclerView = findViewById(R.id.outstandingRecyclerView);

        databaseStaff = FirebaseDatabase.getInstance().getReference("staff");

        mStaff = new ArrayList<>();
        mItem = new ArrayList<>();

        outstandingTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addSampleStaff();
                Intent intent = new Intent(Outstanding.this, Cruise.class);
                startActivityForResult(intent,1);
            }
        });

        databaseStaff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!mStaff.isEmpty()){ mStaff.clear(); mItem.clear(); }
                for(DataSnapshot staffSnapshot : dataSnapshot.getChildren()){
                    Staff staff = staffSnapshot.getValue(Staff.class);
                    mStaff.add(staff);
                    mItem.add(new AdapterItem(staff, hideCheckboxes));
                }
                // Update total outstanding balance
                updateOutstanding();
                // Refresh list
                mAdapter.notifyDataSetChanged();
                Log.i("TESTING", "onDataChange: " + mStaff.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FireBaseError", "onCancelled: Error in database.");
            }
        });

        hideCheckboxes = true;

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StaffAdapter(mItem,this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateOutstanding() {
        double totalOutstanding = 0;
        for (Staff staff : mStaff) {
            totalOutstanding += staff.getBalance().getEuro();
        }
        outstandingTitleText.setText(getString(R.string.display_euro, totalOutstanding));
    }

    private void addSampleStaff() {
        // Add sample Staff instances to FireBase
        String idFire = databaseStaff.push().getKey();
        if (TextUtils.isEmpty(idFire)){
            Log.i("FireBaseError", "addSampleStaff: FireBase key could not be generated");
            return;
        }
        Currency newBalance = new Currency(3,4,5);
        Staff newStaff = new Staff("John", newBalance);
        databaseStaff.child(idFire).setValue(newStaff);
    }
}
