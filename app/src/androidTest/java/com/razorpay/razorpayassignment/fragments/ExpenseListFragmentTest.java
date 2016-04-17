package com.razorpay.razorpayassignment.fragments;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.razorpay.razorpayassignment.BuildConfig;
import com.razorpay.razorpayassignment.Constants;
import com.razorpay.razorpayassignment.activities.ExpenseActivity;
import com.razorpay.razorpayassignment.adapters.ExpenseListAdapter;
import com.razorpay.razorpayassignment.api.ExpenseApi;
import com.razorpay.razorpayassignment.generators.ServiceGenerator;
import com.razorpay.razorpayassignment.models.ExpenseResponse;
import com.razorpay.razorpayassignment.models.ExpenseType;
import com.razorpay.razorpayassignment.services.ExpenseService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;


/**
 * Created by yogeshmadaan on 17/04/16.
 */
@Config(constants = BuildConfig.class, sdk = 21,
        manifest = "app/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ExpenseListFragmentTest {

    private ExpenseActivity expenseActivity;
    private ExpenseListFragment expenseListFragment;
    private ExpenseService expenseService;
    @Mock
    private ExpenseApi expenseApi;

    @Captor
    private ArgumentCaptor<Callback<ExpenseResponse>> callbackArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ActivityController<ExpenseActivity> controller = Robolectric.buildActivity(ExpenseActivity.class);
        expenseListFragment = ExpenseListFragment.newInstance(ExpenseType.ALL );
        expenseActivity = controller.get();
        FragmentManager fragmentManager = expenseActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( expenseListFragment, null );
        fragmentTransaction.commit();
        expenseApi = new ExpenseService(expenseActivity).getExpenseApi();
        // then we need to swap the retrofit api impl. with a mock one
        // I usually store my retrofit api impl as a static singleton in class RestClient, hence:
        expenseListFragment.refreshContent();
        controller.create();
    }
    // Incomplete. Do not run.
    @Test
    public void shouldFillAdapter() throws Exception {
        Observable<ExpenseResponse> expenseResponseObservable = Mockito.verify(expenseApi).getExpenses("570de811e4b01190df5dafec");
        ExpenseResponse expenseResponse = new Gson().fromJson(Constants.RESPONSE_OK,ExpenseResponse.class);
        Response tempResponse = new Response<ExpenseResponse>();
        callbackArgumentCaptor.getValue().onResponse(expenseResponse);

        ExpenseListAdapter expenseListAdapter = expenseListFragment.getExpenseListAdapter(); // obtain adapter
        // simple test check if adapter has as many items as put into response
        Assert.assertEquals(expenseListAdapter.getItemCount(),15);
    }
}