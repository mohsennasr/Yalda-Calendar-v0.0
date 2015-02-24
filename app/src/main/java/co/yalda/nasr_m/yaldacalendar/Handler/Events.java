package co.yalda.nasr_m.yaldacalendar.Handler;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.ListViewAdapter;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class Events {

    private Context context;
    private Calendar cal = Calendar.getInstance();                   //event object date
    private View rootView;                  //root view
    private ListView eventListView;         //list view for showing events list
    private ArrayList<String> eventList;    //event list array
    private ListViewAdapter adapter;        //list view adapter
    private String[] eventClock = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00"
            , "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"
            , "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};

    public Events(Context context, Calendar cal, ListView eventListView) {
        this.context = context;
        this.cal = cal;
        this.eventListView = eventListView;
        initialize();
    }

    private void initialize() {

        //initiate event list array and list view
        eventList = new ArrayList<String>();

        //set list view adapter
//        eventListView.setAdapter(adapter);
//
//        eventList.addAll(Arrays.asList(eventClock));
//        adapter = new ListViewAdapter(eventList);
//        adapter.notifyDataSetChanged();
    }

    public ArrayList<String> getEventList() {
        return eventList;
    }

    public ListViewAdapter getAdapter() {
        return adapter;
    }
}
