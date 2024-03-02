package com.js.pocketaccount.Models;

public class Accounts {
    String amount;
    String date;
    String name;
    String narration;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Accounts(String amount, String date, String name, String narration) {
        this.amount = amount;
        this.date = date;
        this.name = name;
        this.narration = narration;
    }

    public Accounts() {
    }
}
