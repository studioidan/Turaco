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

    public AdapterCamera(Context con, ArrayList<Camera> d) {
        context = con;
        this.data = d;
        playingItem = -100;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        Camera item = data.get(i);

        View v = view;
        if (view == null)
            v = inflater.inflate(R.layout.camera_list_item, null);

        ImageView img = (ImageView) v.findViewById(R.id.vd_camera_list_item);
        if (item.image != 0)
            img.setImageResource(item.image);
        if (item.imageUrl != null && !item.imageUrl.isEmpty()) {
            Picasso.with(context)
                    .load(item.imageUrl)
                    .into(img);
        }

        TextView tvName = (TextView) v.findViewById(R.id.lbl_camera_list_item);
        tvName.setText(item.name);

        ImageView imgIsPlayingIndicator = (ImageView) v.findViewById(R.id.im_curreuntplaying);
        imgIsPlayingIndicator.setVisibility(playingItem == i ? View.VISIBLE : View.INVISIBLE);

        return v;
    }

    public void setPlayingItem(int playingItem) {
        this.playingItem = playingItem;
        notifyDataSetChanged();
    }
}