package com.example.cbrstaff;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Roster implements Parcelable {

    private ArrayList<Staff> staffList;

    Roster() {
    }

    Roster(ArrayList<Staff> staffList) {
        this.staffList = staffList;
    }

    private Roster(Parcel in) {
        staffList = in.createTypedArrayList(Staff.CREATOR);
    }

    public static final Creator<Roster> CREATOR = new Creator<Roster>() {
        @Override
        public Roster createFromParcel(Parcel in) {
            return new Roster(in);
        }

        @Override
        public Roster[] newArray(int size) {
            return new Roster[size];
        }
    };

    ArrayList<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(ArrayList<Staff> staffList) {
        this.staffList = staffList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(staffList);
    }
}
