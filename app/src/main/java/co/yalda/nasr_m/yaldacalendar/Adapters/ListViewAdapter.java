package co.yalda.nasr_m.yaldacalendar.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class ListViewAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListViewAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView listItemTV;
        final Button noteViewBT, noteEditBT, noteRemoveBT;

        //if first time item created, create view for it. else update view
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.note_list_item, parent, false);
        }

        listItemTV = (TextView) convertView.findViewById(R.id.note_item_text);          //list item View
        noteViewBT = (Button) convertView.findViewById(R.id.note_item_view_bt);         //button for view text of list item
        noteEditBT = (Button) convertView.findViewById(R.id.note_edit_bt);         //button for edit text of list item
        noteRemoveBT = (Button) convertView.findViewById(R.id.note_remove_bt);     ////button for remove list item

        //view item button click listener
        noteViewBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //inflate dialog layout
                View noteViewLayout = layoutInflater.inflate(R.layout.note_view_dialog, null);

                TextView noteViewTV = (TextView) noteViewLayout.findViewById(R.id.note_item_text);
                noteViewTV.setText(list.get(position));

                AlertDialog.Builder noteViewDialog = new AlertDialog.Builder(context);
                noteViewDialog.setTitle("مشاهده یادداشت");
                noteViewDialog.setView(noteViewLayout);

                noteViewDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                noteViewDialog.show();
            }
        });

        //edit item button click listener
        noteEditBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View noteEditLayout = layoutInflater.inflate(R.layout.note_edit_dialog, null);

                final EditText noteEditET = (EditText) noteEditLayout.findViewById(R.id.note_edit_tv);
                noteEditET.setText(list.get(position));

                AlertDialog.Builder noteEditDialog = new AlertDialog.Builder(context);
                noteEditDialog.setTitle("ویرایش یادداشت");
                noteEditDialog.setView(noteEditLayout);

                noteEditDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (String.valueOf(noteEditET.getText()).isEmpty()){
                            list.remove(position);
                        }else {
                            list.set(position, String.valueOf(noteEditET.getText()));
                        }
                        notifyDataSetChanged();

                        //TO DO should update note in DB
                    }
                });
                noteEditDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing!!
                    }
                });
                noteEditDialog.show();
            }
        });

        //remove item button click listener
        noteRemoveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder noteRemoveDialog = new AlertDialog.Builder(context);
                noteRemoveDialog.setTitle("حذف یادداشت");
                noteRemoveDialog.setMessage("آیا از حذف یادداشت مطمئن هستید؟");

                noteRemoveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        notifyDataSetChanged();

                        //TO DO should remove note from DB
                    }
                });

                noteRemoveDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                noteRemoveDialog.show();
            }
        });
        return convertView;
    }
}
