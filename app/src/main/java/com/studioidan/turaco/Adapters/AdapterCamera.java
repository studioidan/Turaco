package com.studioidan.turaco.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.Camera;

import java.util.ArrayList;

public class AdapterCamera extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    ArrayList<Camera> data;

    private int playingItem;
    CameraItemCallback callback;

    public AdapterCamera(Context con, ArrayList<Camera> d, CameraItemCallback callback) {
        context = con;
        this.data = d;
        playingItem = -100;
        this.callback = callback;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Camera item = data.get(i);

        View v = view;
        if (view == null)
            v = inflater.inflate(R.layout.camera_list_item, null);

        ImageView img = (ImageView) v.findViewById(R.id.vd_camera_list_item);
        img.setVisibility((item.videoUrl == null || item.videoUrl.isEmpty()) ? View.INVISIBLE : View.VISIBLE);

        if (item.image != 0) {
            img.setImageResource(item.image);
        }
        if (item.imageUrl != null && !item.imageUrl.isEmpty()) {
            Picasso.with(context)
                    .load(item.imageUrl)
                    .into(img);
        }

        ImageView imgStatic = (ImageView) v.findViewById(R.id.imgStatic);
        if (item.staticImageUrl.isEmpty()) {
            imgStatic.setVisibility(View.INVISIBLE);
        } else {
            imgStatic.setVisibility(View.VISIBLE);
            imgStatic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onStaticImageClick(item, i);
                }
            });
        }

        TextView tvName = (TextView) v.findViewById(R.id.lbl_camera_list_item);
        tvName.setText(item.name);

        ImageView imgIsPlayingIndicator = (ImageView) v.findViewById(R.id.im_curreuntplaying);
        imgIsPlayingIndicator.setVisibility(playingItem == i ? View.VISIBLE : View.INVISIBLE);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onItemClick(item, i);
            }
        });
        return v;
    }

    public void setPlayingItem(int playingItem) {
        this.playingItem = playingItem;
        notifyDataSetChanged();
    }

    public interface CameraItemCallback {
        void onStaticImageClick(Camera camera, int position);

        void onItemClick(Camera camera, int position);
    }
}