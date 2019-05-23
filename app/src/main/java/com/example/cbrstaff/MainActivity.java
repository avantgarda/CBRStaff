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

    public static final String EXTRA_STAFF = "com.example.cbrstaff.EXTRA_STAFF";

    TextView mainTitleText;
    TextView outstandingTipsText;
    Button newTipsButton;
    Button outstandingTipsButton;

    DatabaseReference databaseStaff;
    ArrayList<Staff> mStaff;

    @Override
    protected void onStart() {
        super.onStart();

        databaseStaff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!mStaff.isEmpty()){ mStaff.clear(); }
                for(DataSnapshot staffSnapshot : dataSnapshot.getChildren()){
                    Staff staff = staffSnapshot.getValue(Staff.class);
                    mStaff.add(staff);
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

        databaseStaff = FirebaseDatabase.getInstance().getReference("staff");

        mStaff = new ArrayList<>();

        newTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSampleStaff();
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
                intent.putParcelableArrayListExtra(EXTRA_STAFF, mStaff);
                startActivity(intent);
            }
        });
    }

    private void updateOutstanding() {
        double totalOutstanding = 0;
        for (Staff staff : mStaff) {
            totalOutstanding += staff.getBalance().getEuro();
        }
        outstandingTipsText.setText(getString(R.string.display_euro, totalOutstanding));
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
