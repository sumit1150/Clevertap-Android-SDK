package com.ct.ctoobdemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    CleverTapAPI cleverTapAPI;
    //UI Declaration
    EditText Name,PhoneNumber,Email,City,setPassword;
    CheckBox pushConsent;
    Button signUp;
    TextView login_link;
    Spinner spinner;
    //Variables
    String signup_Name,signup_PhoneNumber,signup_Email,signup_City,signup_setPassword,Gender_value,clevertapid;
    Boolean pushConsentCheck;
    //Profile Update hashmap
    HashMap<String, Object> profileUpdate;
    //Data Sharing between multiple Activities
    Intent dataSharing;
    //debugging
    private static final String Tag="SignUpActivity";
    //Shared Preference
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    ArrayAdapter<CharSequence>adapter;
    //Login Crendenrial
    //public static final String userIdentity = null;
    //public static final String userPassword=null;
    //Sign_up Event
    HashMap<String, Object> signUpAction;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);
        //UI Defination
        Name=findViewById(R.id.Name);
        Name.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        PhoneNumber=findViewById(R.id.PhoneNumber);
        PhoneNumber.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        Email=findViewById(R.id.email);
        Email.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        City=findViewById(R.id.City);
        City.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setPassword=findViewById(R.id.setPassword);
        setPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        pushConsent=findViewById(R.id.pushConsent);
        signUp=findViewById(R.id.signUp);
        login_link=findViewById(R.id.login_link);
        spinner=findViewById(R.id.spinner1);

        //Initilizing of Clevertap SDK
        cleverTapAPI=CleverTapAPI.getDefaultInstance(getApplicationContext());
        clevertapid=cleverTapAPI.getCleverTapID();
        Log.d(Tag,clevertapid);
        //Shared preference initilization
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //Push Consent
        if(pushConsent.isChecked()){
            pushConsentCheck=true;
        }else{
            pushConsentCheck=false;
        }
        Toast.makeText(getApplicationContext(), "Push Consent: "+pushConsentCheck, Toast.LENGTH_SHORT).show();

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Fetching values in variables;
                signup_Name=Name.getText().toString();
                Toast.makeText(getApplicationContext(), "Name: "+signup_Name, Toast.LENGTH_SHORT).show();
                signup_PhoneNumber=PhoneNumber.getText().toString();
                Toast.makeText(getApplicationContext(), "Phone: "+signup_PhoneNumber, Toast.LENGTH_SHORT).show();
                signup_Email=Email.getText().toString();
                Toast.makeText(getApplicationContext(), "Email: "+signup_Email, Toast.LENGTH_SHORT).show();
                signup_City=City.getText().toString();
                Toast.makeText(getApplicationContext(), "City: "+signup_City, Toast.LENGTH_SHORT).show();
                signup_setPassword=setPassword.getText().toString();
                Toast.makeText(getApplicationContext(), "Password: "+signup_setPassword, Toast.LENGTH_SHORT).show();

               // Log.d(Tag,signup_Name);
               // Log.d(Tag,signup_PhoneNumber);
               // Log.d(Tag,signup_Email);
               // Log.d(Tag,signup_City);
                //Log.d(Tag,signup_setPassword);

                //Here we will use CT onUserLoginMethod;
                //profilePushMethod();
                //Adding profile property
                adapter=ArrayAdapter.createFromResource(getApplicationContext(), R.array.gender_box, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(adapter);
                Gender_value=String.valueOf(spinner.getSelectedItem());
                //identity=signup_PhoneNumber.substring(0,6);
                // each of the below mentioned fields are optional
                profileUpdate= new HashMap<String, Object>();
                profileUpdate.put("Name", signup_Name);
                profileUpdate.put("Identity", signup_PhoneNumber);      // String or number
                profileUpdate.put("Email", signup_Email); // Email address of the user
                profileUpdate.put("Phone", "+91"+signup_PhoneNumber);   // Phone (with the country code, starting with +)
                profileUpdate.put("Gender", Gender_value);             // Can be either M or F
                profileUpdate.put("city1", signup_City);
                profileUpdate.put("MSG-email", true);        // Disable email notifications
                profileUpdate.put("MSG-push", pushConsentCheck);          // Enable push notifications
                profileUpdate.put("MSG-sms", false);          // Disable SMS notifications
                profileUpdate.put("MSG-whatsapp", false);// Enable WhatsApp notifications
                profileUpdate.put("TestIntValue", 10);// Enable WhatsApp notifications
                    Log.d(Tag,signup_Name);
                    Log.d(Tag,signup_PhoneNumber);
                   // Log.d(Tag,signup_Email);
                    Log.d(Tag,signup_City);
                    Log.d(Tag,signup_setPassword);
                    cleverTapAPI.onUserLogin(profileUpdate);
                Toast.makeText(getApplicationContext(), "Profile Created", Toast.LENGTH_SHORT).show();
                //Data Sharing Intent
                dataSharing = new Intent(getApplicationContext(), MainActivity.class);
                dataSharing.putExtra("userName",signup_PhoneNumber);
                dataSharing.putExtra("userPass",signup_setPassword);
                //signup_Email.equals("") ||
                if(signup_Name.equals("") ||
                        signup_City.equals("") ||signup_PhoneNumber.equals("") ||signup_setPassword.equals("")){

                    Toast.makeText(getApplicationContext(), "Enter the Details properly", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userIdentity", signup_PhoneNumber);
                    editor.putString("userPassword", signup_setPassword);
                    editor.apply();
                    //raising the event
                    signUpAction = new HashMap<String, Object>();
                    signUpAction.put("Action", "SignUp_Successful");
                    signUpAction.put("Identity", signup_PhoneNumber);
                    signUpAction.put("Date", new java.util.Date());
                    cleverTapAPI.pushEvent("SignUp", signUpAction);

                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                }
            }
        });

    }



}