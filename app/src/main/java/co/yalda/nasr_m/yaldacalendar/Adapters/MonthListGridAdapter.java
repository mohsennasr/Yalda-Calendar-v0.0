package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 3/1/2015.
 */
/*
monthListGridAdapter is for showing month selection grid in month second view
 */
public class MonthListGridAdapter extends BaseAdapter{

    ArrayList<String> monthList;
    int currentPosition;

    public MonthListGridAdapter(ArrayList<String> monthList, int currentPosition) {
        this.monthList = monthList;
        this.currentPosition = currentPosition;
    }

    @Override
    public int getCount() {
        return monthList.size();
    }

    @Override
    public Object getItem(int position) {
        return monthList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    /*
    create items for showing in grid
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.simple_list_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.simple_text);
        item.setText(monthList.get(position));
        item.setTextSize(32);
        item.setTypeface(MainActivity.homaFont);

        /*
        resize items base on screen orientation and screen size to fill screen
         */
        convertView.setLayoutParams(new AbsListView.LayoutParams(MainActivity.viewSize[0] /
                (MainActivity.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 4),
                (MainActivity.viewSize[1] - 200) /
                (MainActivity.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 4 : 3))
        );

        if (position == currentPosition)                    //make selected cell if it's showing current month
            convertView.setBackgroundResource(R.drawable.background_rectangle);

        return convertView;
    }

    public void setSelected(int position){
        currentPosition = position;
        notifyDataSetChanged();
    }
}
