package com.example.android.facultypayment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RequestPagerAdapter extends FragmentPagerAdapter {
    public RequestPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                RequestReceivedFragment requestReceivedFragment = new RequestReceivedFragment();
                return requestReceivedFragment;
            case 1:
                RequestSentFragment requestSentFragment = new RequestSentFragment();
                return requestSentFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Received";
            case 1:
                return "Sent";
            default:
                return null;

        }
    }
}
