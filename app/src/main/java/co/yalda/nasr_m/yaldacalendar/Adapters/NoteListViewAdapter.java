package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/17/2015.
 */
/*
list view adapter for showing notes list in dayFull view mode
 */
public class NoteListViewAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public NoteListViewAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.context = context;
        this.layoutInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    //get number of elements in list
    public int getCount() {
        return list.size();
    }

    @Override
    //get list item in position
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    //get item id (itemId = position)
    public long getItemId(int position) {
        return position;
    }

    @Override
    //get item view. return TextView of item in position
    public View getView(final int position, View convertView, final ViewGroup parent) {

        TextView listItemTV;

        //if first time item created, create view for it. else update view
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.note_list_item, parent, false);
            listItemTV = (TextView) convertView.findViewById(R.id.note_item_text);          //list item View
            listItemTV.setText(list.get(position));
        }

        return convertView;
    }
}
