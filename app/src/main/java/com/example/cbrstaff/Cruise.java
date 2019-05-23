package com.example.cbrstaff;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class Cruise extends AppCompatActivity {

    TextView cruiseTitleText;
    TextView cruiseNumText;
    FloatingActionButton decreaseCruise;
    FloatingActionButton increaseCruise;
    LinearLayout cruiseNumberLayout;

    int numCruises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruise);

        cruiseTitleText = findViewById(R.id.cruiseTitle);
        cruiseNumText = findViewById(R.id.cruiseNumDisp);
        decreaseCruise= findViewById(R.id.decrCruiseButton);
        increaseCruise= findViewById(R.id.incrCruiseButton);
        cruiseNumberLayout = findViewById(R.id.cruiseNumLayout);

        numCruises = 1;
        cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));

        decreaseCruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numCruises--;
                cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));
            }
        });

        increaseCruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numCruises++;
                cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));
            }
        });
    }
}
