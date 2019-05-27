package com.example.cbrstaff;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Exchange extends AppCompatActivity {

    LinearLayout overallExchangeLayout;
    LinearLayout dollarLayoutOuter;
    LinearLayout dollarLayoutInner;
    LinearLayout sterlingLayoutOuter;
    LinearLayout sterlingLayoutInner;
    EditText dollarAmountText;
    EditText dollarEuroText;
    EditText sterlingAmountText;
    EditText sterlingEuroText;
    TextView exchangeTitle;
    TextView dollarTitle;
    TextView sterlingTitle;
    ImageView dollarArrow;
    ImageView sterlingArrow;
    Button confirmExchangeButton;

    Currency mCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        overallExchangeLayout = findViewById(R.id.overallExchangeLayout);
        dollarLayoutOuter = findViewById(R.id.dollarLayoutOuter);
        dollarLayoutInner = findViewById(R.id.dollarLayoutInner);
        sterlingLayoutOuter = findViewById(R.id.sterlingLayoutOuter);
        sterlingLayoutInner = findViewById(R.id.sterlingLayoutInner);
        dollarAmountText = findViewById(R.id.dollarAmountText);
        dollarEuroText = findViewById(R.id.dollarEuroText);
        sterlingAmountText = findViewById(R.id.sterlingAmountText);
        sterlingEuroText = findViewById(R.id.sterlingEuroText);
        exchangeTitle = findViewById(R.id.exchangeTitle);
        dollarTitle = findViewById(R.id.dollarTitle);
        sterlingTitle = findViewById(R.id.sterlingTitle);
        dollarArrow = findViewById(R.id.dollarArrow);
        sterlingArrow = findViewById(R.id.sterlingArrow);
        confirmExchangeButton = findViewById(R.id.confirmExchangeButton);

        dollarEuroText.setText("€");
        sterlingEuroText.setText("€");

        Intent intent = getIntent();
        mCurrency = intent.getParcelableExtra(Outstanding.EXTRA_CURRENCY);
        if(mCurrency.getDollar() == 0) { dollarLayoutOuter.setVisibility(View.GONE); }
        else { dollarAmountText.setText(getString(R.string.display_dollar, mCurrency.getDollar())); }
        if(mCurrency.getSterling() == 0) { sterlingLayoutOuter.setVisibility(View.GONE); }
        else { sterlingAmountText.setText(getString(R.string.display_sterling, mCurrency.getSterling())); }

        confirmExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Currency currencyExchange, currencyEuro;
                currencyExchange = new Currency();
                currencyEuro = new Currency();
                String euroD = dollarEuroText.getText().toString();
                String euroS = sterlingEuroText.getText().toString();
                String dollar = dollarAmountText.getText().toString();
                String sterling = sterlingAmountText.getText().toString();
                double euroDollarValue = 0, euroSterlingValue = 0, dollarValue = 0, sterlingValue = 0;
                if (euroD.length() > 1){ euroDollarValue = Double.parseDouble(euroD.substring(1)); }
                if (euroS.length() > 1){ euroSterlingValue = Double.parseDouble(euroS.substring(1)); }
                if (dollar.length() > 1){ dollarValue = Double.parseDouble(dollar.substring(1)); }
                if (sterling.length() > 1){ sterlingValue = Double.parseDouble(sterling.substring(1)); }
                currencyEuro.setDollar(euroDollarValue);
                currencyEuro.setSterling(euroSterlingValue);
                currencyExchange.setDollar(dollarValue);
                currencyExchange.setSterling(sterlingValue);
                boolean errorFlag = false;
                if(dollarLayoutOuter.getVisibility() == View.VISIBLE) {
                    if ((mCurrency.getDollar() - dollarValue) < 0) {
                        dollarAmountText.setTextColor(Color.RED);
                        errorFlag = true;
                    } else {
                        dollarAmountText.setTextColor(Color.BLACK);
                    }
                    if (euroDollarValue == 0) {
                        dollarEuroText.setTextColor(Color.RED);
                        errorFlag = true;
                    } else {
                        dollarEuroText.setTextColor(Color.BLACK);
                    }
                }
                if(sterlingLayoutOuter.getVisibility() == View.VISIBLE) {
                    if ((mCurrency.getSterling() - sterlingValue) < 0) {
                        sterlingAmountText.setTextColor(Color.RED);
                        errorFlag = true;
                    } else {
                        sterlingAmountText.setTextColor(Color.BLACK);
                    }
                    if (euroSterlingValue == 0) {
                        sterlingEuroText.setTextColor(Color.RED);
                        errorFlag = true;
                    } else {
                        sterlingEuroText.setTextColor(Color.BLACK);
                    }
                }
                if(errorFlag){ return; }

                // Return to main activity with result
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Outstanding.EXTRA_CURRENCY, currencyExchange);
                returnIntent.putExtra(Outstanding.EXTRA_CURRENCY_MORE, currencyEuro);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        textListenerSetup(overallExchangeLayout);
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
