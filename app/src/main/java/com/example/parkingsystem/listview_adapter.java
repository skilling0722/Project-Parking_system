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

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.check_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imgview = (ImageView) convertView.findViewById(R.id.item_img);
        TextView spotview = (TextView) convertView.findViewById(R.id.item_spot) ;
        TextView remainview = (TextView) convertView.findViewById(R.id.item_remain) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        listview_item listViewItem = listview_item_list.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        imgview.setImageDrawable(listViewItem.getIcon());
        spotview.setText(listViewItem.getSpot_title());
        remainview.setText(listViewItem.getRemain_title());

        return convertView;

    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listview_item_list.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String title, String text) {
        listview_item item = new listview_item();

        item.setIcon(icon);
        item.setSpot_title(title);
        item.setRemain_title(text);

        listview_item_list.add(item);
    }

}
