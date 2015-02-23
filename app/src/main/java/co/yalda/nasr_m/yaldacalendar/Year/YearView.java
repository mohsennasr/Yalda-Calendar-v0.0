package co.yalda.nasr_m.yaldacalendar.Year;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.YearGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class YearView extends Fragment {

    private View rootView;
    private MonthView[] yearMonth;
    private ArrayList<MonthView> monthViewList;
    private YearGridViewAdapter yearGridAdapter;
    private TextView yearHeader_tv;
    private GridView yearGridView;
    private Calendar yearCal = Calendar.getInstance();

    public static YearView newInstance(Calendar yearCal) {
        YearView yearView = new YearView();
        yearView.yearCal.setTime(yearCal.getTime());
        return yearView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.year_view, container, false);

        yearHeader_tv = (TextView) rootView.findViewById(R.id.year_view_header);
        yearGridView = (GridView) rootView.findViewById(R.id.year_grid_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (monthViewList == null)
            monthViewList = new ArrayList<MonthView>();
        initialYear();
    }

    //initial attributes
    private void initialYear(/*LayoutInflater inflater, @Nullable ViewGroup container*/) {
        PersianCalendar pCal = new PersianCalendar(yearCal);
        pCal.persianSet(Calendar.MONTH, 0);
        yearCal.setTime(pCal.getMiladiDate().getTime());
        yearMonth = new MonthView[12];
        for (int i = 0; i < 12; i++) {
            yearMonth[i] = MonthView.newInstance(yearCal, MainActivity.viewMode.Year);
            monthViewList.add(yearMonth[i]);
            yearCal.add(Calendar.MONTH, 1);
        }

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            yearGridView.setNumColumns(3);
        else
            yearGridView.setNumColumns(4);

        yearHeader_tv.setText(String.valueOf(pCal.getiPersianYear()));
        yearGridAdapter = new YearGridViewAdapter(monthViewList);
        yearGridView.setAdapter(yearGridAdapter);
        yearGridAdapter.notifyDataSetChanged();
    }
}
