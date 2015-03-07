package co.yalda.nasr_m.yaldacalendar.Year;

import android.content.Context;
import android.content.res.Configuration;
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
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.YearGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.YearListGridAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.SELECTED_MONTH_INDEX;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_DAY_FULL;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_DAY_LIST;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.UPDATE_MONTH;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.homaFont;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class YearView extends Fragment implements GridView.OnItemClickListener, View.OnClickListener{

    private View rootView;
    private MonthView[] yearMonth;
    private ArrayList<MonthView> monthViewList;
    private YearGridViewAdapter yearGridAdapter;
    private TextView yearHeader_tv;
    private GridView yearGridView, yearListGrid;
    private Calendar yearCal = Calendar.getInstance();
    private PersianCalendar yearPersianCal;
    private int CURRENT_VIEW = 1;
    private int selectedIndex = -1;
    private LinearLayout completeYear, listYear;
    private YearListGridAdapter yearListGridAdapter;
    private ArrayList<String> yearList;

    public static YearView newInstance(Calendar yearCal) {
        YearView yearView = new YearView();
        yearView.yearCal.setTime(yearCal.getTime());
        yearView.firstInit();
        return yearView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rootView == null)
            firstInit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.year_view, container, false);
//        secondView = inflater.inflate(R.layout.year_second_view, container, false);
//        secondView.setVisibility(View.GONE);
//        yearListGrid = (GridView) secondView.findViewById(R.id.year_list_grid);
//        yearHeader_tv = (TextView) rootView.findViewById(R.id.year_view_header);
//        yearGridView = (GridView) rootView.findViewById(R.id.year_grid_view);

        if (rootView == null)
            firstInit();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void firstInit(){
        if (monthViewList == null)
            monthViewList = new ArrayList<>();
        if (rootView == null){
            LayoutInflater mInfalater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = mInfalater.inflate(R.layout.year_view, null);
//            secondView = mInfalater.inflate(R.layout.year_second_view, null);
//            secondView.setVisibility(View.GONE);
            yearListGrid = (GridView) rootView.findViewById(R.id.year_list_grid);
            completeYear = (LinearLayout) rootView.findViewById(R.id.year_complete_layout);
            listYear = (LinearLayout) rootView.findViewById(R.id.year_list_view);
            listYear.setVisibility(View.INVISIBLE);
            yearHeader_tv = (TextView) rootView.findViewById(R.id.year_view_header);
            yearGridView = (GridView) rootView.findViewById(R.id.year_grid_view);
        }

        initialYear();
    }

    //initial attributes
    public void initialYear(/*LayoutInflater inflater, @Nullable ViewGroup container*/) {
        yearCal.setTime(originalSelectedDate.getTime());
        yearPersianCal = new PersianCalendar(yearCal);
        yearPersianCal.setPersian(Calendar.MONTH, 0);
        yearCal.setTime(yearPersianCal.getMiladiDate().getTime());
        yearMonth = new MonthView[12];
        Integer yearNum = yearPersianCal.getiPersianYear() - 4;
        yearList = new ArrayList<>();
        monthViewList.clear();
        for (int i = 0; i < 12; i++) {
            if (yearMonth[i] == null)
                yearMonth[i] = MonthView.newInstance(yearCal, dayViewMode.Year);
            else
                yearMonth[i].initialMonth(yearCal);
            monthViewList.add(yearMonth[i]);
            yearCal.add(Calendar.MONTH, 1);
        }

        for (int i=0; i<9; i++)
            yearList.add(String.valueOf(yearNum + i));

        setColumns();

        yearHeader_tv.setTypeface(homaFont);
        yearHeader_tv.setText(PersianUtil.toPersian(yearPersianCal.getiPersianYear()));
        yearGridAdapter = new YearGridViewAdapter(monthViewList);
        yearGridView.setAdapter(yearGridAdapter);
        yearGridAdapter.notifyDataSetChanged();

        yearListGridAdapter = new YearListGridAdapter(yearList,4);
        yearListGrid.setAdapter(yearListGridAdapter);
        yearListGridAdapter.notifyDataSetChanged();

        setSelectedDate();

        yearListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 4) {
                    originalSelectedPersianDate.setPersian(Calendar.YEAR, Integer.valueOf(yearList.get(position)));
                    originalSelectedDate.setTime(originalSelectedPersianDate.getMiladiDate().getTime());
                    initialYear();
                    UPDATE_DAY_FULL = true;
                    UPDATE_MONTH = true;
                    UPDATE_DAY_LIST = true;
                }
                yearSwitchView(1);
            }
        });

        yearGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Year Item 2 click...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void setColumns() {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
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
        yearList.clear();
        Integer yearNum = yearCal.get(Calendar.YEAR) - 4;
        for (int i = 0; i < 12; i++) {
            yearMonth[i].initialMonth(yearCal);
            yearMonth[i].monthGridView.setOnItemClickListener(this);
            yearCal.add(Calendar.MONTH, 1);
            monthViewList.add(yearMonth[i]);
        }

        setColumns();

        yearHeader_tv.setText(String.valueOf(yearPersianCal.getiPersianYear()));
        yearGridAdapter.notifyDataSetChanged();

        setSelectedDate();
    }

    public void yearSwitchView(int view){
        if (CURRENT_VIEW == 1 && view == 2){
            completeYear.setVisibility(View.GONE);
            listYear.setVisibility(View.VISIBLE);
            CURRENT_VIEW = 2;
        }else if (CURRENT_VIEW == 2 && view == 1){
            listYear.setVisibility(View.GONE);
            completeYear.setVisibility(View.VISIBLE);
            CURRENT_VIEW = 1;
        }
    }

    public void setSelectedDate(){
        if (SELECTED_MONTH_INDEX >= 0)
            yearMonth[SELECTED_MONTH_INDEX].unSetSelectedDate();
        SELECTED_MONTH_INDEX = originalSelectedPersianDate.getiPersianMonth()-1;
        yearMonth[SELECTED_MONTH_INDEX].setSelectedDate();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, "Year Item click...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "Year click...", Toast.LENGTH_SHORT).show();
    }
}
