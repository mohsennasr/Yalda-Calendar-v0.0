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
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class AddEvent extends Activity {

    private EditText eventTitle, eventDetail;
//    private PersianDatePicker datePicker;
    private TimePicker startTime, endTime;
    private Switch holeDay;
//    private LinearLayout datePickerHolder;
    private TextView start, end, hole;
    private Button ok, cancel;
    private Intent result = new Intent();
    private Context context;
    private MainActivity.eventMode eventMode;
    private boolean isHoleDay = false;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); //Set Direction Right-to-Left
        setContentView(R.layout.create_event);
//        setContentView(R.layout.week_view_simple);

        eventTitle = (EditText) findViewById(R.id.event_title_et);
        eventDetail = (EditText) findViewById(R.id.event_detail_et);
//        datePickerHolder = (LinearLayout) findViewById(R.id.datePicker_holder);
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

//        datePicker = new PersianDatePicker(getApplicationContext());
//        datePicker.setVisibility(View.VISIBLE);
//        datePickerHolder.addView(datePicker);
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
                    isHoleDay = true;
                } else {
                    startTime.setVisibility(View.VISIBLE);
                    start.setVisibility(View.VISIBLE);
                    endTime.setVisibility(View.VISIBLE);
                    end.setVisibility(View.VISIBLE);
                    isHoleDay = false;
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "عنوان رویداد وار نشده است !!", Toast.LENGTH_LONG).show();
                    return;
                } else if ((endTime.getCurrentHour() < startTime.getCurrentHour()) ||
                        (endTime.getCurrentHour() == startTime.getCurrentHour() &&
                                endTime.getCurrentMinute() < startTime.getCurrentMinute())) {
                    Toast.makeText(getApplicationContext(), "زمان خاتمه رویداد کوچکتر از زمان شروع است !!", Toast.LENGTH_LONG).show();
                    return;
                }
                result.putExtra("EventMode", eventMode.toString());
                result.putExtra("Title", eventTitle.getText().toString());
                result.putExtra("Detail", eventDetail.getText().toString());
//                result.putExtra("Date", datePicker.getDisplayPersianDate().getMiladiDate().getTimeInMillis());
                result.putExtra("isHoleDay", isHoleDay);
                if (!isHoleDay){
                    result.putExtra("Start_Time_Hour", startTime.getCurrentHour() < 10 ? "0" + startTime.getCurrentHour().toString()
                            : startTime.getCurrentHour());
                    result.putExtra("Start_Time_Minute", startTime.getCurrentMinute() < 10 ? "0" + startTime.getCurrentMinute().toString()
                            : startTime.getCurrentMinute().toString());
                    result.putExtra("End_Time_Hour", endTime.getCurrentHour() < 10 ? "0" + endTime.getCurrentHour().toString()
                            : endTime.getCurrentHour().toString());
                    result.putExtra("End_Time_Minute", endTime.getCurrentMinute() < 10 ? "0" + endTime.getCurrentMinute().toString()
                            : endTime.getCurrentMinute().toString());
                }

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

        Bundle extras = getIntent().getExtras();
        eventMode = extras.getString("EventMode").equals(MainActivity.eventMode.NewEvent.toString()) ? MainActivity.eventMode.NewEvent :
                MainActivity.eventMode.EditEvent;
        if (eventMode == MainActivity.eventMode.EditEvent){
            eventTitle.setText(extras.getString("Title"));
            eventDetail.setText(extras.getString("Detail"));
            isHoleDay = extras.getBoolean("isHoleDay");
            if (!isHoleDay) {
                startTime.setCurrentHour(Integer.valueOf(extras.getString("Start_Time").substring(0, 2)));
                startTime.setCurrentMinute(Integer.valueOf(extras.getString("Start_Time").substring(3, 5)));
                endTime.setCurrentHour(Integer.valueOf(extras.getString("End_Time").substring(0, 2)));
                endTime.setCurrentMinute(Integer.valueOf(extras.getString("End_Time").substring(3, 5)));
            }else {
                isHoleDay = true;
                holeDay.setChecked(true);
            }
            Calendar cal = Calendar.getInstance();
//            cal.setTimeInMillis(extras.getLong("Date"));
//            datePicker.setDisplayPersianDate(new PersianCalendar(cal));
        }
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
