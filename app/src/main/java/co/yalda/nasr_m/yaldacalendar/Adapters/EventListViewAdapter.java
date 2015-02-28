package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Handler.Events;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class EventListViewAdapter extends BaseAdapter {

    private ArrayList<Events> list;

    public EventListViewAdapter(ArrayList<Events> list) {
        this.list = list;
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
        return list.get(position).rootView;
    }
}
