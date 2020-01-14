package com.deepak.localtravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deepak.localtravel.Interface.ClickListener;
import com.deepak.localtravel.Model.Event;
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

public class EventMain extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase databseEvent;
    DatabaseReference myRefEvent;
    RecyclerView eventRecycle;
    List<Event> list;


    private static final String TAG = "EventMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_main);

        eventRecycle = (RecyclerView) findViewById(R.id.recycleEventViewList);

        databseEvent = FirebaseDatabase.getInstance();
        myRefEvent =  databseEvent.getReference("Events");

        myRefEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = new ArrayList<Event>();

                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Event eventValue = dataSnap.getValue(Event.class);
                    Log.d(TAG, "Value obj"+eventValue.getName());
                    Event eventObj = new Event();
                    String name = eventValue.getName();
                    String description = eventValue.getDescription();
                    String image = eventValue.getImage();
                    String lat = eventValue.getLat();
                    String lng = eventValue.getLang();
                    String id = eventValue.getEventId();

                    eventObj.setName(name);
                    eventObj.setDescription(description);
                    eventObj.setImage(image);
                    eventObj.setLat(lat);
                    eventObj.setLang(lng);
                    eventObj.setEventId(id);

                    list.add(eventObj);

                }
                Log.d(TAG,"List :"+list);
                RecyclerEvent rAdapter1 = new RecyclerEvent(list, EventMain.this);
                RecyclerView.LayoutManager recy = new GridLayoutManager(EventMain.this, 1);
               // RecyclerEvent rAdapter = new RecyclerEvent(list, EventMain.this);
                //RecyclerView.LayoutManager recy = new GridLayoutManager(EventMain.this,1);
                eventRecycle.setLayoutManager(recy);
                eventRecycle.setItemAnimator(new DefaultItemAnimator());
                eventRecycle.setAdapter(rAdapter1);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.d("onCancelled":"Failed to read value");
            }
        });

        eventRecycle.addOnItemTouchListener(new ActivityPunePlacesList.RecyclerTouchListener(this, eventRecycle, new ClickListener() {

            @Override
            public void onClick(View view, final int position) {


                Button mapOnList = (Button) view.findViewById(R.id.showOnMap);
                mapOnList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                                Toast.makeText(mumbai_place_list.this, "Single Click on Image :"+list.get(position).getName(),
//                                        Toast.LENGTH_SHORT).show();
                        list.get(position).getLang();
                        double latitude = Double.parseDouble(list.get(position).getLat());
                        double longitude = Double.parseDouble(list.get(position).getLang());
                       String label = list.get(position).getName().toString();
                        String uriBegin = "geo:" + latitude + "," + longitude;
                        String query = latitude + "," + longitude + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        startActivity(mapIntent);
                    }
                });



//                Button olaButton = (Button) view.findViewById(R.id.ola);
//                olaButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        double latitude = Double.parseDouble(list.get(position).getLat());
//                        double longitude = Double.parseDouble(list.get(position).getLng());
//
//                        String olaUrl = "https://book.olacabs.com/?pickup_name=Current%20Location&lat="+locationLat+"&lng="+locationLang+"&drop_lat="+latitude+"&drop_lng="+longitude;
//                        Intent intent = new Intent(getBaseContext() , CabBook.class);
//                        intent.putExtra("OLAURL",olaUrl);
//                        startActivity(intent);
//                    }
//                });

            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));

    }

    @Override
    public void onClick(View v) {

    }
}
