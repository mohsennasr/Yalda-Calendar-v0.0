package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Handler.Events;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/18/2015.
 */
public class DayListViewAdapter extends BaseAdapter {

//    private ArrayList<String> timeList;
//    private HashMap<String, List<Events>> eventList;
    private ArrayList<Events> eventListArray;
    private LayoutInflater mInflater;
    private Context context;

    public DayListViewAdapter(ArrayList<Events> eventListArray, Context context) {
        this.eventListArray = eventListArray;
        this.context = context;
        this.mInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventListArray.size();
    }

    @Override
    public Object getItem(int position) {
        return eventListArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_view, null);

            TextView startTime = (TextView) convertView.findViewById(R.id.event_start_time);
            TextView endTime = (TextView) convertView.findViewById(R.id.event_end_time);
            TextView eventTitle = (TextView) convertView.findViewById(R.id.event_title);
            TextView eventDetail = (TextView) convertView.findViewById(R.id.event_description);

            startTime.setText(eventListArray.get(position).getStartTime());
            endTime.setText(eventListArray.get(position).getEndTime());
            eventTitle.setText(eventListArray.get(position).getTitle());
            eventDetail.setText(eventListArray.get(position).getDescription());

//            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.event_item_layout);
//            for (int i = 0; i < eventList.get(timeList.get(position)).size(); i++) {
//                if (eventList.get(timeList.get(position)).get(i).rootView.getParent() == null)
//                    layout.addView(eventList.get(timeList.get(position)).get(i).rootView);
//            }

//            ListView listView = (ListView) convertView.findViewById(R.id.event_item_event_list);
//            ArrayList<Events> lists = new ArrayList<>();
//            for (int i = 0; i < eventList.get(timeList.get(position)).size(); i++) {
//                lists.add(eventList.get(timeList.get(position)).get(i));
//            }
//            EventListViewAdapter adapter = new EventListViewAdapter(lists);
//            listView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();

        }
        return convertView;
    }
}
