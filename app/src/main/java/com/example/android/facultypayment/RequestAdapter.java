package com.example.android.facultypayment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestAdapter extends FirebaseRecyclerAdapter<AllUsers, RequestAdapter.Holder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private String currentUser;
    private Context context;
    private DatabaseReference reference;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RequestAdapter(@NonNull FirebaseRecyclerOptions<AllUsers> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final Holder holder, int position, @NonNull final AllUsers allUsers) {

        holder.email.setText(allUsers.getEmail());
        holder.name.setText(allUsers.getName());

        currentUser = mAuth.getCurrentUser().getUid();
        final String user = getSnapshots().getSnapshot(position).getRef().getKey();
        final String emailName = allUsers.getEmail();
        final String name = allUsers.getName();

        holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){

                Intent intent = new Intent(view.getContext(), UserInformation.class);
                intent.putExtra("user_id", user);
                intent.putExtra("email", emailName);
                intent.putExtra("name",name);
                view.getContext().startActivity(intent);

            }
        });

}

    @NonNull
    @Override
    public RequestAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.allusers, parent, false);
        return new RequestAdapter.Holder(v);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView email;
        TextView name;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email_id);
        }
    }
}