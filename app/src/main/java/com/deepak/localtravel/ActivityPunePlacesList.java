package com.deepak.localtravel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deepak.localtravel.Interface.ClickListener;
import com.deepak.localtravel.Model.Place;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class ActivityPunePlacesList extends AppCompatActivity implements View.OnClickListener, LocationListener {

    RecyclerView placeRecycle;
    FirebaseDatabase databse;
    DatabaseReference myRef;
    List<Place> list;
    TextView categoryName;

    private LocationManager locationManager;

    Double locationLat, locationLang;
    private static final String TAG = "Pune_places_list";

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pune_places_list);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);



        final String catName = getIntent().getStringExtra("categoryValue");
        Log.d(TAG, "catName"+catName);

        categoryName = (TextView) findViewById(R.id.categoryName);
        categoryName.setText(catName);

        placeRecycle = (RecyclerView) findViewById(R.id.recycleViewList);

        databse = FirebaseDatabase.getInstance();
        myRef =  databse.getReference(catName);

        //ArrayHolder.cat1[0]="a";




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = new ArrayList<Place>();

                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Place value = dataSnap.getValue(Place.class);
                    Log.d(TAG, "Value obj"+value.getFavorite());
                    Place placeObj = new Place();
                    String name = value.getName();
                    //String map= value.getMap();
                    String map= value.getLng();
                    String description = value.getDescription();
                    String image = value.getImage();
                    String lat = value.getLat();
                    String lng = value.getLng();
                    String category = value.getCategory();
                    String favorite = value.getFavorite();



                    placeObj.setName(name);
                    placeObj.setDescription(description);
                    placeObj.setImage(image);
                    placeObj.setMap(map);
                    placeObj.setLat(lat);
                    placeObj.setLng(lng);
                    placeObj.setCategory(category);
                    placeObj.setFavorite(favorite);

                    list.add(placeObj);

                }
                Log.d(TAG,"List :"+list);
                RecyclerAdapterPunePlace rAdapter = new RecyclerAdapterPunePlace(list, ActivityPunePlacesList.this);
                RecyclerView.LayoutManager recy = new GridLayoutManager(ActivityPunePlacesList.this,1);
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


                        Button mapOnList = (Button) view.findViewById(R.id.mapOnList);
                        mapOnList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(mumbai_place_list.this, "Single Click on Image :"+list.get(position).getName(),
//                                        Toast.LENGTH_SHORT).show();
                                list.get(position).getLat();
                                double latitude = Double.parseDouble(list.get(position).getLat());
                                double longitude = Double.parseDouble(list.get(position).getLng());
                                String label = list.get(position).getName();
                                String uriBegin = "geo:" + latitude + "," + longitude;
                                String query = latitude + "," + longitude + "(" + label + ")";
                                String encodedQuery = Uri.encode(query);
                                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                                Uri uri = Uri.parse(uriString);
                                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                                startActivity(mapIntent);
                            }
                        });

                Button favButton = (Button) view.findViewById(R.id.favButton);
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference category_fav = database.getReference(catName);
                        String label = list.get(position).getName();
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("favorite", "true");
                        category_fav.child(label).updateChildren(taskMap);
                        Toast.makeText(ActivityPunePlacesList.this, "Added Into Favorite List : " + label,
                                Toast.LENGTH_SHORT).show();




                    }
                });

                Button olaButton = (Button) view.findViewById(R.id.ola);
                olaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        double latitude = Double.parseDouble(list.get(position).getLat());
                        double longitude = Double.parseDouble(list.get(position).getLng());

                        String olaUrl = "https://book.olacabs.com/?pickup_name=Current%20Location&lat="+locationLat+"&lng="+locationLang+"&drop_lat="+latitude+"&drop_lng="+longitude;
                        Intent intent = new Intent(getBaseContext() , CabBook.class);
                        intent.putExtra("OLAURL",olaUrl);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLocationChanged(Location location) {
        locationLat = location.getLatitude();
        locationLang = location.getLongitude();

        Log.d(TAG, "locationLan"+ locationLat+"---"+locationLang);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

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




}

