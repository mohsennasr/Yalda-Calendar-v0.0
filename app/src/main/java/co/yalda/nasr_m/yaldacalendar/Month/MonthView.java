package co.yalda.nasr_m.yaldacalendar.Month;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.MonthGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.MonthListGridAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.WeekDaysAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.WeekNumberGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.ArabicCalendar;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.LEFT_TO_RIGHT_VALUE;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.RIGHT_TO_LEFT_VALUE;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Month;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.homaFont;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.lastUCDaySelected;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Left2Right;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Right2Left;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomIn;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomOut;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthView extends Fragment implements View.OnTouchListener{

    public View rootView;
    private DayUC[] dayUC;                    //day user control object
    private Calendar monthCal = Calendar.getInstance();
    int i=0;
    //Month View attributes
    private TextView monthHeader_tv;        //month name TextView
    public GridView monthGridView;         //month days gridView
    private GridView weekDaysGrid;         //month days gridView
    private GridView weekNumberGrid;        //week numbers
    private MonthGridViewAdapter gridViewAdapter;   //month grid adapter
    private WeekNumberGridViewAdapter weekNumberGridAdapter;
    private WeekDaysAdapter weekDaysAdapter;
    private ArrayList<DayUC> dayUCList;     //DayUC Array list
    private dayViewMode viewMode;
    private String[] weekDays = new String[]{"ش", "ی", "د", "س", "چ", "پ", "ج"};
    private String[] weekDaysFull = new String[]{"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"};
    private PersianCalendar monthPersianCal;
    private ArabicCalendar monthArabicCal;
    private ArrayList<String> weekNumArrayList;
    private int remainDay=0, maxDayMonth=0;
    private ArrayList<String> weekDaysArrayList;
    private LinearLayout monthComplete, monthList;
    private GridView monthListGridView;
    private MonthListGridAdapter monthListGridAdapter;
    private int CURRENT_VIEW = 1;
    private ViewGroup container;
    private boolean SWIPE_ACTION = true;
    private boolean GESTURE_ACTION = false;
    private float[] startPoint = new float[4];
    private float[] endPoint = new float[4];
    private float[] distance = new float[2];
    private float dx, dy;
    private touchEvent touchAction;
    private boolean ACTION_FINISHED = false;

    /*
    Fragment Classes have their own constructor method that we can't modify input parameter.
    So, for making Fragment Class with custom input value new method should be written to create
    class objects and set private attributes according to input values
     */
    public static MonthView newInstance(Calendar monthCal, dayViewMode dayViewMode, ViewGroup container) {
        MonthView monthView = new MonthView();
        monthView.monthCal.setTime(monthCal.getTime());
        monthView.viewMode = dayViewMode;
        monthView.container = container;
        monthView.firstInitialization();
        monthView.initialMonth(monthCal);
        return monthView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void firstInitialization() {
        LayoutInflater mInfalater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInfalater.inflate(R.layout.month_view, container, false);

        monthComplete = (LinearLayout) rootView.findViewById(R.id.month_complete);
        monthList = (LinearLayout) rootView.findViewById(R.id.month_list_view);
        monthListGridView = (GridView) rootView.findViewById(R.id.month_list_grid);
        monthHeader_tv = (TextView) rootView.findViewById(R.id.month_view_header);
        monthGridView = (GridView) rootView.findViewById(R.id.month_view_day_grid);
        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                originalSelectedDate.setTime(dayUCList.get(position).getMiladiCalendar().getTime());
                originalSelectedPersianDate.setMiladiDate(originalSelectedDate);
                if (lastUCDaySelected != null) {
                    lastUCDaySelected.unSetSelectedDay();
                }
                dayUCList.get(position).setSelectedDay();
                lastUCDaySelected = dayUCList.get(position);
            }
        });
        weekDaysGrid = (GridView) rootView.findViewById(R.id.month_week_day_name_grid);
        weekNumberGrid = (GridView) rootView.findViewById(R.id.week_number_grid);

        rootView.setOnTouchListener(this);
        monthGridView.setOnTouchListener(this);
        weekDaysGrid.setOnTouchListener(this);
        weekNumberGrid.setOnTouchListener(this);

        if (dayUCList == null)
            dayUCList = new ArrayList<>();
    }

    //initial attributes
    public void initialMonth(Calendar month) {
        monthCal.setTime(month.getTime());
        monthCal.setFirstDayOfWeek(Calendar.SATURDAY);
        monthPersianCal = new PersianCalendar(monthCal);
        maxDayMonth = monthPersianCal.getMaxDayOfMonth();
        monthPersianCal.setPersian(Calendar.DATE, 1);
        monthCal.setTime(monthPersianCal.getMiladiDate().getTime());
        remainDay = monthPersianCal.persianPreMonthRemainingDay();
        monthCal.add(Calendar.DATE, -remainDay);

        //Week Day Name Grid View
        weekDaysArrayList = new ArrayList<>();

        if (dayUC == null) {
            newMonth();
        }else {
            updateMonth();
        }
        monthCal.setTime(month.getTime());
        monthPersianCal.setMiladiDate(monthCal);

        int weekNum = monthPersianCal.getFirstWeekNumberOfMonth();
        int weekNumber = monthPersianCal.numberOfWeeksInMonth();
        for (int i = 0; i < 6; i++) {
            if (i < weekNumber) {
                weekNumArrayList.add(PersianUtil.toPersian(weekNum));
            }else{
                weekNumArrayList.add("");
            }
            if (weekNum >= 52 && monthPersianCal.getiPersianMonth() == 1)
                weekNum = 1;
            else
                weekNum++;
        }

        final ArrayList<String> monthArrayList = new ArrayList<>();
        monthArrayList.addAll(Arrays.asList(PersianCalendar.persianMonthNames));
        monthListGridAdapter = new MonthListGridAdapter(monthArrayList, monthPersianCal.getiPersianMonth()-1);
        monthListGridView.setAdapter(monthListGridAdapter);
        monthListGridAdapter.notifyDataSetChanged();


        weekNumberGridAdapter = new WeekNumberGridViewAdapter(weekNumArrayList, viewMode);
        gridViewAdapter = new MonthGridViewAdapter(dayUCList, viewMode);
        monthGridView.setAdapter(gridViewAdapter);
        weekNumberGrid.setAdapter(weekNumberGridAdapter);
        weekNumberGridAdapter.notifyDataSetChanged();
        gridViewAdapter.notifyDataSetChanged();

        weekDaysAdapter = new WeekDaysAdapter(weekDaysArrayList, viewMode);
        weekDaysGrid.setAdapter(weekDaysAdapter);
        weekDaysAdapter.notifyDataSetChanged();

        monthHeader_tv.setTypeface(homaFont);

        if (viewMode == Month) {
            weekDaysArrayList.addAll(Arrays.asList(weekDaysFull));
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName() + " " + PersianUtil.toPersian(monthPersianCal.getiPersianYear()));
            monthHeader_tv.setTextSize(28);
            monthHeader_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60));
        } else {
            weekDaysArrayList.addAll(Arrays.asList(weekDays));
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName());
            monthHeader_tv.setTextSize(14);
            monthHeader_tv.setTextColor(Color.BLACK);
            monthHeader_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
        }

        monthHeader_tv.setTypeface(homaFont);

//        dayUCList.get(originalSelectedPersianDate.getiPersianDate()+ originalSelectedPersianDate.persianPreMonthRemainingDay()-1).setSelectedDay();
//        lastUCDaySelected = dayUCList.get(originalSelectedPersianDate.getiPersianDate()+ originalSelectedPersianDate.persianPreMonthRemainingDay()-1);

//        monthListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position != (monthPersianCal.getiPersianMonth()-1)) {
//                    originalSelectedPersianDate.setPersian(Calendar.MONTH, position);
//                    originalSelectedDate.setTime(originalSelectedPersianDate.getMiladiDate().getTime());
//                    initialMonth(originalSelectedDate);
////                    setSelectedDate();
//                    UPDATE_DAY_FULL = true;
//                    UPDATE_DAY_LIST = true;
//                }
//                monthSwitchView(1);
//            }
//        });

    }

    private void newMonth(){
        dayUC = new DayUC[42];
        for (int i = 0; i < 42; i++) {
            dayUC[i] = DayUC.newInstance(monthCal, !(i < remainDay | i >= (maxDayMonth + remainDay))
                    , viewMode, (ViewGroup) rootView);
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
        }

        weekNumArrayList = new ArrayList<>();
    }

    private void updateMonth(){
        dayUCList.clear();
        int index = 0;

        for (int i = 0; i < 42; i++) {
            dayUC[i].updateMonth(monthCal, !(i < remainDay | i >= (maxDayMonth + remainDay)));
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
//            if (dayUC[i].persianCalendar.getiPersianDate() == lastUCDaySelected.persianCalendar.getiPersianDate()) {
//
//                index = i;
//            }
        }
//        dayUC[index].setSelectedDay();
//        lastUCDaySelected = dayUC[index];
        weekNumArrayList.clear();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
                    startPoint[0] = event.getX();
                    startPoint[1] = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    endPoint[0] = event.getX();
                    endPoint[1] = event.getY();
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
            }
        }
        if (ACTION_FINISHED) {
            int year = 0;
            switch (touchAction) {
                case Right2Left:                                            //Decrease Date by 1
                    originalSelectedDate.add(Calendar.MONTH, RIGHT_TO_LEFT_VALUE);
                    originalSelectedPersianDate.addPersian(Calendar.MONTH, RIGHT_TO_LEFT_VALUE);
                    break;
                case Left2Right:
                    originalSelectedDate.add(Calendar.MONTH, LEFT_TO_RIGHT_VALUE);
                    originalSelectedPersianDate.addPersian(Calendar.MONTH, LEFT_TO_RIGHT_VALUE);
                    break;
            }
            lastUCDaySelected.unSetSelectedDay();
            initialMonth(originalSelectedDate);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ACTION_FINISHED = false;
            SWIPE_ACTION = true;
            GESTURE_ACTION = false;
            startPoint = new float[4];
            endPoint = new float[4];
            distance = new float[2];
        }
        return false;
    }
}
