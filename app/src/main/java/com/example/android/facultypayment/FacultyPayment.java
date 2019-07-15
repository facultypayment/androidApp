package com.example.android.facultypayment;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FacultyPayment extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
