package com.example.cbrstaff;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {

    private double euro;
    private double dollar;
    private double pound;

    public Currency() {
    }

    Currency(double euro, double dollar, double pound) {
        this.euro = euro;
        this.dollar = dollar;
        this.pound = pound;
    }

    private Currency(Parcel in) {
        euro = in.readDouble();
        dollar = in.readDouble();
        pound = in.readDouble();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    double getEuro() {
        return euro;
    }

    public void setEuro(double euro) {
        this.euro = euro;
    }

    public double getDollar() {
        return dollar;
    }

    public void setDollar(double dollar) {
        this.dollar = dollar;
    }

    public double getPound() {
        return pound;
    }

    public void setPound(double pound) {
        this.pound = pound;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(euro);
        dest.writeDouble(dollar);
        dest.writeDouble(pound);
    }
}
