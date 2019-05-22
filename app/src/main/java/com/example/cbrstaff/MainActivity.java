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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextView mainTitleText;
    TextView outstandingTipsText;
    Button newTipsButton;
    Button outstandingTipsButton;

    DatabaseReference databaseStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTitleText = findViewById(R.id.mainScreenTitle);
        outstandingTipsText = findViewById(R.id.outstandTipsView);
        newTipsButton = findViewById(R.id.newTipsButton);
        outstandingTipsButton = findViewById(R.id.outstandTipsButton);

        databaseStaff = FirebaseDatabase.getInstance().getReference("staff");

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
