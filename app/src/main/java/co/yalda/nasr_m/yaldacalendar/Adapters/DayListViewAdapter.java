package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
public class DayListViewAdapter extends BaseAdapter {

    private ArrayList<String> timeList;
    private HashMap<String, List<Events>> eventList;
    private LayoutInflater mInflater;
    private Context context;

    public DayListViewAdapter(ArrayList<String> timeList, HashMap<String, List<Events>> eventList, Context context) {
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_list_item, null);

            TextView time = (TextView) convertView.findViewById(R.id.event_item_text);
            time.setText(timeList.get(position));

//            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.event_item_layout);
//            for (int i = 0; i < eventList.get(timeList.get(position)).size(); i++) {
//                if (eventList.get(timeList.get(position)).get(i).rootView.getParent() == null)
//                    layout.addView(eventList.get(timeList.get(position)).get(i).rootView);
//            }

            ListView listView = (ListView) convertView.findViewById(R.id.event_item_event_list);
            ArrayList<Events> lists = new ArrayList<>();
            for (int i = 0; i < eventList.get(timeList.get(position)).size(); i++) {
                lists.add(eventList.get(timeList.get(position)).get(i));
            }
            EventListViewAdapter adapter = new EventListViewAdapter(lists);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            final View finalConvertView = convertView;
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    int action = event.getAction();
//                    switch (action) {
//                        case MotionEvent.ACTION_DOWN:
//                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
//                            break;
//
//                        case MotionEvent.ACTION_UP:
//                            // Allow ScrollView to intercept touch events.
//                            finalConvertView.getParent().requestDisallowInterceptTouchEvent(false);
//                            break;
//                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return false;
                }
            });
        }
        return convertView;
    }
}
