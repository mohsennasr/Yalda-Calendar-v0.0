package co.yalda.nasr_m.yaldacalendar.Month;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.CalendarItemAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.MonthGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.MonthListGridAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.SimpleWeekGridAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.ArabicCalendar;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.SELECTED_DAY_INDEX;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.SELECTED_MONTH_INDEX;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_DAY_FULL;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_DAY_LIST;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Month;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Year;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.homaFont;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthView extends Fragment {

    public View rootView;
    private DayUC[] dayUC;                    //day user control object
    private Calendar monthCal = Calendar.getInstance();

    //Month View attributes
    private TextView monthHeader_tv;        //month name TextView
    private GridView monthGridView;         //month days gridView
    private GridView weekDaysGrid;         //month days gridView
    private GridView weekNumberGrid;        //week numbers
    private MonthGridViewAdapter gridViewAdapter;   //month grid adapter
    private SimpleWeekGridAdapter weekNumberGridAdapter;
    private CalendarItemAdapter weekDaysAdapter, weekNumAdapter;
    private ArrayList<DayUC> dayUCList;     //DayUC Array list
    private dayViewMode viewMode;
    private String[] weekDays = new String[]{"ش", "ی", "د", "س", "چ", "پ", "ج"};
    private String[] weekDaysFull = new String[]{"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"};
    private PersianCalendar monthPersianCal;
    private ArabicCalendar monthArabicCal;
    private ArrayList<String> weekNumArrayList;
    private int selectedDayIndex = -1;
    private int remainDay=0, maxDayMonth=0;
    private ArrayList<String> weekDaysArrayList;
    private LinearLayout monthComplete, monthList;
    private GridView monthListGridView;
    private MonthListGridAdapter monthListGridAdapter;
    private int CURRENT_VIEW = 1;

    /*
    Fragment Classes have their own constructor method that we can't modify input parameter.
    So, for making Fragment Class with custom input value new method should be written to create
    class objects and set private attributes according to input values
     */
    public static MonthView newInstance(Calendar monthCal, dayViewMode dayViewMode) {
        MonthView monthView = new MonthView();
        monthView.monthCal.setTime(monthCal.getTime());
        monthView.viewMode = dayViewMode;
        monthView.firstInitialization();
        monthView.initialMonth(monthCal);
        return monthView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            switch (savedInstanceState.getString("ViewMode")) {
                case "Month":
                    viewMode = Month;
                    break;
                case "Year":
                    viewMode = Year;
                    break;
            }
        if (rootView == null) {
            firstInitialization();
            initialMonth(monthCal);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void firstInitialization() {
        LayoutInflater mInfalater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInfalater.inflate(R.layout.month_view, null);
//        rootView.setClickable(true);

        monthComplete = (LinearLayout) rootView.findViewById(R.id.month_complete);
        monthList = (LinearLayout) rootView.findViewById(R.id.month_list_view);
        monthListGridView = (GridView) rootView.findViewById(R.id.month_list_grid);
        monthHeader_tv = (TextView) rootView.findViewById(R.id.month_view_header);
        monthGridView = (GridView) rootView.findViewById(R.id.month_view_day_grid);
        weekDaysGrid = (GridView) rootView.findViewById(R.id.month_week_day_name_grid);
        weekNumberGrid = (GridView) rootView.findViewById(R.id.week_number_grid);

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
            updateeMonth();
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


        weekNumberGridAdapter = new SimpleWeekGridAdapter(weekNumArrayList, viewMode);
//        gridViewAdapter = new MonthGridViewAdapter(dayUCList, weekNumArrayList, viewMode);
        gridViewAdapter = new MonthGridViewAdapter(dayUCList, viewMode);
        monthGridView.setAdapter(gridViewAdapter);
        weekNumberGrid.setAdapter(weekNumberGridAdapter);
        weekNumberGridAdapter.notifyDataSetChanged();
//        monthGridView.setMotionEventSplittingEnabled(false);
        gridViewAdapter.notifyDataSetChanged();

        weekDaysAdapter = new CalendarItemAdapter(weekDaysArrayList, viewMode);
        weekDaysGrid.setAdapter(weekDaysAdapter);
        weekDaysAdapter.notifyDataSetChanged();

        if (viewMode == Month) {
            weekDaysArrayList.addAll(Arrays.asList(weekDaysFull));
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName() + " " + PersianUtil.toPersian(monthPersianCal.getiPersianYear()));
            monthHeader_tv.setTextSize(28);
            monthHeader_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60));
        } else {
            weekDaysArrayList.addAll(Arrays.asList(weekDays));
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName());
            monthHeader_tv.setTextSize(16);
            monthHeader_tv.setTextColor(Color.BLACK);
            monthHeader_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 34));
        }

        monthHeader_tv.setTypeface(homaFont);

        setSelectedDate();

        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.SELECTED_DAY_INDEX > 0)
                    dayUC[SELECTED_DAY_INDEX].unSetSelectedDay();
                if (SELECTED_MONTH_INDEX != (monthPersianCal.getiPersianMonth()-1))
                    MainActivity.UPDATE_YEAR = true;
                SELECTED_DAY_INDEX = position;
                originalSelectedDate.setTime(dayUC[position].getMiladiCalendar().getTime());
                originalSelectedPersianDate.setMiladiDate(originalSelectedDate);
                dayUC[position].setSelectedDay();
            }
        });

        monthListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != (monthPersianCal.getiPersianMonth()-1)) {
                    originalSelectedPersianDate.setPersian(Calendar.MONTH, position);
                    originalSelectedDate.setTime(originalSelectedPersianDate.getMiladiDate().getTime());
                    initialMonth(originalSelectedDate);
                    UPDATE_DAY_FULL = true;
                    UPDATE_DAY_LIST = true;
                }
                monthSwitchView(1);
            }
        });

    }

    private void newMonth(){
        dayUC = new DayUC[42];
        for (int i = 0; i < 42; i++) {
            dayUC[i] = DayUC.newInstance(monthCal, !(i < remainDay | i >= (maxDayMonth + remainDay))
                    , viewMode);
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
        }

        weekNumArrayList = new ArrayList<>();
    }

    private void updateeMonth(){
        dayUCList.clear();
        for (int i = 0; i < 42; i++) {
            dayUC[i].setEnable(!(i < remainDay | i >= (maxDayMonth + remainDay)));
            dayUC[i].updateMonth(monthCal);
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
        }
        weekNumArrayList.clear();
    }

    public void setSelectedDate(){
        if (SELECTED_MONTH_INDEX == (monthPersianCal.getiPersianMonth()-1)) {
            if (SELECTED_DAY_INDEX >= 0)
                dayUC[SELECTED_DAY_INDEX].unSetSelectedDay();
            SELECTED_DAY_INDEX = originalSelectedPersianDate.getiPersianDate() + originalSelectedPersianDate.persianPreMonthRemainingDay() - 1;
            dayUC[SELECTED_DAY_INDEX].setSelectedDay();
        }
    }

    public void unSetSelectedDate(){
        if (SELECTED_DAY_INDEX >= 0)
            dayUC[SELECTED_DAY_INDEX].unSetSelectedDay();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ViewMode", viewMode.toString());
        super.onSaveInstanceState(outState);
    }

    public void monthSwitchView(int view){
        if (CURRENT_VIEW == 1 && view == 2){
            monthComplete.setVisibility(View.GONE);
            monthList.setVisibility(View.VISIBLE);
            CURRENT_VIEW = 2;
        }else if (CURRENT_VIEW == 2 && view == 1){
            monthList.setVisibility(View.GONE);
            monthComplete.setVisibility(View.VISIBLE);
            CURRENT_VIEW = 1;
        }
    }
}
