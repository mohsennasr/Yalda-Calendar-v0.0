package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nasr_M on 3/10/2015.
 */
public class SimpleListAdapter extends BaseAdapter {
    private ArrayList<String> listArray;           //events array list
    private Context context;

    public SimpleListAdapter(ArrayList<String> eventListArray, Context context) {
        this.listArray = eventListArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listArray.size();
    }

    @Override
    public Object getItem(int position) {
        return listArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView itemText = (TextView) convertView.findViewById(android.R.id.text1);
        itemText.setText(listArray.get(position));
        //return event view to show in list view
        return convertView;
    }
}
