package com.example.cbrstaff;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class Cruise extends AppCompatActivity {

    TextView cruiseTitleText;
    TextView cruiseNumText;
    TextView totalTipsText;
    FloatingActionButton decreaseCruise;
    FloatingActionButton increaseCruise;
    LinearLayout cruiseNumberLayout;
    LinearLayout totalTipsLayout;
    EditText euroInput;
    EditText dollarInput;

    int numCruises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruise);

        cruiseTitleText = findViewById(R.id.cruiseTitle);
        cruiseNumText = findViewById(R.id.cruiseNumDisplay);
        totalTipsText = findViewById(R.id.totalTipsTitle);
        decreaseCruise= findViewById(R.id.decreaseCruiseButton);
        increaseCruise= findViewById(R.id.increaseCruiseButton);
        cruiseNumberLayout = findViewById(R.id.cruiseNumLayout);
        totalTipsLayout = findViewById(R.id.tipsCurrencyLayout);
        euroInput = findViewById(R.id.euroTips);
        dollarInput = findViewById(R.id.dollarTips);

        numCruises = 1;
        cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));

        euroInput.setText("€");
        dollarInput.setText("$");

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

        Selection.setSelection(euroInput.getText(), euroInput.getText().length());
        Selection.setSelection(dollarInput.getText(), dollarInput.getText().length());

        euroInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("€")){
                    euroInput.setText("€");
                    Selection.setSelection(euroInput.getText(), euroInput.getText().length());
                }
            }
        });

        dollarInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("$")){
                    dollarInput.setText("$");
                    Selection.setSelection(dollarInput.getText(), dollarInput.getText().length());
                }
            }
        });
    }
}
