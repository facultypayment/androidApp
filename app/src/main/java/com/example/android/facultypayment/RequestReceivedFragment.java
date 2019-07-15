package com.example.android.facultypayment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;


public class RequestReceivedFragment extends Fragment {
    private RecyclerView mUsersList;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;
    private RequestAdapter adapter;
    private FirebaseDatabase db2;
    private DatabaseReference reference ;
    private View mMainView;

    public RequestReceivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_request_received, container, false);
        mUsersList = mMainView.findViewById(R.id.request_list);
        mAuth = FirebaseAuth.getInstance();

        db2 = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        reference = db2.getReference().child("Friend Requests").child(currentUser.getUid());
        Query query = reference.orderByChild("request_type").equalTo("received");
        setupRecyclerView(query);
        return mMainView;

    }

    private void setupRecyclerView(Query query) {
        FirebaseRecyclerOptions<AllUsers> options =new FirebaseRecyclerOptions.Builder<AllUsers>().setQuery(query,AllUsers.class).build();
        adapter = new RequestAdapter(options,getContext());

        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsersList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
