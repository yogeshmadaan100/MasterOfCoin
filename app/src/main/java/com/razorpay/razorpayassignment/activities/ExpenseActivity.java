package com.razorpay.razorpayassignment.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.razorpay.razorpayassignment.R;
import com.razorpay.razorpayassignment.fragments.ExpenseActivityFragment;
import com.razorpay.razorpayassignment.interfaces.ToolbarInterface;
import com.razorpay.razorpayassignment.models.SortType;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpenseActivity extends AppCompatActivity implements ToolbarInterface{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @OnClick(R.id.fab)
    public void onFabClicked()
    {
        //TODO fix Fab position after snackbar
        final CharSequence[] items = new CharSequence[3];
        items[0] = SortType.TIME.toString();
        items[1] = SortType.CATEGORY.toString();
        items[2] = SortType.STATE.toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Sort Criteria");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                sortItems(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }


    public void sortItems(String sortCriteria)
    {
        //TODO improve this interaction mechanism
        ((ExpenseActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment)).sort(sortCriteria);
    }

    @Override
    public void setToolbarSubTitle(String toolbarSubTitle) {
        if(getSupportActionBar()!=null)
            getSupportActionBar().setSubtitle(toolbarSubTitle);
    }
}
