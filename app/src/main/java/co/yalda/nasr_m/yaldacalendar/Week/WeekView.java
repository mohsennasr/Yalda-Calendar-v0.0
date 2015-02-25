package co.yalda.nasr_m.yaldacalendar.Week;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.weekViewMode;

/**
 * Created by Nasr_M on 2/25/2015.
 */
public class WeekView extends Fragment {

    private View rootView;
    private weekViewMode viewMode;
    private GridView simpleWeekGrid;

    public static WeekView newInstance(weekViewMode viewMode) {
        WeekView weekView = new WeekView();
        weekView.viewMode = viewMode;
        weekView.initializer();
        return weekView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rootView != null)
            initializer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    private void initializer() {
        switch (viewMode) {
            case SimpleWeek:

                break;
            case WeekList:

                break;
        }
    }
}
