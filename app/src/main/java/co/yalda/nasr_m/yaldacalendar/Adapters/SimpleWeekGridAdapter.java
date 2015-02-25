package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.MainActivity;

/**
 * Created by Nasr_M on 2/25/2015.
 */
public class SimpleWeekGridAdapter extends BaseAdapter {

    private ArrayList<String> gridList;

    public SimpleWeekGridAdapter(ArrayList<String> gridList) {
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
//        if (convertView == null){
//            LayoutInflater mInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(R.layout.week_view_simple, null);
//        }

        TextView weekItem = new TextView(MainActivity.context);
        weekItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        weekItem.setVisibility(View.VISIBLE);
        weekItem.setText(gridList.get(position));

        return weekItem;
    }
}
