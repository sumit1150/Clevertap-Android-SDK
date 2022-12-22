package com.ct.ctoobdemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CTFeatureFlagsListener;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InboxMessageButtonListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.featureFlags.CTFeatureFlagsController;
import com.clevertap.android.sdk.inbox.CTInboxMessage;
import com.clevertap.android.sdk.interfaces.OnInitCleverTapIDListener;
import com.clevertap.android.sdk.product_config.CTProductConfigController;
import com.clevertap.android.sdk.product_config.CTProductConfigListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Homepage extends AppCompatActivity implements CTInboxListener, InboxMessageButtonListener, DisplayUnitListener {
    ImageButton app_inbox_id;
    CleverTapAPI cleverTapAPI;
    Button ctNativeDisplayEvent,goToWebPage,ct_custom_event,ct_Movie_Watched,id_app_inbox,nativeDisplay2;
    TextView  nativeDisplayResult, productConfigResults,productConfigABResult;
    CTProductConfigController productConfigController;
    String productConfigString,ab_test_String;
    LinearLayout backgroundLayout;
    CTFeatureFlagsController featureFlagsController;
    Boolean featureflagValue;
    private static final String Tag3="Homepage";
    private static final String Tag4="ProdConfigObj";
    HashMap customEventProp,movieProp,nativeDisplayEventProp;
    ImageView imageView;
    String m1;
    ArrayList<CTInboxMessage> str_getAppInboxMessages;
    private static final String Tag5="Inbox_Messages";
    private static final String Tag7="Inbox_Messages_Details";
    private static final String Tag6="Inbox_Messages_count";
    private static final String Tag10="Message_Content";
    HashMap pushPropUpdate;
    String ctInboxMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        app_inbox_id=findViewById(R.id.app_inbox_id);
        ctNativeDisplayEvent=findViewById(R.id.ctNativeDisplayEvent);
        nativeDisplayResult=findViewById(R.id.nativeDisplayResult);
        productConfigResults=findViewById(R.id.productConfigResult);
        productConfigABResult=findViewById(R.id.productConfigABResult);
        backgroundLayout=findViewById(R.id.backgroundLayout);
        goToWebPage=findViewById(R.id.goToWebPage);
        ct_custom_event=findViewById(R.id.ct_custom_event);
        ct_Movie_Watched=findViewById(R.id.ct_Movie_Watched);
        imageView=findViewById(R.id.imageView);
        id_app_inbox=findViewById(R.id.id_app_inbox);
        nativeDisplay2=findViewById(R.id.nativeDisplay2);

        cleverTapAPI=CleverTapAPI.getDefaultInstance(this);


            if(cleverTapAPI!=null){
                //App Inbox listener
                cleverTapAPI.setCTNotificationInboxListener(this);
                //Initialize the inbox and wait for callbacks on overridden methods
                cleverTapAPI.initializeInbox();
            }


            //Log.d(Tag5, String.valueOf(cleverTapAPI.getAllInboxMessages()));
            //Toast.makeText(getApplicationContext(), "App inbox initilized", Toast.LENGTH_SHORT).show();

           // ctInboxMessage=cleverTapAPI.getAllInboxMessages().toString();
            //Log.d(Tag7, ctInboxMessage);
            //Push event on load of Homepage
        /*pushPropUpdate = new HashMap<String, Object>();
        pushPropUpdate.put("Action", "Home Page Load");
        cleverTapAPI.pushEvent("HomeLoadEvent", pushPropUpdate);*/

            //To get all the app inbox message
            //Array list to get all app inbox messages
           // str_getAppInboxMessages= new ArrayList<>();
            //str_getAppInboxMessages= cleverTapAPI.getAllInboxMessages();
            //  Log.d(Tag5, String.valueOf(cleverTapAPI.getAllInboxMessages()));
            //Log.d(Tag6, String.valueOf(cleverTapAPI.getInboxMessageCount()));
            //Toast.makeText(getApplicationContext(), "Total Inbox Count: "+cleverTapAPI.getInboxMessageCount(), Toast.LENGTH_SHORT).show();

        //Native display listener
        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this);
        //Product config Controller
        productConfigController=cleverTapAPI.productConfig();
        //setting up the product config defaults
        cleverTapAPI.productConfig().setDefaults(R.xml.productconfigdefault);
        //setting up the product config listener
        productConfigController.fetchAndActivate();
        Log.d(Tag4,cleverTapAPI.productConfig().getSettings().toString());
        cleverTapAPI.setCTProductConfigListener(new CTProductConfigListener() {
            @Override
            public void onInit() {
                //Must Call activate if you want to apply the last fetched values on init every time.
                cleverTapAPI.productConfig().activate();
                //Toast.makeText(getApplicationContext(), "CT PC init method called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFetched() {
               // productConfigController.fetchAndActivate();
                //Toast.makeText(getApplicationContext(), "onFetched method called", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onActivated() {
              //  productConfigController.activate();
                //Toast.makeText(getApplicationContext(), "onActivated method called", Toast.LENGTH_SHORT).show();
                productConfigString=productConfigController.getString("productConfigKey1");
                ab_test_String=productConfigController.getString("ABTestSumit");
                if(productConfigString.equals("Red")){
                    productConfigResults.setBackgroundColor(Color.parseColor("#FF0000"));
                    productConfigResults.setText("Product Config Results: Config Object Found");
                }
                else if(ab_test_String.equals("Blue")){
                    productConfigABResult.setBackgroundColor(Color.parseColor("0000FF"));
                    productConfigABResult.setText("Product Config Results: Config AB Test Object Found");
                }else if(ab_test_String.equals("Green")){
                    productConfigABResult.setBackgroundColor(Color.parseColor("00FF00"));
                    productConfigABResult.setText("Product Config Results: Config AB Test Object Found");
                }
                else{
                    productConfigResults.setText("Product Config Results: Config Object Not Found");
                    productConfigResults.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    productConfigABResult.setBackgroundColor(Color.parseColor("#DCDCDC"));
                }
            }
        });

        //Setting up the Feature flags
        featureFlagsController=cleverTapAPI.featureFlag();
        //setting up the feature flag listener
        cleverTapAPI.setCTFeatureFlagsListener(new CTFeatureFlagsListener() {
            @Override
            public void featureFlagsUpdated() {
                Toast.makeText(getApplicationContext(), "feature Flag updated method called", Toast.LENGTH_SHORT).show();
                featureflagValue=featureFlagsController.get("featureFlagKey1",false);
                if(featureflagValue)
                {
                    backgroundLayout.setBackgroundColor(Color.parseColor("#00FF00"));
                }
                else{
                    Toast.makeText(getApplicationContext(), "No feature flag value found", Toast.LENGTH_SHORT).show();
                }

            }
        });



        ctNativeDisplayEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativeDisplayEventProp = new HashMap<String, Object>();
                nativeDisplayEventProp.put("BuyerPrice", 200);
                nativeDisplayEventProp.put("Item_name","Porsche 911 Carrera");
                nativeDisplayEventProp.put("image_url","https://db7hsdc8829us.cloudfront.net/dist/1510923264/i/6e7270cf071944109245a48cbe466c06.jpeg");
                CleverTapAPI.getDefaultInstance(getApplicationContext()).pushEvent("CT_Native_Display_Event",nativeDisplayEventProp);
            }
        });

        goToWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),WebviewActivity.class));
            }
        });
        ct_custom_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customEventProp = new HashMap<String, Object>();
                customEventProp.put("Name", "Nike Shoes");
                customEventProp.put("P1", 4000);
                customEventProp.put("Action","successful");
                cleverTapAPI.pushEvent("ct_Custom_Event",customEventProp);
            }
        });
        id_app_inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleverTapAPI.pushEvent("App_inbox_ct_event");
            }
        });
        ct_Movie_Watched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieProp = new HashMap<String, Object>();
                movieProp.put("Name", "Flash");
                movieProp.put("Season", 1);
                movieProp.put("episode",2);
                movieProp.put("action","successful");
                cleverTapAPI.pushEvent("ct_Movie_Watched",movieProp);
            }
        });

        nativeDisplay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CleverTapAPI.getDefaultInstance(getApplicationContext()).pushEvent("CT_Native_Display_Event2");
            }
        });
    }

    @Override
    public void inboxDidInitialize() {
        //Log.d(Tag5, String.valueOf(cleverTapAPI.getAllInboxMessages()));
        app_inbox_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> tabs = new ArrayList<>();
                tabs.add("Promotions");
                tabs.add("Offers");//We support upto 2 tabs only. Additional tabs will be ignored

                CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
                styleConfig.setFirstTabTitle("First Tab");
                styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
                styleConfig.setTabBackgroundColor("#FF0000");
                styleConfig.setSelectedTabIndicatorColor("#0000FF");
                styleConfig.setSelectedTabColor("#0000FF");
                styleConfig.setUnselectedTabColor("#FFFFFF");
                styleConfig.setBackButtonColor("#FF0000");
                styleConfig.setNavBarTitleColor("#FF0000");
                styleConfig.setNavBarTitle("MY INBOX");
                styleConfig.setNavBarColor("#FFFFFF");
                styleConfig.setInboxBackgroundColor("#ADD8E6");
                if(cleverTapAPI!=null) {
                    cleverTapAPI.showAppInbox(styleConfig);
                }
                else{
                    Toast.makeText(Homepage.this, "CT ID not found", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void inboxMessagesDidUpdate() {
      //  Log.d(Tag5, String.valueOf(cleverTapAPI.getAllInboxMessages()));
       // Log.d(Tag7, String.valueOf(cleverTapAPI.getInboxMessageForId("bed6d31")));
       // Toast.makeText(getApplicationContext(), "App Inbox updated, Unread count: "+cleverTapAPI.getInboxMessageUnreadCount()+", Total count: "+cleverTapAPI.getInboxMessageCount(), Toast.LENGTH_SHORT).show();
       // Log.d(Tag5, String.valueOf(cleverTapAPI.getAllInboxMessages()));

    }

    @Override
    public void onInboxButtonClick(HashMap<String, String> payload) {
        //To handle the Button click callbacks
        for(int i=0; i<payload.size();i++){
            Log.d(Tag3,payload.toString());
            Toast.makeText(getApplicationContext(), "App Inbox details:"+payload.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        // you will get display units here
        // implement your logic to create your display views using these Display Units here
        for (int i = 0; i <units.size() ; i++) {
            CleverTapDisplayUnit unit = units.get(i);
            prepareDisplayView(unit);
            Log.i("Native Diplay units", String.valueOf(units));
        }

    }

    private void prepareDisplayView(CleverTapDisplayUnit unit) {
        if(unit.getCustomExtras().containsKey("Nativekey1") && unit.getCustomExtras().containsValue("Red")){
            nativeDisplayResult.setBackgroundColor(Color.parseColor("#FF0000"));
            nativeDisplayResult.setText("Native Display Results: Key Found, Details:"+unit.getCustomExtras().toString());
            cleverTapAPI.pushDisplayUnitViewedEventForID(unit.getUnitID());
            cleverTapAPI.pushDisplayUnitClickedEventForID(unit.getUnitID());
            Log.d(Tag3,unit.toString());
            Toast.makeText(getApplicationContext(), "Units: "+unit.getContents().toString(), Toast.LENGTH_SHORT).show();
        }
        else if(unit.getCustomExtras().containsKey("Nativekey1") && unit.getCustomExtras().containsValue("Blue")){

            nativeDisplayResult.setBackgroundColor(Color.parseColor("#0000FF"));
            nativeDisplayResult.setText("Native Display Results: Key Found, Details:"+unit.getCustomExtras().toString());
            cleverTapAPI.pushDisplayUnitViewedEventForID(unit.getUnitID());
            cleverTapAPI.pushDisplayUnitClickedEventForID(unit.getUnitID());
            Log.d(Tag3,unit.toString());
            Toast.makeText(getApplicationContext(), "Units: "+unit.getContents().toString(), Toast.LENGTH_SHORT).show();
        }
        else if(unit.getCustomExtras().containsKey("custom_data") && unit.getCustomExtras().containsValue("110")){
            Toast.makeText(getApplicationContext(), "API Native display gets run", Toast.LENGTH_SHORT).show();

        }
        //else if(){
          // nativeDisplayResult.setBackgroundColor(Color.parseColor("#00FF00"));
            //nativeDisplayResult.setText("Native Display Results: Key Found, Details:"+unit.getCustomExtras().toString());
            //Log.d(Tag3,unit.toString());

           //Toast.makeText(getApplicationContext(), "Units: "+unit.getContents().toString(), Toast.LENGTH_SHORT).show();
        //}


    }

}