package co.yalda.nasr_m.yaldacalendar.Handler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CalendarView;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class Calendar extends CalendarView {

    Context context;

    public Calendar(Context context) {
        super(context);
        this.context = context;
    }

    public Calendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public Calendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public Calendar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public View getRootView() {
        return new View(context);
    }


}
