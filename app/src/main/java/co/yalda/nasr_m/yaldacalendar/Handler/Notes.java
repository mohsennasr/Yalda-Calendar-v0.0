package co.yalda.nasr_m.yaldacalendar.Handler;

import android.support.v4.app.Fragment;

import java.util.Calendar;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class Notes extends Fragment {

    Calendar cal;

    public static Notes newInstance(Calendar cal) {
        Notes notes = new Notes();
        notes.cal = cal;
        return notes;
    }
}
