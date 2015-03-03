package co.yalda.nasr_m.yaldacalendar;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Adapters.ExpandableListAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.Handler.AddEvent;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomDrawer;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomViewPager;
import co.yalda.nasr_m.yaldacalendar.Handler.Events;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;
import co.yalda.nasr_m.yaldacalendar.PersianDatePicker.PersianDatePicker;
import co.yalda.nasr_m.yaldacalendar.Utilities.ExpandableListPreparation;
import co.yalda.nasr_m.yaldacalendar.Year.YearView;

import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Down2Up;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Left2Right;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Right2Left;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Up2Down;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomIn;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomOut;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static String[] holidayList = new String[]{"01/01", "01/02", "01/03", "01/04"};
    public static calendarType mainCalendarType = calendarType.Solar;
    public static calendarType secondCalendarType = calendarType.Gregorian;
    public static calendarType thirdCalendarType = calendarType.Gregorian;
    public static Calendar originalSelectedDate = Calendar.getInstance();
    public static PersianCalendar originalSelectedPersianDate = new PersianCalendar(Calendar.getInstance());
    public static String[] dayWeekHoliday = new String[]{"5", "6"} ;
    public static List<OCCVAC> holiday;
    public static Context context;
    public static int[] viewSize;
    public static touchEvent touchAction;
    public static ProgressDialog progressDialog;
    private CustomViewPager viewPager;
    private CustomDrawer drawerLayout;
    private ActionBar actionBar;
    private TabViewPagerAdapter tabPagerAdapter;
    private String[] tabNames = new String[]{"روزانه 1", "روزانه 2", "ماهیانه", "سالیانه"};
    private int currentTab = 0;
    private DayUC dayUCList, dayUCFull;
    private MonthView monthView;
    private YearView yearView;
    private ActionBarDrawerToggle drawerToggle;
    private boolean drawerOpen = false;
    private ExpandableListView expListView;
    private ExpandableListAdapter expAdapter;
    private float[] startPoint, endPoint, distance;
    private boolean IS_IN_VIEWPAGER_AREA = false, SWIPE_ACTION = true, ACTION_FINISHED = false;
    private Point point = new Point();
    private boolean UPDATE_DAY_LIST = false;
    private boolean UPDATE_DAY_FULL = false;
    private boolean UPDATE_MONTH = false;
    private boolean UPDATE_YEAR = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); //Set Direction Right-to-Left

        context = this;
        startPoint = new float[4];
        endPoint = new float[4];
        distance = new float[2];

        init();

        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt("Current_Selected_Tab");
            tabPagerAdapter.notifyDataSetChanged();
            actionBar.setSelectedNavigationItem(currentTab);
        }

        Point dim = new Point();
        getWindowManager().getDefaultDisplay().getSize(dim);
        viewSize = new int[2];
        viewSize[0] = dim.x;
        viewSize[1] = dim.y - getActionBar().getHeight();

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            viewSize[1] -= actionBarHeight;
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void init() {
/*
 *      setting up notification panel
 */
        //Initial and Configure ExpandableList
        //Assign ExpandableList to Layout
        expListView = (ExpandableListView) findViewById(R.id.drawer_expList);
        //Prepare ExpandableList Parent and Child Items
        ExpandableListPreparation expListData = new ExpandableListPreparation();
        //ExpandableList Adapter
        expAdapter = new ExpandableListAdapter(expListData.getParentList(), expListData.getChildList());
        expListView.setAdapter(expAdapter);

        //Assign Drawer to Layout
        drawerLayout = (CustomDrawer) findViewById(R.id.drawer_layout);

        //cofig actionBar to display home button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //initial drawer toggle and set open/close title
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, //Drawer Toggle Icon
                R.string.app_name, // Drawer Title - When Open
                R.string.app_name // Drawer Title - When Close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
                // calling onPrepareOptionsMenu() to show action bar icons
                drawerOpen = false;
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("پیام ها");
                // calling onPrepareOptionsMenu() to hide action bar icons
                drawerOpen = true;
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        drawerLayout.requestDisallowInterceptTouchEvent(false);
/*
 *      setting up tabs
 */
        //assign viewpager
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);

        //declare page offset limit in view pager
        viewPager.setOffscreenPageLimit(4);

        //assign action bar
        actionBar = getActionBar();

        //construct tab pager adapter
        tabPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());

        //set view pager adapter
        viewPager.setAdapter(tabPagerAdapter);

        //set action bar configurations
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //add tabs to action bar
        for (String tab_name : tabNames) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        dayUCList = DayUC.newInstance(Calendar.getInstance(), false, dayViewMode.DayList);
        dayUCFull = DayUC.newInstance(Calendar.getInstance(), false, dayViewMode.DayFull);
        monthView = MonthView.newInstance(Calendar.getInstance(), dayViewMode.Month);
        yearView = YearView.newInstance(Calendar.getInstance());

        getOverflowMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add items to the action bar if it is present.


        //if drawer is open hide all menu items
        if (drawerOpen) {
            getMenuInflater().inflate(R.menu.global, menu);
//            for (int i = 0; i < menu.size(); i++)
//                menu.getItem(i).setVisible(false);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.ab_add_note_item:             //add note button clicked
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
                        dayUCFull.addNote(value[0]);
                    }
                });
                inputNote.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                //show dialog
                inputNote.show();
                //TODO add note to DB and ListView
                return true;
            case R.id.ab_today_item:                //today button clicked
                PersianCalendar today = new PersianCalendar(Calendar.getInstance());
                if (today.getMiladiDate().compareTo(originalSelectedDate) == 0)
                    return true;

                if (today.getiPersianYear() != originalSelectedPersianDate.getiPersianYear()) {
                    onDateChange(dayViewMode.Year);
                }else if (today.getiPersianMonth() != originalSelectedPersianDate.getiPersianMonth()){
                    onDateChange(dayViewMode.Month);
                }else {
                    onDateChange(dayViewMode.DayFull);
                }
                originalSelectedDate = Calendar.getInstance();
                originalSelectedPersianDate.setMiladiDate(originalSelectedDate);
                return true;
            case R.id.ab_add_event_item:            //add event button clicked
                Intent eventActivity = new Intent(this, AddEvent.class);
                eventActivity.putExtra("EventMode", eventMode.NewEvent.toString());
                startActivityForResult(eventActivity, 0);
                return true;
            case R.id.ab_date_picker_item:          //date picker button clicked
                //create dialog
                AlertDialog.Builder datePicker = new AlertDialog.Builder(context);
                final Calendar calendar = Calendar.getInstance();
                final PersianCalendar persianCalendar = new PersianCalendar(Calendar.getInstance());
                //create persian date picker object and assign dates
                final PersianDatePicker persianDatePicker = new PersianDatePicker(context);
                PersianCalendar pcal = new PersianCalendar(originalSelectedDate);
                persianDatePicker.setDisplayPersianDate(pcal);
                datePicker.setView(persianDatePicker);

                //set date picker dialog buttons
                datePicker.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendar.setTime(persianDatePicker.getDisplayDate());
                        persianCalendar.setMiladiDate(calendar);
                        originalSelectedDate.setTime(calendar.getTime());
                        originalSelectedPersianDate.setMiladiDate(persianCalendar.getMiladiDate());
                        onDateChange(dayViewMode.Year);
                    }
                });
                datePicker.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                //show dialog
                datePicker.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        switch (tab.getPosition()){
            case 0:
                if (UPDATE_DAY_LIST) {
                    dayUCList.updateDayList();
                    UPDATE_DAY_LIST = false;
                }
                break;
            case 1:
                if (UPDATE_DAY_FULL) {
                    dayUCFull.updateDayFull();
                    UPDATE_DAY_FULL = false;
                }
                break;
            case 2:
                if (UPDATE_MONTH) {
                    monthView.updateMonth(originalSelectedDate);
                    UPDATE_MONTH = false;
                }
                monthView.setSelectedDate();
                break;
            case 3:
                if (UPDATE_YEAR) {
                    yearView.updateYear();
                    UPDATE_YEAR = false;
                }
                yearView.setSelectedDate();
                break;
        }
        viewPager.setCurrentItem(tab.getPosition());              //set view pager content base on selected tab
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        super.dispatchTouchEvent(event);
        if (distance[0] != 0 && event.getAction() == MotionEvent.ACTION_UP)
            ACTION_FINISHED = true;
        if ((event.getPointerCount() >= 2) && (distance[0] != 0)) {     // multiTouch gesture
            SWIPE_ACTION = false;
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                startPoint[0] = event.getX(0);
                startPoint[1] = event.getY(0);
                startPoint[2] = event.getX(1);
                startPoint[3] = event.getY(1);
            } else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                endPoint[0] = event.getX(0);
                endPoint[1] = event.getY(0);
                endPoint[2] = event.getX(1);
                endPoint[3] = event.getY(1);

                //start distance
                distance[0] = (float) Math.sqrt(Math.pow((startPoint[0] - startPoint[2]), 2)
                        + Math.pow((startPoint[1] - startPoint[3]), 2));

                //end distance
                distance[1] = (float) Math.sqrt(Math.pow((endPoint[0] - endPoint[2]), 2)
                        + Math.pow((endPoint[1] - startPoint[3]), 2));

                if (distance[0] < distance[1]) { //zoom in
                    touchAction = ZoomIn;
                } else if (distance[0] > distance[1]) { //zoom out
                    touchAction = ZoomOut;
                }
                ACTION_FINISHED = true;
            }
        }
        if (SWIPE_ACTION)                   // swipe action
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getWindowManager().getDefaultDisplay().getSize(point);
                    if ((event.getX() > point.x - 40) || (event.getY() <= getActionBar().getHeight()) || drawerOpen) {  //check for drawer touch
                        IS_IN_VIEWPAGER_AREA = false;
                        return super.dispatchTouchEvent(event);
                    } else
                        IS_IN_VIEWPAGER_AREA = true;
                    startPoint[0] = event.getX();
                    startPoint[1] = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!IS_IN_VIEWPAGER_AREA)
                        return super.dispatchTouchEvent(event);
                    endPoint[0] = event.getX();
                    endPoint[1] = event.getY();
                    float dx, dy;
                    dx = Math.abs(endPoint[0] - startPoint[0]);
                    dy = Math.abs(endPoint[1] - startPoint[1]);
                    if (dx > 10 || dy > 10) {
                        if ((dx >= dy) && (startPoint[0] >= endPoint[0])) {         // Right-to-Left Swipe
                            touchAction = Right2Left;
                        } else if ((dx > dy) && (startPoint[0] < endPoint[0])) {    // Left-to-Right Swipe
                            touchAction = Left2Right;
                        } else if ((dx < dy) && (startPoint[1] >= endPoint[1])) {   // Down-to-Up Swipe
                            touchAction = Down2Up;
                            if (progressDialog != null)
                                progressDialog.dismiss();
                        } else if ((dx < dy) && (startPoint[1] < endPoint[1])) {    // Up-to-Down Swipe
                            touchAction = Up2Down;
                            if (progressDialog != null)
                                progressDialog.dismiss();
                        }
                    }else{
                        return super.dispatchTouchEvent(event);
                    }
                    ACTION_FINISHED = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (viewPager.getCurrentItem() == 6
                            && Math.abs(event.getX() - startPoint[0]) > 10
                            && Math.abs(event.getY() - startPoint[1]) < Math.abs(event.getY() - startPoint[0])
                            && progressDialog == null
                            && IS_IN_VIEWPAGER_AREA
                            && SWIPE_ACTION
                            /*&& !yearCal.YEAR_LIST*/) {
                        progressDialog = new ProgressDialog(this);
                        progressDialog.setTitle("لطفاً منتظر بمانید...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }
                    break;
            }
        if (ACTION_FINISHED) {
            SWIPE_ACTION = true;
            int month = 0, year = 0;
            switch (touchAction) {
                case Right2Left:                                            //Increase Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 0:         // DayList view Mode
                            month = originalSelectedPersianDate.getiPersianMonth();
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.DATE, 1);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, 1);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else if (originalSelectedPersianDate.getiPersianMonth() != month){
                                onDateChange(dayViewMode.Month);
                            }else {
                                onDateChange(dayViewMode.DayList);
                            }
                            break;
                        case 1:         // DayFull View Mode
                            month = originalSelectedPersianDate.getiPersianMonth();
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.DATE, 1);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, 1);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else if (originalSelectedPersianDate.getiPersianMonth() != month){
                                onDateChange(dayViewMode.Month);
                            }else {
                                onDateChange(dayViewMode.DayFull);
                            }
                            break;
                        case 2:         // Month View Mode
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.MONTH, 1);
                            originalSelectedPersianDate.addPersian(Calendar.MONTH, 1);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else {
                                onDateChange(dayViewMode.Month);
                            }
                            break;
                        case 3:         // Year View Mode
                            originalSelectedDate.add(Calendar.YEAR, 1);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, 1);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case Left2Right:                                            //Decrease Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 0:         // DayList view Mode
                            month = originalSelectedPersianDate.getiPersianMonth();
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.DATE, -1);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, -1);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else if (originalSelectedPersianDate.getiPersianMonth() != month){
                                onDateChange(dayViewMode.Month);
                            }else {
                                onDateChange(dayViewMode.DayList);
                            }
                            break;
                        case 1:         // DayFull View Mode
                            month = originalSelectedPersianDate.getiPersianMonth();
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.DATE, -1);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, -1);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else if (originalSelectedPersianDate.getiPersianMonth() != month){
                                onDateChange(dayViewMode.Month);
                            }else {
                                onDateChange(dayViewMode.DayFull);
                            }
                            break;
                        case 2:         // Month View Mode
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.MONTH, -1);
                            originalSelectedPersianDate.addPersian(Calendar.MONTH, -1);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else {
                                onDateChange(dayViewMode.Month);
                            }
                            break;
                        case 3:         // Year View Mode
                            originalSelectedDate.add(Calendar.YEAR, -1);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, -1);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case Down2Up:                                               //Decrease Parent Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 0:         // DayList view Mode
//                            originalSelectedDate.add(Calendar.MONTH, 1);
//                            originalSelectedPersianDate.addPersianDate(Calendar.MONTH, 1);
//                            dayUCList.updateDayList();
//                            break;
                            return super.dispatchTouchEvent(event);
                        case 1:         // DayFull View Mode
//                            originalSelectedDate.add(Calendar.MONTH, 1);
//                            originalSelectedPersianDate.addPersianDate(Calendar.MONTH, 1);
//                            dayUCFull.updateDayFull();
//                            break;
                            return super.dispatchTouchEvent(event);
                        case 2:         // Month View Mode
                            originalSelectedDate.add(Calendar.YEAR, 1);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, 1);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case Up2Down:                                               //Increase Parent Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 0:         // DayList view Mode
//                            originalSelectedDate.add(Calendar.MONTH, -1);
//                            originalSelectedPersianDate.addPersianDate(Calendar.MONTH, -1);
//                            dayUCList.updateDayList();
//                            break;
                            return super.dispatchTouchEvent(event);
                        case 1:         // DayFull View Mode
//                            originalSelectedDate.add(Calendar.MONTH, -1);
//                            originalSelectedPersianDate.addPersianDate(Calendar.MONTH, -1);
//                            dayUCFull.updateDayFull();
//                            break;
                            return super.dispatchTouchEvent(event);
                        case 2:         // Month View Mode
                            originalSelectedDate.add(Calendar.YEAR, -1);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, -1);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case ZoomIn:
                    switch (viewPager.getCurrentItem()){
                        case 3:
                            yearView.yearSwitchView();
                            break;
                    }
                    break;
                case ZoomOut:
                    switch (viewPager.getCurrentItem()){
                        case 3:
                            yearView.yearSwitchView();
                            break;
                    }
                    break;
            }
            ACTION_FINISHED = false;
            startPoint = new float[4];
            endPoint = new float[4];
            distance = new float[2];
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    //on swipe actions change hole date
    public void onDateChange(dayViewMode view){

        switch (view) {
            case DayList:
                dayUCList.updateDayList();
                UPDATE_DAY_FULL = true;
                break;
            case DayFull:
                dayUCFull.updateDayFull();
                UPDATE_DAY_LIST = true;
                break;
            case Month:
                switch (viewPager.getCurrentItem()){
                    case 0:
                        dayUCList.updateDayList();
                        UPDATE_DAY_LIST = false;
                        UPDATE_DAY_FULL = true;
                        UPDATE_MONTH = true;
                        break;
                    case 1:
                        dayUCFull.updateDayFull();
                        UPDATE_DAY_FULL = false;
                        UPDATE_DAY_LIST = true;
                        UPDATE_MONTH = true;
                        break;
                    case 2:
                        monthView.updateMonth(originalSelectedDate);
                        UPDATE_MONTH = false;
                        UPDATE_DAY_LIST = true;
                        UPDATE_DAY_FULL = true;
                        break;
                    case 3:
                        UPDATE_DAY_FULL = true;
                        UPDATE_DAY_LIST = true;
                        UPDATE_MONTH = true;
                }
                break;
            case Year:
                switch (viewPager.getCurrentItem()){
                    case 0:
                        dayUCList.updateDayList();
                        UPDATE_DAY_LIST = false;
                        UPDATE_DAY_FULL = true;
                        UPDATE_MONTH = true;
                        UPDATE_YEAR = true;
                        break;
                    case 1:
                        dayUCFull.updateDayFull();
                        UPDATE_DAY_FULL = false;
                        UPDATE_DAY_LIST = true;
                        UPDATE_MONTH = true;
                        UPDATE_YEAR = true;
                        break;
                    case 2:
                        monthView.updateMonth(originalSelectedDate);
                        UPDATE_MONTH = false;
                        UPDATE_DAY_LIST = true;
                        UPDATE_DAY_FULL = true;
                        UPDATE_YEAR = true;
                        break;
                    case 3:
                        yearView.updateYear();
                        UPDATE_YEAR = false;
                        UPDATE_DAY_FULL = true;
                        UPDATE_DAY_LIST = true;
                        UPDATE_MONTH = true;
                }
                break;
        }
    }

    //if there is ot enough room for menu items, make overflow menu
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBackPressed() {
        if (drawerOpen)
            drawerLayout.closeDrawers();
//        else {
            super.onBackPressed();
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            //create dialog layout
//            final TextView input = new TextView(context);
//            input.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//            final String[] value = {new String()};
//            input.setText("آیا می خواهید خارج شوید؟");
//
//            //create dialog
//            AlertDialog.Builder inputNote = new AlertDialog.Builder(context);
//            inputNote.setTitle("خروج");
//            inputNote.setView(input);
//
//            //set dialog buttons
//            inputNote.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    finish();
//                }
//            });
//            inputNote.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                }
//            });
//
//            //show dialog
//            inputNote.show();
//        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Current_Selected_Tab", viewPager.getCurrentItem());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String eventTitle, eventDetail;
                Long eventDate;
                int startHour, startMin, endHour, endMin;
                eventMode mode = data.getStringExtra("EventMode").equals(eventMode.NewEvent.toString()) ? eventMode.NewEvent :
                        eventMode.EditEvent;
                eventTitle = data.getStringExtra("Title");
                eventDetail = data.getStringExtra("Detail");
                eventDate = data.getLongExtra("Date", -1);
                startHour = data.getIntExtra("Start_Time_Hour", -1);
                startMin = data.getIntExtra("Start_Time_Minute", -1);
                endHour = data.getIntExtra("End_Time_Hour", -1);
                endMin = data.getIntExtra("End_Time_Minute", -1);

                Events events = Events.newInstance(context,originalSelectedDate);

                events.setEvent(eventTitle, eventDetail, startHour + ":" + startMin, endHour + ":" + endMin);

                if (mode == eventMode.NewEvent)
                    dayUCList.addEvent(events);
                else
                    dayUCList.updateEvent(events);

                //TODO set event
            }
//        }
    }

    public static enum dayViewMode {
        Year,
        Month,
        DayHeader,
        DayFull,
        DayList,
        Week,
        Default
    }

    public static enum weekViewMode {
        SimpleWeek,
        WeekList
    }

    public static enum calendarType {
        Solar,
        Gregorian,
        Hejri
    }

    public static enum touchEvent {
        Right2Left,
        Left2Right,
        Up2Down,
        Down2Up,
        ZoomIn,
        ZoomOut
    }

    public static enum  eventMode {
        NewEvent,
        EditEvent
    }

    public class TabViewPagerAdapter extends FragmentPagerAdapter {

        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (UPDATE_DAY_LIST)
                        dayUCList.updateDayList();
                    return dayUCList;
                case 1:
                    if (UPDATE_DAY_FULL)
                        dayUCFull.updateDayFull();
                    return dayUCFull;
                case 2:
                    if (UPDATE_MONTH)
                        monthView.updateMonth(originalSelectedDate);
                    return monthView;
                case 3:
                    if (UPDATE_YEAR)
                        yearView.updateYear();
                    return yearView;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
