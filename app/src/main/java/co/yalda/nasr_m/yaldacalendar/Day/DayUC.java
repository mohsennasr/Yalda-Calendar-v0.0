package co.yalda.nasr_m.yaldacalendar.Day;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Adapters.DayListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.EventListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.NoteListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.ArabicDateConverter;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.Handler.Events;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static android.graphics.Typeface.createFromAsset;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayFull;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayHeader;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayList;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Month;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Year;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class DayUC extends Fragment {

    public View rootView;                                           // Root View of Fragment
    public TextView mainDate_TV, secondDate_TV, thirdDate_TV;       // Date TextViews (Month & Year View Mode
    private boolean isHoliday;                                      // Is It Holiday
    private boolean isEnable = true;                                // Should be Clickable
    private PersianCalendar persianCalendar;                        // Persian Calendar
    private Calendar miladiCalendar = Calendar.getInstance();       // Day Base Calendar
    private MainActivity.dayViewMode dayViewMode;                   // Day View Mode -> Year, Month, DayList, DayFull
    private TextView dayName, dayDate, dayliNote1, dayliNote2, monthName, miladiFullDate, jalaliFulldate,       // Day Full View Mode TextViews
            arabicFullDate, miladiDate, jalaliDate, arabicdate, holyDayNote;
    private String DateIndex;
    private String[] MonthName = {"January", "February", "March", "April", "May", "June", "July",   // Miladi Month Names
            "August", "September", "October", "November", "December"};
    private NoteListViewAdapter adapter;                                // List View Adapter for Note ListView in DayFull Mode
    private ArrayList<String> notes = new ArrayList<String>();      // Notes Array List for DayFull View Mode
    private ArrayList<String> eventTimeList;                        // Event List Array for DayList Mode
    private HashMap<String, List<Events>> eventDetailList;          // HashMap for Mapping Times and Events in DayList View Mode
    private DayListViewAdapter dayListadapter;                    // Event List View Adapter for DayList View Mode
    private ListView dayEventLV;                                    // Events List View
    private float[] startPoint = new float[4],                      // TouchEvent Positions
            endPoint = new float[4],
            distance = new float[2];
    private Typeface tahomaFont;
    private DayUC dayUCListHeader;                                  // DayUC Object for Showing in DayList View Mode (Header)
    private LayoutInflater mInfalater;

    public static DayUC newInstance(Calendar miladiDate, boolean isEnable, MainActivity.dayViewMode dayViewMode) {
        DayUC dayUC = new DayUC();
        dayUC.miladiCalendar.setTime(miladiDate.getTime());
        dayUC.miladiCalendar.setFirstDayOfWeek(Calendar.SATURDAY);              // Set First Day of Week Based on Primary Calendar
        dayUC.persianCalendar = new PersianCalendar(dayUC.miladiCalendar);      // Initial Persian Calendar
        // TODO Third Calendar (Arabic) Shoul be Added
        dayUC.isEnable = isEnable;                                              // Set Day Clickable
        dayUC.dayViewMode = dayViewMode;                                        // Set Day View Mode

        // Create Layout Inflater
        dayUC.mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dayUC.viewSelector();

        return dayUC;
    }

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
                case "DayHeader":
                    dayViewMode = DayHeader;
                    break;
                case "DayFull":
                    dayViewMode = DayFull;
                    break;
                case "DayList":
                    dayViewMode = DayList;
                    break;
            }

        // In Activity Restart (for rotation) Initialization rootView == Null, so Initial it
        if (rootView == null) {
            mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewSelector();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // If in DayList View Mode, Attach DayList Fragment to rootView
        if (dayViewMode == DayList)
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUCListHeader).commit();
        return rootView;        // Return RootView of Fragment
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void viewSelector() {
        // Set RootView and Initial it Based on ViewMode
        switch (dayViewMode) {
            // Header Mode for Showing in DayList View Mode
            case DayHeader:
                rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, null);
                initialDayHeader();
                break;
            // Single Date for Showing in YearView
            case Year:
                rootView = mInfalater.inflate(R.layout.day_uc_year_view, null);
                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
                initialMonth();
                break;
            // Full Calendar View
            case DayFull:
                rootView = mInfalater.inflate(R.layout.day_full_view, null);
                initialDayFull();
                break;
            // Triple Calendar for Showing in MonthView Mode
            case Month:
                rootView = mInfalater.inflate(R.layout.day_uc_month_view, null);
                mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_main_date);
                secondDate_TV = (TextView) rootView.findViewById(R.id.day_uc_second_date);
                thirdDate_TV = (TextView) rootView.findViewById(R.id.day_uc_third_date);
                initialMonth();
                break;
            // Day List View Mode
            case DayList:
                rootView = mInfalater.inflate(R.layout.day_list_mode_view, null);
                initialDayList();
                break;
        }
        rootView.setClickable(!isEnable);
    }

    /*
    Initial DayHeader Mode for Showing as Header Frame in DayListView Mode
     */
    private void initialDayHeader() {
        miladiCalendar.setTime(originalSelectedDate.getTime());
        mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_tv);
        mainDate_TV.setClickable(isEnable);
        switch (MainActivity.mainCalendarType) {
            case Solar:
                persianCalendar = originalSelectedPersianDate;
                mainDate_TV.setText(persianCalendar.getPersianFullDate());
                tahomaFont = createFromAsset(context.getAssets(), "tahoma.ttf");
                mainDate_TV.setTypeface(tahomaFont);
                break;
            case Gregorian:
                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
                // TODO Arabic Calendar Should Be Added
                break;
        }
    }

    /*
    Initial Triple Date Mode for Showing in MonthView Mode
     */
    public void initialMonth() {
//        mainDate_TV.setClickable(isEnable);
        if (!isEnable)
            mainDate_TV.setBackgroundColor(Color.LTGRAY);
        else
            mainDate_TV.setBackgroundColor(Color.GREEN);

        switch (MainActivity.mainCalendarType) {
            case Solar:
                persianCalendar.setMiladiDate(miladiCalendar);
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

        if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
            secondDate_TV.setClickable(isEnable);
            switch (MainActivity.secondCalendarType) {
                case Solar:
                    persianCalendar.setMiladiDate(miladiCalendar);
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

        if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
            thirdDate_TV.setClickable(isEnable);
            switch (MainActivity.thirdCalendarType) {
                case Solar:
                    persianCalendar.setMiladiDate(miladiCalendar);
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

    /*
    Initial Complete Calendar for DayFullView Mode
     */
    private void initialDayFull() {
        miladiCalendar.setTime(originalSelectedDate.getTime());
        dayFullInitializer();
        dayFullPutData();
    }

    /*
    Initial DayList Mode
     */
    private void initialDayList() {
        miladiCalendar.setTime(originalSelectedDate.getTime());
        dayUCListHeader = DayUC.newInstance(miladiCalendar, false, DayHeader);

        final LinearLayout eventLayout = (LinearLayout) rootView.findViewById(R.id.day_list_event_layout);


        for (int i=0; i<8; i++){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View eventListItem = mInflater.inflate(R.layout.event_list_item, null);
            TextView eventTime = (TextView) eventListItem.findViewById(R.id.event_item_text);
            final ListView eventListView = (ListView) eventListItem.findViewById(R.id.event_item_event_list);
            eventTime.setText("1" + i + ":00 - 1" + (i+1) + ":00");
            ArrayList<Events> eventList = new ArrayList<>();
            for (int j=0; j<8; j++) {
                Events events = Events.newInstance(context, Calendar.getInstance());
                events.setEvent("یادآوری " + (j+1), "یادآوری", "1" + i + ":00", "1" + (i+1) + ":00");
                eventList.add(events);
            }
            EventListViewAdapter eventAdapter = new EventListViewAdapter(eventList);
            eventListView.setAdapter(eventAdapter);
            eventAdapter.notifyDataSetChanged();
            eventListItem.setVisibility(View.VISIBLE);
            eventLayout.addView(eventListItem);
            eventListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    eventLayout.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }



        /*
        dayEventLV = (ListView) rootView.findViewById(R.id.day_list_mode_event_list);
        //initiate event list array and list view
        eventTimeList = new ArrayList<>();
        eventDetailList = new HashMap<>();

        dayListadapter = new DayListViewAdapter(eventTimeList, eventDetailList, context);

        //set list view adapter
        dayEventLV.setAdapter(dayListadapter);

        // TODO Event List should be derived from DB

        List<Events> eventlist1 = new ArrayList<>();
        List<Events> eventlist2 = new ArrayList<>();

//        Events event2 = Events.newInstance(context, Calendar.getInstance());

        eventTimeList.add("10:00" + " - " + "11:00");
        eventTimeList.add("16:00" + " - " + "17:00");
        for (int i = 0; i < 10; i++) {
            Events event1 = Events.newInstance(context, Calendar.getInstance());
            Events event2 = Events.newInstance(context, Calendar.getInstance());
            event1.setEvent("یادآوری " + i, "اولین یادآوری", "10:00", "11:00");
            eventlist1.add(event1);
            event2.setEvent("یادآوری " + i, "دومین یادآوری", "16:00", "17:00");
            eventlist2.add(event2);
        }
//        event2.setEvent("یادآوری 2", "دومین یادآوری", "10:00", "11:00");
//        eventlist1.add(event2);


        eventDetailList.put(eventTimeList.get(0), eventlist1);
        eventDetailList.put(eventTimeList.get(1), eventlist2);

        dayListadapter.notifyDataSetChanged();
        */


    }

    private void dayFullInitializer() {

        //TODO DB connection should be set
        //create DB object
//        try {
//            calendarDB = new MyDB(MainActivity.context);
//            calendarDB.openDB();
//            calendarDB.commentDBPull();
//            calendarDB.setHD();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        //set typefaces for text view fonts
        Typeface iranNastaliq = createFromAsset(MainActivity.context.getAssets(), "iran_nastaliq.ttf");
        Typeface homa = createFromAsset(MainActivity.context.getAssets(), "homa.ttf");
        Typeface arabic = createFromAsset(MainActivity.context.getAssets(), "arabic.ttf");
        Typeface times = createFromAsset(MainActivity.context.getAssets(), "times.ttf");

        holyDayNote = (TextView) rootView.findViewById(R.id.holiday_note_tv);
        holyDayNote.setTypeface(homa);
        holyDayNote.setTextColor(Color.BLACK);

        final ListView noteList = (ListView) rootView.findViewById(R.id.note_list_lv);
        adapter = new NoteListViewAdapter(context, notes);
        noteList.setAdapter(adapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView input = new TextView(context);
                input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                final String[] value = {new String()};
                final boolean[] mo2 = {false};
                value[0] = notes.get(position);
                input.setText(value[0]);

                AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
                inputNote.setTitle("مشاهده یادداشت");
                inputNote.setView(input);

                inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                inputNote.show();
            }
        });

        noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText input = new EditText(context);
                input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                final String[] value = {new String()};
                value[0] = notes.get(position);
                input.setText(value[0]);

                AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
                inputNote.setTitle("ویرایش یادداشت");
                inputNote.setView(input);

                inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().equals("")){
                            notes.remove(position);
                        }else {
                            notes.remove(position);
                            notes.add(position, input.getText().toString());
                        }
                        adapter = new NoteListViewAdapter(context, notes);
                        noteList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //TODO update edited note in listView and DB
                    }
                });
                inputNote.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        notes.remove(position);
                        adapter = new NoteListViewAdapter(context, notes);
                        noteList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //TODO remove note from DB and listView
                    }
                });
                inputNote.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing
                    }
                });
                inputNote.show();
                return true;
            }
        });

        dayDate = (TextView) rootView.findViewById(R.id.date_tv);
        dayDate.setTypeface(homa);
        dayDate.setTextColor(Color.BLACK);
        dayliNote1 = (TextView) rootView.findViewById(R.id.dayli_note_1_tv);
        dayliNote1.setTypeface(homa);
        dayliNote1.setTextColor(Color.BLACK);
        dayliNote2 = (TextView) rootView.findViewById(R.id.dayli_note_2_tv);
        dayliNote2.setTypeface(homa);
        dayliNote2.setTextColor(Color.BLACK);
        dayName = (TextView) rootView.findViewById(R.id.day_name_tv);
        dayName.setTypeface(homa);
        dayName.setTextColor(Color.BLACK);
        monthName = (TextView) rootView.findViewById(R.id.month_name_tv);
        monthName.setTypeface(homa);
        monthName.setTextColor(Color.BLACK);
        jalaliFulldate = (TextView) rootView.findViewById(R.id.jalali_date_full_tv);
        jalaliFulldate.setTypeface(homa);
        jalaliDate = (TextView) rootView.findViewById(R.id.jalali_date_tv);
        jalaliDate.setTypeface(homa);
        jalaliFulldate.setTextColor(Color.WHITE);
        jalaliDate.setTextColor(Color.WHITE);
        arabicFullDate = (TextView) rootView.findViewById(R.id.arabic_date_full_tv);
        arabicFullDate.setTypeface(arabic);
        arabicFullDate.setTextColor(Color.WHITE);
        arabicdate = (TextView) rootView.findViewById(R.id.arabic_date_tv);
        arabicdate.setTypeface(arabic);
        arabicdate.setTextColor(Color.WHITE);
        miladiFullDate = (TextView) rootView.findViewById(R.id.miladi_date_full_tv);
        miladiFullDate.setTypeface(times);
        miladiFullDate.setTextColor(Color.WHITE);
        miladiDate = (TextView) rootView.findViewById(R.id.miladi_date_tv);
        miladiDate.setTypeface(times);
        miladiDate.setTextColor(Color.WHITE);
    }

    //set calendar data
    public void dayFullPutData() {
        ContentValues calendarContent, noteContents;

        //clear notes list for referesh
        notes.clear();

        //set date index based on calendar
        DateIndex = originalSelectedDate.get(Calendar.YEAR) + "" +
                (originalSelectedDate.get(Calendar.MONTH) + 1) + "" + originalSelectedDate.get(Calendar.DAY_OF_MONTH);

        //read data from DB
//        try {
        //read calendar data
//            calendarContent = calendarDB.readData(DateIndex);

        //read notes
//            noteContents = calendarDB.getNote(DateIndex);

        //set daily note
//            dayliNote1.setText(calendarContent.getAsString(MyDB.COLUMN_VERSE_1));
//            dayliNote2.setText(calendarContent.getAsString(MyDB.COLUMN_VERSE_2));
        dayliNote1.setText("سطر اول");
        dayliNote2.setText("سطر دوم");

        //set holiday note
//            holyDayNote.setText(calendarContent.getAsString(MyDB.COLUMN_HOLYDAY));
        holyDayNote.setText("مناسبت");

        //add notes to array list
//            for(int i=0; i< noteContents.size() ; i++)
//                notes.add(noteContents.getAsString(MyDB.COLUMN_NOTE + i));
        for (int i = 0; i < 15; i++)
            notes.add("یادداشت " + i);

        //update array list adapter
        adapter.notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //get arabic date
        Calendar temporary = Calendar.getInstance();
        temporary.set(originalSelectedDate.get(Calendar.YEAR)
                , originalSelectedDate.get(Calendar.MONTH)
                , originalSelectedDate.get(Calendar.DATE));
        temporary.add(Calendar.DATE, -1);
        String[] ADate = ArabicDateConverter.writeIslamicDate(temporary);

        //convert digits to persian digit
        persianCalendar = originalSelectedPersianDate;
//        PersianCalendar pcal = new PersianCalendar(originalSelectedDate);
        String[] PDate = new String[5];
        PDate[0] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianYear()));
        PDate[1] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianMonth()));
        PDate[2] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianDate()));
        PDate[3] = persianCalendar.getPersianMonthName();
        PDate[4] = persianCalendar.getPersianDayName();

        //convert digits to arabic digits
        ADate[0] = PersianUtil.convertDigitstoArabic(ADate[0]);
        ADate[1] = PersianUtil.convertDigitstoArabic(ADate[1]);
        ADate[2] = PersianUtil.convertDigitstoArabic(ADate[2]);

        //set text color to RED for holidays
        if (PDate[4] == "جمعه" || !holyDayNote.getText().toString().isEmpty()) {
            monthName.setTextColor(Color.RED);
            dayName.setTextColor(Color.RED);
            dayDate.setTextColor(Color.RED);
            holyDayNote.setTextColor(Color.RED);
        } else {
            monthName.setTextColor(Color.BLACK);
            dayName.setTextColor(Color.BLACK);
            dayDate.setTextColor(Color.BLACK);
            holyDayNote.setTextColor(Color.BLACK);
        }

        //set calendar textViews
        dayName.setText(PDate[4]);
        dayDate.setText(PDate[2]);
        monthName.setText(PDate[3]);
        jalaliFulldate.setText(PDate[3] + " " + PDate[0]);
        jalaliDate.setText(PDate[2]);

        miladiFullDate.setText(originalSelectedDate.get(Calendar.YEAR) + " "
                + MonthName[originalSelectedDate.get(Calendar.MONTH)]);
        miladiDate.setText(originalSelectedDate.get(Calendar.DAY_OF_MONTH) + "");

        arabicFullDate.setText(ADate[3] + " " + ADate[0]);
        arabicdate.setText(ADate[2]);
    }

    private void checkHoliday() {
        isHoliday = Arrays.asList(MainActivity.holidayList).contains(persianCalendar.getPersianDateIndex());
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void updateDayHeader(){
        miladiCalendar.setTime(originalSelectedDate.getTime());
        persianCalendar.set(originalSelectedPersianDate.getiPersianYear(),
                originalSelectedPersianDate.getiPersianMonth(),
                originalSelectedPersianDate.getiPersianDate());
        switch (MainActivity.mainCalendarType) {
            case Solar:
                mainDate_TV.setText(persianCalendar.getPersianFullDate());
                break;
            case Gregorian:
                mainDate_TV.setText(String.valueOf(originalSelectedDate.get(Calendar.YEAR) + " "
                        + originalSelectedDate.get(Calendar.MONTH) + " "
                + originalSelectedDate.get(Calendar.DATE)));
                break;
            case Hejri:
                // TODO Arabic Calendar Should Be Added
                break;
        }
    }

    public void updateMonth(Calendar calendar){
        miladiCalendar.setTime(calendar.getTime());
        if (!isEnable)
            mainDate_TV.setBackgroundColor(Color.LTGRAY);
        else
            mainDate_TV.setBackgroundColor(Color.GREEN);

        switch (MainActivity.mainCalendarType) {
            case Solar:
                persianCalendar.setMiladiDate(miladiCalendar);
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

        if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
            secondDate_TV.setClickable(isEnable);
            switch (MainActivity.secondCalendarType) {
                case Solar:
                    persianCalendar.setMiladiDate(miladiCalendar);
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

        if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
            thirdDate_TV.setClickable(isEnable);
            switch (MainActivity.thirdCalendarType) {
                case Solar:
                    persianCalendar.setMiladiDate(miladiCalendar);
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

    public void updatedayList(){
//        miladiCalendar.setTime(originalSelectedDate.getTime());
//        eventTimeList.clear();
//        dayUCListHeader.updateDayHeader();
//        List<Events> eventlist1 = new ArrayList<>();
//        List<Events> eventlist2 = new ArrayList<>();
//
//        //TODO get events from DB
//
//        eventTimeList.add("10:00" + " - " + "11:00");
//        eventTimeList.add("16:00" + " - " + "17:00");
//        for (int i = 0; i < 10; i++) {
//            Events event1 = Events.newInstance(context, Calendar.getInstance());
//            Events event2 = Events.newInstance(context, Calendar.getInstance());
//            event1.setEvent("یادآوری " + i, "اولین یادآوری", "10:00", "11:00");
//            eventlist1.add(event1);
//            event2.setEvent("یادآوری " + i, "دومین یادآوری", "16:00", "17:00");
//            eventlist2.add(event2);
//        }
//
//        eventDetailList.put(eventTimeList.get(0), eventlist1);
//        eventDetailList.put(eventTimeList.get(1), eventlist2);
//
//        dayListadapter.notifyDataSetChanged();
        miladiCalendar.setTime(originalSelectedDate.getTime());
        dayUCListHeader = DayUC.newInstance(miladiCalendar, false, DayHeader);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUCListHeader).commit();
        initialDayList();
    }

    public void updateDayFull(){
        miladiCalendar.setTime(originalSelectedDate.getTime());
        persianCalendar.set(originalSelectedPersianDate.getiPersianYear(),
                originalSelectedPersianDate.getiPersianMonth(),
                originalSelectedPersianDate.getiPersianDate());
        dayFullPutData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ViewMode", dayViewMode.toString());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }
}
