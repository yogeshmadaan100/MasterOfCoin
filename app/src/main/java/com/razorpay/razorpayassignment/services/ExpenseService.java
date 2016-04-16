package com.razorpay.razorpayassignment.services;

import android.content.Context;

import com.razorpay.razorpayassignment.api.ExpenseApi;
import com.razorpay.razorpayassignment.generators.ServiceGenerator;

/**
 * Created by yogeshmadaan on 16/04/16.
 */
public class ExpenseService {

    private ExpenseApi expenseApi = null;

    public ExpenseService(Context context)
    {
        expenseApi = ServiceGenerator.createService(ExpenseApi.class,context);
    }

    public ExpenseApi getExpenseApi() {
        return expenseApi;
    }
}
