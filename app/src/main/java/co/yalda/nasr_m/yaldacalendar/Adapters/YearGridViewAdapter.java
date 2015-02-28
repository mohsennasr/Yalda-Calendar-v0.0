package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Month.MonthView;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewSize;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class YearGridViewAdapter extends BaseAdapter {

    private ArrayList<MonthView> gridList;      //list of grid DayUC objects

    public YearGridViewAdapter(ArrayList<MonthView> gridList) {
        this.gridList = gridList;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int width, height;
        width = viewSize[0] - 20;
        height = viewSize[1] - 48;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = width / 3;
            height = height / 4;
        } else {
            width = width / 4;
            height = height / 3;
        }

        gridList.get(position).rootView.setLayoutParams(new AbsListView.LayoutParams(width, height));
        return gridList.get(position).rootView;
    }
}
