package com.deepak.localtravel;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class CabBook extends AppCompatActivity {
    private WebView cabBookWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_book);

        Intent intent = getIntent();
        String str = intent.getStringExtra("OLAURL");


        cabBookWeb = (WebView) findViewById(R.id.olaWebView);
        cabBookWeb.setWebViewClient(new MyWebViewClient());
        String url = "https://book.olacabs.com/?pickup_name=Current%20Location&lat=19.116979&lng=72.8822163&drop_lat=19.1176937&drop_lng=72.8652128";
        cabBookWeb.getSettings().setJavaScriptEnabled(true);
//        cabBookWeb.loadUrl(url);
        cabBookWeb.loadUrl(str);


    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


    }
}
