package com.razorpay.razorpayassignment.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razorpay.razorpayassignment.R;
import com.razorpay.razorpayassignment.models.ExpenseType;

import butterknife.ButterKnife;


public class ExpenseListFragment extends Fragment {

    private static final String ARG_EXPENSE_TYPE = "expenseType";

    private ExpenseType expenseType;


    public ExpenseListFragment() {
        // Required empty public constructor
    }


    public static ExpenseListFragment newInstance(ExpenseType expenseType) {
        ExpenseListFragment fragment = new ExpenseListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXPENSE_TYPE, expenseType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            expenseType = (ExpenseType) getArguments().getSerializable(ARG_EXPENSE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_expense_list, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

}
