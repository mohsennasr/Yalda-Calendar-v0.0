package co.yalda.nasr_m.yaldacalendar.Day;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.EventListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Handler.Notes;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class DayListView extends Fragment {

    private DayUC dayUC;                    //day user control object
    private View rootView;
    private RelativeLayout rootLayout;      //root layout of UC. UC will be linked to this layout

    //List View Day Mode attributes
    private TextView dayDateTV;             //date text view
    private TextView dayDayNameTV;          //day name text view
    private ListView dayEventLV;            //day events list view
    private FrameLayout dayListHeaderFrame; //day notes frame view
    private Notes noteList;                 //day notes
//    private Events eventList;               //day events
    private Button addButton;               //add button for adding new note or event

    private ArrayList<String> eventList;    //event list array
    private EventListViewAdapter adapter;        //list view adapter
    private String[] eventClock = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00"
            , "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"
            , "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00"};

    /*
    Fragment Classes have their own constructor method that we can't modify input parameter.
    So, for making Fragment Class with custom input value new method should be written to create
    class objects and set private attributes according to input values
     */
    public static DayListView newInstance(Calendar dayCal){
        DayListView dayListView = new DayListView();
//        dayListView.dayUC = DayUC.newInstance(dayCal, true, MainActivity.viewMode.DayHeader);
//        dayListView.dayUC = new DayUC(dayCal);
        return dayListView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.day_list_mode_view, container, false);

        initialDay();

        return rootView;
    }

    //initial attributes
    private void initialDay(){

//        dayUC = (DayUC) getFragmentManager().findFragmentByTag("DayList");
//        if (dayUC == null){
            dayUC = DayUC.newInstance(Calendar.getInstance(), true, MainActivity.viewMode.DayHeader);
//            dayUC.setTargetFragment(this, 0);
//            getFragmentManager().beginTransaction().add(dayUC, "DayList").commit();
//        }

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUC).commit();


        dayEventLV = (ListView) rootView.findViewById(R.id.day_list_mode_event_list);
        //initiate event list array and list view
        eventList = new ArrayList<String>();

        eventList.addAll(Arrays.asList(eventClock));
        adapter = new EventListViewAdapter(getActivity(), eventList);

        //set list view adapter
        dayEventLV.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    //set values for elements
    public void setData(){
        dayDateTV.setText("28");
        dayDayNameTV.setText("Tuesday");
    }
}
