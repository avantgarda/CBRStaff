package com.example.cbrstaff;

public class Currency {

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

    public double getEuro() {
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
}
