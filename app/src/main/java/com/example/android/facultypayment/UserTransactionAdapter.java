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

public class UserTransactionAdapter extends FirebaseRecyclerAdapter<Pending, UserTransactionAdapter.UserTransactionHolder> {
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
    public UserTransactionAdapter(@NonNull FirebaseRecyclerOptions<Pending> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserTransactionAdapter.UserTransactionHolder userTransactionHolder, int i, @NonNull Pending pending) {
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
    public UserTransactionAdapter.UserTransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_transaction_history_card,parent,false);
        return new UserTransactionHolder(v);
    }

    public class UserTransactionHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView amount;
        TextView amtTextView;
        TextView status;
        TextView statusTextView;
        public UserTransactionHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amtNumber);
            amtTextView = itemView.findViewById(R.id.amt);
            statusTextView = itemView.findViewById(R.id.status);
            status = itemView.findViewById(R.id.statusValue);
        }
    }
}
