package com.razorpay.razorpayassignment.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razorpay.razorpayassignment.R;
import com.razorpay.razorpayassignment.adapters.ExpenseListAdapter;
import com.razorpay.razorpayassignment.models.Expense;
import com.razorpay.razorpayassignment.models.ExpenseResponse;
import com.razorpay.razorpayassignment.models.ExpenseType;
import com.razorpay.razorpayassignment.services.ExpenseService;
import com.razorpay.razorpayassignment.utils.RxUtils;
import com.razorpay.razorpayassignment.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class ExpenseListFragment extends Fragment implements ExpenseListAdapter.OnExpenseUpdateListener {

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private static final String ARG_EXPENSE_TYPE = "expenseType";
    private static final String TAG = ExpenseListFragment.class.getCanonicalName();
    public static final ExpenseType DEFAULT_EXPENSE_TYPE = ExpenseType.ALL;
    private ExpenseType expenseType = DEFAULT_EXPENSE_TYPE;

    private List<Expense> expenses;
    private ExpenseListAdapter expenseListAdapter;
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private ExpenseResponse expenseResponse;
    private ViewGroup rootView;

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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_expense_list, container, false);
        ButterKnife.bind(this,rootView);
        initViews();
        return rootView;
    }

    public void initViews()
    {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        recyclerView.setHasFixedSize(true);
        expenses = new ArrayList<>();
        expenseListAdapter = new ExpenseListAdapter(getActivity(),expenses,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(expenseListAdapter);
        refreshContent();
    }
    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);

    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }
    public void startRefreshing() {
        swipeRefreshLayout.setRefreshing(true);
    }

    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshContent() {
        startRefreshing();
        _subscriptions.add(//
                new ExpenseService(getActivity()).getExpenseApi().getExpenses(getResources().getString(R.string.string_blob_id))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ExpenseResponse>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                if(!Utils.isConnectedToInternet(getActivity()))
                                    showSnackbar(getResources().getString(R.string.text_no_internet));
                                else
                                    showSnackbar(getResources().getString(R.string.text_default_error));
                            }

                            @Override
                            public void onNext(ExpenseResponse expenseResponse) {
                                refreshData(expenseResponse);
                            }
                        }));
    }

    public void refreshData(ExpenseResponse expenseResponse)
    {
        if (expenseResponse != null && expenseResponse.getExpenses().size() > 0) {

                this.expenseResponse = expenseResponse;
                expenses.clear();
            if(expenseType != ExpenseType.ALL)
            {
                ExpenseResponse.sortExpensesByCategory(expenseResponse.getExpenses(),expenseType);
                expenses.addAll(expenseResponse.getExpenses());
            }
            else
                expenses.addAll(expenseResponse.getExpenses());
            expenseListAdapter.notifyDataSetChanged();
            stopRefreshing();


        }
    }
    public void showSnackbar(String text)
    {
        stopRefreshing();
        Snackbar.make(rootView,text, Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.text_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshContent();
                    }
                }).show();
    }

    @Override
    public void onExpenseUpdate(List<Expense> expenses) {
        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenses(expenses);
        _subscriptions.add(//
                new ExpenseService(getActivity()).getExpenseApi().updateExpenses(getResources().getString(R.string.string_blob_id),expenseResponse)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ExpenseResponse>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                if(!Utils.isConnectedToInternet(getActivity()))
                                    showSnackbar(getResources().getString(R.string.text_no_internet));
                                else
                                    showSnackbar(getResources().getString(R.string.text_default_error));
                            }

                            @Override
                            public void onNext(ExpenseResponse expenseResponse) {
                                refreshData(expenseResponse);
                                //TODO improve update performance by updating specific element of recycler view
                            }
                        }));
    }
}
