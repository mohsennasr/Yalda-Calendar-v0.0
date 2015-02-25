package co.yalda.nasr_m.yaldacalendar.Day;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Adapters.EventListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Adapters.ListViewAdapter;
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

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class DayUC extends Fragment implements View.OnTouchListener {

    public View rootView;                      // root view of fragment
    public TextView mainDate_TV, secondDate_TV, thirdDate_TV;
    private boolean isHoliday;                  //is it holiday
    private boolean isEnable = true;         //should be clickable
    private PersianCalendar persianCalendar;
    private Calendar miladiCalendar = Calendar.getInstance();
    private MainActivity.dayViewMode dayViewMode;
    private TextView dayName, dayDate, dayliNote1, dayliNote2, monthName, miladiFullDate, jalaliFulldate,
            arabicFullDate, miladiDate, jalaliDate, arabicdate, holyDayNote;
    private String DateIndex;
    private String[] MonthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private ListViewAdapter adapter;
    private ArrayList<String> notes = new ArrayList<String>();
    private ArrayList<String> eventTimeList;    //event list array
    private HashMap<String, List<Events>> eventDetailList;
    private EventListViewAdapter dayListadapter;        //list view adapter
    private String[] eventClock = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00"
            , "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"
            , "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private ListView dayEventLV;            //day events list view
    private float[] startPoint = new float[4],
            endPoint = new float[4],
            distance = new float[2];
    private Typeface tahomaFont;
    private DayUC dayUCListHeader;

    public static DayUC newInstance(Calendar miladiDate, boolean isEnable, MainActivity.dayViewMode dayViewMode) {
        DayUC dayUC = new DayUC();
        dayUC.miladiCalendar.setTime(miladiDate.getTime());
        dayUC.miladiCalendar.setFirstDayOfWeek(Calendar.SATURDAY);
        dayUC.persianCalendar = new PersianCalendar(dayUC.miladiCalendar);
        dayUC.isEnable = isEnable;
        dayUC.dayViewMode = dayViewMode;

        LayoutInflater mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (dayViewMode) {
            case DayHeader:
                dayUC.rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, null);
                dayUC.initialDayHeader();
                break;
            case Year:
                dayUC.rootView = mInfalater.inflate(R.layout.day_uc_year_view, null);
                dayUC.initialMonth();
                break;
            case DayFull:
                dayUC.rootView = mInfalater.inflate(R.layout.day_full_view, null);
                dayUC.initialDayFull();
                break;
            case Month:
                dayUC.rootView = mInfalater.inflate(R.layout.day_uc_month_view, null);
                dayUC.initialMonth();
                break;
            case DayList:
                dayUC.rootView = mInfalater.inflate(R.layout.day_list_mode_view, null);
                dayUC.initialDayList();
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

        if (rootView == null){
            LayoutInflater mInfalater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch (dayViewMode) {
                case DayHeader:
                    rootView = mInfalater.inflate(R.layout.day_uc_header_mode_view, null);
                    initialDayHeader();
                    break;
                case Year:
                    rootView = mInfalater.inflate(R.layout.day_uc_year_view, null);
                    initialMonth();
                    break;
                case DayFull:
                    rootView = mInfalater.inflate(R.layout.day_full_view, null);
                    initialDayFull();
                    break;
                case Month:
                    rootView = mInfalater.inflate(R.layout.day_uc_month_view, null);
                    initialMonth();
                    break;
                case DayList:
                    rootView = mInfalater.inflate(R.layout.day_list_mode_view, null);
                    initialDayList();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (dayViewMode == DayHeader) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.day_list_mode_frame, dayUCListHeader).commit();
        }
    }

    private void initialDayHeader() {
        mainDate_TV = (TextView) rootView.findViewById(R.id.day_uc_header_mode_tv);
        mainDate_TV.setClickable(isEnable);
        switch (MainActivity.mainCalendarType) {
            case Solar:
                persianCalendar = new PersianCalendar(miladiCalendar);
                mainDate_TV.setText(persianCalendar.getPersianFullDate());
                tahomaFont = createFromAsset(context.getAssets(), "tahoma.ttf");
                mainDate_TV.setTypeface(tahomaFont);
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
        if (!isEnable)
            mainDate_TV.setBackgroundColor(Color.LTGRAY);
        else
            mainDate_TV.setBackgroundColor(Color.GREEN);
//        mainDate_TV.setLayoutParams(new ViewGroup.LayoutParams((MainActivity.viewSize[0] - 40)/7,(MainActivity.viewSize[1] - 100)/6 ));

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

        if ((MainActivity.secondCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
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

        if ((MainActivity.thirdCalendarType != null) && (dayViewMode != MainActivity.dayViewMode.Year)) {
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
        dayFullInitializer();
        dayFullPutData();
    }

    private void initialDayList() {
        dayUCListHeader = DayUC.newInstance(originalSelectedDate, false, DayHeader);
        dayEventLV = (ListView) rootView.findViewById(R.id.day_list_mode_event_list);
        //initiate event list array and list view
        eventTimeList = new ArrayList<>();
        eventDetailList = new HashMap<>();

        dayListadapter = new EventListViewAdapter(eventTimeList, eventDetailList, context);

        //set list view adapter
        dayEventLV.setAdapter(dayListadapter);

        dayListSetData();
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

        holyDayNote = (TextView) rootView.findViewById(R.id.holyday_note_tv);
        holyDayNote.setTypeface(homa);
        holyDayNote.setTextColor(Color.BLACK);

        ListView noteList = (ListView) rootView.findViewById(R.id.note_list_lv);
        adapter = new ListViewAdapter(context, notes);
        noteList.setAdapter(adapter);

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

    public void dayListSetData() {

        // TODO Event List should be derived from DB

        List<Events> eventlist1 = new ArrayList<>();
        List<Events> eventlist2 = new ArrayList<>();
        Events event1 = Events.newInstance(context, Calendar.getInstance());
        Events event2 = Events.newInstance(context, Calendar.getInstance());
        Events event3 = Events.newInstance(context, Calendar.getInstance());
        eventTimeList.add("10:00" + " - " + "11:00");
        event1.setEvent("یادآوری 1", "اولین یادآوری", "10:00", "11:00");
        eventlist1.add(event1);
        event2.setEvent("یادآوری 2", "دومین یادآوری", "10:00", "11:00");
        eventlist1.add(event2);
        eventTimeList.add("16:00" + " - " + "17:00");
        event3.setEvent("یادآوری 3", "سومین یادآوری", "16:00", "17:00");
        eventlist2.add(event3);

        eventDetailList.put(eventTimeList.get(0), eventlist1);
        eventDetailList.put(eventTimeList.get(1), eventlist2);

        dayListadapter.notifyDataSetChanged();
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
        for(int i=0; i< 5 ; i++)
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
        PersianCalendar pcal = new PersianCalendar(originalSelectedDate);
        String[] PDate = new String[5];
        PDate[0] = PersianUtil.convertDigits(String.valueOf(pcal.getiPersianYear()));
        PDate[1] = PersianUtil.convertDigits(String.valueOf(pcal.getiPersianMonth()));
        PDate[2] = PersianUtil.convertDigits(String.valueOf(pcal.getiPersianDate()));
        PDate[3] = pcal.getPersianMonthName();
        PDate[4] = pcal.getPersianDayName();

        //convert digits to arabic digits
        ADate[0] = PersianUtil.convertDigitstoArabic(ADate[0]);
        ADate[1] = PersianUtil.convertDigitstoArabic(ADate[1]);
        ADate[2] = PersianUtil.convertDigitstoArabic(ADate[2]);

        //set text color to RED for holidays
        if(PDate[4] == "جمعه"  || !holyDayNote.getText().toString().isEmpty()){
            monthName.setTextColor(Color.RED);
            dayName.setTextColor(Color.RED);
            dayDate.setTextColor(Color.RED);
            holyDayNote.setTextColor(Color.RED);
        }else{
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ViewMode", dayViewMode.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(context, "touch event . . . ", Toast.LENGTH_SHORT);
        return true;
    }
}
