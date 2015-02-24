package co.yalda.nasr_m.yaldacalendar.Day;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.EventListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Handler.Notes;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewMode.DayHeader;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class DayListView extends Fragment implements View.OnTouchListener{

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

    private ArrayList<String> eventList;    //event list array
    private EventListViewAdapter adapter;        //list view adapter
    private String[] eventClock = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00"
            , "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"
            , "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00"};

    private float[] startPoint = new float[4],
            endPoint = new float[4],
            distance = new float[2];

    /*
    Fragment Classes have their own constructor method that we can't modify input parameter.
    So, for making Fragment Class with custom input value new method should be written to create
    class objects and set private attributes according to input values
     */
    public static DayListView newInstance(Calendar dayCal) {
        DayListView dayListView = new DayListView();

        return dayListView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.day_list_mode_view, container, false);

        initialDay();

        return rootView;
    }

    //initial attributes
    private void initialDay() {
        dayEventLV = (ListView) rootView.findViewById(R.id.day_list_mode_event_list);
        //initiate event list array and list view
        eventList = new ArrayList<String>();

        adapter = new EventListViewAdapter(eventList, getActivity());

        //set list view adapter
        dayEventLV.setAdapter(adapter);

        setData();

    }

    //set values for elements
    public void setData() {
        dayUC = DayUC.newInstance(originalSelectedDate, false, DayHeader);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUC).commit();
        eventList.addAll(Arrays.asList(eventClock));
        adapter.notifyDataSetChanged();

        // TODO Event List should be derived from DB
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String swipeDirection = "";
        if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            startPoint[0] = event.getX(0);
            startPoint[1] = event.getY(0);
            startPoint[2] = event.getX(1);
            startPoint[3] = event.getY(1);
        }else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
            endPoint[0] = event.getX(0);
            endPoint[1] = event.getY(0);
            endPoint[2] = event.getX(1);
            endPoint[3] = event.getY(1);

            //start distance
            distance[0] = (float) Math.sqrt(Math.pow ((startPoint[0] - startPoint[2]), 2)
                    + Math.pow ((startPoint[1] - startPoint[3]), 2));

            //end distance
            distance[1] = (float) Math.sqrt(Math.pow ((endPoint[0] - endPoint[2]), 2)
                    + Math.pow ((endPoint[1] - startPoint[3]), 2));

            if (distance[0] < distance[1]) { //zoom in

            }else if (distance[0] > distance[1]) { //zoom out

            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                getWindowManager().getDefaultDisplay().getSize(point);
//                if ((event.getX() > point.x - 40) || (event.getY() <= getActionBar().getHeight()) || drawerOpen) {
//                    IS_IN_VIEWPAGER_AREA = false;
//                    return super.dispatchTouchEvent(event);
//                } else
//                    IS_IN_VIEWPAGER_AREA = true;
                startPoint[0] = event.getX();
                startPoint[1] = event.getY();
                break;
            case MotionEvent.ACTION_UP:
//                if (!IS_IN_VIEWPAGER_AREA)
//                    return super.dispatchTouchEvent(event);
                endPoint[0] = event.getX();
                endPoint[1] = event.getY();
                float dx, dy;
                dx = Math.abs(endPoint[0] - startPoint[0]);
                dy = Math.abs(endPoint[1] - startPoint[1]);

                if ((dx >= dy) && (startPoint[0] >= endPoint[0])) {
                    swipeDirection = "R2L";
                } else if ((dx > dy) && (startPoint[0] < endPoint[0])) {
                    swipeDirection = "L2R";
                } else if ((dx < dy) && (startPoint[1] >= endPoint[1])) {
                    swipeDirection = "D2U";
//                    if (progressDialog != null)
//                        progressDialog.dismiss();
                } else if ((dx < dy) && (startPoint[1] < endPoint[1])) {
                    swipeDirection = "U2D";
//                    if (progressDialog != null)
//                        progressDialog.dismiss();
                }
                if (dx > 10) {
                    startPoint[0] = endPoint[0] = startPoint[1] = endPoint[1] = 0;
                    if (swipeDirection.equals("L2R")){
                        originalSelectedDate.add(Calendar.DATE, 1);
                        setData();
                    }else if (swipeDirection.equals("R2L")){
                        originalSelectedDate.add(Calendar.DATE, -1);
                        setData();
                    }
                }else
                    return false;
                break;
            case MotionEvent.ACTION_MOVE:
//                if (customViewPager.getCurrentItem() == 2
//                        && Math.abs(event.getX() - startPoint[0]) > 10
//                        && Math.abs(event.getY() - startPoint[1]) < Math.abs(event.getY() - startPoint[0])
//                        && progressDialog == null
//                        && IS_IN_VIEWPAGER_AREA
//                        && SWIPE_ACTION
//                        && !yearCal.YEAR_LIST) {
//                    progressDialog = new ProgressDialog(this);
//                    progressDialog.setTitle("لطفاً منتظر بمانید...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                }
                break;
        }
//        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//            SWIPE_ACTION = true;
//        }
        return true;
    }
}
