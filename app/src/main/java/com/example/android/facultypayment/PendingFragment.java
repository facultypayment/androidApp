package com.example.android.facultypayment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//import static androidx.fragment.app.FragmentManagerImpl.FragmentTag.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment {

    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference mPaymentReference;
    private RecyclerView mPendingList;
    private View mMainView;
    private PendingAdapter adapter;


    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_pending, container, false);
        mAuth = FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();
        mPaymentReference = FirebaseDatabase.getInstance().getReference().child("Payment").child(currentUserId);
        mPendingList = mMainView.findViewById(R.id.pending_list);
        mPaymentReference.keepSynced(true);


        Query query = mPaymentReference.orderByKey();
        setUpRecyclerView(query);
        return mMainView;
        //mPayment.child(currentUser.getUid()).




    }

    private void setUpRecyclerView(Query query) {
        FirebaseRecyclerOptions<Pending> options = new FirebaseRecyclerOptions.Builder<Pending>().setQuery(query,Pending.class).build();
        adapter = new PendingAdapter(PendingFragment.this,options);
        mPendingList.setHasFixedSize(true);
        mPendingList.setLayoutManager(new LinearLayoutManager(getContext()));
        mPendingList.setAdapter(adapter);
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
