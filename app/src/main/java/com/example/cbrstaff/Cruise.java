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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    LinearLayout totalTipsLayoutOuter;
    LinearLayout totalTipsLayoutInner;
    EditText euroInput;
    EditText dollarInput;
    EditText sterlingInput;
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
        totalTipsLayoutOuter = findViewById(R.id.tipsCurrencyLayoutOuter);
        totalTipsLayoutInner = findViewById(R.id.tipsCurrencyLayoutInner);
        euroInput = findViewById(R.id.euroTips);
        dollarInput = findViewById(R.id.dollarTips);
        sterlingInput = findViewById(R.id.sterlingTips);
        nextButton = findViewById(R.id.cruiseDetailsNext);

        numCruises = 1;
        cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));

        euroInput.setText("€");
        dollarInput.setText("$");
        sterlingInput.setText("£");

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
            sterlingInput.setText(getString(R.string.display_sterling, currency.getSterling()));
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String euro = euroInput.getText().toString().substring(1);
                String dollar = dollarInput.getText().toString().substring(1);
                String sterling = sterlingInput.getText().toString().substring(1);
                double euroValue = 0, dollarValue = 0, sterlingValue = 0;
                if (!TextUtils.isEmpty(euro)){ euroValue = Double.parseDouble(euro); }
                if (!TextUtils.isEmpty(dollar)){ dollarValue = Double.parseDouble(dollar); }
                if (!TextUtils.isEmpty(sterling)){ sterlingValue = Double.parseDouble(sterling); }
                Currency currency = new Currency(euroValue, dollarValue, sterlingValue);
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

        textListenerSetup(totalTipsLayoutOuter);
    }

    private void textListenerSetup(ViewGroup view) {
        for(int i = 0; i < view.getChildCount(); i++){
            View v = view.getChildAt(i);
            if(v instanceof ViewGroup){ textListenerSetup((ViewGroup)v); }
            else if(v instanceof EditText){
                final EditText vText = (EditText)v;
                vText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String temp = s.toString();
                        if (!temp.startsWith("$") &&
                            !temp.startsWith("£") &&
                            !temp.startsWith("€")) {
                            vText.setText(vText.getHint());
                            Selection.setSelection(vText.getText(), vText.getText().length());
                        }
                    }
                });
            }
        }
    }
}
