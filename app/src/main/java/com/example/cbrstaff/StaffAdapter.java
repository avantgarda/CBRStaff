package com.example.cbrstaff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private ArrayList<AdapterItem> mItem;
    private Context mContext;

    StaffAdapter(ArrayList<AdapterItem> item, Context context){
        mItem = item;
        mContext = context;
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView balanceTextView;
        LinearLayout cruiseCheck;
        CheckBox cruiseOne;
        CheckBox cruiseTwo;
        CheckBox cruiseThree;

        StaffViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameText);
            balanceTextView = itemView.findViewById(R.id.balanceText);
            cruiseCheck = itemView.findViewById(R.id.cruiseCheckLayout);
            cruiseOne = itemView.findViewById(R.id.cruiseOneCheck);
            cruiseTwo = itemView.findViewById(R.id.cruiseTwoCheck);
            cruiseThree = itemView.findViewById(R.id.cruiseThreeCheck);
        }
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.staff_item, viewGroup,false);
        return new StaffViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder staffViewHolder, int i) {

        final int position = i;

        if(mItem.get(position).isHideCheckbox()) {
            staffViewHolder.cruiseCheck.setVisibility(View.GONE);
            staffViewHolder.balanceTextView.setVisibility(View.VISIBLE);
        }
        else {
            staffViewHolder.cruiseCheck.setVisibility(View.VISIBLE);
            staffViewHolder.balanceTextView.setVisibility(View.GONE);
        }

        Staff currentStaff = mItem.get(position).getStaff();
        staffViewHolder.nameTextView.setText(currentStaff.getName());
        staffViewHolder.balanceTextView.setText(mContext.getString(R.string.display_euro, currentStaff.getBalance().getEuro()));

        // Loop through all checkboxes in view and set up state saving
        for(int check = 0; check < staffViewHolder.cruiseCheck.getChildCount(); check++){
            // Declare index as final
            final int index = check;
            CheckBox v = (CheckBox) staffViewHolder.cruiseCheck.getChildAt(index);
            //in some cases, it will prevent unwanted situations
            v.setOnCheckedChangeListener(null);
            //if true, your checkbox will be selected, else unselected
            v.setChecked(mItem.get(position).getChecked()[index]);

            v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //set your object's last status
                    mItem.get(position).setChecked(index, isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }
}
