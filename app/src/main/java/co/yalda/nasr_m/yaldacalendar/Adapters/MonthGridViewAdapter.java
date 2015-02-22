package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthGridViewAdapter extends BaseAdapter {

//    private ArrayList<DayUC> gridList;      //list of grid DayUC objects
    private ArrayList<String> gridList;      //list of grid DayUC objects
    Context context;

    public MonthGridViewAdapter(Context context, ArrayList<String> gridList) {
        this.gridList = gridList;
//        if (gridList == null)
//            gridList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return gridList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridList.get(position);
//        return DayUC.newInstance(Calendar.getInstance(), true, MainActivity.viewMode.DayHeader);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = infalInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(gridList.get(position));
        return convertView;
//        return gridList.get(position).getView();
    }
}
