package co.yalda.nasr_m.yaldacalendar.Day;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewMode.DayFull;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewMode.DayHeader;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewMode.Month;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewMode.Year;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class DayUC extends Fragment {

    private boolean isHoliday;                  //is it holiday
    private boolean isEnable = true;         //should be clickable
    public View rootView;                      // root view of fragment
    private PersianCalendar persianCalendar;
    private Calendar miladiCalendar = Calendar.getInstance();
    private MainActivity.viewMode viewMode;

    public TextView mainDate_TV, secondDate_TV, thirdDate_TV;

    public static DayUC newInstance(Calendar miladiDate, boolean isEnable, MainActivity.viewMode viewMode) {
        DayUC dayUC = new DayUC();
        dayUC.miladiCalendar.setTime(miladiDate.getTime());
        dayUC.miladiCalendar.setFirstDayOfWeek(Calendar.SATURDAY);
        dayUC.isEnable = isEnable;
        dayUC.viewMode = viewMode;

        LayoutInflater mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewMode) {
            case DayHeader:
                dayUC.rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, null);
                dayUC.initialDayHeader();
                break;
            case Year:
                dayUC.rootView = mInfalater.inflate(R.layout.day_uc_year_view, null);
                dayUC.initialMonth();
                break;
            case DayFull:
                break;
            case Month:
                dayUC.rootView = mInfalater.inflate(R.layout.day_uc_month_view, null);
                dayUC.initialMonth();
                break;
        }

        return dayUC;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            switch (savedInstanceState.getString("ViewMode")){
                case "Month":
                    viewMode = Month;
                    break;
                case "Year":
                    viewMode = Year;
                    break;
                case "DayHeader":
                    viewMode = DayHeader;
                    break;
                case "DayFull":
                    viewMode = DayFull;
                    break;
            }

        if (rootView == null){
            LayoutInflater mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch (viewMode) {
                case DayHeader:
                    rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, null);
                    initialDayHeader();
                    break;
                case Year:
                    rootView = mInfalater.inflate(R.layout.day_uc_year_view, null);
                    initialMonth();
                    break;
                case DayFull:
                    break;
                case Month:
                    rootView = mInfalater.inflate(R.layout.day_uc_month_view, null);
                    initialMonth();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void initialDayHeader() {
        mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_tv);
        mainDate_TV.setClickable(isEnable);
        switch (MainActivity.mainCalendarType) {
            case Solar:
                persianCalendar = new PersianCalendar(miladiCalendar);
                mainDate_TV.setText(persianCalendar.getPersianFullDate());
                break;
            case Gregorian:
                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
//                mainDate_TV.setText(String.valueOf(arabicCalendar.getDate()));
                break;
        }
    }

    private void initialMonth() {
//        View root = ((LayoutInflater)(getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
//                .inflate(R.layout.day_uc_month_view, null);
        mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
        mainDate_TV.setClickable(isEnable);

        switch (MainActivity.mainCalendarType) {
            case Solar:
                persianCalendar = new PersianCalendar(miladiCalendar);
                mainDate_TV.setText(String.valueOf(persianCalendar.getiPersianDate()));
                break;
            case Gregorian:
                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
//                arabicCalendar = new ArabicCalendar(miladiCalendar);
//                mainDate_TV.setText(String.valueOf(arabicCalendar.get(Calendar.DATE)));
                break;
        }

        if ((MainActivity.secondCalendarType != null) && (viewMode != MainActivity.viewMode.Year)) {
            secondDate_TV = (TextView) rootView.findViewById(R.id.day_uc_second_date);
            secondDate_TV.setClickable(isEnable);
            switch (MainActivity.secondCalendarType) {
                case Solar:
                    persianCalendar = new PersianCalendar(miladiCalendar);
                    secondDate_TV.setText(String.valueOf(persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    secondDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
//                arabicCalendar = new ArabicCalendar(miladiCalendar);
//                secondDate_TV.setText(String.valueOf(arabicCalendar.get(Calendar.DATE)));
                    break;
            }
        }

        if ((MainActivity.thirdCalendarType != null) && (viewMode != MainActivity.viewMode.Year)) {
            thirdDate_TV = (TextView) rootView.findViewById(R.id.day_uc_third_date);
            thirdDate_TV.setClickable(isEnable);
            switch (MainActivity.thirdCalendarType) {
                case Solar:
                    persianCalendar = new PersianCalendar(miladiCalendar);
                    thirdDate_TV.setText(String.valueOf(persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    thirdDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
//                    arabicCalendar = new ArabicCalendar(miladiCalendar);
//                thirdDate_TV.setText(String.valueOf(arabicCalendar.get(Calendar.DATE)));
                    break;
            }
        }
    }

    private void initialDayFull() {

    }

    private void initialDaySimple() {

    }

    private void checkHoliday() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ViewMode", viewMode.toString());
        super.onSaveInstanceState(outState);
    }
}
