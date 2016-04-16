package com.razorpay.razorpayassignment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.razorpay.razorpayassignment.fragments.ExpenseListFragment;
import com.razorpay.razorpayassignment.models.ExpenseType;

/**
 * Created by yogeshmadaan on 15/04/16.
 */
public class ExpensePagerAdapter extends FragmentStatePagerAdapter {
    String[] titles = {ExpenseType.ALL.toString(), ExpenseType.TAXI.toString(), ExpenseType.RECHARGE.toString()};
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
