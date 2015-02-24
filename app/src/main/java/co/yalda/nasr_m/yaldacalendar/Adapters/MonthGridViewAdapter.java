package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthGridViewAdapter extends BaseAdapter {

    private ArrayList<DayUC> gridList;      //list of grid DayUC objects
    private ArrayList<String> weekGridList;      //list of grid DayUC objects

    public MonthGridViewAdapter(ArrayList<DayUC> gridList, ArrayList<String> weekGridList) {
        this.gridList = gridList;
        this.weekGridList = weekGridList;
    }

    @Override
    public int getCount() {
        return gridList.size() + weekGridList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position%8 == 0)
            return weekGridList.get(position/8);
        return gridList.get((position/8)*7 + position%8 - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position%8 == 0){
            if (convertView == null){
                LayoutInflater minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = minflater.inflate(R.layout.simple_list_item, null);
            }
            TextView item = (TextView) convertView.findViewById(R.id.simple_text);
            item.setText(weekGridList.get(position/8));
            item.setGravity(View.TEXT_ALIGNMENT_CENTER);
            item.setTextColor(Color.BLACK);
            return convertView;
        }
        return gridList.get((position/8)*7 + position%8 - 1).rootView;
    }
}
