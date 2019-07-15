package com.example.android.facultypayment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllTransactionsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference mUsers;
    private RecyclerView mTransactionList;
    private AllTransactionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("All Transactions");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        mTransactionList = findViewById(R.id.all_transactions_list);
        mUsers.keepSynced(true);
        Query query = mUsers;
        setupRecyclerView(query);
    }

    private void setupRecyclerView(Query query) {
        FirebaseRecyclerOptions<Pending> options = new FirebaseRecyclerOptions.Builder<Pending>().setQuery(query,Pending.class).build();
        adapter = new AllTransactionsAdapter(options);
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
