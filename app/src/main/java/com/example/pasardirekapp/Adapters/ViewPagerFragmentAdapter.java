package com.example.pasardirekapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pasardirekapp.Fragments.ContactFragment;
import com.example.pasardirekapp.Fragments.ProductFragment;
import com.example.pasardirekapp.Fragments.TransactionFragment;
import com.example.pasardirekapp.Fragments.UserFragment;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new UserFragment();
            case 1:
                return new ProductFragment();
            case 2:
                return new TransactionFragment();
            case 3:
                return new ContactFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
