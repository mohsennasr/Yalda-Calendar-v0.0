package co.yalda.nasr_m.yaldacalendar.Year;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.YearGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.YearListGridAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class YearView extends Fragment {

    private View rootView, secondView;
    private MonthView[] yearMonth;
    private ArrayList<MonthView> monthViewList;
    private YearGridViewAdapter yearGridAdapter;
    private TextView yearHeader_tv;
    private GridView yearGridView, yearListGrid;
    private Calendar yearCal = Calendar.getInstance();
    private PersianCalendar yearPersianCal;
    private int CURRENT_VIEW = 1;
    private int selectedIndex = -1;

    public static YearView newInstance(Calendar yearCal) {
        YearView yearView = new YearView();
        yearView.yearCal.setTime(yearCal.getTime());
        return yearView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.year_view, container, false);
        secondView = inflater.inflate(R.layout.year_second_view, container, false);
        secondView.setVisibility(View.GONE);
        yearListGrid = (GridView) secondView.findViewById(R.id.year_list_grid);
        yearHeader_tv = (TextView) rootView.findViewById(R.id.year_view_header);
        yearGridView = (GridView) rootView.findViewById(R.id.year_grid_view);
        yearGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Grid Item " + position + " Selected", Toast.LENGTH_LONG).show();
            }
        });

        return CURRENT_VIEW == 1 ? rootView : secondView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (monthViewList == null)
            monthViewList = new ArrayList<>();
        initialYear();
    }

    //initial attributes
    private void initialYear(/*LayoutInflater inflater, @Nullable ViewGroup container*/) {
        yearPersianCal = new PersianCalendar(yearCal);
        yearPersianCal.setPersian(Calendar.MONTH, 0);
        yearCal.setTime(yearPersianCal.getMiladiDate().getTime());
        yearMonth = new MonthView[12];
        Integer yearNum = yearCal.get(Calendar.YEAR) - 4;
        ArrayList<Integer> yearList = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            yearMonth[i] = MonthView.newInstance(yearCal, dayViewMode.Year);
            monthViewList.add(yearMonth[i]);
            yearCal.add(Calendar.MONTH, 1);
            yearList.add(yearNum + i);
        }

        setColumns();

        yearHeader_tv.setText(String.valueOf(yearPersianCal.getiPersianYear()));
        yearGridAdapter = new YearGridViewAdapter(monthViewList);
        yearGridView.setAdapter(yearGridAdapter);
        yearGridAdapter.notifyDataSetChanged();

        YearListGridAdapter yearListGridAdapter = new YearListGridAdapter(yearList);
        yearListGrid.setAdapter(yearListGridAdapter);
        yearListGridAdapter.notifyDataSetChanged();
    }

    public void setColumns() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            yearGridView.setNumColumns(3);
        else
            yearGridView.setNumColumns(4);
    }

    public void updateYear(){
        yearCal.setTime(originalSelectedDate.getTime());
        yearPersianCal.setMiladiDate(yearCal);
        yearPersianCal.setPersian(Calendar.MONTH, 0);
        yearCal.setTime(yearPersianCal.getMiladiDate().getTime());
        monthViewList.clear();
        for (int i = 0; i < 12; i++) {
            yearMonth[i].updateMonth(yearCal);
            yearCal.add(Calendar.MONTH, 1);
            monthViewList.add(yearMonth[i]);
        }

        setColumns();

        yearHeader_tv.setText(String.valueOf(yearPersianCal.getiPersianYear()));
        yearGridAdapter.notifyDataSetChanged();
    }

    public void yearSwitchView(){
        if (CURRENT_VIEW == 1){
            rootView.setVisibility(View.GONE);
            secondView.setVisibility(View.VISIBLE);
            CURRENT_VIEW = 2;
        }else{
            secondView.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
            CURRENT_VIEW = 1;
        }
    }

    public void setSelectedDate(){
        if (selectedIndex >= 0)
            yearMonth[selectedIndex].unSetSelectedDate();
        selectedIndex = originalSelectedPersianDate.getiPersianMonth()-1;
        yearMonth[selectedIndex].setSelectedDate();
    }
}
