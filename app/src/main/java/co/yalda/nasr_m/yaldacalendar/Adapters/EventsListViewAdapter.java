package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Handler.Events;

/**
 * Created by Nasr_M on 2/18/2015.
 */

/*
Adapter for event list view in DayList view mode
 */
public class EventsListViewAdapter extends BaseAdapter {
    private ArrayList<Events> eventListArray;           //events array list
    private Context context;

    public EventsListViewAdapter(ArrayList<Events> eventListArray, Context context) {
        this.eventListArray = eventListArray;
        this.context = context;
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
        //return event view to show in list view
        return eventListArray.get(position).rootView;
    }
}
