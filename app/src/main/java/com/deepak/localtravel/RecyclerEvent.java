package com.deepak.localtravel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepak.localtravel.Model.Event;
import com.deepak.localtravel.Model.Place;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RecyclerEvent  extends RecyclerView.Adapter<RecyclerEvent.MyHoder> {

    List<Event> list;
    Context context;

    public RecyclerEvent(List<Event> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_recycler_view,parent,false);
        MyHoder myHoder = new MyHoder(view);
        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {

        Event eventList = list.get(position);
        String url = eventList.getImage();
        //new DownLoadImageTask(holder.placeImg).execute(url);
        holder.eventName.setText(eventList.getName());
        holder.eventDescription.setText(eventList.getDescription());
        PicassoClient.downloadImage(context, url, holder.eventImg);

    }

    @Override
    public int getItemCount() {

        int arr = 0;
        try{
            if(list.size()==0){
                arr = 0;
            }
            else{
                arr=list.size();
            }
        }catch (Exception e){
        }
        return arr;
    }

    class MyHoder extends RecyclerView.ViewHolder{

        TextView eventName,eventDescription;
        public ImageView eventImg;

        public MyHoder(View itemView) {
            super(itemView);

            eventImg = (ImageView) itemView.findViewById(R.id.eventImg);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            eventDescription = (TextView) itemView.findViewById(R.id.eventDesc);


        }
    }


}

