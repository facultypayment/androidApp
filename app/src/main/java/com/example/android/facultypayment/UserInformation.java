package com.example.android.facultypayment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
/*import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserInformation extends AppCompatActivity {

    private TextView email;
    private TextView username;
    private Button send;
    private FirebaseFirestore db ;
    private String mCurrent_state = "not_friends";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mNotificationDatabase;
    private Button pay;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private  String user_id;
    private  String emailId;
    private  String name;
    private  String currentUserName;
    private FirebaseDatabase db2;
    private Button declineRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("User Information");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot = task.getResult();
                    currentUserName = snapshot.getString("name");
                }
            }
        });

        db2 = FirebaseDatabase.getInstance();
        mDatabaseReference = db2.getReference().child("Friend Requests");
        mDatabaseReference.keepSynced(true);
        mFriendsDatabase = db2.getReference().child("Friends");
        mFriendsDatabase.keepSynced(true);
        mNotificationDatabase = db2.getReference().child("notifications");

        user_id = getIntent().getStringExtra("user_id");
        emailId = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");

        email = findViewById(R.id.email_of_user);
        username = findViewById(R.id.user_name);
        send = findViewById(R.id.send_request);
        pay = findViewById(R.id.pay);
        declineRequest = findViewById(R.id.decline_request);
        send.setEnabled(false);
        declineRequest.setEnabled(false);
        declineRequest.setVisibility(View.INVISIBLE);

        username.setText(name);
        email.setText(emailId);

        mDatabaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    String request_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                    if(request_type.equals("received"))
                    {
                        mCurrent_state = "req_received";
                        send.setText("ACCEPT FRIEND REQUEST");
                        declineRequest.setVisibility(View.VISIBLE);
                        declineRequest.setEnabled(true);
                    }
                    else if(request_type.equals("sent"))
                    {
                        mCurrent_state = "req_sent";
                        send.setText("CANCEL REQUEST");
                        declineRequest.setVisibility(View.INVISIBLE);
                        declineRequest.setEnabled(false);


                    }

                }
                else{
                    mFriendsDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(user_id))
                            {
                                mCurrent_state = "friends";
                                send.setText("UNFRIEND");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                send.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineRequest();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
    }

    private void declineRequest() {
        declineRequest.setEnabled(false);
        if(mCurrent_state.equals("req_received"))
        {
            mDatabaseReference.child(currentUser.getUid()).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        mDatabaseReference.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                declineRequest.setVisibility(View.INVISIBLE);
                                Toast.makeText(UserInformation.this, "Request declined", Toast.LENGTH_LONG).show();
                                mCurrent_state = "not_friends";
                                send.setText("SEND REQUEST");
                            }
                        });
                    }
                    else {
                        Toast.makeText(UserInformation.this, "Error in declining request", Toast.LENGTH_LONG).show();
                        declineRequest.setEnabled(true);
                    }
                }
            });
        }
    }

    private void pay() {
        if(!mCurrent_state.equals("friends"))
            Toast.makeText(UserInformation.this,"You must be friends with this user to pay",Toast.LENGTH_LONG).show();
        else
        {
            Intent intent = new Intent(UserInformation.this,PaymentActivity.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("email",emailId);
            intent.putExtra("recName",name);
            intent.putExtra("senderName",currentUserName);
            startActivity(intent);
            finish();
        }
    }

    private void sendRequest() {

        send.setEnabled(false);
        // --------------------SEND FRIEND REQUEST------------
        if(mCurrent_state.equals("not_friends"))
        {

            db2.getReference().child("Friends").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(user_id))
                        Toast.makeText(UserInformation.this,"request already sent",Toast.LENGTH_LONG).show();
                    else{
                        db2.getReference().child("Friend Requests").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(user_id))
                                    Toast.makeText(UserInformation.this,"You are already friends with this user",Toast.LENGTH_LONG).show();
                                else
                                    sendReq();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        // --------------------CANCEL FRIEND REQUEST------------
        else if(mCurrent_state.equals("req_sent"))
        {
            Map cancelRequest = new HashMap();
            cancelRequest.put("Friend Requests/"+currentUser.getUid()+ "/"+ user_id,null);
            cancelRequest.put("Friend Requests/"+user_id+ "/"+ currentUser.getUid(),null);

            db2.getReference().updateChildren(cancelRequest, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError != null)
                        Toast.makeText(UserInformation.this,"Request cancellation failed",Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(UserInformation.this, "Request cancelled", Toast.LENGTH_LONG).show();
                        send.setText("SEND REQUEST");
                        send.setEnabled(true);
                        mCurrent_state = "not_friends";
                    }
                }
            });
        }
        // --------------------ACCEPT FRIEND REQUEST------------
        else if(mCurrent_state.equals("req_received"))
        {
            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

            Map friendsMap = new HashMap();

            friendsMap.put("Friends/"+currentUser.getUid()+ "/"+ user_id+ "/date",currentDate);
            friendsMap.put("Friends/"+currentUser.getUid()+ "/"+ user_id+ "/name",name);
            friendsMap.put("Friends/"+currentUser.getUid()+ "/"+ user_id+ "/email",emailId);
            friendsMap.put("Friends/"+user_id+ "/"+ currentUser.getUid()+ "/date",currentDate);
            friendsMap.put("Friends/"+user_id+ "/"+ currentUser.getUid()+ "/name",currentUserName);
            friendsMap.put("Friends/"+user_id+ "/"+ currentUser.getUid()+ "/email",currentUser.getEmail());

            friendsMap.put("Friend Requests/"+currentUser.getUid()+ "/"+ user_id,null);
            friendsMap.put("Friend Requests/"+user_id+ "/"+ currentUser.getUid(),null);

            db2.getReference().updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError!=null)
                        Toast.makeText(UserInformation.this,"Request acceptance failed",Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(UserInformation.this, "Request accepted", Toast.LENGTH_LONG).show();
                        send.setText("UNFRIEND");
                        send.setEnabled(true);
                        mCurrent_state = "friends";
                    }
                }
            });
        }
        // --------------------UNFRIEND------------
        else if(mCurrent_state.equals("friends"))
        {
            Map unfriendMap = new HashMap();

            unfriendMap.put("Friends/"+currentUser.getUid()+ "/"+ user_id,null);
            unfriendMap.put("Friends/"+user_id+ "/"+ currentUser.getUid(),null);
            db2.getReference().updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError!=null)
                        Toast.makeText(UserInformation.this,"error",Toast.LENGTH_LONG).show();
                    else
                    {
                        mCurrent_state = "not_friends";
                        send.setText("SEND REQUEST");
                        send.setEnabled(true);
                        Toast.makeText(UserInformation.this,"unfriended",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        declineRequest.setEnabled(false);
        declineRequest.setVisibility(View.INVISIBLE);
    }

    private void sendReq() {
        DatabaseReference newNotificationRef = db2.getReference().child("notifications").child(user_id).push();
        String newNotificationId  = newNotificationRef.getKey();
        Map<String,String> notificationsData = new HashMap<>();
        notificationsData.put("from",currentUser.getUid());
        notificationsData.put("type","friend request");

        Map dataSender = new HashMap();
        dataSender.put("request_type","sent");
        dataSender.put("email",emailId);
        dataSender.put("name",name);

        Map dataRec = new HashMap();
        dataRec.put("request_type","received");
        dataRec.put("email",currentUser.getEmail());
        dataRec.put("name",currentUserName);
        Map requestMap = new HashMap();

        requestMap.put("Friend Requests/"+currentUser.getUid()+ "/"+ user_id,dataSender);

        requestMap.put("Friend Requests/"+user_id+ "/"+ currentUser.getUid(),dataRec);
        //requestMap.put("Friend Requests/"+user_id+ "/"+ currentUser.getUid()+ "/email",currentUser.getEmail());
        requestMap.put("notifications/"+user_id+newNotificationId , notificationsData);

        db2.getReference().updateChildren(requestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError != null)
                    Toast.makeText(UserInformation.this,"there was some error in sending request",Toast.LENGTH_LONG).show();
                else{
                    send.setEnabled(true);
                    mCurrent_state = "req_sent";
                    send.setText("CANCEL REQUEST");

                    Toast.makeText(UserInformation.this,"Request sent Successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
        declineRequest.setEnabled(false);
        declineRequest.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_information_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.transaction_hist:
                Intent intent = new Intent(UserInformation.this,UserTransactionActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("email",emailId);
                startActivity(intent);
                default:
                    return false;
        }
    }
}
