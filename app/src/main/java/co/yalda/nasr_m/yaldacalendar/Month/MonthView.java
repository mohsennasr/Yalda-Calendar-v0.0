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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.CalendarItemAdaoter;
import co.yalda.nasr_m.yaldacalendar.Adapters.MonthGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.PersianDatePicker.Util.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Month;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Year;

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
    private MonthGridViewAdapter gridViewAdapter;   //month grid adapter
    private CalendarItemAdaoter weekDaysAdapter, weekNumAdapter;
    private ArrayList<DayUC> dayUCList;     //DayUC Array list
    private dayViewMode viewMode;
    private String[] weekDays = new String[]{"", "ش", "ی", "د", "س", "چ", "پ", "ج"};
    private String[] weekDaysFull = new String[]{"", "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"};
    private PersianCalendar monthPersianCal;
    private ArrayList<String> weekNumArrayList;

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
        monthView.initialMonth();
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
            initialMonth();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void firstInitialization() {
        LayoutInflater mInfalater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInfalater.inflate(R.layout.month_view, null);
        rootView.setClickable(true);

        monthHeader_tv = (TextView) rootView.findViewById(R.id.month_view_header);
        monthGridView = (GridView) rootView.findViewById(R.id.month_view_day_grid);
        weekDaysGrid = (GridView) rootView.findViewById(R.id.month_week_day_name_grid);
        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Grid Item " + position + " Selected", Toast.LENGTH_LONG).show();
            }
        });

        if (dayUCList == null)
            dayUCList = new ArrayList<>();
    }

    //initial attributes
    private void initialMonth() {
        monthCal.setFirstDayOfWeek(Calendar.SATURDAY);
        monthPersianCal = new PersianCalendar(monthCal.getTime().getTime());
        int maxDayMonth = monthPersianCal.getMaxDayOfMonth();
        monthPersianCal.setPersian(Calendar.DATE, 1);
        monthCal.setTime(monthPersianCal.getTime());
        int remainDay = monthPersianCal.persianPreMonthRemainingDay();
        monthCal.add(Calendar.DATE, -remainDay);
        dayUC = new DayUC[42];
        for (int i = 0; i < 42; i++) {
            dayUC[i] = DayUC.newInstance(monthCal, !(i < remainDay | i >= (maxDayMonth + remainDay))
                    , viewMode);
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
        }

        weekNumArrayList = new ArrayList<>();

        int weekNum = monthPersianCal.getFirstWeekNumberOfMonth();
        for (int i = 0; i < 6; i++) {
            weekNumArrayList.add(String.valueOf(weekNum));
            if (weekNum >= 52 && monthPersianCal.getPersianMonth() == 0)
                weekNum = 1;
            else
                weekNum++;
        }

        gridViewAdapter = new MonthGridViewAdapter(dayUCList, weekNumArrayList, viewMode);
        monthGridView.setAdapter(gridViewAdapter);
        monthGridView.setMotionEventSplittingEnabled(false);
        gridViewAdapter.notifyDataSetChanged();

        //Week Day Name Grid View
        ArrayList<String> weekDaysArrayList = new ArrayList<>();

        if (viewMode == Month) {
            weekDaysArrayList.addAll(Arrays.asList(weekDaysFull));
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName() + " " + String.valueOf(monthPersianCal.getPersianYear()));
            monthHeader_tv.setTextSize(34);
            monthHeader_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60));
        } else {
            weekDaysArrayList.addAll(Arrays.asList(weekDays));
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName());
            monthHeader_tv.setTextSize(20);
            monthHeader_tv.setTextColor(Color.BLACK);
            monthHeader_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 32));
        }

        weekDaysAdapter = new CalendarItemAdaoter(weekDaysArrayList, viewMode);
        weekDaysGrid.setAdapter(weekDaysAdapter);
        weekDaysAdapter.notifyDataSetChanged();
    }

    public void updateMonth(Calendar month){
        monthCal.setTime(month.getTime());
        monthPersianCal.setTime(monthCal.getTime());
        int maxDayMonth = monthPersianCal.getMaxDayOfMonth();
        monthPersianCal.setPersian(Calendar.DATE, 1);
        monthCal.setTime(monthPersianCal.getTime());
        int remainDay = monthPersianCal.persianPreMonthRemainingDay();
        monthCal.add(Calendar.DATE, -remainDay);
        dayUCList.clear();
        for (int i = 0; i < 42; i++) {
            dayUC[i].setEnable(!(i < remainDay | i >= (maxDayMonth + remainDay)));
            dayUC[i].initialMonth();
            dayUCList.add(dayUC[i]);
        }

        weekNumArrayList.clear();
        int weekNum = monthPersianCal.getFirstWeekNumberOfMonth();
        for (int i = 0; i < 6; i++) {
            weekNumArrayList.add(String.valueOf(weekNum));
            if (weekNum >= 52 && monthPersianCal.getPersianMonth() == 0)
                weekNum = 1;
            else
                weekNum++;
        }

        gridViewAdapter.notifyDataSetChanged();

        if (viewMode == Month) {
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName() + " " + String.valueOf(monthPersianCal.getPersianYear()));
        } else {
            monthHeader_tv.setText(monthPersianCal.getPersianMonthName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ViewMode", viewMode.toString());
        super.onSaveInstanceState(outState);
    }
}
