package com.studioidan.turaco.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.PushLog;

import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 01/12/2015.
 */
public class AdapterPushLog extends BaseAdapter {
    ArrayList<PushLog> data;
    Context con;
    LayoutInflater inflater;

    public AdapterPushLog(ArrayList<PushLog> data, Context con) {
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
        PushLog item = data.get(i);
        View v = view;
        if (view == null)
            v = inflater.inflate(R.layout.view_push_log, null);

        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        TextView tvOpCode = (TextView) v.findViewById(R.id.tvOpCode);

        tvTitle.setText(item.date);
        tvOpCode.setText("Operation Code: " + item.opCode);
        return v;
    }
}
