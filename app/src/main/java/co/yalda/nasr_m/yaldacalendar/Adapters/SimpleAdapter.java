package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.MainActivity;

/**
 * Created by Nasr_M on 2/23/2015.
 */
public class SimpleAdapter extends BaseAdapter {

    private ArrayList<String> list;

    public SimpleAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater minflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = minflater.inflate(android.R.layout.simple_list_item_1, null);

        TextView itemText = (TextView) convertView.findViewById(android.R.id.text1);
        itemText.setGravity(View.TEXT_ALIGNMENT_CENTER);
        itemText.setText(list.get(position));

        return convertView;
    }
}
