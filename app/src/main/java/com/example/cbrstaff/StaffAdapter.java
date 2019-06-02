package com.example.cbrstaff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.cbrstaff.Outstanding.RESULT_CRUISE;

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
        LinearLayout staffDetailsLayout;
        CheckBox cruiseOne;
        CheckBox cruiseTwo;
        CheckBox cruiseThree;
        Button addStaffButton;

        StaffViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameText);
            balanceTextView = itemView.findViewById(R.id.balanceText);
            cruiseCheck = itemView.findViewById(R.id.cruiseCheckLayout);
            staffDetailsLayout = itemView.findViewById(R.id.staffDetailsLayout);
            cruiseOne = itemView.findViewById(R.id.cruiseOneCheck);
            cruiseTwo = itemView.findViewById(R.id.cruiseTwoCheck);
            cruiseThree = itemView.findViewById(R.id.cruiseThreeCheck);
            addStaffButton = itemView.findViewById(R.id.addStaffButton);
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

        if(position == (getItemCount() - 1)){
            staffViewHolder.staffDetailsLayout.setVisibility(View.GONE);
            staffViewHolder.addStaffButton.setVisibility(View.VISIBLE);

            // Set button listener here
            staffViewHolder.addStaffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add new staff dialog
                    Intent intent = new Intent(mContext, AddStaff.class);
                    intent.putParcelableArrayListExtra(Outstanding.EXTRA_ITEM, mItem);
                    ((Activity) mContext).startActivityForResult(intent, Outstanding.RESULT_ADD_STAFF);
                }
            });
        }
        else {
            staffViewHolder.staffDetailsLayout.setVisibility(View.VISIBLE);
            staffViewHolder.addStaffButton.setVisibility(View.GONE);

            Staff currentStaff = mItem.get(position).getStaff();
            staffViewHolder.nameTextView.setText(currentStaff.getName());

            if (mItem.get(position).isHideCheckbox()) {
                staffViewHolder.cruiseCheck.setVisibility(View.GONE);
                staffViewHolder.balanceTextView.setVisibility(View.VISIBLE);
                staffViewHolder.balanceTextView.setText(mContext.getString(R.string.display_euro, currentStaff.getBalance().getEuro()));
            } else {
                staffViewHolder.cruiseCheck.setVisibility(View.VISIBLE);
                staffViewHolder.balanceTextView.setVisibility(View.GONE);

                // Loop through all checkboxes in view and set up state saving
                for (int check = 0; check < staffViewHolder.cruiseCheck.getChildCount(); check++) {
                    // Declare index as final
                    final int index = check;
                    CheckBox v = (CheckBox) staffViewHolder.cruiseCheck.getChildAt(index);
                    // Check if visible
                    if (mItem.get(position).getHideCheckbox()[index]) {
                        v.setVisibility(View.GONE);
                        continue;
                    } else {
                        v.setVisibility(View.VISIBLE);
                    }
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

            staffViewHolder.staffDetailsLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Delete staff
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }
}
