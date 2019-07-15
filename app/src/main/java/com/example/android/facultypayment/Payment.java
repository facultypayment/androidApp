package com.example.android.facultypayment;

public class Payment {
    String emailId;
    String amount;
    public Payment(){}
    public Payment(String emailId,String amount)
    {
        this.amount = amount;
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getAmount() {
        return amount;
    }
}
