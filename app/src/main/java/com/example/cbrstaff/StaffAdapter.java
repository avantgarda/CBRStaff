package com.example.cbrstaff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private ArrayList<Staff> mStaffList;
    private Context mContext;

    StaffAdapter(ArrayList<Staff> staffList, Context context){
        mStaffList = staffList;
        mContext = context;
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView balanceTextView;

        StaffViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameText);
            balanceTextView = itemView.findViewById(R.id.balanceText);
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
        Staff currentStaff = mStaffList.get(i);
        staffViewHolder.nameTextView.setText(currentStaff.getName());
        staffViewHolder.balanceTextView.setText(mContext.getString(R.string.display_euro, currentStaff.getBalance().getEuro()));

    }

    @Override
    public int getItemCount() {
        return mStaffList.size();
    }
}
