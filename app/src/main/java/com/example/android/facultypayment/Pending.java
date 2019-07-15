package com.example.android.facultypayment;

public class Pending
{
    String email,date,amount,type,status,id,name;
    public Pending(){}

    public Pending(String email, String date,String amount,String type,String status,String id,String name) {
        this.email = email;
        this.date = date;
        this.amount = amount;
        this.status = status;
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public String getId(){
        return id;
    }

    public String getAmount()
    {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }
}
