package com.cbr.cbrstaff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AddStaff extends AppCompatActivity {

    TextView addStaffTitle;
    EditText addStaffName;
    Button addStaffButton;

    ArrayList<AdapterItem> mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        addStaffTitle = findViewById(R.id.addStaffTitle);
        addStaffName = findViewById(R.id.staffNameInput);
        addStaffButton = findViewById(R.id.addStaffButton);

        final Intent intent = getIntent();
        mItem = intent.getParcelableArrayListExtra(Outstanding.EXTRA_ITEM);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        addStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addStaffName.getText().toString();
                if(duplicateName(name)){
                    addStaffName.setHintTextColor(Color.RED);
                    addStaffName.setTextColor(Color.RED);
                }
                else{
                    // Add staff
                    intent.putExtra(Outstanding.EXTRA_STAFF, (Parcelable)new Staff(name, new Currency()));
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        addStaffName.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private boolean duplicateName(String name) {
        if(name.isEmpty()){ return true; }
        for(AdapterItem item : mItem){
            if(item.getStaff().getName().equals(name)){ return true; }
        }
        return false;
    }
}
