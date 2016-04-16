package com.razorpay.razorpayassignment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.razorpay.razorpayassignment.fragments.ExpenseListFragment;
import com.razorpay.razorpayassignment.models.ExpenseType;

/**
 * Created by yogeshmadaan on 15/04/16.
 */
public class ExpensePagerAdapter extends FragmentStatePagerAdapter {
    String[] titles = {ExpenseType.ALL.toString(), ExpenseType.TAXI.toString(), ExpenseType.RECHARGE.toString()};

    public ExpensePagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return ExpenseListFragment.newInstance(ExpenseType.ALL);
            case 1:
                return ExpenseListFragment.newInstance(ExpenseType.TAXI);
            case 2:
                return ExpenseListFragment.newInstance(ExpenseType.RECHARGE);
            default:
                return ExpenseListFragment.newInstance(ExpenseType.ALL);
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
