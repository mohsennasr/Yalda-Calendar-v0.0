package co.yalda.nasr_m.yaldacalendar.Day;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Calendars.ArabicCalendar;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayEvent;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayFull;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Month;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Year;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.homaFont;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.lastUCDaySelected;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.tahomaFont;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.timesFont;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class DayUC extends Fragment{

    public View rootView;                                                                                           // Root View of Fragment
    public TextView mainDate_TV, secondDate_TV, thirdDate_TV;                                                       // Date TextViews (Month & Year View Mode
    private boolean isHoliday;                                                                                      // Is It Holiday
    public boolean isEnable = true;                                                                                 // Should be Clickable
    public PersianCalendar persianCalendar;                                                                        // Persian Calendar
    private Calendar miladiCalendar;                                                                                // Day Base Calendar
    private ArabicCalendar arabicCalendar;                             // Arabic Calendar
    private MainActivity.dayViewMode dayViewMode;                                                                   // Day View Mode -> Year, Month, DayList, DayFull
    private DayUC thisDay;
    private ViewGroup container;
    private LayoutInflater mInfalater;                                                                              // Layout Inflater

    /**
     * return new instance of class
     * @param miladiDate    base miladi calendar of day
     * @param isEnable      if day enable (in current month or remaining day of previous/next month
     * @param dayViewMode   view mode
     * @return              instance od DayUC class
     */
    public static DayUC newInstance(Calendar miladiDate, boolean isEnable, MainActivity.dayViewMode dayViewMode, ViewGroup container) {
        DayUC dayUC = new DayUC();
        dayUC.miladiCalendar = Calendar.getInstance();
        dayUC.miladiCalendar.setTime(miladiDate.getTime());
        dayUC.miladiCalendar.setFirstDayOfWeek(Calendar.SATURDAY);              // Set First Day of Week Based on Primary Calendar
        dayUC.persianCalendar  = new PersianCalendar(Calendar.getInstance());
        dayUC.persianCalendar.setMiladiDate(dayUC.miladiCalendar);              // Initial Persian Calendar
        dayUC.arabicCalendar = new ArabicCalendar(Calendar.getInstance());
        dayUC.arabicCalendar.setBaseMiladiCalendar(dayUC.miladiCalendar);       // Initial Arabic Calendar
        dayUC.isEnable = isEnable;                                              // Set Day Clickable
        dayUC.dayViewMode = dayViewMode;                                        // Set Day View Mode

        // Create Layout Inflater
        dayUC.mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dayUC.checkHoliday();                                                   // Check if Day is Holiday
        dayUC.viewSelector();                                                   // Initial Day Based on ViewMode

        return dayUC;
    }

    /**
     * when class created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve ViewMode From SaveInstanceState
        if (savedInstanceState != null)
            switch (savedInstanceState.getString("ViewMode")) {
                case "Month":
                    dayViewMode = Month;
                    break;
                case "Year":
                    dayViewMode = Year;
                    break;
                case "DayEvent":
                    dayViewMode = DayEvent;
                    break;
                case "DayFull":
                    dayViewMode = DayFull;
                    break;
            }
    }

    /**
     * when Fragment view created
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return                      rootView of Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewSelector();
        }
        return rootView;        // Return RootView of Fragment
    }

    /**
     * Set RootView and Initial it Based on ViewMode
     */
    private void viewSelector() {
        switch (dayViewMode) {
            // Header Mode for Showing in DayList View Mode
            case DayEvent:
                rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, container, false);
                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_tv);
                secondDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_miladi_tv);
                thirdDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_hejri_tv);
                mainDate_TV.setClickable(false);
                secondDate_TV.setClickable(false);
                thirdDate_TV.setClickable(false);
                initialDayHeader();
                break;
            // Single Date for Showing in YearView
            case Year:
                rootView = mInfalater.inflate(R.layout.day_uc_year_view, container, false);
                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
                initialYear();
                break;
            // Full Calendar View
            case DayFull:
                rootView = mInfalater.inflate(R.layout.day_full_view, container, false);
                break;
            // Triple Calendar for Showing in MonthView Mode
            case Month:
                rootView = mInfalater.inflate(R.layout.day_uc_month_view, container, false);
                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
                secondDate_TV = (TextView) rootView.findViewById(R.id.day_uc_second_date);
                thirdDate_TV = (TextView) rootView.findViewById(R.id.day_uc_third_date);
                initialMonth();
                break;
        }
    }

    /**
     * Initial DayEvent Mode for Showing as Header Frame in DayListView Mode
     */
    private void initialDayHeader() {
        switch (MainActivity.mainCalendarType) {
            case Solar:
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Solar, true));
                break;
            case Gregorian:
                mainDate_TV.setTypeface(timesFont);
                mainDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Gregorian, true));
                break;
            case Hejri:
                mainDate_TV.setTypeface(tahomaFont);
                mainDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Hejri, true));
                break;
        }

        if ((MainActivity.secondCalendarType != null)) {
            switch (MainActivity.secondCalendarType) {
                case Solar:
                    secondDate_TV.setTypeface(homaFont);
                    secondDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Solar, false));
                    break;
                case Gregorian:
                    secondDate_TV.setTypeface(timesFont);
                    secondDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Gregorian, false));
                    break;
                case Hejri:
                    secondDate_TV.setTypeface(tahomaFont);
                    secondDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Hejri, false));
                    break;
            }
        }

        if ((MainActivity.thirdCalendarType != null)) {
            switch (MainActivity.thirdCalendarType) {
                case Solar:
                    thirdDate_TV.setTypeface(homaFont);
                    thirdDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Solar, false));
                    break;
                case Gregorian:
                    thirdDate_TV.setTypeface(timesFont);
                    thirdDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Gregorian, false));
                    break;
                case Hejri:
                    thirdDate_TV.setTypeface(tahomaFont);
                    thirdDate_TV.setText(persianCalendar.getDateString(MainActivity.calendarType.Hejri, false));
                    break;
            }
        }
    }

    /**
     * update dayHeader view
     */
    public void updateDayHeader(){
        miladiCalendar.setTime(originalSelectedDate.getTime());
        persianCalendar.setMiladiDate(originalSelectedDate);
        arabicCalendar.setBaseMiladiCalendar(originalSelectedDate);
        initialDayHeader();
    }

    public void initialYear(){
        if (isEnable) {
            mainDate_TV.setTextColor(Color.BLACK);
        }else {
            mainDate_TV.setTextColor(Color.LTGRAY);
        }

        if (isHoliday){
            mainDate_TV.setTextColor(Color.RED);
        }

        setSelectedDay();

        mainDate_TV.setTypeface(homaFont);
        switch (MainActivity.mainCalendarType) {
            case Solar:
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
                break;
            case Gregorian:
                mainDate_TV.setTypeface(timesFont);
                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
                break;
        }
    }

    /**
     * Initial Triple Date Mode for Showing in MonthView Mode
     */
    public void initialMonth() {
        if (isEnable) {
            mainDate_TV.setTextColor(Color.BLACK);
            if ((MainActivity.secondCalendarType != null))
                secondDate_TV.setTextColor(Color.BLACK);
            if ((MainActivity.thirdCalendarType != null))
                thirdDate_TV.setTextColor(Color.BLACK);
        }else {
            mainDate_TV.setTextColor(Color.LTGRAY);
            if ((MainActivity.secondCalendarType != null))
                secondDate_TV.setTextColor(Color.LTGRAY);
            if ((MainActivity.thirdCalendarType != null))
                thirdDate_TV.setTextColor(Color.LTGRAY);
        }

        if (isHoliday){
            mainDate_TV.setTextColor(Color.RED);
            if ((MainActivity.secondCalendarType != null))
                secondDate_TV.setTextColor(Color.RED);
            if ((MainActivity.thirdCalendarType != null))
                thirdDate_TV.setTextColor(Color.RED);
        }

        setSelectedDay();

        mainDate_TV.setTypeface(homaFont);
        switch (MainActivity.mainCalendarType) {
            case Solar:
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
                break;
            case Gregorian:
                mainDate_TV.setTypeface(timesFont);
                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
                break;
        }

        if ((MainActivity.secondCalendarType != null)) {
            switch (MainActivity.secondCalendarType) {
                case Solar:
                    secondDate_TV.setTypeface(homaFont);
                    secondDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    secondDate_TV.setTypeface(timesFont);
                    secondDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
                    secondDate_TV.setTypeface(homaFont);
                    secondDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
                    break;
            }
        }

        if ((MainActivity.thirdCalendarType != null)) {
            switch (MainActivity.thirdCalendarType) {
                case Solar:
                    thirdDate_TV.setTypeface(homaFont);
                    thirdDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    thirdDate_TV.setTypeface(timesFont);
                    thirdDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
                    thirdDate_TV.setTypeface(homaFont);
                    thirdDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
                    break;
            }
        }
    }

    /**
     * update day in month/year view mode
     * @param calendar      base day calendar in month
     * @param isEnable      is day selectable
     */
    public void updateMonth(Calendar calendar, boolean isEnable){
        miladiCalendar.setTime(calendar.getTime());
        persianCalendar.setMiladiDate(miladiCalendar);
        arabicCalendar.setBaseMiladiCalendar(miladiCalendar);
        this.isEnable = isEnable;
        unSetSelectedDay();
        checkHoliday();
        initialMonth();
    }

    /**
     * check if it's holiday
     */
    private void checkHoliday() {
        isHoliday = ((Arrays.asList(MainActivity.holidayList).contains(persianCalendar.getPersianDateIndex()))
        || Arrays.asList(MainActivity.dayWeekHoliday).contains(String.valueOf(miladiCalendar.get(Calendar.DAY_OF_WEEK)))) && isEnable;
    }

    /**
     * set day selectable
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * set border for seleted day in month/year view
     */
    public void setSelectedDay(){
        if (persianCalendar.persianCompare(new PersianCalendar(Calendar.getInstance())) == 0 && isEnable) {     // if it's today, set background
            rootView.setBackgroundResource(R.drawable.background_rectangle);
            lastUCDaySelected = this;
        }else if (persianCalendar.persianCompare(originalSelectedPersianDate) == 0 && isEnable) {               // if it's selected day, set border
            rootView.setBackgroundResource(R.drawable.border_rectangle);
        }
    }

    /**
     * remove border from day month/year view if it's not today
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void unSetSelectedDay(){
        if (persianCalendar.persianCompare(new PersianCalendar(Calendar.getInstance())) != 0)
            rootView.setBackground(null);
    }

    /**
     * return day miladi calendar
     */
    public Calendar getMiladiCalendar() {
        return miladiCalendar;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ViewMode", dayViewMode.toString());
        Bundle bundle = new Bundle();
        bundle.putLong("MiladiDate", miladiCalendar.getTimeInMillis());
        bundle.putString("ViewMode", dayViewMode.toString());
        bundle.putBoolean("IsHoliday", isHoliday);
        bundle.putBoolean("IsEnable", isEnable);
        outState.putBundle("GeneralValues", bundle);
    }

    /**
     * show context menu on event long click
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.day_list_mode_event_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("رویداد");
            String[] menuItems = new String[]{"ویرایش", "حذف"};
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
}
