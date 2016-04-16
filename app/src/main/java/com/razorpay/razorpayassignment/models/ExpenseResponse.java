package com.razorpay.razorpayassignment.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yogeshmadaan on 16/04/16.
 */
public class ExpenseResponse {

    @SerializedName("expenses")
    @Expose
    private List<Expense> expenses = new ArrayList<Expense>();

    /**
     *
     * @return
     * The expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     *
     * @param expenses
     * The expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public static void sortExpensesByCategory(List<Expense> expenses, ExpenseType expenseType)
    {
        Iterator<Expense> expenseIterator = expenses.iterator();
        while (expenseIterator.hasNext()) {
            Expense expense = expenseIterator.next();
            if (!expense.getCategory().equalsIgnoreCase(expenseType.toString())) {
                expenseIterator.remove();
            }
        }
    }

}