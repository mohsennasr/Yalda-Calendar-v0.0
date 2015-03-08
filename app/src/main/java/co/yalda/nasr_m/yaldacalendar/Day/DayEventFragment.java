package co.yalda.nasr_m.yaldacalendar.Day;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.EventsListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Handler.AddEvent;
import co.yalda.nasr_m.yaldacalendar.Handler.Events;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.LEFT_TO_RIGHT_VALUE;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.RIGHT_TO_LEFT_VALUE;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.SELECTED_DAY_CHANGED;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_DAY_FULL;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_MONTH;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_YEAR;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.actionBarHeight;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.drawerOpen;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.point;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Left2Right;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Right2Left;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomIn;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomOut;

/**
 * Created by Nasr_M on 3/8/2015.
 */
public class DayEventFragment extends Fragment implements View.OnTouchListener {

    private DayUC dayUC;
    private ListView eventList;
    private View rootView;
    private ArrayList<Events> eventsArrayList;
    private EventsListViewAdapter eventsListViewAdapter;
    private int CURRENT_SELECTED_EVENT = -1;
    private boolean SWIPE_ACTION = true;
    private boolean GESTURE_ACTION = false;
    private float[] startPoint = new float[4];
    private float[] endPoint = new float[4];
    private float[] distance = new float[2];
    private MainActivity.touchEvent touchAction;
    private boolean ACTION_FINISHED = false;
    private boolean IS_IN_VIEWPAGER_AREA = false;

    public static DayEventFragment newInstance(Calendar calendar) {
        DayEventFragment dayEventFragment = new DayEventFragment();
        dayEventFragment.dayUC = DayUC.newInstance(calendar, false, dayViewMode.DayEvent);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dayEventFragment.rootView = mInflater.inflate(R.layout.day_uc_list_mode_view, null);
        dayEventFragment.eventList = (ListView) dayEventFragment.rootView.findViewById(R.id.day_list_mode_event_list);
        dayEventFragment.initial();
        return dayEventFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUC).commit();
        return rootView;
    }

    private void initial() {
        // TODO get data from DB
        eventsArrayList = new ArrayList<>();
        Events events = Events.newInstance(context, Calendar.getInstance());
        events.setEvent("asdf0", "asdfasdfasdfasdfa sdf ", "10:25", "16:56");
        eventsArrayList.add(events);
        eventsListViewAdapter = new EventsListViewAdapter(eventsArrayList, context);

        eventList.setAdapter(eventsListViewAdapter);
        eventsListViewAdapter.notifyDataSetChanged();

        // show event detail on event list item click
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title, detail, start, end;
                title = eventsArrayList.get(position).getTitle();
                detail = eventsArrayList.get(position).getDescription();
                start = eventsArrayList.get(position).getStartTime();
                end = eventsArrayList.get(position).getEndTime();

                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View eventView = mInflater.inflate(R.layout.event_show_dialog, null);

                TextView titleEvent = (TextView) eventView.findViewById(R.id.event_title_view);
                TextView detailEvent = (TextView) eventView.findViewById(R.id.event_detail_view);
                TextView startEvent = (TextView) eventView.findViewById(R.id.start_time);
                TextView endEvent = (TextView) eventView.findViewById(R.id.end_time);

                titleEvent.setText(title);
                detailEvent.setText(detail);
                startEvent.setText(start);
                endEvent.setText(end);

                //create dialog
                AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
                inputNote.setTitle("مشاهده رویداد");
                inputNote.setView(eventView);

                //set dialog buttons
                inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                //show dialog
                inputNote.show();
            }
        });

        // register for context menu on long click
        registerForContextMenu(eventList);
    }

    public void update(){
        dayUC.updateDayHeader();
        initial();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.day_list_mode_event_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("رویداد");
            String[] menuItems = new String[]{"ویرایش", "حذف"};
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    /**
     * select action for selected event : Edit , Remove
     *
     * @param item selected item
     * @return true
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:             //Edit Event
                Intent eventActivity = new Intent(context, AddEvent.class);
                eventActivity.putExtra("EventMode", MainActivity.eventMode.EditEvent.toString());
                eventActivity.putExtra("Title", eventsArrayList.get(info.position).getTitle());
                eventActivity.putExtra("Detail", eventsArrayList.get(info.position).getDescription());
                eventActivity.putExtra("isHoleDay", eventsArrayList.get(info.position).isHoleDay());
                if (!eventsArrayList.get(info.position).isHoleDay()) {
                    eventActivity.putExtra("Start_Time", eventsArrayList.get(info.position).getStartTime());
                    eventActivity.putExtra("End_Time", eventsArrayList.get(info.position).getEndTime());
                }
//                eventActivity.putExtra("Date", eventsArrayList.get(info.position).getCal().getTimeInMillis());
                CURRENT_SELECTED_EVENT = info.position;
                startActivityForResult(eventActivity, 0);
                break;
            case 1:             //remove event
                eventsArrayList.remove(info.position);
                eventsListViewAdapter = new EventsListViewAdapter(eventsArrayList, context);
                eventList.setAdapter(eventsListViewAdapter);
                eventsListViewAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(context, "touch event", Toast.LENGTH_SHORT).show();
        if ((event.getPointerCount() >= 2)) {     // multiTouch gesture
            SWIPE_ACTION = false;
            GESTURE_ACTION = true;

            if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                startPoint[0] = event.getX(0);
                startPoint[1] = event.getY(0);
                startPoint[2] = event.getX(1);
                startPoint[3] = event.getY(1);
            } else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                endPoint[0] = event.getX(0);
                endPoint[1] = event.getY(0);
                endPoint[2] = event.getX(1);
                endPoint[3] = event.getY(1);

                //start distance
                distance[0] = (float) Math.sqrt(Math.pow((startPoint[0] - startPoint[2]), 2)
                        + Math.pow((startPoint[1] - startPoint[3]), 2));

                //end distance
                distance[1] = (float) Math.sqrt(Math.pow((endPoint[0] - endPoint[2]), 2)
                        + Math.pow((endPoint[1] - startPoint[3]), 2));

                if (distance[0] < distance[1]) { //zoom in
                    touchAction = ZoomIn;
                } else if (distance[0] > distance[1]) { //zoom out
                    touchAction = ZoomOut;
                }
                ACTION_FINISHED = true;
            }
        }
        if (SWIPE_ACTION) {                  // swipe action
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if ((event.getX() > point.x - 40) || (event.getY() <= actionBarHeight) || drawerOpen) {  //check for drawer touch
                        IS_IN_VIEWPAGER_AREA = false;
                        return false;
                    } else
                        IS_IN_VIEWPAGER_AREA = true;
                    startPoint[0] = event.getX();
                    startPoint[1] = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!IS_IN_VIEWPAGER_AREA)
                        return false;
                    endPoint[0] = event.getX();
                    endPoint[1] = event.getY();
                    float dx, dy;
                    dx = Math.abs(endPoint[0] - startPoint[0]);
                    dy = Math.abs(endPoint[1] - startPoint[1]);
                    if ((dx >= dy) && dx > 10) {
                        if (startPoint[0] >= endPoint[0]) {         // Right-to-Left Swipe
                            touchAction = Right2Left;
                        } else if ((startPoint[0] < endPoint[0])) {    // Left-to-Right Swipe
                            touchAction = Left2Right;
                        }
                    } else {
                        return false;
                    }
                    ACTION_FINISHED = true;
                    break;
                case MotionEvent.ACTION_MOVE:
//                    if (viewPager.getCurrentItem() == 6
//                            && Math.abs(event.getX() - startPoint[0]) > 10
//                            && Math.abs(event.getY() - startPoint[1]) < Math.abs(event.getY() - startPoint[0])
//                            && progressDialog == null
//                            && IS_IN_VIEWPAGER_AREA
//                            && SWIPE_ACTION
//                            /*&& !yearCal.YEAR_LIST*/) {
//
//                    }
                    break;
            }
        }
        if (ACTION_FINISHED) {
            int month = 0, year = 0;
            switch (touchAction) {
                case Right2Left:                                            //Decrease Date by 1
                    month = originalSelectedPersianDate.getiPersianMonth();
                    year = originalSelectedPersianDate.getiPersianYear();
                    originalSelectedDate.add(Calendar.DATE, RIGHT_TO_LEFT_VALUE);
                    originalSelectedPersianDate.addPersian(Calendar.DATE, RIGHT_TO_LEFT_VALUE);
                    if (originalSelectedPersianDate.getiPersianYear() != year) {
                        UPDATE_YEAR = true;
                        UPDATE_MONTH = true;
                        UPDATE_DAY_FULL = true;
                        SELECTED_DAY_CHANGED = true;
                    } else if (originalSelectedPersianDate.getiPersianMonth() != month) {
                        UPDATE_MONTH = true;
                        UPDATE_DAY_FULL = true;
                        SELECTED_DAY_CHANGED = true;
                    } else {
                        UPDATE_DAY_FULL = true;
                        SELECTED_DAY_CHANGED = true;
                    }
                    break;
                case Left2Right:
                    month = originalSelectedPersianDate.getiPersianMonth();
                    year = originalSelectedPersianDate.getiPersianYear();
                    originalSelectedDate.add(Calendar.DATE, LEFT_TO_RIGHT_VALUE);
                    originalSelectedPersianDate.addPersian(Calendar.DATE, LEFT_TO_RIGHT_VALUE);
                    if (originalSelectedPersianDate.getiPersianYear() != year) {
                        UPDATE_YEAR = true;
                        UPDATE_MONTH = true;
                        UPDATE_DAY_FULL = true;
                        SELECTED_DAY_CHANGED = true;
                    } else if (originalSelectedPersianDate.getiPersianMonth() != month) {
                        UPDATE_MONTH = true;
                        UPDATE_DAY_FULL = true;
                        SELECTED_DAY_CHANGED = true;
                    } else {
                        UPDATE_DAY_FULL = true;
                        SELECTED_DAY_CHANGED = true;
                    }
                    break;
                default:
                    return false;
            }
            update();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ACTION_FINISHED = false;
            SWIPE_ACTION = true;
            GESTURE_ACTION = false;
            startPoint = new float[4];
            endPoint = new float[4];
            distance = new float[2];
        }
        return true;
    }
}