package com.example.cbrstaff;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        confirmExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Currency currency = new Currency(0,0,0);

                // Return to main activity with result
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Outstanding.EXTRA_CURRENCY, currency);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
