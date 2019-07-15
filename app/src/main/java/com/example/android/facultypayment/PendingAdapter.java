package com.example.android.facultypayment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PendingAdapter extends FirebaseRecyclerAdapter<Pending, PendingAdapter.PendingHolder> {
    private FirebaseAuth mAuth =FirebaseAuth.getInstance();;
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mUsers = db.getReference().child("Users");
    private DatabaseReference mPay = db.getReference().child("Payment");
    private PendingFragment context ;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param context
     * @param options
     */
    public PendingAdapter(PendingFragment context, @NonNull FirebaseRecyclerOptions<Pending> options) {
        super(options);

        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final PendingHolder pendingHolder, int i, @NonNull Pending pending) {
        final String email = pending.getEmail();
        final String name = pending.getName();
        final String id = getSnapshots().getSnapshot(i).getRef().getKey();
        final String user_id = pending.getId();
        final String type = pending.getType();
        final String amount = pending.getAmount();
        pendingHolder.date.setText(pending.getDate());
        pendingHolder.name.setText(name);
        pendingHolder.amount.setText(amount);
        if(("sent").equals(type)) {
            pendingHolder.amtTextView.setText("You paid   ");
            pendingHolder.amtTextView.setTextColor(Color.rgb(94,198,57));
            pendingHolder.receive.setVisibility(View.INVISIBLE);
            pendingHolder.dNR.setVisibility(View.INVISIBLE);
            pendingHolder.receive.setEnabled(false);
            pendingHolder.dNR.setEnabled(false);
        }

        pendingHolder.receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acknowledgement(email,type,amount,id,user_id, "Received");
                pendingHolder.receive.setClickable(false);
                pendingHolder.receive.setEnabled(false);

            }
        });
        pendingHolder.dNR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acknowledgement(email,type,amount,id, user_id,"didNotReceive");
                pendingHolder.dNR.setClickable(false);
                pendingHolder.dNR.setEnabled(false);
            }
        });


    }

    private void acknowledgement(String email, String type, String amount, String id, String user_id, String reply) {
        String status;
        if("didNotReceive".equals(reply))
            status = "unsuccessful";
        else
            status = "successful";
        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
        Map historyRec = new HashMap();
        historyRec.put("email",email);
        historyRec.put("type",type);
        historyRec.put("amount",amount);
        historyRec.put("date",currentDate);
        historyRec.put("status",status);

        Map historySender = new HashMap();
        historySender.put("email",currentUser.getEmail());
        historySender.put("type","sent");
        historySender.put("amount",amount);
        historySender.put("date",currentDate);
        historySender.put("status",status);
        String key = mUsers.child(currentUser.getUid()).push().getKey();


        Map update = new HashMap();
        update.put("/"+currentUser.getUid()+"/"+key,historyRec);
        update.put("/"+user_id +"/"+key,historySender);

        final Map remove = new HashMap();
        remove.put("/"+currentUser.getUid()+"/"+id,null);
        remove.put("/"+ user_id +"/"+id,null);

        mUsers.updateChildren(update, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError!=null)
                    Toast.makeText(context.getContext(),"1-Sorry acknowledgement failed "+databaseError,Toast.LENGTH_LONG).show();
                else
                {
                    mPay.updateChildren(remove, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError!=null)
                                Toast.makeText(context.getContext(),"2- sorry acknowledgement failed "+databaseError,Toast.LENGTH_LONG).show();


                        }
                    });
                }
            }
        });
    }

    @NonNull
    @Override
    public PendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending,parent,false);
        return new PendingHolder(v);
    }

    public class PendingHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView amount;
        TextView amtTextView;
        Button receive;
        Button dNR;
        public PendingHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amtNumber);
            amtTextView = itemView.findViewById(R.id.amt);
            receive = itemView.findViewById(R.id.receive);
            dNR = itemView.findViewById(R.id.dnr);
        }
    }
}
