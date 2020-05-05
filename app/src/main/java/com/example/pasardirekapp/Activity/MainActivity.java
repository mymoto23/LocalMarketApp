package com.example.pasardirekapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.pasardirekapp.Fragments.ContactFragment;
import com.example.pasardirekapp.Fragments.ProductFragment;
import com.example.pasardirekapp.Fragments.TransactionFragment;
import com.example.pasardirekapp.Fragments.UserFragment;
import com.example.pasardirekapp.R;
import com.example.pasardirekapp.Adapters.ViewPagerFragmentAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActionBar bar;
    private RecyclerView mRecyclerView;
    ViewPager2 myViewPager2;
    ViewPagerFragmentAdapter myAdapter;
    private ArrayList<Fragment> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myViewPager2 = findViewById(R.id.pager);

        bar = getSupportActionBar();
            bar.setDisplayShowTitleEnabled(true);
            bar.setTitle("PasarDirekApp");
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ViewPager2.OnPageChangeCallback viewpagerlistener = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bar.setSelectedNavigationItem(position);
            }
        };


        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                myViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

        };

        ActionBar.Tab tab1 = bar.newTab().setText("My Page").setTabListener(tabListener);
        ActionBar.Tab tab2 = bar.newTab().setText("Products").setTabListener(tabListener);
        ActionBar.Tab tab3 = bar.newTab().setText("Transactions").setTabListener(tabListener);
        ActionBar.Tab tab4 = bar.newTab().setText("Contacts").setTabListener(tabListener);

        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3);
        bar.addTab(tab4);

//        arrayList.add(new UserFragment());
//        arrayList.add(new ProductFragment());
//        arrayList.add(new TransactionFragment());
//        arrayList.add(new ContactFragment());

        myAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());

        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);
        myViewPager2.registerOnPageChangeCallback(viewpagerlistener);
        myViewPager2.setPageTransformer(new MarginPageTransformer(500));

//        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_products);
//        new FirebaseDatabaseHelper().readProducts(new FirebaseDatabaseHelper.DataStatus() {
//            @Override
//            public void DataIsLoaded(List<Product> products, List<String> keys) {
//                new RecyclerView_Config().setConfig(mRecyclerView, MainActivity.this, products, keys);
//
//            }
//
//            @Override
//            public void DataIsInsterted() {
//
//            }
//
//            @Override
//            public void DataIsUpdated() {
//
//            }
//
//            @Override
//            public void DataIsDeleted() {
//
//            }
//        });

    }






}
