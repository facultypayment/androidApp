package com.example.android.facultypayment;

public class Friends {
    String date,email,name;
    public Friends(){}

    public Friends(String date,String email,String name)
    {
        this.date = date;
        this.email = email;
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public String getEmail()
    {return email;}

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
