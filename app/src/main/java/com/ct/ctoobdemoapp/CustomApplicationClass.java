package com.ct.ctoobdemoapp;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.interfaces.NotificationHandler;
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.segment.analytics.Analytics;

import java.util.Objects;

public class CustomApplicationClass extends Application {
    String ctID;
    private static final String Tag15="CTID";

    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
            CleverTapAPI.setNotificationHandler((NotificationHandler)new PushTemplateNotificationHandler());
        super.onCreate();

            //Segment

        // Create an analytics client with the given context and Segment write key.
        Analytics analytics = new Analytics.Builder(getApplicationContext(), "LNhrPMl1KhxgW80G4tpcpEvVRRcNd3if")
                // Enable this to record certain application events automatically!
                .trackApplicationLifecycleEvents()
                // Enable this to record screen views automatically!
                .recordScreenViews()
                .build();
        // Set the initialized instance as a globally accessible instance.
        Analytics.setSingletonInstance(analytics);

       // ctID= Objects.requireNonNull(CleverTapAPI.getDefaultInstance(this)).getCleverTapID();
        //Log.d(Tag15,ctID);
        //Toast.makeText(getApplicationContext(), "CTID: "+ctID, Toast.LENGTH_SHORT).show();

    }
}
