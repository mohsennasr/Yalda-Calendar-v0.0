package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthGridViewAdapter extends BaseAdapter {

    private ArrayList<DayUC> gridList;      //list of grid DayUC objects
    //    private ArrayList<String> gridList;      //list of grid DayUC objects
    Context context;

    public MonthGridViewAdapter(Context context, ArrayList<DayUC> gridList) {
        this.gridList = gridList;
//        if (gridList == null)
//            gridList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return gridList.size();
    }

    @Override
    public DayUC getItem(int position) {
        return gridList.get(position);
//        return DayUC.newInstance(Calendar.getInstance(), true, MainActivity.viewMode.DayHeader);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.day_uc_month_view, null);

        DayUC currentDay = gridList.get(position);

        TextView first, second, third;
        first = (TextView) convertView.findViewById(R.id.day_uc_main_date);
        second = (TextView) convertView.findViewById(R.id.day_uc_second_date);
        third = (TextView) convertView.findViewById(R.id.day_uc_third_date);

        switch (MainActivity.mainCalendarType) {
            case Solar:
                currentDay.persianCalendar = new PersianCalendar(currentDay.miladiCalendar);
                first.setText(String.valueOf(currentDay.persianCalendar.getiPersianDate()));
                break;
            case Gregorian:
                first.setText(String.valueOf(currentDay.miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
//                arabicCalendar = new ArabicCalendar(miladiCalendar);
//                mainDate_TV.setText(String.valueOf(arabicCalendar.get(Calendar.DATE)));
                break;
        }

        if (MainActivity.secondCalendarType != null) {
            switch (MainActivity.secondCalendarType) {
                case Solar:
                    currentDay.persianCalendar = new PersianCalendar(currentDay.miladiCalendar);
                    second.setText(String.valueOf(currentDay.persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    second.setText(String.valueOf(currentDay.miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
//                arabicCalendar = new ArabicCalendar(miladiCalendar);
//                secondDate_TV.setText(String.valueOf(arabicCalendar.get(Calendar.DATE)));
                    break;
            }
        }

        if (MainActivity.thirdCalendarType != null) {
            switch (MainActivity.thirdCalendarType) {
                case Solar:
                    currentDay.persianCalendar = new PersianCalendar(currentDay.miladiCalendar);
                    third.setText(String.valueOf(currentDay.persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    third.setText(String.valueOf(currentDay.miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
//                    arabicCalendar = new ArabicCalendar(miladiCalendar);
//                thirdDate_TV.setText(String.valueOf(arabicCalendar.get(Calendar.DATE)));
                    break;
            }
        }

        return convertView;
    }
}
