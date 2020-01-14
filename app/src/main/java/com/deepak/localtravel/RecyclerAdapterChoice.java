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


public class RecyclerAdapterChoice extends RecyclerView.Adapter<RecyclerAdapterChoice.MyViewHolder> {

    List<Place> list;
    Context context;

    public RecyclerAdapterChoice(List<Place> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_my_choice_recycler_view,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,description;
        public ImageView placeImg;
        Button placeMap;
        //ToggleButton toggleButton;


        public MyViewHolder(View itemView) {
            super(itemView);

            placeImg = (ImageView) itemView.findViewById(R.id.placeImg1);
            name = (TextView) itemView.findViewById(R.id.placeName1);
            description = (TextView) itemView.findViewById(R.id.placeDescription1);


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
