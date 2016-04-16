package com.razorpay.razorpayassignment.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.razorpay.razorpayassignment.R;
import com.razorpay.razorpayassignment.models.Expense;
import com.razorpay.razorpayassignment.models.ExpenseType;
import com.razorpay.razorpayassignment.models.StateType;
import com.razorpay.razorpayassignment.utils.Utils;

import java.util.List;

/**
 * Created by yogeshmadaan on 16/04/16.
 */
public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewholder>{
    
    Context context =null;
    List<Expense> expenses;
    OnExpenseUpdateListener onExpenseUpdateListener;
    public ExpenseListAdapter(Context context, List<Expense> expenses, OnExpenseUpdateListener onExpenseUpdateListener)
    {
        this.context = context;
        this.expenses = expenses;
        this.onExpenseUpdateListener = onExpenseUpdateListener;
    }

    @Override
    public ExpenseViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_expense, parent, false);

        return new ExpenseViewholder(v);    }

    @Override
    public void onBindViewHolder(ExpenseViewholder holder, int position) {
        final Expense expense = expenses.get(position);
        holder.txtExpenseID.setText(context.getResources().getString(R.string.text_expense_id)+" " + expense.getId());
        holder.txtExpenseDescription.setText(expense.getDescription());
        holder.txtExpenseState.setText(expense.getState().toUpperCase());
        holder.txtExpenseAmount.setText(context.getResources().getString(R.string.text_rupee)+" " + expense.getAmount());
        holder.txtExpenseTimestamp.setText(Utils.formatDate(expense.getTime()));
        setBackgroundColor(holder.itemView,expense.getCategory());
        holder.actionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                if(expense.getState().equalsIgnoreCase(StateType.UNVERIFIED.toString()))
                {
                    popupMenu.getMenu().add(0, 0, 0, context.getResources().getString(R.string.change_to_verified));
                    popupMenu.getMenu().add(0, 2, 1, context.getResources().getString(R.string.change_to_fraudulent));
                }
                else if(expense.getState().equalsIgnoreCase(StateType.UNVERIFIED.toString()))
                {
                    popupMenu.getMenu().add(1, 1, 0, context.getResources().getString(R.string.change_to_unverified));
                    popupMenu.getMenu().add(1, 2, 1, context.getResources().getString(R.string.change_to_fraudulent));
                }
                else
                {
                    popupMenu.getMenu().add(2, 0, 0, context.getResources().getString(R.string.change_to_verified));
                    popupMenu.getMenu().add(2, 1, 1, context.getResources().getString(R.string.change_to_unverified));
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case 0:
                                expense.setState(StateType.VERIFIED.toString());
                                break;
                            case 1:
                                expense.setState(StateType.UNVERIFIED.toString());
                                break;
                            case 2:
                                expense.setState(StateType.FRAUDULENT.toString());
                                break;
                        }
                        onExpenseUpdateListener.onExpenseUpdate(expenses);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }


    public class ExpenseViewholder extends RecyclerView.ViewHolder{
        TextView txtExpenseID;
        TextView txtExpenseDescription;
        TextView txtExpenseTimestamp;
        TextView txtExpenseAmount;
        TextView txtExpenseState;
        ImageView actionMore;
        public ExpenseViewholder(View itemView)
        {
            super(itemView);
            txtExpenseID = (TextView) itemView.findViewById(R.id.expenseID);
            txtExpenseDescription = (TextView) itemView.findViewById(R.id.expenseDescription);
            txtExpenseTimestamp = (TextView) itemView.findViewById(R.id.expenseTimestamp);
            txtExpenseState = (TextView) itemView.findViewById(R.id.expenseState);
            txtExpenseAmount = (TextView) itemView.findViewById(R.id.expenseAmount);
            actionMore = (ImageView) itemView.findViewById(R.id.actionMore);
        }
    }

    public void setBackgroundColor(View view,String expenseType)
    {
        if(expenseType.equalsIgnoreCase(ExpenseType.TAXI.toString()))
            view.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.color_taxi)));
        else
            view.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.color_recharge)));

    }

    public interface  OnExpenseUpdateListener{
        void onExpenseUpdate(List<Expense> expenses);
    }
}
