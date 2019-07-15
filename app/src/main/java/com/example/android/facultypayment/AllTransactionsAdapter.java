package com.example.android.facultypayment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllTransactionsAdapter extends FirebaseRecyclerAdapter<Pending, AllTransactionsAdapter.AllTransactionHolder> {
    private FirebaseAuth mAuth =FirebaseAuth.getInstance();;
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mUsers = db.getReference().child("Users");
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllTransactionsAdapter(@NonNull FirebaseRecyclerOptions<Pending> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllTransactionsAdapter.AllTransactionHolder userTransactionHolder, int i, @NonNull Pending pending) {
        userTransactionHolder.email.setText(pending.getEmail());
        userTransactionHolder.date.setText(pending.getDate());
        userTransactionHolder.amount.setText(pending.getAmount());
        String status = pending.getStatus();
        String type = pending.getType();
        if("sent".equals(type))
            userTransactionHolder.amtTextView.setText("You sent   ");
        else
            userTransactionHolder.amtTextView.setText("You received   ");
        if("successful".equals(status))
            userTransactionHolder.status.setText("Successful");
        else
            userTransactionHolder.status.setText("Unsuccessful");
    }

    @NonNull
    @Override
    public AllTransactionsAdapter.AllTransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_transactions,parent,false);
        return new AllTransactionHolder(v);
    }

    public class AllTransactionHolder extends RecyclerView.ViewHolder {
        TextView email;
        TextView date;
        TextView amount;
        TextView amtTextView;
        TextView status;
        TextView statusTextView;
        public AllTransactionHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email_);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amtNumber);
            amtTextView = itemView.findViewById(R.id.amt);
            statusTextView = itemView.findViewById(R.id.status);
            status = itemView.findViewById(R.id.statusValue);
        }
    }
}
