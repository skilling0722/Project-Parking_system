package com.example.parkingsystem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class listview_adapter extends BaseAdapter {
    private ArrayList<listview_item > listview_item_list = new ArrayList<listview_item >();

    public listview_adapter(ArrayList listview_item_list) {

    }

    public listview_adapter() {

    }

    @Override
    public int getCount() {
        return listview_item_list.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        /*각 item을 생성하고 거기에 값을 넣는다. */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.check_listview_item, parent, false);
        }

        ImageView imgview = (ImageView) convertView.findViewById(R.id.item_img);
        TextView spotview = (TextView) convertView.findViewById(R.id.item_spot) ;
        TextView remainview = (TextView) convertView.findViewById(R.id.item_remain) ;

        listview_item listViewItem = listview_item_list.get(position);

        imgview.setImageDrawable(listViewItem.getIcon());
        spotview.setText(listViewItem.getSpot_title());
        remainview.setText(listViewItem.getRemain_title());

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listview_item_list.get(position) ;
    }

    public void addItem(Drawable icon, String title, String text) {
        listview_item item = new listview_item();

        item.setIcon(icon);
        item.setSpot_title(title);
        item.setRemain_title(text);

        listview_item_list.add(item);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }



}
