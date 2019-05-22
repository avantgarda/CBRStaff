package com.example.cbrstaff;

public class Staff {

    private String name;
    private Currency balance;

    public Staff() {
    }

    Staff(String name, Currency balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getBalance() {
        return balance;
    }

    public void setBalance(Currency balance) {
        this.balance = balance;
    }
}
