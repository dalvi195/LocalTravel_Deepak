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


import androidx.recyclerview.widget.RecyclerView;

import com.deepak.localtravel.Model.Place;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class RecyclerAdapterPunePlace extends RecyclerView.Adapter<RecyclerAdapterPunePlace.MyHoder> {

    List<Place> list;
    Context context;

    public RecyclerAdapterPunePlace(List<Place> list, Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.places,parent,false);
        MyHoder myHoder = new MyHoder(view);
        return myHoder;
    }
    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
        Place mylist = list.get(position);
        String url = mylist.getImage();
        //new DownLoadImageTask(holder.placeImg).execute(url);
        holder.name.setText(mylist.getName());
        holder.description.setText(mylist.getDescription());
        PicassoClient.downloadImage(context, url, holder.placeImg);
       // holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.grey_star));

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

        TextView name,description;
        public ImageView placeImg;
        Button placeMap;
        //ToggleButton toggleButton;


        public MyHoder(View itemView) {
            super(itemView);

            placeImg = (ImageView) itemView.findViewById(R.id.placeImg);
            name = (TextView) itemView.findViewById(R.id.placeName);
            description = (TextView) itemView.findViewById(R.id.placeDescription);


        }
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }


        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }


}
