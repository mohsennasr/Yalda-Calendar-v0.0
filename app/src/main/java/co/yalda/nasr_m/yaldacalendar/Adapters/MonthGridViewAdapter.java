package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Day.DayUC;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.dayViewMode;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewSize;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthGridViewAdapter extends BaseAdapter {

    private ArrayList<DayUC> gridList;           //list of grid DayUC objects
//    private ArrayList<String> weekGridList;      //list of week Numbers objects
    private dayViewMode viewMode;                //month ViewMode

    public MonthGridViewAdapter(ArrayList<DayUC> gridList, /*ArrayList<String> weekGridList,*/ dayViewMode viewMode) {
        this.gridList = gridList;
//        this.weekGridList = weekGridList;
        this.viewMode = viewMode;
    }

    @Override
    public int getCount() {
        return gridList.size()/* + weekGridList.size()*/;
    }

    @Override
    public Object getItem(int position) {
//        if (position % 8 == 0)
//            return weekGridList.get(position / 8);
//        return gridList.get((position / 8) * 7 + position % 8 - 1);
        return gridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMode == dayViewMode.Month) {
            gridList.get(position).rootView.setLayoutParams(new AbsListView.LayoutParams(
                    (viewSize[0] - 40) / 7,
                    (viewSize[1] - 260) / 6));
        }
        return gridList.get(position).rootView;
    }

    @Override
    public boolean isEnabled(int position) {
        return gridList.get(position).isEnable;
    }
}
