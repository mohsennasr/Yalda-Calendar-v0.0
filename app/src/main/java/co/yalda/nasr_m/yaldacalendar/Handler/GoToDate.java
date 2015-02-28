package co.yalda.nasr_m.yaldacalendar.Handler;

import android.app.AlertDialog;
import android.content.DialogInterface;

import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.PersianDatePicker.PersianDatePicker;
import co.yalda.nasr_m.yaldacalendar.PersianDatePicker.Util.PersianCalendar;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class GoToDate {

    private co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar persianCalendar;

    public GoToDate() {
        //create dialog
        AlertDialog.Builder datePicker = new AlertDialog.Builder(MainActivity.context);

        //create persian date picker object and assign dates
        final PersianDatePicker persianDatePicker = new PersianDatePicker(MainActivity.context);
        PersianCalendar pcal = new PersianCalendar(MainActivity.originalSelectedDate.getTimeInMillis());
        persianDatePicker.setDisplayPersianDate(pcal);
        datePicker.setView(persianDatePicker);

        //set date picker dialog buttons
        datePicker.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                persianCalendar = new co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar(persianDatePicker.getDisplayDate());
                MainActivity.originalSelectedDate.setTime(persianDatePicker.getDisplayPersianDate().getTime());
                //TODO update calendar views
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

    public co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar getPersianCalendar() {
        return persianCalendar;
    }
}
