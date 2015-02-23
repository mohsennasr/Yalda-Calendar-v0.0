package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Month.MonthView;

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
        return gridList.get(position).getView();
    }
}
