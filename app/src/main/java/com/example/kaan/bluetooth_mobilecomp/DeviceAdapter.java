package com.example.kaan.bluetooth_mobilecomp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kaan on 11/9/2015.
 */
public class DeviceAdapter extends ArrayAdapter<Device> {

    public DeviceAdapter(Context context, ArrayList<Device> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item at this pos
        Device d = getItem(position);
        //check if being reused, otherwise inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // get the view
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        nameTextView.setText(d.name);

        return convertView;
    }
}
