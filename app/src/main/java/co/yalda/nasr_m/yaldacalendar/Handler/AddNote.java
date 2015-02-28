package co.yalda.nasr_m.yaldacalendar.Handler;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class AddNote {

    public String note;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public AddNote() {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //create dialog layout
        final EditText input = new EditText(context);
        input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        final String[] value = {new String()};

        //create dialog
        AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
        inputNote.setTitle("اضافه کردن یادداشت");
        inputNote.setView(input);

        //set dialog buttons
        inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value[0] = input.getText().toString();
                note = value[0];
                //TODO add note to current date note list
            }
        });
        inputNote.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                note = "";
            }
        });

        //show dialog
        inputNote.show();
    }

    public String getNote() {
        return note;
    }
}
