package com.bank.main;

public class Statement {
    
    private String date;
    private String time;
    private String type;
    private String initial_amount;
    private String final_amount;
    private String amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitial_amount() {
        return initial_amount;
    }

    public void setInitial_amount(String initial_amount) {
        this.initial_amount = initial_amount;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
