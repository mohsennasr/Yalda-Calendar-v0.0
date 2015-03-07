package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.homaFont;

/**
 * Created by Nasr_M on 2/23/2015.
 */
/*
grid view adapter for week days name in month view
 */
public class WeekDaysAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private MainActivity.dayViewMode view;

    public WeekDaysAdapter(ArrayList<String> list, MainActivity.dayViewMode view) {
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

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.simple_list_item, null);

            TextView itemText = (TextView) convertView.findViewById(R.id.simple_text);
            itemText.setText(list.get(position));
            itemText.setTextAppearance(context, R.style.BoldText);
            if (view == MainActivity.dayViewMode.Month) {
                itemText.setTextSize(24);
                itemText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60));
            } else {
                itemText.setTextSize(12);
                itemText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            }
            itemText.setTypeface(homaFont);
        }

        return convertView;
    }
}
