package com.example.cbrstaff;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView mainTitleText;
    TextView outstandingTipsText;
    Button newTipsButton;
    Button outstandingTipsButton;

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

    private void updateOutstanding() {
        double totalOutstanding = 0;
        for(Staff staff : staffList){
            totalOutstanding += staff.getBalance().getEuro();
        }
        outstandingTipsText.setText(getString(R.string.display_euro, totalOutstanding));
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

        staffList = new ArrayList<>();

        newTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add sample Staff instance to FireBase
                String idFire = databaseStaff.push().getKey();
                if (TextUtils.isEmpty(idFire)){
                    Log.i("FireBaseError", "onClick: FireBase key could not be generated");
                    return;
                }
                Currency newBalance = new Currency(3,4,5);
                Staff newStaff = new Staff("John", newBalance);
                databaseStaff.child(idFire).setValue(newStaff);
            }
        });
    }
}
