package co.yalda.nasr_m.yaldacalendar.Handler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TimePicker;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class Picker extends TimePicker {

    Context context;

    public Picker(Context context) {
        super(context);
        this.context = context;
    }

    public Picker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public Picker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public Picker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }


}
