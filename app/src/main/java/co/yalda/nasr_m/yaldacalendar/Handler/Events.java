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
    private String title, description;
    private boolean hasReminder;
    private TextView title_tv, description_tv;

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

        if (title != null)
            setData();
    }

    private void setData() {
        title_tv.setText(title);
        description_tv.setText(description);
    }

    public void setEvent(String title, String description, String startTime, String endTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        initializer();
    }
}
