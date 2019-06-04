package com.cbr.cbrstaff;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Staff implements Parcelable, Serializable {

    public String name;
    public Currency balance;

    public Staff() {
    }

    Staff(String name, Currency balance) {
        this.name = name;
        this.balance = balance;
    }

    private Staff(Parcel in) {
        name = in.readString();
        balance = in.readParcelable(Currency.class.getClassLoader());
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Currency getBalance() {
        return balance;
    }

    public void setBalance(Currency balance) {
        this.balance = balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(balance, flags);
    }
}
