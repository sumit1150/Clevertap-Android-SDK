package com.ct.ctoobdemoapp;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.appsflyer.AppsFlyerLib;
import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.interfaces.NotificationHandler;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.clevertap.android.sdk.pushnotification.amp.CTPushAmpListener;
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
//import com.segment.analytics.Analytics;

import java.util.HashMap;
import java.util.Objects;

public class CustomApplicationClass extends Application  implements CTPushAmpListener, CTPushNotificationListener {

    //implements CTPushAmpListener
    String ctID;
    private static final String Tag15="CTID";
    CleverTapAPI cleverTapAPI;
    private static final String Tag11="PushAmpPayload";

    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);


            CleverTapAPI.setNotificationHandler((NotificationHandler)new PushTemplateNotificationHandler());
            //Custom push Amplification

        super.onCreate();

        cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
        //assert cleverTapAPI != null;
        cleverTapAPI.setCTPushAmpListener(this);
        cleverTapAPI.setCTPushNotificationListener(this);

        //Appsflayer SDK
        AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();
        AppsFlyerLib.getInstance().init("TN6PNVCuJE7iKmHyaaAvAC", null, this);

            //Segment

        // Create an analytics client with the given context and Segment write key.
       /* Analytics analytics = new Analytics.Builder(getApplicationContext(), "LNhrPMl1KhxgW80G4tpcpEvVRRcNd3if")
                // Enable this to record certain application events automatically!
                .trackApplicationLifecycleEvents()
                // Enable this to record screen views automatically!
                .recordScreenViews()
                .build();
        // Set the initialized instance as a globally accessible instance.
        Analytics.setSingletonInstance(analytics);*/

       // ctID= Objects.requireNonNull(CleverTapAPI.getDefaultInstance(this)).getCleverTapID();
        //Log.d(Tag15,ctID);
        //Toast.makeText(getApplicationContext(), "CTID: "+ctID, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPushAmpPayloadReceived(Bundle extras) {

        Log.d(Tag11,extras.toString());
        Toast.makeText(getApplicationContext(), "Received Push Amplification message", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNotificationClickedPayloadReceived(HashMap<String, Object> payload) {
        System.out.println("my_payload "+payload.toString());

    }
}
