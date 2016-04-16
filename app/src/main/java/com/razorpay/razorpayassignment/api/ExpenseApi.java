package com.razorpay.razorpayassignment.api;

import com.razorpay.razorpayassignment.models.ExpenseResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yogeshmadaan on 16/04/16.
 */
public interface ExpenseApi {

    @GET("api/jsonBlob/{id}")
    Observable<ExpenseResponse> getExpenses(@Path("id") String id);
    @PUT("api/jsonBlob/{id}")
    Observable<ExpenseResponse> updateExpenses(@Path("id") String id,  @Body ExpenseResponse body);
}
