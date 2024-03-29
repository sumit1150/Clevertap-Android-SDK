package com.ct.ctoobdemoapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.geofence.CTGeofenceAPI;
import com.clevertap.android.geofence.CTGeofenceSettings;
import com.clevertap.android.geofence.Logger;
import com.clevertap.android.geofence.interfaces.CTGeofenceEventsListener;
import com.clevertap.android.geofence.interfaces.CTLocationUpdatesListener;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements CTPushNotificationListener, InAppNotificationButtonListener, CTInboxListener {

    CleverTapAPI cleverTapAPI;
    private static final String Tag1="MainActivity";
    private static final String Tag2="Sumit";
    String clevertapID="";
    //UI Declaration
    EditText UserName,Password;
    TextView sign_up_link,pushTestProfile,skip;
    Button loginNow;
    //UI Variables
    String username;
    String password;
    String u11,p11;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    //profile update
    HashMap<String, Object> profileUpdate;
    HashMap<String, Object> loginAction;
    HashMap<String, Object> pushPropUpdate;
    Bundle payload1;
    int i=0;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;//For geofencing
    private FirebaseAnalytics mFirebaseAnalytics;//this is for real time uninatall tracking




    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initilization of clevertap SDK
        cleverTapAPI=CleverTapAPI.getDefaultInstance(getApplicationContext());
        //setup the CT debugger
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);
        //Get Shared preference
        //Get Clevertap ID
        clevertapID=cleverTapAPI.getCleverTapID();
        Toast.makeText(getApplicationContext(), "Clevertap ID"+clevertapID, Toast.LENGTH_SHORT).show();
        //Log.d(Tag1,clevertapID);
        //Creating a Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CleverTapAPI.createNotificationChannel(getApplicationContext(),"General","General","General Channel",NotificationManager.IMPORTANCE_MAX,true,"four.mp3");
        }

        //Handling the deeplink in App kill state
        /*Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.d(Tag1,action);
        Log.d(Tag1, String.valueOf(data));*/



        if(cleverTapAPI!=null) {
            //Set Push Notification Listener
            cleverTapAPI.setCTPushNotificationListener(this);
            //set in-App button click listener
            cleverTapAPI.setInAppNotificationButtonListener(this);
            //set App inbox listener
            //I need to put this in App inbox button click
           // cleverTapAPI.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
           // cleverTapAPI.initializeInbox();
        }
        //Custom Push Notification handling
        //Step 1: Send FCM Tokan to the Clevertap

        //String fcmRegId = FirebaseInstanceId.getInstance().getToken();
        //String fcmRegId= String.valueOf(FirebaseMessaging.getInstance().getToken());
        //Toast.makeText(getApplicationContext(), "Push Token: "+fcmRegId, Toast.LENGTH_SHORT).show();
        //cleverTapAPI.pushFcmRegistrationId(fcmRegId,true);


        //UI Defination
        UserName=findViewById(R.id.UserName);
        Password=findViewById(R.id.Password);
        sign_up_link=findViewById(R.id.sign_up_link);
        loginNow=findViewById(R.id.loginNow);
        pushTestProfile=findViewById(R.id.testProfile);
        skip=findViewById(R.id.skip);
        //Profile update
        profileUpdate = new HashMap<String, Object>();

        sign_up_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
            }
        });

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=UserName.getText().toString();
                password=Password.getText().toString();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                u11 = sharedpreferences.getString("userIdentity", "");
                p11 = sharedpreferences.getString("userPassword", "");
                    if(username.equals(u11) && password.equals(p11))
                    {
                        Log.d(Tag1,u11);
                        Log.d(Tag1,p11);
                        profileUpdate.put("Identity", u11);
                        cleverTapAPI.onUserLogin(profileUpdate);
                        //Login Event property
                        loginAction = new HashMap<String, Object>();
                        loginAction.put("Action", "Login_Successful");
                        loginAction.put("Identity", u11);
                        loginAction.put("Date", new Date());
                        cleverTapAPI.pushEvent("Login", loginAction);

                        startActivity(new Intent(getApplicationContext(),Homepage.class));

                    }
                    else if(username.equals("9855290227") && password.equals("1234"))
                    {
                        profileUpdate.put("Identity", "9855290227");
                        cleverTapAPI.onUserLogin(profileUpdate);
                        loginAction = new HashMap<String, Object>();
                        loginAction.put("Action", "Login_Successful");
                        loginAction.put("Identity", u11);
                        loginAction.put("Date", new Date());
                        cleverTapAPI.pushEvent("Login", loginAction);

                        startActivity(new Intent(getApplicationContext(),Homepage.class));
                    }
                    else if(username.equals("") && password.equals("")){
                        profileUpdate.put("Identity", "9855290227");
                        cleverTapAPI.onUserLogin(profileUpdate);
                        loginAction = new HashMap<String, Object>();
                        loginAction.put("Action", "Login_Successful");
                        loginAction.put("Identity", u11);
                        loginAction.put("Date", new Date());
                        cleverTapAPI.pushEvent("Login", loginAction);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrong Password :(", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        pushTestProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate.put("Identity", "9855290227");
                profileUpdate.put("Name", "Sumit Sharma");
                profileUpdate.put("Email", "sumit.kumar@clevertap.com"); // Email address of the user
                profileUpdate.put("Phone", "+919855290227");   // Phone (with the country code, starting with +)
                profileUpdate.put("Gender", "M");             // Can be either M or F
                profileUpdate.put("city1", "GGC");
                profileUpdate.put("MSG-email", true);        // Disable email notifications
                profileUpdate.put("MSG-push", true);          // Enable push notifications
                profileUpdate.put("MSG-sms", false);          // Disable SMS notifications
                profileUpdate.put("MSG-whatsapp", false);// Enable WhatsApp notifications
                profileUpdate.put("TestIntValue", 10);
                cleverTapAPI.onUserLogin(profileUpdate);
                Toast.makeText(getApplicationContext(), "Test Profile Pushed Successfully", Toast.LENGTH_SHORT).show();
                cleverTapAPI.pushEvent("LoginWithTestProfile");
                startActivity(new Intent(getApplicationContext(),Homepage.class));
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Homepage.class));
            }
        });

        //triggred location
        try {
            CTGeofenceAPI.getInstance(getApplicationContext()).triggerLocation();
            Toast.makeText(getApplicationContext(), "Location triggred", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e){

            // thrown when this method is called before geofence SDK initialization
        }
        //Geofence Permission check function calling
        permissionCheck();
        ctGeofenceAPIFunc();
        //Geofence api callbacks

        //New way to write Geofence api


        //Calling for Geofence
       /* if (cleverTapAPI != null) {
            // proceed only if cleverTap instance is not null
            if (!checkPermissions()) {
               requestPermissions();
                SystemClock.sleep(6000);

            } else {

                initCTGeofenceApi();

            }
        }*/

    //Realtime uninstall analysis
       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //mFirebaseAnalytics.setUserProperty("ct_objectId",
          //      Objects.requireNonNull(CleverTapAPI.getDefaultInstance(this)).getCleverTapID());

        //Device Network Information Reporting in Android
        cleverTapAPI.enableDeviceNetworkInfoReporting(true);

    }

    public void permissionCheck(){

        //Location permission check
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            //Log.d(Tag1, String.valueOf(grantResults[0]));
            //Log.d(Tag1, String.valueOf(grantResults[1]));


        switch (requestCode) {
            case 1:
                //{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  ) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission 1 Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission 1 Denied", Toast.LENGTH_SHORT).show();
                }
               return;

           // }
           case 2:
                //{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission 2 Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission 2 Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            //}
        }
    }

    public void ctGeofenceAPIFunc(){
        //Geofence Setting
        CTGeofenceSettings ctGeofenceSettings = new CTGeofenceSettings.Builder()
                .enableBackgroundLocationUpdates(true)//boolean to enable background location updates
                .setLogLevel(Logger.DEBUG)//Log Level
                .setLocationAccuracy(CTGeofenceSettings.ACCURACY_HIGH)//byte value for Location Accuracy
                .setLocationFetchMode(CTGeofenceSettings.FETCH_CURRENT_LOCATION_PERIODIC)//byte value for Fetch Mode
                .setGeofenceMonitoringCount(99)//int value for number of Geofences CleverTap can monitor
                .setInterval(3600000)//long value for interval in milliseconds
                .setFastestInterval(1800000)//long value for fastest interval in milliseconds
                .setSmallestDisplacement(1000f)//float value for smallest Displacement in meters
                .setGeofenceNotificationResponsiveness(300000)// int value for geofence notification responsiveness in milliseconds
                .build();
        //setting up CT Geofence api
        CTGeofenceAPI.getInstance(getApplicationContext()).init(ctGeofenceSettings,cleverTapAPI);
        CTGeofenceAPI.getInstance(getApplicationContext())
                .setOnGeofenceApiInitializedListener(new CTGeofenceAPI.OnGeofenceApiInitializedListener() {
                    @Override
                    public void OnGeofenceApiInitialized() {
                        //App is notified on the main thread that CTGeofenceAPI is initialized
                        Toast.makeText(getApplicationContext(), "Geofence api is initilized", Toast.LENGTH_SHORT).show();
                    }
                });
        CTGeofenceAPI.getInstance(getApplicationContext()).setCtGeofenceEventsListener(new CTGeofenceEventsListener() {
                    @Override
                    public void onGeofenceEnteredEvent(JSONObject jsonObject) {
                        //Callback on the main thread when the user enters Geofence with info in jsonObject
                        Toast.makeText(getApplicationContext(), "Geofence Entered event triggered", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGeofenceExitedEvent(JSONObject jsonObject) {
                        //Callback on the main thread when user exits Geofence with info in jsonObject
                        Toast.makeText(getApplicationContext(), "Geofence Exited event triggered", Toast.LENGTH_SHORT).show();
                    }
                });
        CTGeofenceAPI.getInstance(getApplicationContext()).setCtLocationUpdatesListener(new CTLocationUpdatesListener() {
                    @Override
                    public void onLocationUpdates(Location location) {
                        //New location on the main thread as provided by the Android OS

                        if(location!=null){
                            cleverTapAPI.setLocation(location);
                            String lat= String.valueOf(location.getLatitude());
                            String lon= String.valueOf(location.getLongitude());
                            Toast.makeText(getApplicationContext(), "Location is updated, Lat: "+lat+", Long: "+lon, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Location is point to null object", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }



    // I have decided the Deeplink URL= ctdl://ct.com/home and ctdl://ct.com/deep
    //When I use https in front of deeplink as a scheme then it opens the external browser. hence I am using mu own scheme as a Custom solution (Need to discuss this with Prashant)
   //To handle the deeplinking in App Kill state I have added a intent filter as below also before using this intent filter my deeplink feature works in app foregrounf and app background state and the value I am getting is
    // ctdl://ct.com/home
    /*
    <intent-filter>
                <data android:scheme="ctdl" android:host="ct.com" android:pathPrefix="/"/>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
            </intent-filter>
    * */
    //After using this intent filter i am getting the the deeplink value as wzrk_dl":"ctdl:\/\/ct.com\/deep"

    @Override
    public void onNotificationClickedPayloadReceived(HashMap<String, Object> payload) {
        Log.d(Tag1, String.valueOf(payload));
        //String deplink= (String) payload.get("wzrk_dl");
        /*if(deplink.equals("ctdl://ct.com/deep")){
            startActivity(new Intent(getApplicationContext(),DeeplinkActivity.class));
        }*/
        //Log.d(Tag2,deplink);
        //Log.d(Tag2, String.valueOf(payload.containsValue("ctdl:\\/\\/ct.com\\/home")));
       if(payload.containsKey("wzrk_dl") && (payload.containsValue("ctdl://ct.com/home") || payload.containsValue("ctdl:\\/\\/ct.com\\/home"))){
            startActivity(new Intent(getApplicationContext(),Homepage.class));
        }
        else if(payload.containsKey("wzrk_dl") && (payload.containsValue("ctdl://ct.com/deep") || payload.containsValue("ctdl:\\/\\/ct.com\\/deep"))){
            startActivity(new Intent(getApplicationContext(),DeeplinkActivity.class));
        }
       else if(payload.containsKey("wzrk_dl") && (payload.containsValue("ctdl://ct.com/home/updateEventProp") || payload.containsValue("ctdl:\\/\\/ct.com\\/home\\/updateEventProp"))){
           pushPropUpdate = new HashMap<String, Object>();
           pushPropUpdate.put("Action", "Home Page Load");
           pushPropUpdate.put("PushDataPassed", "true");
           cleverTapAPI.pushEvent("HomeLoadEvent", pushPropUpdate);
           startActivity(new Intent(getApplicationContext(),Homepage.class));
       }

        else{
           Toast.makeText(getApplicationContext(), "Not any defined deeplink found in payload", Toast.LENGTH_SHORT).show();
       }



        //Action button payload check
        if(payload.containsKey("wzrk_acts") && payload.get("dl").equals("ctdl://ct.com/home"))
        {
            Toast.makeText(getApplicationContext(), "Action Deeplink found", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Homepage.class));
        }else if(payload.containsKey("wzrk_acts") && payload.get("dl").equals("ctdl://ct.com/deep")){
            startActivity(new Intent(getApplicationContext(),DeeplinkActivity.class));
        }
        else{
            Toast.makeText(getApplicationContext(), "Not any defined deeplink found in Notification Action payload", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onInAppButtonClick(HashMap<String, String> inApp_payload) {
        Toast.makeText(getApplicationContext(), "In-App Payload: "+inApp_payload, Toast.LENGTH_LONG).show();
        if(inApp_payload!=null){
            //read values
            Log.d(Tag2, String.valueOf(inApp_payload));
            if(inApp_payload.containsKey("wzrk_dl") && (inApp_payload.containsValue("ctdl://ct.com/home") || inApp_payload.containsValue("ctdl:\\/\\/ct.com\\/home"))){
                startActivity(new Intent(getApplicationContext(),Homepage.class));
            }
            else if(inApp_payload.containsKey("wzrk_dl") && (inApp_payload.containsValue("ctdl://ct.com/deep") || inApp_payload.containsValue("ctdl:\\/\\/ct.com\\/deep"))){
                startActivity(new Intent(getApplicationContext(),DeeplinkActivity.class));
            }
        }else{
            Toast.makeText(getApplicationContext(), "No Click found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void inboxDidInitialize() {

    }

    @Override
    public void inboxMessagesDidUpdate() {

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        payload1=intent.getExtras();
        if(payload1!=null){
            Toast.makeText(getApplicationContext(), "Payload: "+payload1, Toast.LENGTH_SHORT).show();
        }
        else{
        Log.d(Tag1, String.valueOf(payload1));
        Toast.makeText(getApplicationContext(), "Intent value: "+payload1, Toast.LENGTH_SHORT).show();
       /* if (payload1.containsKey("pt_id")&& payload1.getString("pt_id").equals("pt_rating"))
        {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(payload1.getInt("notificationId"));
        }
        if (payload1.containsKey("pt_id")&& payload1.getString("pt_id").equals("pt_product_display"))
        {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(payload1.getInt("notificationId"));
        }

    }*/
    }
    }
    //Show SnakeBar
  /* private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }
    private void initCTGeofenceApi() {
        Toast.makeText(getApplicationContext(), "Geofence API called", Toast.LENGTH_SHORT).show();
        if (cleverTapAPI == null)
            return;
        // proceed only if cleverTap instance is not null
        final Context con = this;
        CTGeofenceAPI.getInstance(getApplicationContext())
                .init(new CTGeofenceSettings.Builder()
                        .enableBackgroundLocationUpdates(true)
                        .setLogLevel(Logger.VERBOSE)
                        .setLocationAccuracy(CTGeofenceSettings.ACCURACY_HIGH)
                        .setLocationFetchMode(CTGeofenceSettings.FETCH_CURRENT_LOCATION_PERIODIC)
                        .setGeofenceMonitoringCount(99)
                        .setInterval(3600000) // 30 minuts
                        .setFastestInterval(1800000) // 30 minutes
                        .setSmallestDisplacement(1000)// 1 km
                        .build(), cleverTapAPI);
        Toast.makeText(getApplicationContext(), "InIt method called", Toast.LENGTH_SHORT).show();

        CTGeofenceAPI.getInstance(getApplicationContext())
                .setOnGeofenceApiInitializedListener(new CTGeofenceAPI.OnGeofenceApiInitializedListener() {
                    @Override
                    public void OnGeofenceApiInitialized() {
                        Toast.makeText(con, "Geofence API initialized", Toast.LENGTH_SHORT).show();
                    }
                });

        CTGeofenceAPI.getInstance(getApplicationContext())
                .setCtGeofenceEventsListener(new CTGeofenceEventsListener() {
                    @Override
                    public void onGeofenceEnteredEvent(JSONObject jsonObject) {
                        //String geofence_entered=jsonObject.toString();
                         Toast.makeText(con, "Geofence Entered", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGeofenceExitedEvent(JSONObject jsonObject) {
                        //String geofence_exited=jsonObject.toString();
                        Toast.makeText(con, "Geofence Exited ", Toast.LENGTH_SHORT).show();
                    }
                });

        CTGeofenceAPI.getInstance(getApplicationContext())
                .setCtLocationUpdatesListener(new CTLocationUpdatesListener() {
                    @Override
                    public void onLocationUpdates(Location location) {
                        if(location!=null){
                            //String loc=location.toString();
                            String lat,lon;
                            lat= String.valueOf(location.getLatitude());
                            lon= String.valueOf(location.getLongitude());
                              Toast.makeText(con, "Location Updated: Lat "+lat+" lon: "+lon, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(con, "Please turn on the device Location settings", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
    }
    private boolean checkPermissions() {
        Toast.makeText(getApplicationContext(), "check Permission method called", Toast.LENGTH_SHORT).show();
        int fineLocationPermissionState = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);

        int backgroundLocationPermissionState = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) : PackageManager.PERMISSION_GRANTED;

        return (fineLocationPermissionState == PackageManager.PERMISSION_GRANTED) &&
                (backgroundLocationPermissionState == PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermissions() {
        boolean permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean backgroundLocationPermissionApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean shouldProvideRationale =
                permissionAccessFineLocationApproved && backgroundLocationPermissionApproved;

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        //R.string.permission_rationale
        if (shouldProvideRationale) {
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initCTGeofenceApi();
            } else {

                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}