package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 3/1/2015.
 */
public class YearListGridAdapter extends BaseAdapter{

    ArrayList<String> yearList;
    int currentPosition;

    public YearListGridAdapter(ArrayList<String> yearList, int currentPosition) {
        this.yearList = yearList;
        this.currentPosition = currentPosition;
    }

    @Override
    public int getCount() {
        return yearList.size();
    }

    @Override
    public Object getItem(int position) {
        return yearList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.simple_list_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.simple_text);
        item.setText(String.valueOf(PersianUtil.toPersian(Integer.valueOf(yearList.get(position)))));
        item.setTextSize(32);
        item.setTypeface(MainActivity.homaFont);

        convertView.setLayoutParams(new AbsListView.LayoutParams(MainActivity.viewSize[0] / 3,
//                (MainActivity.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 4),
                (MainActivity.viewSize[1] - 200) / 3
//                (MainActivity.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 4 : 3))
        ));

        if (position == currentPosition)
            convertView.setBackgroundResource(R.drawable.background_rectangle);

        return convertView;
    }
}
