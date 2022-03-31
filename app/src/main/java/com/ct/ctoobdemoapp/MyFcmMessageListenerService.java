package com.ct.ctoobdemoapp;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

//import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFcmMessageListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

       // new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);


    }
}

