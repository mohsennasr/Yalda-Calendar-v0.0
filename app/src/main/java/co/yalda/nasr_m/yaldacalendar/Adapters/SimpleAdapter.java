package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.simpleListViewMode;

/**
 * Created by Nasr_M on 2/23/2015.
 */
public class SimpleAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private simpleListViewMode view;

    public SimpleAdapter(ArrayList<String> list, simpleListViewMode view) {
        this.list = list;
        this.view = view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = minflater.inflate(R.layout.simple_list_item, null);

//        LinearLayout simpleRoot = (LinearLayout) convertView.findViewById(R.id.simple_layout);

//        TextView item = new TextView(context);
//        ViewGroup.LayoutParams dayNameParams = new ViewGroup.LayoutParams(80,40);
//        ViewGroup.LayoutParams weekNumParams = new ViewGroup.LayoutParams(40,80);

        TextView itemText = (TextView) convertView.findViewById(R.id.simple_text);
        itemText.setText(list.get(position));
        itemText.setTextColor(Color.BLACK);

//        switch (view){
//            case MonthWeekDays:
//                item.setLayoutParams(dayNameParams);
//                break;
//            case MonthWeekNumbers:
//                item.setLayoutParams(weekNumParams);
//                break;
//            case YearWeekDays:
//                itemText.setMinHeight(20);
//                itemText.setMinWidth(20);
//                break;
//            case YearWeekNumbers:
//                itemText.setMinHeight(20);
//                itemText.setMinWidth(20);
//                break;
//        }

//        item.setText(list.get(position));
//        item.setVisibility(View.VISIBLE);
//        item.setTextColor(Color.BLACK);
//        item.setGravity(View.TEXT_ALIGNMENT_CENTER);
//
//        simpleRoot.addView(item);

        return convertView;
    }
}
