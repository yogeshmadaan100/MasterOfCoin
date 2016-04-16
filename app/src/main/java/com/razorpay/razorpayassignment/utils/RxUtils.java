package com.razorpay.razorpayassignment.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yogeshmadaan on 16/04/16.
 */
public class RxUtils {

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }
}
