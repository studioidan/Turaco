package com.studioidan.turaco.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by PopApp_laptop on 19/11/2015.
 */
public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.Holder> {
    public int[] data;
    Context context;
    GalleryCallback callback;

    public AdapterGallery(Context c, int[] d) {
        this.data = d;
        this.context = c;
    }

    public AdapterGallery setCallback(GalleryCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_message, viewGroup, false);
        View v = new ImageView(context);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        holder.img.setImageResource(data[i]);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onImageClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView img;

        public Holder(View itemView) {
            super(itemView);
            img = (ImageView) itemView;
            ViewGroup.LayoutParams params = img.getLayoutParams();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120);
            lp.setMargins(3, 0, 3, 0);
            img.setLayoutParams(lp);
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    public interface GalleryCallback {
        void onImageClick(int pos);
    }
}


