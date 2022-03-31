package com.ct.ctoobdemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.clevertap.android.sdk.CTWebInterface;
import com.clevertap.android.sdk.CleverTapAPI;

public class WebviewActivity extends AppCompatActivity {

    WebView webActivity;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webActivity=findViewById(R.id.webActivity);
        webSettings = webActivity.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        //webActivity.loadUrl("file:///android_asset/webpagedemo.html");
        webActivity.addJavascriptInterface(new CTWebInterface(CleverTapAPI.getDefaultInstance(this)),"CleverTap");
        webActivity.loadUrl("http://sumit123.c1.biz/webviewdemo.html");

    }
}