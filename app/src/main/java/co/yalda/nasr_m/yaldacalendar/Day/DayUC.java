package co.yalda.nasr_m.yaldacalendar.Day;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.EventsListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.NoteListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.ArabicCalendar;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.Handler.AddEvent;
import co.yalda.nasr_m.yaldacalendar.Handler.Events;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.arabicFont;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayFull;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayHeader;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.DayList;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Month;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode.Year;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.homaFont;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.timesFont;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class DayUC extends Fragment {

    public View rootView;                                           // Root View of Fragment
    public TextView mainDate_TV, secondDate_TV, thirdDate_TV;       // Date TextViews (Month & Year View Mode
    private boolean isHoliday;                                      // Is It Holiday
    public boolean isEnable = true;                                // Should be Clickable
    private PersianCalendar persianCalendar = new PersianCalendar(Calendar.getInstance());                        // Persian Calendar
    private Calendar miladiCalendar = Calendar.getInstance();       // Day Base Calendar
    private ArabicCalendar arabicCalendar = new ArabicCalendar(Calendar.getInstance());
    private MainActivity.dayViewMode dayViewMode;                   // Day View Mode -> Year, Month, DayList, DayFull
    private TextView dayName, dayDate, dayliNote1, dayliNote2, monthName, miladiFullDate, jalaliFulldate,       // Day Full View Mode TextViews
            arabicFullDate, miladiDate, jalaliDate, arabicdate, holyDayNote;
    private String DateIndex;
    private String[] MonthName = {"January", "February", "March", "April", "May", "June", "July",   // Miladi Month Names
            "August", "September", "October", "November", "December"};
    private NoteListViewAdapter adapter;                                // List View Adapter for Note ListView in DayFull Mode
    private ArrayList<String> notes = new ArrayList<String>();      // Notes Array List for DayFull View Mode
    private ArrayList<Events> eventsArrayList;
    private EventsListViewAdapter dayListadapter;                    // Event List View Adapter for DayList View Mode
    private ListView dayEventLV;                                    // Events List View
    private ListView noteList;
    private DayUC dayUCListHeader;                                  // DayUC Object for Showing in DayList View Mode (Header)
    private LayoutInflater mInfalater;
    private int CURRENT_SELECTED_EVENT = -1;                    //selected event in dayList mode

    public static DayUC newInstance(Calendar miladiDate, boolean isEnable, MainActivity.dayViewMode dayViewMode) {
        DayUC dayUC = new DayUC();
        dayUC.miladiCalendar.setTime(miladiDate.getTime());
        dayUC.miladiCalendar.setFirstDayOfWeek(Calendar.SATURDAY);              // Set First Day of Week Based on Primary Calendar
        dayUC.persianCalendar = new PersianCalendar(dayUC.miladiCalendar);      // Initial Persian Calendar
        dayUC.arabicCalendar = new ArabicCalendar(dayUC.miladiCalendar);
        dayUC.isEnable = isEnable;                                              // Set Day Clickable
        dayUC.dayViewMode = dayViewMode;                                        // Set Day View Mode

        // Create Layout Inflater
        dayUC.mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dayUC.checkHoliday();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // If in DayList View Mode, Attach DayList Fragment to rootView
        if (rootView == null) {
            mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewSelector();
        }
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
                rootView = mInfalater.inflate(R.layout.day_uc_list_mode_view, null);
                miladiCalendar.setTime(originalSelectedDate.getTime());
                dayUCListHeader = DayUC.newInstance(miladiCalendar, false, DayHeader);
                initialDayList();
                break;
        }
//        rootView.setClickable(!isEnable);
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
                persianCalendar.setMiladiDate(miladiCalendar);
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(persianCalendar.getPersianFullDate());
                break;
            case Gregorian:
                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
                arabicCalendar.setBaseMiladiCalendar(miladiCalendar);
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(arabicCalendar.getArabicFullDate());
                break;
        }
    }

    /*
    Initial Triple Date Mode for Showing in MonthView Mode
     */
    public void initialMonth() {
//        mainDate_TV.setClickable(isEnable);
        persianCalendar.setMiladiDate(miladiCalendar);
        arabicCalendar.setBaseMiladiCalendar(miladiCalendar);
        mainDate_TV.setTypeface(homaFont);
        if (!isEnable) {
            mainDate_TV.setTextColor(Color.LTGRAY);
            if (dayViewMode == Month) {
                secondDate_TV.setTextColor(Color.LTGRAY);
                thirdDate_TV.setTextColor(Color.LTGRAY);
            }
        }else {
            mainDate_TV.setTextColor(Color.BLACK);
            if (dayViewMode == Month) {
                secondDate_TV.setTextColor(Color.BLACK);
                thirdDate_TV.setTextColor(Color.BLACK);
            }
        }

        if (isHoliday){
            mainDate_TV.setTextColor(Color.RED);
            if (dayViewMode == Month) {
                secondDate_TV.setTextColor(Color.RED);
                thirdDate_TV.setTextColor(Color.RED);
            }
        }

        setSelectedDay();

        switch (MainActivity.mainCalendarType) {
            case Solar:
                mainDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
                break;
            case Gregorian:
                mainDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                break;
            case Hejri:
                mainDate_TV.setTypeface(homaFont);
                mainDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
                break;
        }

        if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
//            secondDate_TV.setClickable(isEnable);
            switch (MainActivity.secondCalendarType) {
                case Solar:
                    secondDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    secondDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
                    secondDate_TV.setTypeface(homaFont);
                    secondDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
                    break;
            }
        }

        if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
//            thirdDate_TV.setClickable(isEnable);
            switch (MainActivity.thirdCalendarType) {
                case Solar:
                    thirdDate_TV.setText(PersianUtil.toPersian(persianCalendar.getiPersianDate()));
                    break;
                case Gregorian:
                    thirdDate_TV.setText(String.valueOf(miladiCalendar.get(Calendar.DATE)));
                    break;
                case Hejri:
                    thirdDate_TV.setTypeface(homaFont);
                    thirdDate_TV.setText(PersianUtil.toArabic(arabicCalendar.getArabicDate()));
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

        //TODO get event list from DB

        eventsArrayList = new ArrayList<>();
        Events events = Events.newInstance(context, Calendar.getInstance());
        events.setEvent("asdf0", "asdfasdfasdfasdfa sdf ", "10:25", "16:56");
        eventsArrayList.add(events);
        dayListadapter = new EventsListViewAdapter(eventsArrayList, context);

        dayEventLV = (ListView) rootView.findViewById(R.id.day_list_mode_event_list);
        dayEventLV.setAdapter(dayListadapter);
        dayListadapter.notifyDataSetChanged();

        dayEventLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title, detail, start, end;
                title = eventsArrayList.get(position).getTitle();
                detail = eventsArrayList.get(position).getDescription();
                start = eventsArrayList.get(position).getStartTime();
                end = eventsArrayList.get(position).getEndTime();

                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View eventView = mInflater.inflate(R.layout.event_show_dialog, null);

                TextView titleEvent = (TextView) eventView.findViewById(R.id.event_title_view);
                TextView detailEvent = (TextView) eventView.findViewById(R.id.event_detail_view);
                TextView startEvent = (TextView) eventView.findViewById(R.id.start_time);
                TextView endEvent = (TextView) eventView.findViewById(R.id.end_time);

                titleEvent.setText(title);
                detailEvent.setText(detail);
                startEvent.setText(start);
                endEvent.setText(end);

                //create dialog
                AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
                inputNote.setTitle("مشاهده رویداد");
                inputNote.setView(eventView);

                //set dialog buttons
                inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                //show dialog
                inputNote.show();
            }
        });
        registerForContextMenu(dayEventLV);

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


        holyDayNote = (TextView) rootView.findViewById(R.id.holiday_note_tv);
        holyDayNote.setTypeface(homaFont);
        holyDayNote.setTextColor(Color.BLACK);

        noteList = (ListView) rootView.findViewById(R.id.note_list_lv);

        adapter = new NoteListViewAdapter(context, notes);
        noteList.setAdapter(adapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView input = new TextView(context);
                input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                final String[] value = {new String()};
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
        dayDate.setTypeface(homaFont);
        dayDate.setTextColor(Color.BLACK);
        dayliNote1 = (TextView) rootView.findViewById(R.id.dayli_note_1_tv);
        dayliNote1.setTypeface(homaFont);
        dayliNote1.setTextColor(Color.BLACK);
        dayliNote2 = (TextView) rootView.findViewById(R.id.dayli_note_2_tv);
        dayliNote2.setTypeface(homaFont);
        dayliNote2.setTextColor(Color.BLACK);
        dayName = (TextView) rootView.findViewById(R.id.day_name_tv);
        dayName.setTypeface(homaFont);
        dayName.setTextColor(Color.BLACK);
        monthName = (TextView) rootView.findViewById(R.id.month_name_tv);
        monthName.setTypeface(homaFont);
        monthName.setTextColor(Color.BLACK);
        jalaliFulldate = (TextView) rootView.findViewById(R.id.jalali_date_full_tv);
        jalaliFulldate.setTypeface(homaFont);
        jalaliDate = (TextView) rootView.findViewById(R.id.jalali_date_tv);
        jalaliDate.setTypeface(homaFont);
        jalaliFulldate.setTextColor(Color.WHITE);
        jalaliDate.setTextColor(Color.WHITE);
        arabicFullDate = (TextView) rootView.findViewById(R.id.arabic_date_full_tv);
        arabicFullDate.setTypeface(arabicFont);
        arabicFullDate.setTextColor(Color.WHITE);
        arabicdate = (TextView) rootView.findViewById(R.id.arabic_date_tv);
        arabicdate.setTypeface(arabicFont);
        arabicdate.setTextColor(Color.WHITE);
        miladiFullDate = (TextView) rootView.findViewById(R.id.miladi_date_full_tv);
        miladiFullDate.setTypeface(timesFont);
        miladiFullDate.setTextColor(Color.WHITE);
        miladiDate = (TextView) rootView.findViewById(R.id.miladi_date_tv);
        miladiDate.setTypeface(timesFont);
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
//        for (int i = 0; i < 15; i++)
//            notes.add("یادداشت " + i);

        //update array list adapter
        adapter.notifyDataSetChanged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //get arabic date
//        Calendar temporary = Calendar.getInstance();
//        temporary.setTime(originalSelectedDate.getTime());
//        temporary.add(Calendar.DATE, -1);
        String[] ADate = new String[4];

        //convert digits to persian digit
//        persianCalendar.setMiladiDate(originalSelectedDate);
//        arabicCalendar.setBaseMiladiCalendar(originalSelectedDate);
//        PersianCalendar pcal = new PersianCalendar(originalSelectedDate);
        String[] PDate = new String[5];
        PDate[0] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianYear()));
        PDate[1] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianMonth()));
        PDate[2] = PersianUtil.convertDigits(String.valueOf(persianCalendar.getiPersianDate()));
        PDate[3] = persianCalendar.getPersianMonthName();
        PDate[4] = persianCalendar.getPersianDayName();

        //convert digits to arabic digits
        ADate[0] = PersianUtil.toArabic(arabicCalendar.getArabicYear());
        ADate[1] = PersianUtil.toArabic(arabicCalendar.getArabicMonth());
        ADate[2] = PersianUtil.toArabic(arabicCalendar.getArabicDate());
        ADate[3] = arabicCalendar.getArabicMonthName();

        //set text color to RED for holidays
//        if (PDate[4] == "جمعه" || !holyDayNote.getText().toPersian().isEmpty()) {
        if (isHoliday){
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
        monthName.setTypeface(homaFont);
    }

    private void checkHoliday() {
        isHoliday = ((Arrays.asList(MainActivity.holidayList).contains(persianCalendar.getPersianDateIndex()))
        || Arrays.asList(MainActivity.dayWeekHoliday).contains(String.valueOf(miladiCalendar.get(Calendar.DAY_OF_WEEK)))) && isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void updateDayHeader(){
        miladiCalendar.setTime(originalSelectedDate.getTime());
        persianCalendar.setMiladiDate(originalSelectedDate);
        arabicCalendar.setBaseMiladiCalendar(originalSelectedDate);
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
                mainDate_TV.setText(arabicCalendar.getArabicFullDate());
                break;
        }
    }

    public void updateMonth(Calendar calendar){
        miladiCalendar.setTime(calendar.getTime());
        unSetSelectedDay();
        checkHoliday();
        initialMonth();
    }

    public void updateDayList(){
        miladiCalendar.setTime(originalSelectedDate.getTime());
//        dayUCListHeader = DayUC.newInstance(miladiCalendar, false, DayHeader);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUCListHeader).commit();
        dayUCListHeader.updateDayHeader();
        initialDayList();
    }

    public void updateDayFull(){
        miladiCalendar.setTime(originalSelectedDate.getTime());
        persianCalendar.setMiladiDate(miladiCalendar);
        arabicCalendar.setBaseMiladiCalendar(miladiCalendar);
        dayFullPutData();
    }

    public void addNote(String note){
        notes.add(note);
        adapter = new NoteListViewAdapter(context, notes);
        noteList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void addEvent(Events events){
        eventsArrayList.add(events);
        dayListadapter = new EventsListViewAdapter(eventsArrayList, context);
        dayEventLV.setAdapter(dayListadapter);
        dayListadapter.notifyDataSetChanged();
    }

    public void updateEvent(Events events){
        if (CURRENT_SELECTED_EVENT >=0) {
            eventsArrayList.remove(CURRENT_SELECTED_EVENT);
            eventsArrayList.add(CURRENT_SELECTED_EVENT, events);
            dayListadapter = new EventsListViewAdapter(eventsArrayList, context);
            dayEventLV.setAdapter(dayListadapter);
            dayListadapter.notifyDataSetChanged();
            CURRENT_SELECTED_EVENT = -1;
        }
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case 0:             //Edit Event
                Intent eventActivity = new Intent(context, AddEvent.class);
                eventActivity.putExtra("EventMode", MainActivity.eventMode.EditEvent.toString());
                eventActivity.putExtra("Title", eventsArrayList.get(info.position).getTitle());
                eventActivity.putExtra("Detail", eventsArrayList.get(info.position).getDescription());
                eventActivity.putExtra("isHoleDay", eventsArrayList.get(info.position).isHoleDay());
                if (!eventsArrayList.get(info.position).isHoleDay()) {
                    eventActivity.putExtra("Start_Time", eventsArrayList.get(info.position).getStartTime());
                    eventActivity.putExtra("End_Time", eventsArrayList.get(info.position).getEndTime());
                }
                eventActivity.putExtra("Date", eventsArrayList.get(info.position).getCal().getTimeInMillis());
                CURRENT_SELECTED_EVENT = info.position;
                startActivityForResult(eventActivity, 0);
                break;
            case 1:             //remove event
                eventsArrayList.remove(info.position);
                dayListadapter = new EventsListViewAdapter(eventsArrayList, context);
                dayEventLV.setAdapter(dayListadapter);
                dayListadapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    public void setSelectedDay(){
        if (persianCalendar.persianCompare(originalSelectedPersianDate) == 0 && isEnable)
            rootView.setBackgroundResource(R.drawable.border_rectangle);
        if (persianCalendar.persianCompare(new PersianCalendar(Calendar.getInstance())) == 0 && isEnable)
            rootView.setBackgroundResource(R.drawable.background_rectangle);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void unSetSelectedDay(){
        if (persianCalendar.persianCompare(new PersianCalendar(Calendar.getInstance())) != 0)
            rootView.setBackground(null);
    }

    public PersianCalendar getPersianCalendar() {
        return persianCalendar;
    }

    public Calendar getMiladiCalendar() {
        return miladiCalendar;
    }
}
