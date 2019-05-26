package com.example.cbrstaff;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
    Button nextButton;

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
        nextButton = findViewById(R.id.cruiseDetailsNext);

        numCruises = 1;
        cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));

        euroInput.setText("€");
        dollarInput.setText("$");

        // Get intent and change title correspondingly
        Intent intent = getIntent();
        if(intent.getBooleanExtra(Outstanding.EXTRA_EDIT,false)){
            cruiseTitleText.setText(R.string.edit_cruise_title);
            cruiseTitleText.setTextColor(Color.RED);
            nextButton.setText(R.string.update_cruise);
            numCruises = intent.getIntExtra(Outstanding.EXTRA_CRUISES, 1);
            cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));
            Currency currency = intent.getParcelableExtra(Outstanding.EXTRA_CURRENCY);
            euroInput.setText(getString(R.string.display_euro, currency.getEuro()));
            dollarInput.setText(getString(R.string.display_dollar, currency.getDollar()));
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String euro = euroInput.getText().toString().substring(1);
                String dollar = dollarInput.getText().toString().substring(1);
                double euroValue = 0, dollarValue = 0;
                if (!TextUtils.isEmpty(euro)){ euroValue = Double.parseDouble(euro); }
                if (!TextUtils.isEmpty(dollar)){ dollarValue = Double.parseDouble(dollar); }
                Currency currency = new Currency(euroValue, dollarValue,0);
                // Return to main activity with result
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Outstanding.EXTRA_CRUISES, numCruises);
                returnIntent.putExtra(Outstanding.EXTRA_CURRENCY, currency);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        decreaseCruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numCruises == 1){ return; }
                numCruises--;
                cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));
            }
        });

        increaseCruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numCruises == Outstanding.MAX_CRUISES){ return; }
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
