package co.yalda.nasr_m.yaldacalendar.Day;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Adapters.ListViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.ArabicDateConverter;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static android.graphics.Typeface.createFromAsset;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
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
    private TextView dayName, dayDate, dayliNote1, dayliNote2, monthName, miladiFullDate, jalaliFulldate,
            arabicFullDate, miladiDate, jalaliDate, arabicdate, holyDayNote;
    private String DateIndex;
    private String[] MonthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private ListViewAdapter adapter;
    private ArrayList<String> notes = new ArrayList<String>();

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
                    rootView = mInfalater.inflate(R.layout.day_full_view, null);
                    initialDayFull();
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
        Initializer();
        putData();
    }

    private void initialDaySimple() {

    }

    private void checkHoliday() {

    }

    //set calendar data
    public void putData() {
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

    //initial calendar
    private void Initializer() {

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
        adapter = new ListViewAdapter(getActivity(), notes);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ViewMode", viewMode.toString());
        super.onSaveInstanceState(outState);
    }
}
