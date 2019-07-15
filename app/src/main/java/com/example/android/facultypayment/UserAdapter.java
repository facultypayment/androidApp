package com.example.android.facultypayment;

import android.content.Intent;
/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAdapter extends FirestoreRecyclerAdapter<AllUsers,UserAdapter.UserHolder> {
    private FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserAdapter(@NonNull FirestoreRecyclerOptions<AllUsers> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position, @NonNull AllUsers model) {
        holder.email.setText("-"+model.getEmail());
        holder.name.setText(model.getName());
        final String user = getSnapshots().getSnapshot(position).getId();
        final String name = model.getName();
        final String emailName = model.getEmail();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!emailName.equals(currentUser.getEmail())) {
                    Intent intent = new Intent(view.getContext(), UserInformation.class);
                    intent.putExtra("user_id", user);
                    intent.putExtra("email",emailName);
                    intent.putExtra("name",name);
                    view.getContext().startActivity(intent);
                }
            }
        });


    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.allusers,viewGroup,false);
        return new UserHolder(v);
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        TextView email;
        TextView name;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email_id);
            name = itemView.findViewById(R.id.name);

        }

    }


}
