//package co.yalda.nasr_m.yaldacalendar.Day;
//
//import android.annotation.TargetApi;
//import android.app.AlertDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.ContextMenu;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//
//import co.yalda.nasr_m.yaldacalendar.Adapters.EventsListViewAdapter;
//import co.yalda.nasr_m.yaldacalendar.Adapters.NoteListViewAdapter;
//import co.yalda.nasr_m.yaldacalendar.Calendars.ArabicCalendar;
//import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
//import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
//import co.yalda.nasr_m.yaldacalendar.Handler.AddEvent;
//import co.yalda.nasr_m.yaldacalendar.Handler.Events;
//import co.yalda.nasr_m.yaldacalendar.MainActivity;
//import co.yalda.nasr_m.yaldacalendar.R;
//
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.arabicFont;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayFull;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayEvent;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayList;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Month;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Year;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.homaFont;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.timesFont;
//
///**
// * Created by Nasr_M on 2/17/2015.
// */
//public class DayUC1 extends Fragment {
//
//    public View rootView;                                                                                           // Root View of Fragment
//    public TextView mainDate_TV, secondDate_TV, thirdDate_TV;                                                       // Date TextViews (Month & Year View Mode
//    private boolean isHoliday;                                                                                      // Is It Holiday
//    public boolean isEnable = true;                                                                                 // Should be Clickable
//    private PersianCalendar persianCalendar = new PersianCalendar(Calendar.getInstance());                          // Persian Calendar
//    private Calendar miladiCalendar = Calendar.getInstance();                                                       // Day Base Calendar
//    private ArabicCalendar arabicCalendar = new ArabicCalendar(Calendar.getInstance());                             // Arabic Calendar
//    private MainActivity.dayViewMode dayViewMode;                                                                   // Day View Mode -> Year, Month, DayList, DayFull
//    private TextView dayName, dayDate, dayliNote1, dayliNote2, monthName, miladiFullDate, jalaliFulldate,           // Day Full View Mode TextViews
//            arabicFullDate, miladiDate, jalaliDate, arabicDate, holyDayNote;
//    private String DateIndex;
//    private String[] MonthName = {"January", "February", "March", "April", "May", "June", "July",                   // Miladi Month Names
//            "August", "September", "October", "November", "December"};
//    private NoteListViewAdapter adapter;                                                                            // List View Adapter for Note ListView in DayFull Mode
//    private ArrayList<String> notes = new ArrayList<String>();                                                           // Notes Array List for DayFull View Mode
//    private ArrayList<Events> eventsArrayList;                                                                      // Events Array List for DayList View Mode
//    private EventsListViewAdapter dayListadapter;                                                                   // Event List View Adapter for DayList View Mode
//    private ListView dayEventLV;                                                                                    // Events List View
//    private ListView noteList;                                                                                      // Notes List View
//    private DayUC1 dayUCListHeader;                                                                                  // DayUC Object for Showing in DayList View Mode (Header)
//    private LayoutInflater mInfalater;                                                                              // Layout Inflater
//    private int CURRENT_SELECTED_EVENT = -1;                                                                        // Selected event in dayList mode
//
//    /**
//     * return new instance of class
//     * @param miladiDate    base miladi calendar of day
//     * @param isEnable      if day enable (in current month or remaining day of previous/next month
//     * @param dayViewMode   view mode
//     * @return              instance od DayUC class
//     */
//    public static DayUC1 newInstance(Calendar miladiDate, boolean isEnable, MainActivity.dayViewMode dayViewMode) {
//        DayUC1 dayUC = new DayUC1();
//        dayUC.miladiCalendar.setTime(miladiDate.getTime());
//        dayUC.miladiCalendar.setFirstDayOfWeek(Calendar.SATURDAY);              // Set First Day of Week Based on Primary Calendar
//        dayUC.persianCalendar.setMiladiDate(dayUC.miladiCalendar);              // Initial Persian Calendar
//        dayUC.arabicCalendar.setBaseMiladiCalendar(dayUC.miladiCalendar);       // Initial Arabic Calendar
//        dayUC.isEnable = isEnable;                                              // Set Day Clickable
//        dayUC.dayViewMode = dayViewMode;                                        // Set Day View Mode
//
//        // Create Layout Inflater
//        dayUC.mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        dayUC.checkHoliday();                                                   // Check if Day is Holiday
//        dayUC.viewSelector();                                                   // Initial Day Based on ViewMode
//
//        return dayUC;
//    }
//
//    /**
//     * when class created
//     * @param savedInstanceState
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Retrieve ViewMode From SaveInstanceState
//        if (savedInstanceState != null)
//            switch (savedInstanceState.getString("ViewMode")) {
//                case "Month":
//                    dayViewMode = Month;
//                    break;
//                case "Year":
//                    dayViewMode = Year;
//                    break;
//                case "DayEvent":
//                    dayViewMode = DayEvent;
//                    break;
//                case "DayFull":
//                    dayViewMode = DayFull;
//                    break;
//                case "DayList":
//                    dayViewMode = DayList;
//                    break;
//            }
//    }
//
//    /**
//     * when Fragment view created
//     * @param inflater
//     * @param container
//     * @param savedInstanceState
//     * @return                      rootView of Fragment
//     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            viewSelector();
//        }
//
//        // If in DayList View Mode, Attach DayList Fragment to rootView
//        if (dayViewMode == DayList)
//            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUCListHeader).commit();
//        return rootView;        // Return RootView of Fragment
//    }
//
//    /**
//     * Set RootView and Initial it Based on ViewMode
//     */
//    private void viewSelector() {
//        switch (dayViewMode) {
//            // Header Mode for Showing in DayList View Mode
//            case DayEvent:
//                rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, null);
//                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_tv);
//                mainDate_TV.setClickable(false);
//                mainDate_TV.setTypeface(homaFont);
//                initialDayHeader();
//                break;
//            // Single Date for Showing in YearView
//            case Year:
//                rootView = mInfalater.inflate(R.layout.day_uc_year_view, null);
//                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
//                initialMonth();
//                break;
//            // Full Calendar View
//            case DayFull:
//                rootView = mInfalater.inflate(R.layout.day_full_view, null);
//                initialDayFull();
//                break;
//            // Triple Calendar for Showing in MonthView Mode
//            case Month:
//                rootView = mInfalater.inflate(R.layout.day_uc_month_view, null);
//                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
//                secondDate_TV = (TextView) rootView.findViewById(R.id.day_uc_second_date);
//                thirdDate_TV = (TextView) rootView.findViewById(R.id.day_uc_third_date);
//                initialMonth();
//                break;
//            // Day List View Mode
//            case DayList:
//                rootView = mInfalater.inflate(R.layout.day_uc_list_mode_view, null);
//                initialDayList();
//                break;
//        }
//    }
//
//    /**
//     * Initial DayEvent Mode for Showing as Header Frame in DayListView Mode
//     */
//    private void initialDayHeader() {
//        switch (MainActivity.mainCalendarType) {
//            case Solar:
//                mainDate_TV.setText(persianCalendar.getPersianFullDate());
//                break;
//            case Gregorian:
//                mainDate_TV.setText(miladiCalendar.getTime().toString());
//                break;
//            case Hejri:
//                mainDate_TV.setText(arabicCalendar.getArabicFullDate());
//                break;
//        }
//    }
//
//    /**
//     * update dayHeader view
//     */
//    public void updateDayHeader(){
//        miladiCalendar.setTime(originalSelectedDate.getTime());
//        persianCalendar.setMiladiDate(originalSelectedDate);
//        arabicCalendar.setBaseMiladiCalendar(originalSelectedDate);
//        initialDayHeader();
//    }
//
//    /**
//     * Initial Triple Date Mode for Showing in MonthView Mode
//     */
//    public void initialMonth() {
//        if (isEnable) {
//            mainDate_TV.setTextColor(Color.BLACK);
//            if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year))
//                secondDate_TV.setTextColor(Color.BLACK);
//            if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year))
//                thirdDate_TV.setTextColor(Color.BLACK);
//        }else {
//            mainDate_TV.setTextColor(Color.LTGRAY);
//            if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year))
//                secondDate_TV.setTextColor(Color.LTGRAY);
//            if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year))
//                thirdDate_TV.setTextColor(Color.LTGRAY);
//        }
//
//        if (isHoliday){
//            mainDate_TV.setTextColor(Color.RED);
//            if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year))
//                secondDate_TV.setTextColor(Color.RED);
//            if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year))
//                thirdDate_TV.setTextColor(Color.RED);
//        }
//
//        setSelectedDay();
//
//        mainDate_TV.setTypeface(homaFont);
//        switch (MainActivity.mainCalendarType) {
//            case Solar:
//                mainDate_TV.setTypeface(homaFont);
//                mainDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
//                break;
//            case Gregorian:
//                mainDate_TV.setTypeface(timesFont);
//                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
//                break;
//            case Hejri:
//                mainDate_TV.setTypeface(homaFont);
//                mainDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
//                break;
//        }
//
//        if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
//            switch (MainActivity.secondCalendarType) {
//                case Solar:
//                    secondDate_TV.setTypeface(homaFont);
//                    secondDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
//                    break;
//                case Gregorian:
//                    secondDate_TV.setTypeface(timesFont);
//                    secondDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
//                    break;
//                case Hejri:
//                    secondDate_TV.setTypeface(homaFont);
//                    secondDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
//                    break;
//            }
//        }
//
//        if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
//            switch (MainActivity.thirdCalendarType) {
//                case Solar:
//                    thirdDate_TV.setTypeface(homaFont);
//                    thirdDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
//                    break;
//                case Gregorian:
//                    thirdDate_TV.setTypeface(timesFont);
//                    thirdDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
//                    break;
//                case Hejri:
//                    thirdDate_TV.setTypeface(homaFont);
//                    thirdDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
//                    break;
//            }
//        }
//    }
//
//    /**
//     * update day in month/year view mode
//     * @param calendar      base day calendar in month
//     * @param isEnable      is day selectable
//     */
//    public void updateMonth(Calendar calendar, boolean isEnable){
//        miladiCalendar.setTime(calendar.getTime());
//        persianCalendar.setMiladiDate(miladiCalendar);
//        arabicCalendar.setBaseMiladiCalendar(miladiCalendar);
//        this.isEnable = isEnable;
//        unSetSelectedDay();
//        checkHoliday();
//        initialMonth();
//    }
//
//    /**
//     * Initial DayFull View Mode
//     */
//    private void initialDayFull() {
//        dayFullInitializer();
//        dayFullPutData();
//    }
//
//    /**
//     * first initialization of dayFull view
//     */
//    private void dayFullInitializer() {
//
//        //TODO DB connection should be set
//        //create DB object
////        try {
////            calendarDB = new MyDB(MainActivity.context);
////            calendarDB.openDB();
////            calendarDB.commentDBPull();
////            calendarDB.setHD();
////        }catch (Exception e){
////            e.printStackTrace();
////        }
//
//
//        holyDayNote = (TextView) rootView.findViewById(R.id.holiday_note_tv);
//        holyDayNote.setTypeface(homaFont);
//        holyDayNote.setTextColor(Color.BLACK);
//
//        noteList = (ListView) rootView.findViewById(R.id.note_list_lv);
//
//        adapter = new NoteListViewAdapter(context, notes);
//        noteList.setAdapter(adapter);
//        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final TextView input = new TextView(context);
//                input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                final String[] value = {new String()};
//                value[0] = notes.get(position);
//                input.setText(value[0]);
//
//                AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
//                inputNote.setTitle("مشاهده یادداشت");
//                inputNote.setView(input);
//
//                inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//                inputNote.show();
//            }
//        });
//
//        noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                final EditText input = new EditText(context);
//                input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                final String[] value = {new String()};
//                value[0] = notes.get(position);
//                input.setText(value[0]);
//
//                AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
//                inputNote.setTitle("ویرایش یادداشت");
//                inputNote.setView(input);
//
//                inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        if (input.getText().toString().equals("")){
//                            notes.remove(position);
//                        }else {
//                            notes.remove(position);
//                            notes.add(position, input.getText().toString());
//                        }
//                        adapter = new NoteListViewAdapter(context, notes);
//                        noteList.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                        //TODO update edited note in listView and DB
//                    }
//                });
//                inputNote.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        notes.remove(position);
//                        adapter = new NoteListViewAdapter(context, notes);
//                        noteList.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                        //TODO remove note from DB and listView
//                    }
//                });
//                inputNote.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // do nothing
//                    }
//                });
//                inputNote.show();
//                return true;
//            }
//        });
//
//        dayDate = (TextView) rootView.findViewById(R.id.date_tv);
//        dayDate.setTypeface(homaFont);
//        dayliNote1 = (TextView) rootView.findViewById(R.id.dayli_note_1_tv);
//        dayliNote1.setTypeface(homaFont);
//        dayliNote1.setTextColor(Color.BLACK);
//        dayliNote2 = (TextView) rootView.findViewById(R.id.dayli_note_2_tv);
//        dayliNote2.setTypeface(homaFont);
//        dayliNote2.setTextColor(Color.BLACK);
//        dayName = (TextView) rootView.findViewById(R.id.day_name_tv);
//        dayName.setTypeface(homaFont);
//        monthName = (TextView) rootView.findViewById(R.id.month_name_tv);
//        monthName.setTypeface(homaFont);
//        jalaliFulldate = (TextView) rootView.findViewById(R.id.jalali_date_full_tv);
//        jalaliFulldate.setTypeface(homaFont);
//        jalaliDate = (TextView) rootView.findViewById(R.id.jalali_date_tv);
//        jalaliDate.setTypeface(homaFont);
//        arabicFullDate = (TextView) rootView.findViewById(R.id.arabic_date_full_tv);
//        arabicFullDate.setTypeface(arabicFont);
//        arabicDate = (TextView) rootView.findViewById(R.id.arabic_date_tv);
//        arabicDate.setTypeface(arabicFont);
//        miladiFullDate = (TextView) rootView.findViewById(R.id.miladi_date_full_tv);
//        miladiFullDate.setTypeface(timesFont);
//        miladiDate = (TextView) rootView.findViewById(R.id.miladi_date_tv);
//        miladiDate.setTypeface(timesFont);
//    }
//
//    /**
//     * set calendar data
//     */
//    public void dayFullPutData() {
//        ContentValues calendarContent, noteContents;
//        int firstTextColor, secondTextColor;
//
//        //clear notes list for referesh
//        notes.clear();
//
//        //set date index based on calendar
//        DateIndex = originalSelectedDate.get(Calendar.YEAR) + "" +
//                (originalSelectedDate.get(Calendar.MONTH) + 1) + "" + originalSelectedDate.get(Calendar.DAY_OF_MONTH);
//
//        //read data from DB
////        try {
//        //read calendar data
////            calendarContent = calendarDB.readData(DateIndex);
//
//        //read notes
////            noteContents = calendarDB.getNote(DateIndex);
//
//        //set daily note
////            dayliNote1.setText(calendarContent.getAsString(MyDB.COLUMN_VERSE_1));
////            dayliNote2.setText(calendarContent.getAsString(MyDB.COLUMN_VERSE_2));
//        dayliNote1.setText("سطر اول");
//        dayliNote2.setText("سطر دوم");
//
//        //set holiday note
////            holyDayNote.setText(calendarContent.getAsString(MyDB.COLUMN_HOLYDAY));
//
//        //add notes to array list
////            for(int i=0; i< noteContents.size() ; i++)
////                notes.add(noteContents.getAsString(MyDB.COLUMN_NOTE + i));
////        for (int i = 0; i < 15; i++)
////            notes.add("یادداشت " + i);
//
//        //update array list adapter
////        adapter.notifyDataSetChanged();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        //get arabic date
////        Calendar temporary = Calendar.getInstance();
////        temporary.setTime(originalSelectedDate.getTime());
////        temporary.add(Calendar.DATE, -1);
//        String[] ADate = new String[4];
//
//        //convert digits to persian digit
////        persianCalendar.setMiladiDate(originalSelectedDate);
////        arabicCalendar.setBaseMiladiCalendar(originalSelectedDate);
////        PersianCalendar pcal = new PersianCalendar(originalSelectedDate);
////        String[] PDate = new String[5];
////        PDate[0] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianYear()));
////        PDate[1] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianMonth()));
////        PDate[2] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianDate()));
////        PDate[3] = persianCalendar.getPersianMonthName();
////        PDate[4] = persianCalendar.getPersianDayName();
////
////        //convert digits to arabic digits
////        ADate[0] = PersianUtil.toArabic(arabicCalendar.getArabicYear());
////        ADate[1] = PersianUtil.toArabic(arabicCalendar.getArabicMonth());
////        ADate[2] = PersianUtil.toArabic(arabicCalendar.getArabicDate());
////        ADate[3] = arabicCalendar.getArabicMonthName();
//
//        //set text color to RED for holidays
////        if (PDate[4] == "جمعه" || !holyDayNote.getText().toPersian().isEmpty()) {
//        if (isHoliday){
//            firstTextColor = Color.RED;
//            secondTextColor = Color.RED;
////            monthName.setTextColor(Color.RED);
////            dayName.setTextColor(Color.RED);
////            dayDate.setTextColor(Color.RED);
////            holyDayNote.setTextColor(Color.RED);
//        } else {
//            firstTextColor = Color.BLACK;
//            secondTextColor = Color.WHITE;
////            monthName.setTextColor(Color.BLACK);
////            dayName.setTextColor(Color.BLACK);
////            dayDate.setTextColor(Color.BLACK);
////            holyDayNote.setTextColor(Color.BLACK);
//        }
//
//        //set calendar textViews
//        holyDayNote.setText("مناسبت");
//        holyDayNote.setTextColor(firstTextColor);
//        dayName.setText(persianCalendar.getPersianDayName());
//        dayName.setTextColor(firstTextColor);
//        dayDate.setText(PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianDate())));
//        dayDate.setTextColor(firstTextColor);
//        monthName.setText(persianCalendar.getPersianMonthName());
//        monthName.setTextColor(firstTextColor);
//        jalaliFulldate.setText(persianCalendar.getPersianMonthName() + " "
//                + PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianYear())));
//        jalaliFulldate.setTextColor(secondTextColor);
//        jalaliDate.setText(PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianDate())));
//        jalaliDate.setTextColor(secondTextColor);
//        miladiFullDate.setText(originalSelectedDate.get(Calendar.YEAR) + " "
//                + MonthName[originalSelectedDate.get(Calendar.MONTH)]);
//        miladiFullDate.setTextColor(secondTextColor);
//        miladiDate.setText(originalSelectedDate.get(Calendar.DAY_OF_MONTH) + "");
//        miladiDate.setTextColor(secondTextColor);
//        arabicFullDate.setText(arabicCalendar.getArabicMonthName() + " " + PersianUtil.toArabic(arabicCalendar.getArabicYear()));
//        arabicFullDate.setTextColor(secondTextColor);
//        arabicDate.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
//        arabicDate.setTextColor(secondTextColor);
//        monthName.setTypeface(homaFont);
//    }
//
//    /**
//     * update dayFull view
//     */
//    public void updateDayFull(){
//        miladiCalendar.setTime(originalSelectedDate.getTime());
//        persianCalendar.setMiladiDate(miladiCalendar);
//        arabicCalendar.setBaseMiladiCalendar(miladiCalendar);
//        dayFullPutData();
//    }
//
//    /**
//    Initial DayList Mode
//     */
//    private void initialDayList() {
//        dayUCListHeader = DayUC1.newInstance(miladiCalendar, false, DayEvent);
//
//        //TODO get event list from DB
//
//        eventsArrayList = new ArrayList<>();
//        Events events = Events.newInstance(context, Calendar.getInstance());
//        events.setEvent("asdf0", "asdfasdfasdfasdfa sdf ", "10:25", "16:56");
//        eventsArrayList.add(events);
//        dayListadapter = new EventsListViewAdapter(eventsArrayList, context);
//
//        dayEventLV = (ListView) rootView.findViewById(R.id.day_list_mode_event_list);
//        dayEventLV.setAdapter(dayListadapter);
//        dayListadapter.notifyDataSetChanged();
//
//        // show event detail on event list item click
//        dayEventLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String title, detail, start, end;
//                title = eventsArrayList.get(position).getTitle();
//                detail = eventsArrayList.get(position).getDescription();
//                start = eventsArrayList.get(position).getStartTime();
//                end = eventsArrayList.get(position).getEndTime();
//
//                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View eventView = mInflater.inflate(R.layout.event_show_dialog, null);
//
//                TextView titleEvent = (TextView) eventView.findViewById(R.id.event_title_view);
//                TextView detailEvent = (TextView) eventView.findViewById(R.id.event_detail_view);
//                TextView startEvent = (TextView) eventView.findViewById(R.id.start_time);
//                TextView endEvent = (TextView) eventView.findViewById(R.id.end_time);
//
//                titleEvent.setText(title);
//                detailEvent.setText(detail);
//                startEvent.setText(start);
//                endEvent.setText(end);
//
//                //create dialog
//                AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
//                inputNote.setTitle("مشاهده رویداد");
//                inputNote.setView(eventView);
//
//                //set dialog buttons
//                inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//
//                //show dialog
//                inputNote.show();
//            }
//        });
//
//        // register for context menu on long click
//        registerForContextMenu(dayEventLV);
//    }
//
//    /**
//     * update dayList view mode
//     */
//    public void updateDayList(){
//        miladiCalendar.setTime(originalSelectedDate.getTime());
//        persianCalendar.setMiladiDate(miladiCalendar);
//        arabicCalendar.setBaseMiladiCalendar(miladiCalendar);
//        dayUCListHeader.updateDayHeader();
//        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUCListHeader).commit();
//        initialDayList();
//    }
//
//    /**
//     * check if it's holiday
//     */
//    private void checkHoliday() {
//        isHoliday = ((Arrays.asList(MainActivity.holidayList).contains(persianCalendar.getPersianDateIndex()))
//        || Arrays.asList(MainActivity.dayWeekHoliday).contains(String.valueOf(miladiCalendar.get(Calendar.DAY_OF_WEEK)))) && isEnable;
//    }
//
//    /**
//     * add new note to note list in dayFull view mode
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public void addNote(){
//        //create dialog layout
//        final EditText input = new EditText(context);
//        input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        final String[] value = {""};
//
//        //create dialog
//        AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
//        inputNote.setTitle("اضافه کردن یادداشت");
//        inputNote.setView(input);
//
//        //set dialog buttons
//        inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                value[0] = input.getText().toString();
//                notes.add(value[0]);
//                adapter = new NoteListViewAdapter(context, notes);
//                noteList.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                //TODO add note to DB
//            }
//        });
//        inputNote.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//            }
//        });
//
//        //show dialog
//        inputNote.show();
//    }
//
//    /**
//     * add new event to event list in dayList view mode
//     */
//    public void addEvent(Events events){
//        eventsArrayList.add(events);
//        dayListadapter = new EventsListViewAdapter(eventsArrayList, context);
//        dayEventLV.setAdapter(dayListadapter);
//        dayListadapter.notifyDataSetChanged();
//    }
//
//    /**
//     * update event list
//     */
//    public void updateEvent(Events events){
//        if (CURRENT_SELECTED_EVENT >=0) {
//            eventsArrayList.remove(CURRENT_SELECTED_EVENT);
//            eventsArrayList.add(CURRENT_SELECTED_EVENT, events);
//            dayListadapter = new EventsListViewAdapter(eventsArrayList, context);
//            dayEventLV.setAdapter(dayListadapter);
//            dayListadapter.notifyDataSetChanged();
//            CURRENT_SELECTED_EVENT = -1;
//        }
//    }
//
//    /**
//     * set day selectable
//     */
//    public void setEnable(boolean isEnable) {
//        this.isEnable = isEnable;
//    }
//
//    /**
//     * set border for seleted day in month/year view
//     */
//    public void setSelectedDay(){
//        if (persianCalendar.persianCompare(new PersianCalendar(Calendar.getInstance())) == 0 && isEnable) {     // if it's today, set background
//            rootView.setBackgroundResource(R.drawable.background_rectangle);
//        }else if (persianCalendar.persianCompare(originalSelectedPersianDate) == 0 && isEnable) {               // if it's selected day, set border
//            rootView.setBackgroundResource(R.drawable.border_rectangle);
//        }
//    }
//
//    /**
//     * remove border from day month/year view if it's not today
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public void unSetSelectedDay(){
//        if (persianCalendar.persianCompare(new PersianCalendar(Calendar.getInstance())) != 0)
//            rootView.setBackground(null);
//    }
//
//    /**
//     * return day persian calendar
//     */
//    public PersianCalendar getPersianCalendar() {
//        return persianCalendar;
//    }
//
//    /**
//     * return day miladi calendar
//     */
//    public Calendar getMiladiCalendar() {
//        return miladiCalendar;
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("ViewMode", dayViewMode.toString());
//        Bundle bundle = new Bundle();
//        bundle.putLong("MiladiDate", miladiCalendar.getTimeInMillis());
//        bundle.putString("ViewMode", dayViewMode.toString());
//        bundle.putBoolean("IsHoliday", isHoliday);
//        bundle.putBoolean("IsEnable", isEnable);
//        outState.putBundle("GeneralValues", bundle);
//    }
//
//    /**
//     * show context menu on event long click
//     * @param menu
//     * @param v
//     * @param menuInfo
//     */
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        if (v.getId()==R.id.day_list_mode_event_list) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
//            menu.setHeaderTitle("رویداد");
//            String[] menuItems = new String[]{"ویرایش", "حذف"};
//            for (int i = 0; i<menuItems.length; i++) {
//                menu.add(Menu.NONE, i, i, menuItems[i]);
//            }
//        }
//    }
//
//    /**
//     * select action for selected event : Edit , Remove
//     * @param item      selected item
//     * @return          true
//     */
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        switch (item.getItemId()){
//            case 0:             //Edit Event
//                Intent eventActivity = new Intent(context, AddEvent.class);
//                eventActivity.putExtra("EventMode", MainActivity.eventMode.EditEvent.toString());
//                eventActivity.putExtra("Title", eventsArrayList.get(info.position).getTitle());
//                eventActivity.putExtra("Detail", eventsArrayList.get(info.position).getDescription());
//                eventActivity.putExtra("isHoleDay", eventsArrayList.get(info.position).isHoleDay());
//                if (!eventsArrayList.get(info.position).isHoleDay()) {
//                    eventActivity.putExtra("Start_Time", eventsArrayList.get(info.position).getStartTime());
//                    eventActivity.putExtra("End_Time", eventsArrayList.get(info.position).getEndTime());
//                }
////                eventActivity.putExtra("Date", eventsArrayList.get(info.position).getCal().getTimeInMillis());
//                CURRENT_SELECTED_EVENT = info.position;
//                startActivityForResult(eventActivity, 0);
//                break;
//            case 1:             //remove event
//                eventsArrayList.remove(info.position);
//                dayListadapter = new EventsListViewAdapter(eventsArrayList, context);
//                dayEventLV.setAdapter(dayListadapter);
//                dayListadapter.notifyDataSetChanged();
//                break;
//        }
//        return true;
//    }
//}
