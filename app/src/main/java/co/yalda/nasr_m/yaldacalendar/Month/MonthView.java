package co.yalda.nasr_m.yaldacalendar.Month;

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

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthView extends Fragment {

    private DayUC[] dayUC;                    //day user control object
    private View rootView;
    private Calendar monthCal = Calendar.getInstance();

    //Month View attributes
    private TextView monthHeader_tv;        //month name TextView
    private GridView monthGridView;         //month days gridView
    private MonthGridViewAdapter gridViewAdapter;   //month grid adapter
    private ArrayList<DayUC> dayUCList;     //DayUC Array list

    /*
    Fragment Classes have their own constructor method that we can't modify input parameter.
    So, for making Fragment Class with custom input value new method should be written to create
    class objects and set private attributes according to input values
     */
    public static MonthView newInstance(Calendar monthCal) {
        MonthView monthView = new MonthView();
        monthView.monthCal.setTime(monthCal.getTime());
        return monthView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.month_view, container, false);

        monthHeader_tv = (TextView) rootView.findViewById(R.id.month_view_header);
        monthGridView = (GridView) rootView.findViewById(R.id.month_view_day_grid);

        if (dayUCList == null)
            dayUCList = new ArrayList<>();
        initialMonth();

        return rootView;
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
            dayUC[i] = DayUC.newInstance(monthCal, !(i < remainDay | i > (maxDayMonth + remainDay)), MainActivity.viewMode.Month);
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
        }

        monthHeader_tv.setText(pCal.getPersianMonthName() + " " + String.valueOf(pCal.getiPersianYear()));


        gridViewAdapter = new MonthGridViewAdapter(getActivity(), dayUCList);
        monthGridView.setAdapter(gridViewAdapter);
        gridViewAdapter.notifyDataSetChanged();

        //Week Day Name Grid View
        String[] weekDays = new String[]{"یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "چمعه", "شنبه"};
        GridView weekDaysGrid = (GridView) rootView.findViewById(R.id.month_week_day_name_grid);
        ArrayList<String> weekDaysArrayList = new ArrayList<>();
        weekDaysArrayList.addAll(Arrays.asList(weekDays));
        SimpleAdapter weekDaysAdapter = new SimpleAdapter(weekDaysArrayList, getActivity());
        weekDaysGrid.setAdapter(weekDaysAdapter);
        weekDaysAdapter.notifyDataSetChanged();

        //Week Numbers Grid View
        String[] weekNums = new String[]{"1", "2", "3", "4", "5", "6"};
        GridView weekNumGrid = (GridView) rootView.findViewById(R.id.month_week_number_grid);
        ArrayList<String> weekNumArrayList = new ArrayList<>();
        weekNumArrayList.addAll(Arrays.asList(weekNums));
        SimpleAdapter weekNumAadapter = new SimpleAdapter(weekNumArrayList, getActivity());
        weekNumGrid.setAdapter(weekNumAadapter);
        weekNumAadapter.notifyDataSetChanged();
    }
}
