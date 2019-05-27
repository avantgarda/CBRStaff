package com.example.cbrstaff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Outstanding extends AppCompatActivity {

    public static final String EXTRA_CRUISES = "com.example.cbrstaff.EXTRA_CRUISES";
    public static final String EXTRA_CURRENCY = "com.example.cbrstaff.EXTRA_CURRENCY";
    public static final String EXTRA_CURRENCY_MORE = "com.example.cbrstaff.EXTRA_CURRENCY_MORE";
    public static final String EXTRA_EDIT = "com.example.cbrstaff.EXTRA_EDIT";
    public static final int RESULT_CRUISE = 1;
    public static final int RESULT_EXCHANGE = 2;
    public static final int MAX_CRUISES = 3;

    LinearLayout tipsLayoutOuter;
    LinearLayout tipsLayoutInner;
    ConstraintLayout buttonContainer;
    TextView euroText;
    TextView dollarText;
    TextView sterlingText;
    RecyclerView mRecyclerView;
    ImageButton cancelB;
    ImageButton confirmB;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Staff> mStaff;
    ArrayList<AdapterItem> mItem;
    ArrayList<String> mKeys;
    Currency mCurrency;
    int mCruises, prevCruises;
    boolean hideCheckboxes;

    DatabaseReference databaseStaff;

    changeView changeView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CRUISE) {
            if(resultCode == Activity.RESULT_OK){
                prevCruises = mCruises;
                mCruises = data.getIntExtra(EXTRA_CRUISES, RESULT_CRUISE);
                mCurrency = data.getParcelableExtra(EXTRA_CURRENCY);
                changeView.showRoster();
            }
            else { Log.i("IntentError", "onActivityResult: RESULT_OK (CRUISE) not received [" + resultCode + "]"); }
        }
        else if (requestCode == RESULT_EXCHANGE) {
            if(resultCode == Activity.RESULT_OK){
                Currency updatedExchange = data.getParcelableExtra(EXTRA_CURRENCY);
                Currency updatedEuro = data.getParcelableExtra(EXTRA_CURRENCY_MORE);

                Map<String, Staff> updatedStaff = new HashMap<>();
                int index = 0;

                // Calculate exchanges and allocate
                for(Staff staff : mStaff){
                    double staffEuro = staff.getBalance().getEuro();
                    double staffDollar = staff.getBalance().getDollar();
                    double staffSterling = staff.getBalance().getSterling();
                    if(staffDollar > 0){
                        double dollarFraction = staffDollar / mCurrency.getDollar();
                        staffDollar -= updatedExchange.getDollar() * dollarFraction;
                        staffEuro += updatedEuro.getDollar() * dollarFraction;
                    }
                    if(staffSterling > 0){
                        double sterlingFraction = staffSterling / mCurrency.getSterling();
                        staffSterling -= updatedExchange.getSterling() * sterlingFraction;
                        staffEuro += updatedEuro.getSterling() * sterlingFraction;
                    }

                    // Put updated staff into FireBase array
                    updatedStaff.put(mKeys.get(index++), new Staff(staff.getName(), new Currency(staffEuro, staffDollar, staffSterling)));
                }
                // Push updates to FireBase
                databaseStaff.setValue(updatedStaff);
            }
            else { Log.i("IntentError", "onActivityResult: RESULT_OK (EXCHANGE) not received [" + resultCode + "]"); }
        }
    }

    private interface changeView {
        void showRoster();
        void showOutstanding();
        void refresh();
    }

    private class changeViewImpl implements changeView {

        @Override
        public void showRoster() {
            updateTitle();
            if(!hideCheckboxes && prevCruises == mCruises){ return; }
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

        @Override
        public void refresh() {
            if(hideCheckboxes){ updateOutstanding(); }
            updateTitle();
            updateView();
        }
    }

    private void updateView() {
        boolean[] temp = new boolean[MAX_CRUISES];
        if(hideCheckboxes) { buttonContainer.setVisibility(View.GONE); Arrays.fill(temp, Boolean.TRUE); }
        else {
            for(int i = 0; i < (MAX_CRUISES - mCruises); i++){ temp[MAX_CRUISES - i - 1] = true; }
            buttonContainer.setVisibility(View.VISIBLE);
        }
        for(AdapterItem item : mItem){ item.setHideCheckbox(temp); }
        mAdapter.notifyDataSetChanged();
    }

    private void updateTitle() {
        euroText.setText(getString(R.string.display_euro, mCurrency.getEuro()));
        dollarText.setText(getString(R.string.display_dollar, mCurrency.getDollar()));
        sterlingText.setText(getString(R.string.display_sterling, mCurrency.getSterling()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

        tipsLayoutOuter = findViewById(R.id.tipsDisplayOuter);
        tipsLayoutInner = findViewById(R.id.tipsDisplayInner);
        buttonContainer = findViewById(R.id.buttonLayout);
        euroText = findViewById(R.id.euroDisplay);
        dollarText = findViewById(R.id.dollarDisplay);
        sterlingText = findViewById(R.id.sterlingDisplay);
        mRecyclerView = findViewById(R.id.outstandingRecyclerView);
        cancelB = findViewById(R.id.cancelButton);
        confirmB = findViewById(R.id.confirmButton);

        databaseStaff = FirebaseDatabase.getInstance().getReference("staff");

        changeView = new changeViewImpl();

        mStaff = new ArrayList<>();
        mItem = new ArrayList<>();
        mKeys = new ArrayList<>();
        mCurrency = new Currency();

        mCruises = 1;
        prevCruises = 1;

        euroText.setText(getString(R.string.main_screen_loading));

        buttonContainer.setVisibility(View.GONE);

        tipsLayoutOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mStaff.size() == 0){ return; }
                Intent intent = new Intent(Outstanding.this, Cruise.class);
                if(hideCheckboxes) { for(AdapterItem item : mItem){ item.resetChecked(); }}
                else {
                    intent.putExtra(EXTRA_EDIT,true);
                    intent.putExtra(Outstanding.EXTRA_CRUISES, mCruises);
                    intent.putExtra(Outstanding.EXTRA_CURRENCY, mCurrency);
                }
                startActivityForResult(intent, RESULT_CRUISE);
            }
        });

        tipsLayoutOuter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!hideCheckboxes || (mCurrency.getSterling() == 0 && mCurrency.getDollar() == 0)){ return true; }
                // Start currency exchange dialog
                Intent intent = new Intent(Outstanding.this, Exchange.class);
                intent.putExtra(Outstanding.EXTRA_CURRENCY, mCurrency);
                startActivityForResult(intent, RESULT_EXCHANGE);
                return true;
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView.showOutstanding();
            }
        });

        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCheckboxes = true;
                calculateTips();
            }
        });

//        confirmB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addSampleStaff();
//            }
//        });

        databaseStaff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<AdapterItem> tempItem = new ArrayList<>();
                ArrayList<String> tempKey = new ArrayList<>();

                if(!mStaff.isEmpty()){ mStaff.clear(); }
                for(DataSnapshot staffSnapshot : dataSnapshot.getChildren()){
                    Staff staff = staffSnapshot.getValue(Staff.class);
                    mStaff.add(staff);
                    tempKey.add(staffSnapshot.getKey());
                }

                for(int x = 0; x < mStaff.size(); x++){
                    boolean flag = false;
                    int y = 0;
                    while(y < mKeys.size()){
                        if(tempKey.get(x).equals(mKeys.get(y++))){ flag = true; break; }
                    }
                    if(flag && (y > 0)) { tempItem.add(new AdapterItem(mStaff.get(x), hideCheckboxes, mItem.get(y - 1).getChecked())); }
                    else { tempItem.add(new AdapterItem(mStaff.get(x), hideCheckboxes)); }
                }

                mItem.clear();
                mKeys.clear();
                mItem.addAll(tempItem);
                mKeys.addAll(tempKey);

                // Update total outstanding balance and refresh list
                changeView.refresh();
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

        buttonContainer.bringToFront();
    }

    private void calculateTips() {

        int staffCount = 0, index = 0;
        Map<String, Staff> updatedStaff = new HashMap<>();
        // Count total staff per cruise
        for(AdapterItem item : mItem){
            for(int cruise = 0; cruise < mCruises; cruise++){
                if(item.getChecked()[cruise]){ staffCount++; }
            }
        }
        if(staffCount == 0){ hideCheckboxes = false; changeView.showOutstanding(); return; }
        // Calculate divisions and allocate
        for(AdapterItem item : mItem){
            Currency currency = item.getStaff().getBalance();
            for(int cruise = 0; cruise < mCruises; cruise++){
                if(item.getChecked()[cruise]) {
                    currency.setEuro(currency.getEuro() + mCurrency.getEuro() / staffCount);
                    currency.setDollar(currency.getDollar() + mCurrency.getDollar() / staffCount);
                    currency.setSterling(currency.getSterling() + mCurrency.getSterling() / staffCount);
                }
            }
            // Upload changed item to FireBase
            updatedStaff.put(mKeys.get(index++), new Staff(item.getStaff().getName(), currency));
        }
        // Push updates to FireBase
        databaseStaff.setValue(updatedStaff);
    }

    private void updateOutstanding() {
        double totalEuro = 0, totalDollar = 0, totalSterling = 0;
        for (Staff staff : mStaff) {
            totalEuro += staff.getBalance().getEuro();
            totalDollar += staff.getBalance().getDollar();
            totalSterling += staff.getBalance().getSterling();
        }
        mCurrency.setEuro(totalEuro);
        mCurrency.setDollar(totalDollar);
        mCurrency.setSterling(totalSterling);
    }

    private void addSampleStaff() {
        // Add sample Staff instances to FireBase
        String idFire = databaseStaff.push().getKey();
        if (TextUtils.isEmpty(idFire)){
            Log.i("FireBaseError", "addSampleStaff: FireBase key could not be generated");
            return;
        }
        Currency newBalance = new Currency(3,4,5);
        Staff newStaff = new Staff("Simon", newBalance);
        databaseStaff.child(idFire).setValue(newStaff);
    }
}
