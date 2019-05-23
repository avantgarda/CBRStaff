package com.example.cbrstaff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ROSTER = "com.example.cbrstaff.EXTRA_ROSTER";
    public static final String EXTRA_WORKDAY = "com.example.cbrstaff.EXTRA_WORKDAY";

    public static final int SAMPLE_ROSTER_INDEX = 0;

    TextView mainTitleText;
    TextView outstandingTipsText;
    Button newTipsButton;
    Button outstandingTipsButton;

    DatabaseReference databaseRoster;
    DatabaseReference databaseWorkDay;
    ArrayList<Roster> rosterList;
    ArrayList<WorkDay> workDayList;

    @Override
    protected void onStart() {
        super.onStart();

        databaseRoster.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!rosterList.isEmpty()){ rosterList.clear(); }
                for(DataSnapshot rosterSnapshot : dataSnapshot.getChildren()){
                    Roster roster = rosterSnapshot.getValue(Roster.class);
                    rosterList.add(roster);
                }
                // Update total outstanding balance
                updateOutstanding();
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
        setContentView(R.layout.activity_main);

        mainTitleText = findViewById(R.id.mainScreenTitle);
        outstandingTipsText = findViewById(R.id.outstandTipsView);
        newTipsButton = findViewById(R.id.newTipsButton);
        outstandingTipsButton = findViewById(R.id.outstandTipsButton);

        databaseRoster = FirebaseDatabase.getInstance().getReference("roster");
        databaseWorkDay = FirebaseDatabase.getInstance().getReference("workDay");

        rosterList = new ArrayList<>();
        workDayList = new ArrayList<>();

        newTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSampleRoster();
                /*String idFire = databaseWorkDay.push().getKey();
                // Change Outstanding to new activity class for WorkDay creation
                Intent intent = new Intent(MainActivity.this, Outstanding.class);
                intent.putExtra(EXTRA_WORKDAY, idFire);
                startActivity(intent);*/
            }
        });

        outstandingTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Outstanding.class);
                intent.putExtra(EXTRA_ROSTER, getRoster(rosterList, SAMPLE_ROSTER_INDEX));
                startActivity(intent);
            }
        });
    }

    private void updateOutstanding() {
        double totalOutstanding = 0;
        if(!rosterList.isEmpty()) {
            if(!rosterList.get(SAMPLE_ROSTER_INDEX).getStaffList().isEmpty()) {
                for (Staff staff : rosterList.get(SAMPLE_ROSTER_INDEX).getStaffList()) {
                    totalOutstanding += staff.getBalance().getEuro();
                }
            }
        }
        outstandingTipsText.setText(getString(R.string.display_euro, totalOutstanding));
    }

    private Roster getRoster(ArrayList<Roster> rosterArrayList, int index) {
        if(rosterArrayList.isEmpty()){ return new Roster(); }
        return rosterArrayList.get(index);
    }

    private void addSampleRoster() {
        // Add sample Roster instance to FireBase
        String idFire = databaseRoster.push().getKey();
        if (TextUtils.isEmpty(idFire)){
            Log.i("FireBaseError", "addSampleRoster: FireBase key could not be generated");
            return;
        }
        Currency newBalance = new Currency(3,4,5);
        Staff newStaff = new Staff("John", newBalance);
        ArrayList<Staff> newStaffList = new ArrayList<>();
        newStaffList.add(newStaff);
        newStaffList.add(newStaff);
        newStaffList.add(newStaff);
        Roster newRoster = new Roster(newStaffList);
        databaseRoster.child(idFire).setValue(newRoster);
    }
}
