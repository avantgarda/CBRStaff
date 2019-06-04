package com.cbr.cbrstaff;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Currency implements Parcelable, Serializable {

    public double euro;
    public double dollar;
    public double sterling;

    Currency() {
    }

    Currency(double euro, double dollar, double sterling) {
        this.euro = euro;
        this.dollar = dollar;
        this.sterling = sterling;
    }

    private Currency(Parcel in) {
        euro = in.readDouble();
        dollar = in.readDouble();
        sterling = in.readDouble();
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

    void setEuro(double euro) {
        this.euro = euro;
    }

    double getDollar() {
        return dollar;
    }

    void setDollar(double dollar) {
        this.dollar = dollar;
    }

    double getSterling() {
        return sterling;
    }

    void setSterling(double sterling) {
        this.sterling = sterling;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(euro);
        dest.writeDouble(dollar);
        dest.writeDouble(sterling);
    }
}
