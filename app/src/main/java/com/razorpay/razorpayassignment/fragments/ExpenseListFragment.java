package com.razorpay.razorpayassignment.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razorpay.razorpayassignment.R;
import com.razorpay.razorpayassignment.adapters.ExpenseListAdapter;
import com.razorpay.razorpayassignment.interfaces.ToolbarInterface;
import com.razorpay.razorpayassignment.models.Expense;
import com.razorpay.razorpayassignment.models.ExpenseResponse;
import com.razorpay.razorpayassignment.models.ExpenseType;
import com.razorpay.razorpayassignment.models.SortType;
import com.razorpay.razorpayassignment.services.ExpenseService;
import com.razorpay.razorpayassignment.utils.RxUtils;
import com.razorpay.razorpayassignment.utils.SharedPrefUtils;
import com.razorpay.razorpayassignment.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class ExpenseListFragment extends Fragment implements ExpenseListAdapter.OnExpenseUpdateListener {

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";


    IntentFilter filter = new IntentFilter(ACTION);
    NetworkStateReceiver networkStateReceiver;

    private static final String ARG_EXPENSE_TYPE = "expenseType";
    private static final String TAG = ExpenseListFragment.class.getCanonicalName();
    private static final ExpenseType DEFAULT_EXPENSE_TYPE = ExpenseType.ALL;
    private ExpenseType expenseType = DEFAULT_EXPENSE_TYPE;
    private SortType SORT_TYPE = null;
    private static final String KEY_SORT_TYPE ="sortType";

    private List<Expense> expenses;
    private ExpenseListAdapter expenseListAdapter;
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private ViewGroup rootView;
    public Subscription getExpenseSubscrption, putExpenseSubscription;
    private ToolbarInterface toolbarInterface = null;
    private SharedPrefUtils sharedPrefUtils;
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
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    public void initViews() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        recyclerView.setHasFixedSize(true);
        expenses = new ArrayList<>();
        expenseListAdapter = new ExpenseListAdapter(getActivity(), expenses, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(expenseListAdapter);
        refreshContent();
        networkStateReceiver = new NetworkStateReceiver();
        sharedPrefUtils = new SharedPrefUtils(getActivity());
        SORT_TYPE = SortType.NONE;
        Log.e("setting sort","none");
    }

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        getActivity().registerReceiver(networkStateReceiver, filter);
        refreshContent();
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
        getExpenseSubscrption = null;
        getActivity().unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            toolbarInterface = (ToolbarInterface)context;
        }catch(ClassCastException e)
        {

        }
    }

    public void startRefreshing() {
        swipeRefreshLayout.setRefreshing(true);
    }

    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshContent() {
        startRefreshing();

        if(getExpenseSubscrption==null) {
            registerGetExpenseSubscription();
            addSubscription(getExpenseSubscrption);
        }
    }
    //
//                Observable.timer(0, 2000, TimeUnit.MILLISECONDS)
//                        .flatMap(new Func1<Long, Observable<ExpenseResponse>>(){
//
//                            @Override
//                            public Observable<ExpenseResponse> call(Long tick) {
//                                return new ExpenseService(getActivity()).getExpenseApi().getExpenses(getResources().getString(R.string.string_blob_id))
////                                        .onErrorResumeNext(new Func1<Throwable, Observable<ExpenseResponse>>(){
////                                            @Override
////                                            public Observable<ExpenseResponse> call(Throwable throwable) {
////                                                return Observable.empty();
////                                            }
////                                        })
// ;
//                            }
//                        })
//                        .subscribeOn(Schedulers.io())
//                        .retryWhen(new RetryWithDelay(5,5000))
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<ExpenseResponse>() {
//                            @Override
//                            public void onCompleted() {
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                if(!Utils.isConnectedToInternet(getActivity()))
//                                    showSnackbar(getResources().getString(R.string.text_no_internet));
//                                else
//                                    showSnackbar(getResources().getString(R.string.text_default_error));
//                            }
//
//                            @Override
//                            public void onNext(ExpenseResponse expenseResponse) {
//                                refreshData(expenseResponse);
//                            }
//                        }));


    public void refreshData(ExpenseResponse expenseResponse) {
        if (expenseResponse != null && expenseResponse.getExpenses().size() > 0) {
            sharedPrefUtils.setLastTimestamp(System.currentTimeMillis());
            sharedPrefUtils.setServerStatus("ONLINE");
            updateToolbarSubtitle();
            expenses.clear();
            if (expenseType != ExpenseType.ALL) {
                ExpenseResponse.sortExpensesByCategory(expenseResponse.getExpenses(), expenseType);
                expenses.addAll(expenseResponse.getExpenses());
            } else
                expenses.addAll(expenseResponse.getExpenses());
            sort(SORT_TYPE.toString());
            expenseListAdapter.notifyDataSetChanged();
            stopRefreshing();


        }
    }

    public void showSnackbar(String text) {
        stopRefreshing();
        Snackbar.make(rootView, text, Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.text_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshContent();
                    }
                }).show();
    }

    public void registerGetExpenseSubscription() {
        getExpenseSubscrption = new ExpenseService(getActivity()).getExpenseApi().getExpenses(getResources().getString(R.string.string_blob_id))
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(5, 20000))
                .repeatWhen(new RepeatWithDelay(20000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExpenseResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!Utils.isConnectedToInternet(getActivity()))
                        {
                            showSnackbar(getResources().getString(R.string.text_no_internet));
                            sharedPrefUtils.setServerStatus("OFFLINE");
                            updateToolbarSubtitle();

                        }
                        else
                            showSnackbar(getResources().getString(R.string.text_default_error));
                    }

                    @Override
                    public void onNext(ExpenseResponse expenseResponse) {
                        refreshData(expenseResponse);
                    }
                });
    }

    public void addSubscription(Subscription subscription)
    {
        Log.e("subscription","Added");
        _subscriptions.add(subscription);
    }

    public void removeSubscription(Subscription subscription)
    {
        try{
            _subscriptions.remove(subscription);
        }catch (Exception e)
        {
            Log.d("Subscription","Remove Failed");
        }
    }
    @Override
    public void onExpenseUpdate(List<Expense> expenses) {
        startRefreshing();
        if (!Utils.isConnectedToInternet(getActivity())) { // To save calls when internet not working
            showSnackbar(getResources().getString(R.string.text_no_internet));
        } else {
            ExpenseResponse expenseResponse = new ExpenseResponse();
            expenseResponse.setExpenses(expenses);
            _subscriptions.add(//
                    new ExpenseService(getActivity()).getExpenseApi().updateExpenses(getResources().getString(R.string.string_blob_id), expenseResponse)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ExpenseResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (!Utils.isConnectedToInternet(getActivity()))
                                    {
                                        showSnackbar(getResources().getString(R.string.text_no_internet));
                                        sharedPrefUtils.setServerStatus("OFFLINE");
                                        updateToolbarSubtitle();
                                    }
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Don't Do anything here as we always want updated data
        outState.putSerializable(KEY_SORT_TYPE,SORT_TYPE);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("View state","restored");
        if(savedInstanceState!=null && savedInstanceState.getSerializable(KEY_SORT_TYPE)!=null)
            SORT_TYPE = (SortType) savedInstanceState.getSerializable(KEY_SORT_TYPE);
    }

    //public static class RetryWithDelay
 public class RetryWithDelay implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int _maxRetries;
    private final int _retryDelayMillis;
    private int _retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        _maxRetries = maxRetries;
        _retryDelayMillis = retryDelayMillis;
        _retryCount = 0;
    }

    // this is a notificationhandler, all that is cared about here
    // is the emission "type" not emission "content"
    // only onNext triggers a re-subscription (onError + onComplete kills it)

    @Override
    public Observable<?> call(Observable<? extends Throwable> inputObservable) {

        // it is critical to use inputObservable in the chain for the result
        // ignoring it and doing your own thing will break the sequence

        return inputObservable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (++_retryCount < _maxRetries) {

                    // When this Observable calls onNext, the original
                    // Observable will be retried (i.e. re-subscribed)

                    Log.d("Retrying in %d ms", "" + _retryCount * _retryDelayMillis);

                    return Observable.timer(_retryCount * _retryDelayMillis,
                            TimeUnit.MILLISECONDS);
                }

                Log.d("Retry", "Argh! i give up");

                // Max retries hit. Pass an error so the chain is forcibly completed
                // only onNext triggers a re-subscription (onError + onComplete kills it)
                removeSubscription(getExpenseSubscrption);
                getExpenseSubscrption = null;
                return Observable.error(throwable);
            }
        });
    }
    }

//public static class RepeatWithDelay
    public class RepeatWithDelay implements Func1<Observable<? extends Void>, Observable<?>> {

    private final int _repeatDelayMillis;

    public RepeatWithDelay(final int repeatDelayMillis) {
        _repeatDelayMillis = repeatDelayMillis;
    }

    // this is a notificationhandler, all that is cared about here
    // is the emission "type" not emission "content"
    // only onNext triggers a re-subscription (onError + onComplete kills it)


    @Override
    public Observable<?> call(Observable<? extends Void> observable) {
        return observable.flatMap(new Func1<Void, Observable<?>>() {
            @Override
            public Observable<?> call(Void aVoid) {
                return Observable.timer(_repeatDelayMillis,
                        TimeUnit.MILLISECONDS);
            }

        });
    }
    }


    public class NetworkStateReceiver extends BroadcastReceiver {
        // post event if there is no Internet connection
        public void onReceive(Context context, Intent intent) {
            //super.onReceive(context, intent);
            if (intent.getExtras() != null) {
                NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {

                    // there is Internet connection
                    if (getExpenseSubscrption == null)
                    {
                        registerGetExpenseSubscription();
                        addSubscription(getExpenseSubscrption);
                    }

                }
                if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                    // no Internet connection, send network state changed
                }
            }
        }
    }
    public void updateToolbarSubtitle()
    {
        if(toolbarInterface!=null)
        {
            toolbarInterface.setToolbarSubTitle("LastUpdated : "+Utils.formatLastUpdatedTimestamp(sharedPrefUtils.getLastTimestamp()) + "("+sharedPrefUtils.getServerStatus()+")");
        }
    }
    public void sort(String sortCriteria)
    {
        if(sortCriteria.equalsIgnoreCase(SORT_TYPE.TIME.toString()))
        {
            Collections.sort(expenses,new TimeComparator());
            SORT_TYPE = SortType.TIME;
        }
        else if(sortCriteria.equalsIgnoreCase(SORT_TYPE.CATEGORY.toString()))
        {
            Collections.sort(expenses,new CategoryComparator());
            SORT_TYPE = SortType.CATEGORY;
        }
        else if(sortCriteria.equalsIgnoreCase(SORT_TYPE.CATEGORY.toString()))
        {
            Collections.sort(expenses,new StateComparator());
            SORT_TYPE = SortType.STATE;
        }
    }

    public class TimeComparator implements Comparator<Expense>
    {

        @Override
        public int compare(Expense lhs, Expense rhs) {
            return Utils.convertDatetoTimestamp(lhs.getTime()).compareTo(Utils.convertDatetoTimestamp(rhs.getTime()));
        }

    }
    public class StateComparator implements Comparator<Expense>
    {
        @Override
        public int compare(Expense lhs, Expense rhs) {
            return lhs.getState().compareTo(rhs.getState());
        }
    }

    public class CategoryComparator implements Comparator<Expense>
    {
        @Override
        public int compare(Expense lhs, Expense rhs) {
            return lhs.getCategory().compareTo(rhs.getCategory());
        }
    }


    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}

