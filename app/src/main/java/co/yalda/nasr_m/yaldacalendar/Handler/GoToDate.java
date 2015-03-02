package co.yalda.nasr_m.yaldacalendar.Handler;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.PersianDatePicker.PersianDatePicker;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class GoToDate {

    private PersianCalendar persianCalendar = new PersianCalendar(Calendar.getInstance());
    private Calendar calendar = Calendar.getInstance();



    public GoToDate() {
        //create dialog
        AlertDialog.Builder datePicker = new AlertDialog.Builder(context);

        //create persian date picker object and assign dates
        final PersianDatePicker persianDatePicker = new PersianDatePicker(context);
        PersianCalendar pcal = new PersianCalendar(originalSelectedDate);
        persianDatePicker.setDisplayPersianDate(pcal);
        datePicker.setView(persianDatePicker);

        //set date picker dialog buttons
        datePicker.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                calendar.setTime(persianDatePicker.getDisplayDate());
                persianCalendar.setMiladiDate(calendar);
                originalSelectedDate.setTime(calendar.getTime());
                originalSelectedPersianDate.setMiladiDate(persianCalendar.getMiladiDate());
            }
        });
        datePicker.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //show dialog
        datePicker.show();
    }

    public PersianCalendar getPersianCalendar() {
        return persianCalendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

}
