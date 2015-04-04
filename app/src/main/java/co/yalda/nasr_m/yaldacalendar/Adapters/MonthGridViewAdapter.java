package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Day.DayUC;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.lastUCDaySelected;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.originalSelectedPersianDate;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewSize;

/**
 * Created by Nasr_M on 2/21/2015.
 */
/*
grid view adapter for showing month days grid
 */
public class MonthGridViewAdapter extends BaseAdapter {

    private ArrayList<DayUC> gridList;           //list of grid DayUC objects
    private dayViewMode viewMode;                //month ViewMode

    public MonthGridViewAdapter(ArrayList<DayUC> gridList, dayViewMode viewMode) {
        this.gridList = gridList;
        this.viewMode = viewMode;
    }

    @Override
    public int getCount() {
        return gridList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    /*
    return dayUC view for showing in grid view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMode == dayViewMode.Month) {
            gridList.get(position).rootView.setLayoutParams(new AbsListView.LayoutParams(       //resize day view to fit hole screen
                    (viewSize[0] - 40) / 7,
                    (viewSize[1] - 260) / 6));
        }else {
            gridList.get(position).rootView.setLayoutParams(new AbsListView.LayoutParams(       //resize day view to fit hole screen
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return gridList.get(position).rootView;
    }

    @Override
    public boolean isEnabled(int position) {
        return gridList.get(position).isEnable;
    }

    public void update(Calendar calendar, int position, boolean isEnable){
        gridList.get(position).updateMonth(calendar, isEnable);
        if (gridList.get(position).persianCalendar.getiPersianDay() == originalSelectedPersianDate.getiPersianDay()
                && gridList.get(position).isEnable){
            gridList.get(position).setSelectedDay();
            lastUCDaySelected = gridList.get(position);
        }
    }
}
