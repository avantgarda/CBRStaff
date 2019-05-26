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
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Outstanding extends AppCompatActivity {

    public static final String EXTRA_CRUISES = "com.example.cbrstaff.EXTRA_CRUISES";
    public static final String EXTRA_CURRENCY = "com.example.cbrstaff.EXTRA_CURRENCY";
    public static final String EXTRA_EDIT = "com.example.cbrstaff.EXTRA_EDIT";
    public static final int MAX_CRUISES = 3;

    LinearLayout tipsLayout;
    TextView euroText;
    TextView dollarText;
    RecyclerView mRecyclerView;
    Button cancelB;
    Button confirmB;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Staff> mStaff;
    ArrayList<AdapterItem> mItem;
    Currency mCurrency;
    int mCruises;
    boolean hideCheckboxes;

    DatabaseReference databaseStaff;

    changeView changeView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mCruises = data.getIntExtra(EXTRA_CRUISES, 1);
                mCurrency = data.getParcelableExtra(EXTRA_CURRENCY);
//                Staff newStaff = new Staff("Tim", new Currency(5,2,1));
//                mStaff.add(newStaff);
//                mItem.add(new AdapterItem(newStaff, hideCheckboxes));
//                mStaff.get(0).setName("Peter");
                changeView.showRoster();
            }
            else { Log.i("IntentError", "onActivityResult: RESULT_OK not received [" + resultCode + "]"); }
        }
    }

    private interface changeView {
        void showRoster();
        void showOutstanding();
    }

    private class changeViewImpl implements changeView {

        @Override
        public void showRoster() {
            updateTitle();
            if(!hideCheckboxes){ return; }
            hideCheckboxes = false;
            updateView();
        }

        @Override
        public void showOutstanding() {
            updateOutstanding();
            updateTitle();
            if(hideCheckboxes){ return; }
            hideCheckboxes = true;
            updateView();
        }
    }

    private void updateView() {
        for(AdapterItem item : mItem){ item.setHideCheckbox(hideCheckboxes); }
        mAdapter.notifyDataSetChanged();
    }

    private void updateTitle() {
        euroText.setText(getString(R.string.display_euro, mCurrency.getEuro()));
        dollarText.setText(getString(R.string.display_dollar, mCurrency.getDollar()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

        tipsLayout = findViewById(R.id.tipsDisplay);
        euroText = findViewById(R.id.euroDisplay);
        dollarText = findViewById(R.id.dollarDisplay);
        mRecyclerView = findViewById(R.id.outstandingRecyclerView);
        cancelB = findViewById(R.id.cancelButton);
        confirmB = findViewById(R.id.confirmButton);

        databaseStaff = FirebaseDatabase.getInstance().getReference("staff");

        changeView = new changeViewImpl();

        mStaff = new ArrayList<>();
        mItem = new ArrayList<>();
        mCurrency = new Currency();

        euroText.setText(getString(R.string.display_euro,0.0));
        dollarText.setText(getString(R.string.display_dollar,0.0));

        tipsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Outstanding.this, Cruise.class);
                if(hideCheckboxes) { for(AdapterItem item : mItem){ item.resetChecked(); }}
                else {
                    intent.putExtra(EXTRA_EDIT,true);
                    intent.putExtra(Outstanding.EXTRA_CRUISES, mCruises);
                    intent.putExtra(Outstanding.EXTRA_CURRENCY, mCurrency);
                }
                startActivityForResult(intent,1);
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView.showOutstanding();
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
                // Update total outstanding balance and refresh list
                changeView.showOutstanding();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FireBaseError", "onCancelled: Error in database.");
            }
        });

        hideCheckboxes = false;

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StaffAdapter(mItem,this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateOutstanding() {
        double totalEuro = 0, totalDollar = 0;
        for (Staff staff : mStaff) {
            totalEuro += staff.getBalance().getEuro();
            totalDollar += staff.getBalance().getDollar();
        }
        mCurrency.setEuro(totalEuro);
        mCurrency.setDollar(totalDollar);
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
