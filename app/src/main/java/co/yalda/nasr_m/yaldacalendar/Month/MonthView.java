package co.yalda.nasr_m.yaldacalendar.Month;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.MonthGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.SimpleAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

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
    private SimpleAdapter weekDaysAdapter, weekNumAdapter;
    private ArrayList<DayUC> dayUCList;     //DayUC Array list
    private MainActivity.dayViewMode dayViewMode;
    private String[] weekDays = new String[]{"", "ش", "ی", "د", "س", "چ", "پ", "ج"};
    private String[] weekDaysFull = new String[]{"", "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"};

    /*
    Fragment Classes have their own constructor method that we can't modify input parameter.
    So, for making Fragment Class with custom input value new method should be written to create
    class objects and set private attributes according to input values
     */
    public static MonthView newInstance(Calendar monthCal, MainActivity.dayViewMode dayViewMode) {
        MonthView monthView = new MonthView();
        monthView.monthCal.setTime(monthCal.getTime());
        monthView.dayViewMode = dayViewMode;
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
                    dayViewMode = Month;
                    break;
                case "Year":
                    dayViewMode = Year;
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
        LayoutInflater mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInfalater.inflate(R.layout.month_view, null);

        monthHeader_tv = (TextView) rootView.findViewById(R.id.month_view_header);
        monthGridView = (GridView) rootView.findViewById(R.id.month_view_day_grid);
        weekDaysGrid = (GridView) rootView.findViewById(R.id.month_week_day_name_grid);

        if (dayUCList == null)
            dayUCList = new ArrayList<>();
    }

    //initial attributes
    private void initialMonth() {
        monthCal.setFirstDayOfWeek(Calendar.SATURDAY);
        PersianCalendar pCal = new PersianCalendar(monthCal);
        int maxDayMonth = pCal.getMaxDayOfMonth();
        pCal.persianSet(Calendar.DATE, 1);
        monthCal.setTime(pCal.getMiladiDate().getTime());
        int remainDay = pCal.persianPreMonthRemainingDay();
        monthCal.add(Calendar.DATE, -remainDay);
        dayUC = new DayUC[42];
        for (int i = 0; i < 42; i++) {
            dayUC[i] = DayUC.newInstance(monthCal, !(i < remainDay | i > (maxDayMonth + remainDay))
                    && (dayViewMode == Month)
                    , dayViewMode);
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
        }

        if (dayViewMode == Month)
            monthHeader_tv.setText(pCal.getPersianMonthName() + " " + String.valueOf(pCal.getiPersianYear()));
        else
            monthHeader_tv.setText(pCal.getPersianMonthName());

        ArrayList<String> weekNumArrayList = new ArrayList<>();

        int weekNum = pCal.getFirstWeekNumberOfMonth();
        for (int i = weekNum; i < weekNum + 6; i++)
            weekNumArrayList.add(String.valueOf(i));

        gridViewAdapter = new MonthGridViewAdapter(dayUCList, weekNumArrayList);
        monthGridView.setAdapter(gridViewAdapter);
        gridViewAdapter.notifyDataSetChanged();

        //Week Day Name Grid View
        ArrayList<String> weekDaysArrayList = new ArrayList<>();
        if (dayViewMode == Month)
            weekDaysArrayList.addAll(Arrays.asList(weekDaysFull));
        else
            weekDaysArrayList.addAll(Arrays.asList(weekDays));
        weekDaysAdapter = new SimpleAdapter(weekDaysArrayList, dayViewMode);
        weekDaysGrid.setAdapter(weekDaysAdapter);
        weekDaysAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ViewMode", dayViewMode.toString());
        super.onSaveInstanceState(outState);
    }
}
