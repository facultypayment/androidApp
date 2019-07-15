package com.example.android.facultypayment;

/*import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private Button pay;
    private EditText amount;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private  String user_id;
    private  String emailId;
    private  String senderName;
    private  String recName;
    private FirebaseDatabase db2;
    private DatabaseReference mPayReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Send Money");
        amount = findViewById(R.id.amount_to_pay);
        pay = findViewById(R.id.pay);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db2 = FirebaseDatabase.getInstance();
        mPayReference = db2.getReference().child("Payment");
        user_id = getIntent().getStringExtra("user_id");
        emailId = getIntent().getStringExtra("email");
        senderName = getIntent().getStringExtra("senderName");
        recName = getIntent().getStringExtra("recName");
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
    }

    private void pay() {
        pay.setEnabled(false);
        String amt = amount.getText().toString();
        if (TextUtils.isEmpty(amt)) {
            Toast.makeText(PaymentActivity.this, "Fill the amount field", Toast.LENGTH_LONG).show();
            pay.setEnabled(true);
        }
        else
        {

            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
            final Map sentMap = new HashMap();
            sentMap.put("id",user_id);
            sentMap.put("amount", amt);
            sentMap.put("type", "sent");
            sentMap.put("status","pending");
            sentMap.put("date",currentDate);
            sentMap.put("email",emailId);
            sentMap.put("name",recName);

            final Map recMap = new HashMap();
            recMap.put("id",currentUser.getUid());
            recMap.put("amount", amt);
            recMap.put("type", "received");
            recMap.put("status","pending");
            recMap.put("date",currentDate);
            recMap.put("email",currentUser.getEmail());
            recMap.put("name",senderName);


            String key = mPayReference.child(currentUser.getUid()).push().getKey();
            //String key2 = mPayReference.child(user_id).push().getKey();
            Map update = new HashMap();
            update.put("/"+currentUser.getUid()+ "/"+key,sentMap);
            update.put("/"+user_id+ "/"+key,recMap);


            mPayReference.updateChildren(update, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError!=null)
                        Toast.makeText(PaymentActivity.this, "Unsuccessful!!", Toast.LENGTH_LONG).show();
                    else
                    {
                        Toast.makeText(PaymentActivity.this, "Successful!!", Toast.LENGTH_LONG).show();
                        amount.setText("");
                        pay.setEnabled(true);
                    }
                }
            });


        }
    }
}
