package com.example.android.facultypayment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FriendsAdapter extends FirebaseRecyclerAdapter<Friends,FriendsAdapter.FriendsHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FriendsAdapter(@NonNull FirebaseRecyclerOptions<Friends> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsAdapter.FriendsHolder friendsHolder, int i, @NonNull Friends friends) {
        //friendsHolder.date.setText(friends.getDate());
        friendsHolder.name.setText(friends.getName());
        final String user = getSnapshots().getSnapshot(i).getRef().getKey();
        final String emailName = friends.getEmail();
        final String name = friends.getName();
        friendsHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), UserInformation.class);
                    intent.putExtra("user_id", user);
                    intent.putExtra("email",emailName);
                    intent.putExtra("name",name);
                    view.getContext().startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public FriendsAdapter.FriendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends,parent,false);
        return new FriendsHolder(v);
    }
    public class FriendsHolder extends RecyclerView.ViewHolder {
        TextView name;
        //TextView date;
        public FriendsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            //date = itemView.findViewById(R.id.date);

        }

    }

}

