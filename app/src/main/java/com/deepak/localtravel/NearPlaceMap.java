package com.deepak.localtravel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class NearPlaceMap extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    String mapCategory;
    private WebView mapWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_place_map);
        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Button button=(Button)findViewById(R.id.buttonSearch);
        mapWeb = (WebView) findViewById(R.id.mapWebView);

        Places.initialize(getApplicationContext(), getString(R.string.google_map_API_KEY));
        PlacesClient placesClient = Places.createClient(this);



        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Restaurants");
        categories.add("Hotels");
        categories.add("Bars");
        categories.add("Coffee");
        categories.add("Hospitals");
        categories.add("Banks");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                mapWeb.setWebViewClient(new NearPlaceMap.MyWebViewClient());
                mapWeb.getSettings().setJavaScriptEnabled(true);
                mapWeb.getSettings().setGeolocationEnabled(true);
                mapWeb.clearHistory();

                String url ="https://mobile.here.com/?x=ep";

               // String url = "https://www.google.com/maps/place/H+B+T+Trauma+Care+Municipal+Hospital/@19.1234086,72.882283,13z/data=!4m8!1m2!2m1!1sBanks!3m4!1s0x3be7b7cd70d3451f:0x3607631faadd83ae!8m2!3d19.1410329!4d72.8541183";
                mapWeb.getSettings().setJavaScriptEnabled(true);
//        cabBookWeb.loadUrl(url);
                mapWeb.loadUrl(url);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        mapCategory = item;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


    }

}
