package com.razorpay.razorpayassignment.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razorpay.razorpayassignment.R;
import com.razorpay.razorpayassignment.adapters.ExpensePagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExpenseActivityFragment extends Fragment {
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    ExpensePagerAdapter expensePagerAdapter = null;
    public ExpenseActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_expense, container, false);
        ButterKnife.bind(this,rootView);
        initViews();
        return rootView;
    }

    public void initViews()
    {
        expensePagerAdapter = new ExpensePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(expensePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(0);
    }
}
