package com.example.android.facultypayment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserTransactionActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference mUsers;
    private RecyclerView mTransactionList;

    private UserTransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_transaction);
        final String email = getIntent().getStringExtra("email");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        mTransactionList = findViewById(R.id.transaction_list);
        mUsers.keepSynced(true);
        Query query = mUsers.orderByChild("email").equalTo(email);
        setupRecyclerView(query);

    }

    private void setupRecyclerView(Query query) {
        FirebaseRecyclerOptions<Pending> options = new FirebaseRecyclerOptions.Builder<Pending>().setQuery(query,Pending.class).build();
        adapter = new UserTransactionAdapter(options);
        mTransactionList.setHasFixedSize(true);
        mTransactionList.setLayoutManager(new LinearLayoutManager(this));
        mTransactionList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
