package com.ct.ctoobdemoapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

//import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

//import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
//import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationReceiver;
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFcmMessageListenerService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
   // CleverTapAPI.getDefaultInstance(getApplicationContext()).pushFcmRegistrationId(s,true);



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Custom Rendering Notification comes from CleverTap
    //  new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
      // new CTFcmMessageHandler().processPushAmp(getApplicationContext(), remoteMessage);
        super.onMessageReceived(remoteMessage);



        Bundle bundle=new Bundle();
        //new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);

        Map<String, String> messageData = remoteMessage.getData();
        Log.d("Message: ", messageData.toString());
        String CID = messageData.get("wzrk_cid");
        String ID = messageData.get("wzrk_id");
        String title = messageData.get("nt");
        String text = messageData.get("nm");
        String Deeplink=messageData.get("wzrk_dl");

        Intent intent=new Intent(getApplicationContext(), MainActivity.class);

       for (Map.Entry<String, String> entry : messageData.entrySet()) {
            //intent.putExtra(entry.getKey(), entry.getValue());
            bundle.putString(entry.getKey(), entry.getValue());
        }
        intent.putExtra("bundle",bundle);


        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Create a PendingIntent using FLAG_IMMUTABLE.
             pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        //To simulate the pending intent mutability crash
       // pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        //PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);


        //Notification custom Builder
       notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this, "General");
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.noti_icon)
                .setContentIntent(pendingIntent)
              //  .setWhen(System.currentTimeMillis())
                //.setTicker(remoteMessage.getNotification().getTicker())
                //.setPriority(remoteMessage.getPriority())
                .setContentTitle(title)
                .setContentText(text);
                //.setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
        CleverTapAPI.getDefaultInstance(this).pushNotificationViewedEvent(bundle);


       //new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("Firebase Token:",s);
        //CleverTapAPI.getDefaultInstance(this).pushFcmRegistrationId(s,true);
        //String fcmRegId= String.valueOf(FirebaseMessaging.getInstance().getToken());
        CleverTapAPI.getDefaultInstance(getApplicationContext()).pushFcmRegistrationId(s,true);
    }
}

