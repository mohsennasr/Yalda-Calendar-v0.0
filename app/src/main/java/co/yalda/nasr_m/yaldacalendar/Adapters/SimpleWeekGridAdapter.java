package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.viewSize;

/**
 * Created by Nasr_M on 2/25/2015.
 */
public class SimpleWeekGridAdapter extends BaseAdapter {

    private ArrayList<String> gridList;
    private MainActivity.dayViewMode viewMode;

    public SimpleWeekGridAdapter(ArrayList<String> gridList, MainActivity.dayViewMode viewMode) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.week_view_simple, null);
        }

        TextView weekItem = (TextView) convertView.findViewById(R.id.simple_week_number_text);
        weekItem.setText(gridList.get(position));

        if (viewMode == MainActivity.dayViewMode.Month) {
            convertView.setLayoutParams(new AbsListView.LayoutParams(40, (viewSize[1] - 200) / 6));
        }

        return convertView;
    }
}
