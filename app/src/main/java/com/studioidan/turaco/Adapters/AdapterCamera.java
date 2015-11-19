package com.studioidan.turaco.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.Camera;

import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 18/11/2015.
 */
public class AdapterCamera extends BaseAdapter {
    ArrayList<Camera> data;
    Context con;
    LayoutInflater inflater;

    public AdapterCamera(ArrayList<Camera> data, Context con) {
        this.data = data;
        this.con = con;
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

        View vi = view;
        if (view == null)
            vi = inflater.inflate(R.layout.camera_item, null);

        TextView mText = ((TextView)vi.findViewById(R.id.tv_changecamer2camera2));
        mText.setText(item.name);

        return vi;
    }
}
