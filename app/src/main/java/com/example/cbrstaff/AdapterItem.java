package com.example.cbrstaff;

import java.util.Arrays;

public class AdapterItem {

    private Staff staff;
    private boolean[] checked;
    private boolean[] hideCheckbox;

    public AdapterItem() {
    }

    AdapterItem(Staff staff, boolean hideCheckboxes) {
        this.staff = staff;
        this.hideCheckbox = new boolean[Outstanding.MAX_CRUISES];
        if(hideCheckboxes){ Arrays.fill(hideCheckbox, Boolean.TRUE); }
        this.checked = new boolean[Outstanding.MAX_CRUISES];
    }

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
}
