package com.deepak.localtravel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deepak.localtravel.Interface.ClickListener;
import com.deepak.localtravel.Model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityMyChoice extends AppCompatActivity {

    RecyclerView placeRecycle;
    FirebaseDatabase databse;
    DatabaseReference myRef;
    List<Place> list = new ArrayList<Place>();
    //List<String> listCategory = new ArrayList<String>();
    int i;

    private static final String TAG = "MyChoice";
    // for mumbai === private String[] category = {"Amusement Park", "Beaches", "Garden And Park", "Hotel", "Lakes", "Museums", "Religious Place", "Shopping Place", "Tourist attraction"};

    private String[] category = {"Amusement Park", "Garden And Park", "Hotel", "Lakes", "Museums", "Religious Place", "Shopping Place", "Tourist attraction"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_choice);

        placeRecycle = (RecyclerView) findViewById(R.id.recycleViewList);

        databse = FirebaseDatabase.getInstance();

        for(i = 0; i< category.length; i++ ) {
            myRef = databse.getReference(category[i]);


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {



                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                        Place value = dataSnap.getValue(Place.class);
                        Log.d(TAG, "VAlue obj" + value.getLng());
                        Place placeObj = new Place();
                        String name = value.getName();
                        //String map= value.getMap();
                        String map = value.getLng();
                        String description = value.getDescription();
                        String image = value.getImage();
                        String lat = value.getLat();
                        String lng = value.getLng();
                        String category = value.getCategory();
                        String favorite = value.getFavorite();
                        Log.d(TAG, "VAlue obj" + value.getLng());

                        if(value.getFavorite().equals("true")) {

                            placeObj.setName(name);
                            placeObj.setDescription(description);
                            placeObj.setImage(image);
                            placeObj.setMap(map);
                            placeObj.setLat(lat);
                            placeObj.setLng(lng);
                            placeObj.setFavorite(favorite);
                            placeObj.setCategory(category);




                            list.add(placeObj);
//                            Log.d(TAG,"List :"+listCategory);
                        }

                    }
                    //Log.d(TAG, "List :" + list);
                    RecyclerAdapterChoice rAdapter = new RecyclerAdapterChoice(list, ActivityMyChoice.this);
                    RecyclerView.LayoutManager recy = new GridLayoutManager(ActivityMyChoice.this, 1);
                    placeRecycle.setLayoutManager(recy);
                    placeRecycle.setItemAnimator(new DefaultItemAnimator());
                    placeRecycle.setAdapter(rAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Log.d("onCancelled":"Failed to read value");
                }


            });


        }

        placeRecycle.addOnItemTouchListener(new RecyclerTouchListener(this, placeRecycle, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {


                Button mapOnList = (Button) view.findViewById(R.id.mapOnList1);
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

                Button favButton = (Button) view.findViewById(R.id.favButton1);
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference category_fav = database.getReference(list.get(position).getCategory());
                        String label = list.get(position).getName();
                        Map<String,Object> taskMap = new HashMap<String,Object>();
                        taskMap.put("favorite", "false");
                        category_fav.child(label).updateChildren(taskMap);
                        Toast.makeText(ActivityMyChoice.this, "Removed From Favorite List : " + label,
                                Toast.LENGTH_SHORT).show();






                    }

                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




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



}
