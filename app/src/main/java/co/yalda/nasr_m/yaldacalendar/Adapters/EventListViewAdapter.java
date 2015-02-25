package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Handler.Events;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/18/2015.
 */
public class EventListViewAdapter extends BaseAdapter {

    private ArrayList<String> timeList;
    private HashMap<String, List<Events>> eventList;
    private LayoutInflater mInflater;
    private Context context;

    public EventListViewAdapter(ArrayList<String> timeList, HashMap<String, List<Events>> eventList, Context context) {
        this.timeList = timeList;
        Collections.sort(this.timeList);
        this.context = context;
        this.eventList = eventList;
        this.mInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.event_list_item, null);

        TextView time = (TextView) convertView.findViewById(R.id.event_item_text);
        time.setText(timeList.get(position));

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.event_item_event_frame);
        for (int i = 0; i < eventList.get(timeList.get(position)).size(); i++) {
            layout.addView(eventList.get(timeList.get(position)).get(i).rootView);
        }

        return convertView;
    }
}
