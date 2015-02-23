package co.yalda.nasr_m.yaldacalendar.Day;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/23/2015.
 */
public class DayLinearLayout extends Fragment {

    private boolean isHoliday;                  //is it holiday
    private boolean isEnable = true;         //should be clickable
    public View rootView;                      // root view of fragment
    private ViewGroup parent;
    private PersianCalendar persianCalendar;
    private Calendar miladiCalendar = Calendar.getInstance();
    private MainActivity.viewMode viewMode;
    private Context context;
    private TextView mainDate_TV, secondDate_TV, thirdDate_TV;

    public static DayLinearLayout newInstance(Context context, Calendar miladiDate, boolean isEnable,
                                              MainActivity.viewMode viewMode) {
        DayLinearLayout dayLinearLayout = new DayLinearLayout();
        dayLinearLayout.miladiCalendar.setTime(miladiDate.getTime());
        dayLinearLayout.miladiCalendar.setFirstDayOfWeek(Calendar.SATURDAY);
        dayLinearLayout.isEnable = isEnable;
        dayLinearLayout.viewMode = viewMode;
        dayLinearLayout.context = context;

        LayoutInflater mInfalater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewMode) {
            case DayHeader:
                dayLinearLayout.rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, null);
                dayLinearLayout.initialDayHeader();
                break;
            case DaySimple:
                break;
            case DayFull:
                break;
            case Month:
                dayLinearLayout.rootView = mInfalater.inflate(R.layout.day_uc_month_view, null);
                dayLinearLayout.initialMonth();
                break;
        }
        return dayLinearLayout;
    }

    private void initialDayHeader() {
        mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_tv);
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

//    private void initialDayHeader(){
//        View root = ((LayoutInflater)(getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
//                .inflate(R.layout.day_uc_header_mode_view, (android.view.ViewGroup) getTargetFragment().getView().findViewById(R.id.day_list_mode_frame));
//        mainDate_TV = (TextView) root.findViewById(R.id.day_uc_header_mode_tv);
//        switch (MainActivity.mainCalendarType){
//            case Solar:
//                persianCalendar = new PersianCalendar(miladiCalendar);
//                mainDate_TV.setText(persianCalendar.getPersianFullDate());
//                break;
//            case Gregorian:
//                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
//                break;
//            case Hejri:
////                mainDate_TV.setText(String.valueOf(arabicCalendar.getDate()));
//                break;
//        }
//    }

    private void initialMonth() {
//        View root = ((LayoutInflater)(getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
//                .inflate(R.layout.day_uc_month_view, null);
        mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
        secondDate_TV = (TextView) rootView.findViewById(R.id.day_uc_second_date);
        thirdDate_TV = (TextView) rootView.findViewById(R.id.day_uc_third_date);

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

        if (MainActivity.secondCalendarType != null) {
            secondDate_TV = (TextView) rootView.findViewById(R.id.day_uc_second_date);
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

        if (MainActivity.thirdCalendarType != null) {
            thirdDate_TV = (TextView) rootView.findViewById(R.id.day_uc_third_date);
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
}
