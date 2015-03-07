package co.yalda.nasr_m.yaldacalendar.Handler;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class Events extends Fragment {

    public View rootView;
    private Context context;
    private Calendar cal = Calendar.getInstance();                   //event object date
    private String startTime, endTime;
    private boolean isHoleDay = false;
    private String title, description;
    private boolean hasReminder;
    private TextView title_tv, description_tv, holeDay_tv, startTime_tv, endTime_tv, event_time_tv;

    public static Events newInstance(Context context, Calendar cal) {
        Events events = new Events();
        events.context = context;
        events.cal = cal;
        return events;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rootView == null)
            initializer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void initializer() {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInflater.inflate(R.layout.event_view, null);

        title_tv = (TextView) rootView.findViewById(R.id.event_title);
        title_tv.setTextColor(Color.BLACK);
        description_tv = (TextView) rootView.findViewById(R.id.event_description);
        description_tv.setTextColor(Color.BLACK);
        holeDay_tv = (TextView) rootView.findViewById(R.id.event_hole_day);
        startTime_tv = (TextView) rootView.findViewById(R.id.event_start_time);
        endTime_tv = (TextView) rootView.findViewById(R.id.event_end_time);
        event_time_tv = (TextView) rootView.findViewById(R.id.event_time);

        if (title != null)
            setData();
    }

    private void setData() {
        title_tv.setText(title);
        description_tv.setText(description);
        if (isHoleDay){
            event_time_tv.setText("تمام روز");
            event_time_tv.setTextAppearance(context, R.style.BoldText);
//            holeDay_tv.setVisibility(View.VISIBLE);
            startTime_tv.setVisibility(View.INVISIBLE);
            endTime_tv.setVisibility(View.INVISIBLE);
        }else {
            startTime_tv.setVisibility(View.VISIBLE);
            startTime_tv.setText(startTime);
            endTime_tv.setVisibility(View.VISIBLE);
            endTime_tv.setText(endTime);
//            holeDay_tv.setVisibility(View.INVISIBLE);
            event_time_tv.setText("-");
        }
    }

    public void setEvent(String title, String description, String startTime, String endTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isHoleDay = false;
        initializer();
    }

    public void setEvent(String title, String description) {
        this.title = title;
        this.description = description;
        this.isHoleDay = true;
        initializer();
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getCal() {
        return cal;
    }

    public boolean isHoleDay() {
        return isHoleDay;
    }
}
