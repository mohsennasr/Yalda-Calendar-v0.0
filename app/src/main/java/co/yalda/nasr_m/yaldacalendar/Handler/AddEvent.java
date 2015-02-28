package co.yalda.nasr_m.yaldacalendar.Handler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import co.yalda.nasr_m.yaldacalendar.PersianDatePicker.PersianDatePicker;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class AddEvent extends Activity {

    EditText eventTitle, eventDetail;
    PersianDatePicker datePicker;
    TimePicker startTime, endTime;
    Switch holeDay;
    LinearLayout datePickerHolder;
    TextView start, end, hole;
    Button ok, cancel;
    Intent result = new Intent();
    Context context;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); //Set Direction Right-to-Left
        setContentView(R.layout.create_event);
//        setContentView(R.layout.week_view_simple);

        eventTitle = (EditText) findViewById(R.id.event_title_et);
        eventDetail = (EditText) findViewById(R.id.event_detail_et);
        datePickerHolder = (LinearLayout) findViewById(R.id.datePicker_holder);
        startTime = (TimePicker) findViewById(R.id.start_timePicker);
        startTime.setIs24HourView(true);
        endTime = (TimePicker) findViewById(R.id.end_timePicker);
        endTime.setIs24HourView(true);
        holeDay = (Switch) findViewById(R.id.hole_day_s);
        hole = (TextView) findViewById(R.id.hole_day_tv);
        start = (TextView) findViewById(R.id.start_time_tv);
        end = (TextView) findViewById(R.id.end_time_tv);
        ok = (Button) findViewById(R.id.event_ok_bt);
        cancel = (Button) findViewById(R.id.event_cancel_bt);

        context = this;

        datePicker = new PersianDatePicker(getApplicationContext());
        datePicker.setVisibility(View.VISIBLE);
        datePickerHolder.addView(datePicker);
        endTime.setCurrentHour(startTime.getCurrentHour() + 1);

        holeDay.setChecked(false);
        holeDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startTime.setVisibility(View.GONE);
                    start.setVisibility(View.GONE);
                    endTime.setVisibility(View.GONE);
                    end.setVisibility(View.GONE);
                } else {
                    startTime.setVisibility(View.VISIBLE);
                    start.setVisibility(View.VISIBLE);
                    endTime.setVisibility(View.VISIBLE);
                    end.setVisibility(View.VISIBLE);
                }

            }
        });

        startTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                endTime.setCurrentHour(startTime.getCurrentHour() + 1);
                endTime.setCurrentMinute(startTime.getCurrentMinute());
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Event Title Could not be Empty !!", Toast.LENGTH_LONG).show();
                    return;
                } else if ((endTime.getCurrentHour() < startTime.getCurrentHour()) ||
                        (endTime.getCurrentHour() == startTime.getCurrentHour() &&
                                endTime.getCurrentMinute() < startTime.getCurrentMinute())) {
                    Toast.makeText(getApplicationContext(), "Event End Time Should be Greater Than Start Time !!", Toast.LENGTH_LONG).show();
                    return;
                }

                result.putExtra("Title", eventTitle.getText());
                result.putExtra("Detail", eventDetail.getText());
                result.putExtra("Date", datePicker.getDisplayDate().getTime());
                result.putExtra("Start_Time_Hour", startTime.getCurrentHour());
                result.putExtra("Start_Time_Minute", startTime.getCurrentMinute());
                result.putExtra("End_Time_Hour", endTime.getCurrentHour());
                result.putExtra("End_Time_Minute", endTime.getCurrentMinute());

                setResult(RESULT_OK, result);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //create dialog layout
        final TextView input = new TextView(context);
        input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        final String[] value = {new String()};
        input.setText("هیچ یک از تغییرات اعمال نخواهد شد. آیا میخواهید خارج شوید؟");

        //create dialog
        AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
        inputNote.setTitle("خروج");
        inputNote.setView(input);

        //set dialog buttons
        inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                setResult(RESULT_CANCELED, result);
                finish();
            }
        });
        inputNote.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        //show dialog
        inputNote.show();

    }
}
