package com.cbr.cbrstaff;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.Locale;

public class Cruise extends AppCompatActivity {

    TextView cruiseTitleText;
    TextView cruiseNumText;
    TextView totalTipsText;
    FloatingActionButton decreaseCruise;
    FloatingActionButton increaseCruise;
    LinearLayout cruiseNumberLayout;
    LinearLayout totalTipsLayoutOuter;
    LinearLayout totalTipsLayoutInnerOne;
    LinearLayout totalTipsLayoutInnerTwo;
    LinearLayout totalTipsLayoutInnerThree;
    EditText euroInputOne, euroInputTwo, euroInputThree;
    EditText dollarInputOne, dollarInputTwo, dollarInputThree;
    EditText sterlingInputOne, sterlingInputTwo, sterlingInputThree;
    Button nextButton;

    int numCruises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruise);

        cruiseTitleText = findViewById(R.id.addStaffTitle);
        cruiseNumText = findViewById(R.id.cruiseNumDisplay);
        totalTipsText = findViewById(R.id.staffNameInput);
        decreaseCruise= findViewById(R.id.decreaseCruiseButton);
        increaseCruise= findViewById(R.id.increaseCruiseButton);
        cruiseNumberLayout = findViewById(R.id.cruiseNumLayout);
        totalTipsLayoutOuter = findViewById(R.id.tipsCurrencyLayoutOuter);
        totalTipsLayoutInnerOne = findViewById(R.id.tipsCurrencyLayoutInnerOne);
        totalTipsLayoutInnerTwo = findViewById(R.id.tipsCurrencyLayoutInnerTwo);
        totalTipsLayoutInnerThree = findViewById(R.id.tipsCurrencyLayoutInnerThree);
        euroInputOne = findViewById(R.id.euroTipsOne);
        euroInputTwo = findViewById(R.id.euroTipsTwo);
        euroInputThree = findViewById(R.id.euroTipsThree);
        dollarInputOne = findViewById(R.id.dollarTipsOne);
        dollarInputTwo = findViewById(R.id.dollarTipsTwo);
        dollarInputThree = findViewById(R.id.dollarTipsThree);
        sterlingInputOne = findViewById(R.id.sterlingTipsOne);
        sterlingInputTwo = findViewById(R.id.sterlingTipsTwo);
        sterlingInputThree = findViewById(R.id.sterlingTipsThree);
        nextButton = findViewById(R.id.addStaffButton);

        numCruises = 1;
        cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));

        euroInputOne.setText("€");
        euroInputTwo.setText("€");
        euroInputThree.setText("€");
        dollarInputOne.setText("$");
        dollarInputTwo.setText("$");
        dollarInputThree.setText("$");
        sterlingInputOne.setText("£");
        sterlingInputTwo.setText("£");
        sterlingInputThree.setText("£");

        // Get intent and change title correspondingly
        Intent intent = getIntent();
        if(intent.getBooleanExtra(Outstanding.EXTRA_EDIT,false)){
            cruiseTitleText.setText(R.string.edit_cruise_title);
            cruiseTitleText.setTextColor(Color.RED);
            nextButton.setText(R.string.update_cruise);
            numCruises = intent.getIntExtra(Outstanding.EXTRA_CRUISES, 1);
            Log.i("HELP", "onCreate: " + numCruises);
            cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));
            ArrayList<Currency> currencies = intent.getParcelableArrayListExtra(Outstanding.EXTRA_CURRENCY);
            euroInputOne.setText(getString(R.string.display_euro, currencies.get(0).getEuro()));
            euroInputTwo.setText(getString(R.string.display_euro, currencies.get(1).getEuro()));
            euroInputThree.setText(getString(R.string.display_euro, currencies.get(2).getEuro()));
            dollarInputOne.setText(getString(R.string.display_dollar, currencies.get(0).getDollar()));
            dollarInputTwo.setText(getString(R.string.display_dollar, currencies.get(1).getDollar()));
            dollarInputThree.setText(getString(R.string.display_dollar, currencies.get(2).getDollar()));
            sterlingInputOne.setText(getString(R.string.display_sterling, currencies.get(0).getSterling()));
            sterlingInputTwo.setText(getString(R.string.display_sterling, currencies.get(1).getSterling()));
            sterlingInputThree.setText(getString(R.string.display_sterling, currencies.get(2).getSterling()));
        }

        if(numCruises == 2){ totalTipsLayoutInnerThree.setVisibility(View.GONE); }
        else if(numCruises == 1){
            totalTipsLayoutInnerTwo.setVisibility(View.GONE);
            totalTipsLayoutInnerThree.setVisibility(View.GONE);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String euroOne = euroInputOne.getText().toString().substring(1);
                String dollarOne = dollarInputOne.getText().toString().substring(1);
                String sterlingOne = sterlingInputOne.getText().toString().substring(1);
                String euroTwo = euroInputTwo.getText().toString().substring(1);
                String dollarTwo = dollarInputTwo.getText().toString().substring(1);
                String sterlingTwo = sterlingInputTwo.getText().toString().substring(1);
                String euroThree = euroInputThree.getText().toString().substring(1);
                String dollarThree = dollarInputThree.getText().toString().substring(1);
                String sterlingThree = sterlingInputThree.getText().toString().substring(1);
                double euroValueOne = 0, dollarValueOne = 0, sterlingValueOne = 0,
                        euroValueTwo = 0, dollarValueTwo = 0, sterlingValueTwo = 0,
                        euroValueThree = 0, dollarValueThree = 0, sterlingValueThree = 0;
                if (!TextUtils.isEmpty(euroOne)){ euroValueOne = Double.parseDouble(euroOne); }
                if (!TextUtils.isEmpty(dollarOne)){ dollarValueOne = Double.parseDouble(dollarOne); }
                if (!TextUtils.isEmpty(sterlingOne)){ sterlingValueOne = Double.parseDouble(sterlingOne); }
                if (!TextUtils.isEmpty(euroTwo)){ euroValueTwo = Double.parseDouble(euroTwo); }
                if (!TextUtils.isEmpty(dollarTwo)){ dollarValueTwo = Double.parseDouble(dollarTwo); }
                if (!TextUtils.isEmpty(sterlingTwo)){ sterlingValueTwo = Double.parseDouble(sterlingTwo); }
                if (!TextUtils.isEmpty(euroThree)){ euroValueThree = Double.parseDouble(euroThree); }
                if (!TextUtils.isEmpty(dollarThree)){ dollarValueThree = Double.parseDouble(dollarThree); }
                if (!TextUtils.isEmpty(sterlingThree)){ sterlingValueThree = Double.parseDouble(sterlingThree); }
                boolean flag = false;
                if((euroValueOne == 0) && (dollarValueOne == 0) && (sterlingValueOne == 0)){
                    euroInputOne.setTextColor(Color.RED);
                    dollarInputOne.setTextColor(Color.RED);
                    sterlingInputOne.setTextColor(Color.RED);
                    flag = true;
                }
                else {
                    euroInputOne.setTextColor(Color.BLACK);
                    dollarInputOne.setTextColor(Color.BLACK);
                    sterlingInputOne.setTextColor(Color.BLACK);
                }
                if((numCruises > 1) && (euroValueTwo == 0) && (dollarValueTwo == 0) && (sterlingValueTwo == 0)){
                    euroInputTwo.setTextColor(Color.RED);
                    dollarInputTwo.setTextColor(Color.RED);
                    sterlingInputTwo.setTextColor(Color.RED);
                    flag = true;
                }
                else {
                    euroInputTwo.setTextColor(Color.BLACK);
                    dollarInputTwo.setTextColor(Color.BLACK);
                    sterlingInputTwo.setTextColor(Color.BLACK);
                }
                if((numCruises > 2) && (euroValueThree == 0) && (dollarValueThree == 0) && (sterlingValueThree == 0)){
                    euroInputThree.setTextColor(Color.RED);
                    dollarInputThree.setTextColor(Color.RED);
                    sterlingInputThree.setTextColor(Color.RED);
                    flag = true;
                }
                else {
                    euroInputThree.setTextColor(Color.BLACK);
                    dollarInputThree.setTextColor(Color.BLACK);
                    sterlingInputThree.setTextColor(Color.BLACK);
                }
                if(flag){ return; }
                Currency currencyOne, currencyTwo, currencyThree;
                currencyOne = new Currency(euroValueOne, dollarValueOne, sterlingValueOne);
                currencyTwo = new Currency(0,0,0);
                if(numCruises > 1){ currencyTwo = new Currency(euroValueTwo, dollarValueTwo, sterlingValueTwo); }
                currencyThree = new Currency(0,0,0);
                if(numCruises > 2){ currencyThree = new Currency(euroValueThree, dollarValueThree, sterlingValueThree); }
                ArrayList<Currency> currencies = new ArrayList<>();
                currencies.add(currencyOne);
                currencies.add(currencyTwo);
                currencies.add(currencyThree);
                // Return to main activity with result
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Outstanding.EXTRA_CRUISES, numCruises);
                returnIntent.putParcelableArrayListExtra(Outstanding.EXTRA_CURRENCY, currencies);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        decreaseCruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numCruises == 1){ return; }
                numCruises--;
                Log.i("HELP", "onClick: " + numCruises);
                if(numCruises == 2){ totalTipsLayoutInnerThree.setVisibility(View.GONE); }
                else if(numCruises == 1){ totalTipsLayoutInnerTwo.setVisibility(View.GONE); }
                cruiseNumText.setText(String.format(Locale.getDefault(),"%d",numCruises));
            }
        });

        increaseCruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numCruises == Outstanding.MAX_CRUISES){ return; }
                numCruises++;
                Log.i("HELP", "onClick: " + numCruises);
                if(numCruises == 2){ totalTipsLayoutInnerTwo.setVisibility(View.VISIBLE); }
                else if(numCruises == 3){ totalTipsLayoutInnerThree.setVisibility(View.VISIBLE); }
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
