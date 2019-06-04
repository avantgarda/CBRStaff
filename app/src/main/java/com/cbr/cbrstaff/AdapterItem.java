package com.cbr.cbrstaff;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Arrays;

public class AdapterItem implements Parcelable, Serializable {

    public Staff staff;
    public boolean[] checked;
    public boolean[] hideCheckbox;

    AdapterItem() {
    }

    AdapterItem(Staff staff, boolean hideCheckboxes) {
        this.staff = staff;
        this.hideCheckbox = new boolean[Outstanding.MAX_CRUISES];
        if(hideCheckboxes){ Arrays.fill(hideCheckbox, Boolean.TRUE); }
        this.checked = new boolean[Outstanding.MAX_CRUISES];
    }

    AdapterItem(Staff staff, boolean hideCheckboxes, boolean[] checked) {
        this.staff = staff;
        this.hideCheckbox = new boolean[Outstanding.MAX_CRUISES];
        if(hideCheckboxes){ Arrays.fill(hideCheckbox, Boolean.TRUE); }
        this.checked = checked;
    }

    private AdapterItem(Parcel in) {
        staff = in.readParcelable(Staff.class.getClassLoader());
        checked = in.createBooleanArray();
        hideCheckbox = in.createBooleanArray();
    }

    public static final Creator<AdapterItem> CREATOR = new Creator<AdapterItem>() {
        @Override
        public AdapterItem createFromParcel(Parcel in) {
            return new AdapterItem(in);
        }

        @Override
        public AdapterItem[] newArray(int size) {
            return new AdapterItem[size];
        }
    };

    Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    boolean[] getChecked() {
        return checked;
    }

    void setChecked(int index, boolean checked) {
        this.checked[index] = checked;
    }

    boolean[] getHideCheckbox() { return hideCheckbox; }

    void setHideCheckbox(boolean[] hideCheckbox) {
        this.hideCheckbox = hideCheckbox;
    }

    boolean isHideCheckbox(){
        boolean temp = true;
        for(boolean box : hideCheckbox){ temp = temp && box; }
        return temp;
    }

    void resetChecked() {
        this.checked = new boolean[Outstanding.MAX_CRUISES];
    }

    void resetHideCheckbox() {
        this.hideCheckbox = new boolean[Outstanding.MAX_CRUISES];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(staff, flags);
        dest.writeBooleanArray(checked);
        dest.writeBooleanArray(hideCheckbox);
    }
}
