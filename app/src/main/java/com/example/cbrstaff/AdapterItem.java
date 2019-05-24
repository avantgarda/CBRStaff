package com.example.cbrstaff;

public class AdapterItem {

    private Staff staff;
    private boolean[] checked;
    private boolean hideCheckbox;

    public AdapterItem() {
    }

    AdapterItem(Staff staff, boolean hideCheckbox) {
        this.staff = staff;
        this.hideCheckbox = hideCheckbox;
        checked = new boolean[Outstanding.MAX_CRUISES];
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

    boolean isHideCheckbox() {
        return hideCheckbox;
    }

    void setHideCheckbox(boolean hideCheckbox) {
        this.hideCheckbox = hideCheckbox;
    }

    void resetChecked() {
        this.checked = new boolean[Outstanding.MAX_CRUISES];
    }
}
