package com.deepak.localtravel;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepak.localtravel.Interface.ClickListener;
import com.deepak.localtravel.MapActivityFolder.MapMainActivity;
import com.deepak.localtravel.Model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class ActivityPunePage extends AppCompatActivity implements View.OnClickListener {

    RecyclerView placeRecycle;
    FirebaseDatabase databse;
     DatabaseReference myRef;
    private Button googleMap;
    TextView textE;
    List<Category> list;

    private DrawerLayout mDrawerLayout;
    TextView nav_header_smart;

    private RecyclerView recyclerView;
    private NavigationView navigationView;

    private static final String TAG = "Pune_Page";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pune_page);
        checkPermission();

        //Declare Elements from Layout
        googleMap = (Button) findViewById(R.id.placeNear);
        textE = (TextView) findViewById(R.id.textExample);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        googleMap.setOnClickListener(this);
        textE.setText("Pune Life");

        View header = navigationView.getHeaderView(0);
        nav_header_smart = (TextView)header.findViewById(R.id.nav_header_smart);

        SharedPreferences sharePref = getSharedPreferences("user_login_details", Context.MODE_PRIVATE);
        String name_user_sh = sharePref.getString("name_shared_preferance","");
        nav_header_smart.setText(name_user_sh);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // set item as selected to persist highlight
                item.setChecked(true);
                switch (item.getItemId()){

                    case R.id.profile :
                        Intent intent = new Intent(ActivityPunePage.this, UserProfile.class);
                        startActivity(intent);
                        break;

                    case R.id.my_choice:
                        Intent intentChoice = new Intent(ActivityPunePage.this, ActivityMyChoice.class);
                        startActivity(intentChoice);
                        break;

                    case R.id.translatoy:

                        Intent intentTranslate = new Intent(ActivityPunePage.this, TextToSpeech.class);
                        startActivity(intentTranslate);
                        break;

                    case R.id.eventPune:

                        Intent intentEvent = new Intent(ActivityPunePage.this, EventMain.class);
                        startActivity(intentEvent);
                        break;

                    case R.id.log_out:
                        AlertDialog alertDialog = new AlertDialog.Builder(ActivityPunePage.this).create(); //Read Update
                        alertDialog.setTitle("Wan't to LogOut");
                        // alertDialog.setMessage("Wan't to LogOut");

                        alertDialog.setButton("OK..", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // here you can add functions
                                SharedPreferences sharePref = getSharedPreferences("user_login_details", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharePref.edit();

                                //Store false when caller ress logout
                                editor.putBoolean("active", false);
                                editor.putString("googleLogin", "");
                                editor.putString("name_shared_preferance", "");
                                editor.putString("phone_number_shared_preferance","");
                                editor.putString("email_shared_preferance","");
                                editor.apply();

                                //signout from google account
//                                Auth.GoogleSignInApi.signOut(googleMap).setResultCallback(new ResultCallback<Status>() {
//                                    @Override
//                                    public void onResult(@NonNull Status status) {
//
//                                    }
//                                });

                                //move to main activity
                                Intent intentTranslate = new Intent(ActivityPunePage.this, ActivityMainLogin.class);
                                startActivity(intentTranslate);


                            }
                        });

                        alertDialog.show();


                            }
                return false;
            }
                        });

        placeRecycle = (RecyclerView) findViewById(R.id.recycleView);

        databse = FirebaseDatabase.getInstance();
        myRef =  databse.getReference("Category");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = new ArrayList<Category>();

                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Category value = dataSnap.getValue(Category.class);
                    Category placeObj = new Category();
                    String name = value.getName();
                    String image = value.getImage();
                    placeObj.setName(name);
                    placeObj.setImage(image);


                    list.add(placeObj);
                    Log.d(TAG,"snapshot: "+name + image);
                }

                RecyclerAdapterPunePage rAdapter = new RecyclerAdapterPunePage(list, ActivityPunePage.this);
                RecyclerView.LayoutManager recy = new GridLayoutManager(ActivityPunePage.this,1);
                placeRecycle.setLayoutManager(recy);
                placeRecycle.setItemAnimator(new DefaultItemAnimator());
                placeRecycle.setAdapter(rAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.d("onCancelled":"Failed to read value");
            }
        });

        placeRecycle.addOnItemTouchListener(new RecyclerTouchListener(this, placeRecycle, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                methodForClick(view, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Values are passing to activity & to fragment as well
                methodForClick(view, position);
            }
        }));

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.placeNear:
                Intent placeMap = new Intent(ActivityPunePage.this, MapMainActivity.class);
                startActivity(placeMap);
                break;


        }

    }


    public void methodForClick(View view, int position){
        final TextView picture=(TextView) view.findViewById(R.id.categoryName);
        final ImageView categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext() , ActivityPunePlacesList.class);
                intent.putExtra("categoryValue",picture.getText().toString());
                startActivity(intent);
            }
        });

        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , ActivityPunePlacesList.class);
                intent.putExtra("categoryValue",picture.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

    private ClickListener clicklistener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

        this.clicklistener=clicklistener;
        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && clicklistener!=null){
                    clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
            clicklistener.onClick(child,rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

//only check permission for audio record
private void checkPermission(){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        if(!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)){

            Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
startActivity(intent);
finish();

        }
    }
}


}