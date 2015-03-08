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
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Adapters.ExpandableListAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Day.DaySwitch;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.Handler.AddEvent;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomDrawer;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomViewPager;
import co.yalda.nasr_m.yaldacalendar.Handler.Events;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;
import co.yalda.nasr_m.yaldacalendar.PersianDatePicker.PersianDatePicker;
import co.yalda.nasr_m.yaldacalendar.Utilities.ExpandableListPreparation;
import co.yalda.nasr_m.yaldacalendar.Year.YearView;

import static android.graphics.Typeface.createFromAsset;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Down2Up;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Left2Right;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Right2Left;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.Up2Down;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomIn;
import static co.yalda.nasr_m.yaldacalendar.MainActivity.touchEvent.ZoomOut;

/**
 * Created by Nasr_M on 2/28/2015.
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener, View.OnClickListener{

    public static String[] holidayList = new String[]{"01/01", "01/02", "01/03", "01/04"};
    public static calendarType mainCalendarType = calendarType.Solar;
    public static calendarType secondCalendarType = calendarType.Gregorian;
    public static calendarType thirdCalendarType = calendarType.Hejri;
    public static Calendar originalSelectedDate = Calendar.getInstance();
    public static PersianCalendar originalSelectedPersianDate = new PersianCalendar(Calendar.getInstance());
    public static String[] dayWeekHoliday = new String[]{"5", "6"} ;
    public static List<OCCVAC> holiday;
    public static Context context;
    public static int[] viewSize;
    public static touchEvent touchAction;
    public static ProgressDialog progressDialog;
    public static Typeface tahomaFont, homaFont, iranNastaliqFont, arabicFont, timesFont;
    public static int SELECTED_MONTH_INDEX = -1;
    public static int SELECTED_DAY_INDEX = -1;
    public static boolean SELECTED_DAY_CHANGED = false;
    public static boolean UPDATE_DAY_LIST = false;
    public static boolean UPDATE_DAY_FULL = false;
    public static boolean UPDATE_MONTH = false;
    public static boolean UPDATE_YEAR = false;
    private CustomViewPager viewPager;
    private CustomDrawer drawerLayout;
    private ActionBar actionBar;
    private TabViewPagerAdapter tabPagerAdapter;
    private String[] tabNames = new String[]{"روزانه 1", "روزانه 2", "ماهیانه", "سالیانه"};
    private DayUC dayUCList, dayUCFull;
    private MonthView monthView;
    private YearView yearView;
    private ActionBarDrawerToggle drawerToggle;
    private boolean drawerOpen = false;
    private ExpandableListView expListView;
    private ExpandableListAdapter expAdapter;
    private float[] startPoint, endPoint, distance;
    private boolean IS_IN_VIEWPAGER_AREA = false, SWIPE_ACTION = true, ACTION_FINISHED = false, GESTURE_ACTION = false;
    private Point point = new Point();
    private TextView actionBarSelectedDate;
    private boolean BACK_PRESSED = false;
    private long PRESSED_TIME = 0;
    private DaySwitch daySwitch = new DaySwitch();
    private int RIGHT_TO_LEFT_VALUE = -1;
    private int LEFT_TO_RIGHT_VALUE = 1;
    private int UP_TO_DOWN_VALUE = -1;
    private int DOWN_TO_UP_VALUE = 1;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); //Set Direction Right-to-Left

        context = this;

        init();

        if (savedInstanceState != null) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("Current_Selected_Tab"));
            tabPagerAdapter.notifyDataSetChanged();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void init() {
        startPoint = new float[4];
        endPoint = new float[4];
        distance = new float[2];
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

//        drawerLayout.requestDisallowInterceptTouchEvent(false);
/*
 *      setting up tabs
 */
        //assign viewpager
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);

        //declare page offset limit in view pager
        viewPager.setOffscreenPageLimit(3);

        //assign action bar
        actionBar = getActionBar();

        //construct tab pager adapter
        tabPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());

        //set view pager adapter
        viewPager.setAdapter(tabPagerAdapter);

        //set action bar configurations
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //add tabs to action bar
        for (String tab_name : tabNames) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        Point dim = new Point();
        getWindowManager().getDefaultDisplay().getSize(dim);
        viewSize = new int[2];
        viewSize[0] = dim.x;
        viewSize[1] = dim.y - actionBar.getHeight();

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            viewSize[1] -= actionBarHeight;
        }

        dayUCList = DayUC.newInstance(Calendar.getInstance(), false, dayViewMode.DayList);
        dayUCFull = DayUC.newInstance(Calendar.getInstance(), false, dayViewMode.DayFull);
        monthView = MonthView.newInstance(Calendar.getInstance(), dayViewMode.Month);
        yearView = YearView.newInstance(Calendar.getInstance());

        getOverflowMenu();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("لطفاً منتظر بمانید...");
        progressDialog.setCancelable(false);

        tahomaFont = createFromAsset(context.getAssets(), "tahoma.ttf");
        homaFont = createFromAsset(context.getAssets(), "homa.ttf");
        iranNastaliqFont = createFromAsset(context.getAssets(), "iran_nastaliq.ttf");
        arabicFont = createFromAsset(context.getAssets(), "arabic.ttf");
        timesFont = createFromAsset(context.getAssets(), "times.ttf");
    }

    @Override
    //Create Option menu items
    public boolean onCreateOptionsMenu(Menu menu) {
        //add items to the action bar if it is present.
        //if drawer is open hide all menu items
        if (drawerOpen) {
            getMenuInflater().inflate(R.menu.global, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
            View v = menu.findItem(R.id.selected_date).getActionView();
            actionBarSelectedDate = ( TextView ) v.findViewById(R.id.date_view_text);
            actionBarSelectedDate.setText(new PersianCalendar(Calendar.getInstance()).getPersianFullDate());
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    // action bar item listener
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //Handle action bar actions click
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
            case R.id.ab_add_note_item:             //add note button clicked
                dayUCFull.addNote();
                return true;
            case R.id.ab_today_item:                //today button clicked
                PersianCalendar today = new PersianCalendar(Calendar.getInstance());
                if (today.getMiladiDate().compareTo(originalSelectedDate) == 0)
                    return true;
                dayViewMode view;
                if (today.getiPersianYear() != originalSelectedPersianDate.getiPersianYear()) {
                    view = dayViewMode.Year;
                }else if (today.getiPersianMonth() != originalSelectedPersianDate.getiPersianMonth()){
                    view = dayViewMode.Month;
                }else {
                    view = viewPager.getCurrentItem() == 0 ? dayViewMode.DayList : dayViewMode.DayFull;
                }
                originalSelectedDate = Calendar.getInstance();
                originalSelectedPersianDate.setMiladiDate(originalSelectedDate);
                onDateChange(view);
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
                        dayViewMode view;
                        if (persianCalendar.getiPersianYear() != originalSelectedPersianDate.getiPersianYear())
                            view = dayViewMode.Year;
                        else if (persianCalendar.getiPersianMonth() != originalSelectedPersianDate.getiPersianMonth())
                            view = dayViewMode.Month;
                        else
                            view = viewPager.getCurrentItem() == 0 ? dayViewMode.DayList : dayViewMode.DayFull;
                        originalSelectedDate.setTime(calendar.getTime());
                        originalSelectedPersianDate.setMiladiDate(calendar);
                        onDateChange(view);
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
    // tab selection listener for change views based on tab selected
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        switch (tab.getPosition()){
            case 0:
                if (UPDATE_DAY_LIST && dayUCList != null) {
                    dayUCList.updateDayList();
                    UPDATE_DAY_LIST = false;
                }
                break;
            case 1:
                if (UPDATE_DAY_FULL && dayUCFull != null) {
                    dayUCFull.updateDayFull();
                    UPDATE_DAY_FULL = false;
                }
                break;
            case 2:
                if (UPDATE_MONTH && monthView != null) {
                    monthView.initialMonth(originalSelectedDate);
                    UPDATE_MONTH = false;
                }
                if (monthView != null)
                    monthView.setSelectedDate();
                break;
            case 3:
                if (UPDATE_YEAR && yearView != null) {
                    yearView.initialYear();
                    UPDATE_YEAR = false;
                }
                if (yearView != null)
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
    //capture all touch events and detect motions
    public boolean dispatchTouchEvent(MotionEvent event) {
//        super.dispatchTouchEvent(event);
//        if (distance[0] != 0 && event.getAction() == MotionEvent.ACTION_UP)
//            ACTION_FINISHED = true;
        if ((event.getPointerCount() >= 2)) {     // multiTouch gesture
            SWIPE_ACTION = false;
            GESTURE_ACTION = true;
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
                    if ((event.getX() > point.x - 40) || (event.getY() <= actionBar.getHeight()) || drawerOpen) {  //check for drawer touch
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
//                                if (progressDialog != null)
//                                    progressDialog.dismiss();
                        } else if ((dx < dy) && (startPoint[1] < endPoint[1])) {    // Up-to-Down Swipe
                            touchAction = Up2Down;
//                                if (progressDialog != null)
//                                    progressDialog.dismiss();
                        }
                    } else {
                        return super.dispatchTouchEvent(event);
                    }
                    ACTION_FINISHED = true;
                    break;
                case MotionEvent.ACTION_MOVE:
//                    if (viewPager.getCurrentItem() == 6
//                            && Math.abs(event.getX() - startPoint[0]) > 10
//                            && Math.abs(event.getY() - startPoint[1]) < Math.abs(event.getY() - startPoint[0])
//                            && progressDialog == null
//                            && IS_IN_VIEWPAGER_AREA
//                            && SWIPE_ACTION
//                            /*&& !yearCal.YEAR_LIST*/) {
//
//                    }
                    break;
            }
        if (ACTION_FINISHED) {
            int month = 0, year = 0;
            switch (touchAction) {
                case Right2Left:                                            //Increase Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 0:         // DayList view Mode
                            month = originalSelectedPersianDate.getiPersianMonth();
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.DATE, RIGHT_TO_LEFT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, RIGHT_TO_LEFT_VALUE);
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
                            originalSelectedDate.add(Calendar.DATE, RIGHT_TO_LEFT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, RIGHT_TO_LEFT_VALUE);
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
                            originalSelectedDate.add(Calendar.MONTH, RIGHT_TO_LEFT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.MONTH, RIGHT_TO_LEFT_VALUE);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else {
                                onDateChange(dayViewMode.Month);
                            }
                            break;
                        case 3:         // Year View Mode
                            originalSelectedDate.add(Calendar.YEAR, RIGHT_TO_LEFT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, RIGHT_TO_LEFT_VALUE);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case Left2Right:                                            //Decrease Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 0:         // DayList view Mode
                            month = originalSelectedPersianDate.getiPersianMonth();
                            year = originalSelectedPersianDate.getiPersianYear();
                            originalSelectedDate.add(Calendar.DATE, LEFT_TO_RIGHT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, LEFT_TO_RIGHT_VALUE);
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
                            originalSelectedDate.add(Calendar.DATE, LEFT_TO_RIGHT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.DATE, LEFT_TO_RIGHT_VALUE);
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
                            originalSelectedDate.add(Calendar.MONTH, LEFT_TO_RIGHT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.MONTH, LEFT_TO_RIGHT_VALUE);
                            if (originalSelectedPersianDate.getiPersianYear() != year){
                                onDateChange(dayViewMode.Year);
                            }else {
                                onDateChange(dayViewMode.Month);
                            }
                            break;
                        case 3:         // Year View Mode
                            originalSelectedDate.add(Calendar.YEAR, LEFT_TO_RIGHT_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, LEFT_TO_RIGHT_VALUE);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case Down2Up:                                               //Increase Parent Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 2:         // Month View Mode
                            originalSelectedDate.add(Calendar.YEAR, DOWN_TO_UP_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, DOWN_TO_UP_VALUE);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case Up2Down:                                               //Decrease Parent Date by 1
                    switch (viewPager.getCurrentItem()) {
                        case 2:         // Month View Mode
                            originalSelectedDate.add(Calendar.YEAR, UP_TO_DOWN_VALUE);
                            originalSelectedPersianDate.addPersian(Calendar.YEAR, UP_TO_DOWN_VALUE);
                            onDateChange(dayViewMode.Year);
                            break;
                    }
                    break;
                case ZoomIn:
                    switch (viewPager.getCurrentItem()){
//                        case 0:
//                            daySwitch.switchView(1);
                        case 2:
                            monthView.monthSwitchView(1);
                            break;
                        case 3:
                            yearView.yearSwitchView(1);
                            break;
                    }
                    break;
                case ZoomOut:
                    switch (viewPager.getCurrentItem()){
//                        case 0:
//                            daySwitch.switchView(2);
                        case 2:
                            monthView.monthSwitchView(2);
                            break;
                        case 3:
                            yearView.yearSwitchView(2);
                            break;
                    }
                    break;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            ACTION_FINISHED = false;
            SWIPE_ACTION = true;
            GESTURE_ACTION = false;
            startPoint = new float[4];
            endPoint = new float[4];
            distance = new float[2];
        }
        return super.dispatchTouchEvent(event);
    }

    //update tabs date based on date changes
    public void onDateChange(dayViewMode view){
        SELECTED_MONTH_INDEX = originalSelectedPersianDate.getiPersianMonth() -1;
        SELECTED_DAY_INDEX = originalSelectedPersianDate.getiPersianDate() + originalSelectedPersianDate.persianPreMonthRemainingDay();
        switch (view) {
            case DayList:
                dayUCList.updateDayList();
                SELECTED_DAY_CHANGED = true;
                UPDATE_DAY_FULL = (dayUCFull.getMiladiCalendar().compareTo(originalSelectedDate) != 0);
                UPDATE_DAY_LIST = false;
                break;
            case DayFull:
                dayUCFull.updateDayFull();
                SELECTED_DAY_CHANGED = true;
                UPDATE_DAY_LIST = (dayUCList.getMiladiCalendar().compareTo(originalSelectedDate) != 0);
                UPDATE_DAY_FULL = false;
                break;
            case Month:
                switch (viewPager.getCurrentItem()){
                    case 0:
                        dayUCList.updateDayList();
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_DAY_LIST = false;
                        UPDATE_DAY_FULL = (dayUCFull.getMiladiCalendar().compareTo(originalSelectedDate) != 0);
                        UPDATE_MONTH = true;
                        break;
                    case 1:
                        dayUCFull.updateDayFull();
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_DAY_FULL = false;
                        UPDATE_DAY_LIST = (dayUCList.getMiladiCalendar().compareTo(originalSelectedDate) != 0);
                        UPDATE_MONTH = true;
                        break;
                    case 2:
                        monthView.initialMonth(originalSelectedDate);
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_MONTH = false;
                        UPDATE_DAY_LIST = true;
                        UPDATE_DAY_FULL = true;
                        break;
                    case 3:
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_DAY_FULL = true;
                        UPDATE_DAY_LIST = true;
                        UPDATE_MONTH = true;
                }
                break;
            case Year:
                switch (viewPager.getCurrentItem()){
                    case 0:
                        dayUCList.updateDayList();
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_DAY_LIST = false;
                        UPDATE_DAY_FULL = (dayUCFull.getMiladiCalendar().compareTo(originalSelectedDate) != 0);
                        UPDATE_MONTH = true;
                        UPDATE_YEAR = true;
                        break;
                    case 1:
                        dayUCFull.updateDayFull();
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_DAY_FULL = false;
                        UPDATE_DAY_LIST = (dayUCList.getMiladiCalendar().compareTo(originalSelectedDate) != 0);
                        UPDATE_MONTH = true;
                        UPDATE_YEAR = true;
                        break;
                    case 2:
                        monthView.initialMonth(originalSelectedDate);
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_MONTH = false;
                        UPDATE_DAY_LIST = true;
                        UPDATE_DAY_FULL = true;
                        UPDATE_YEAR = true;
                        break;
                    case 3:
//                        progressDialog.show();
                        yearView.initialYear();
                        SELECTED_DAY_CHANGED = true;
                        UPDATE_YEAR = false;
                        UPDATE_DAY_FULL = true;
                        UPDATE_DAY_LIST = true;
                        UPDATE_MONTH = true;
//                        progressDialog.dismiss();
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
    //if back button pressed close drawer (if open) otherwise show message for exit app
    public void onBackPressed() {
        if (drawerOpen) {
            drawerLayout.closeDrawers();
        }else if (BACK_PRESSED && (Math.abs(PRESSED_TIME - Calendar.getInstance().getTimeInMillis()) < 1000)){
            finish();
        }else{
            Toast.makeText(context, "لطفا برای خروج کلید بازگشت را مجددا فشار دهید", Toast.LENGTH_SHORT).show();
            BACK_PRESSED = true;
            PRESSED_TIME = Calendar.getInstance().getTimeInMillis();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Current_Selected_Tab", viewPager.getCurrentItem());
    }

    @Override
    // get event detail from event activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String eventTitle, eventDetail, startHour, startMin, endHour, endMin;;
                boolean isHoleDay;
                Long eventDate;
                eventMode mode = data.getStringExtra("EventMode").equals(eventMode.NewEvent.toString()) ? eventMode.NewEvent :
                        eventMode.EditEvent;
                eventTitle = data.getStringExtra("Title");
                eventDetail = data.getStringExtra("Detail");
//                eventDate = data.getLongExtra("Date", -1);
//                Calendar eventCal = Calendar.getInstance();
//                eventCal.setTimeInMillis(eventDate);
                isHoleDay = data.getBooleanExtra("isHoleDay", false);
                Events events = Events.newInstance(context,originalSelectedDate);
                if (!isHoleDay) {
                    startHour = data.getStringExtra("Start_Time_Hour");
                    startMin = data.getStringExtra("Start_Time_Minute");
                    endHour = data.getStringExtra("End_Time_Hour");
                    endMin = data.getStringExtra("End_Time_Minute");
                    events.setEvent(eventTitle, eventDetail, startHour + ":" + startMin, endHour + ":" + endMin);
                }else
                    events.setEvent(eventTitle, eventDetail);

                if (mode == eventMode.NewEvent)
                    dayUCList.addEvent(events);
                else
                    dayUCList.updateEvent(events);
                //TODO set event
            }
//        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "Click...", Toast.LENGTH_SHORT).show();
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

    //select tabs views
    public class TabViewPagerAdapter extends FragmentPagerAdapter {

        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (UPDATE_DAY_LIST) {
                        dayUCList.updateDayList();
                        UPDATE_DAY_LIST = false;
                    }
                    return dayUCList;
//                    return daySwitch;
                case 1:
                    if (UPDATE_DAY_FULL) {
                        dayUCFull.updateDayFull();
                        UPDATE_DAY_FULL = false;
                    }
                    return dayUCFull;
                case 2:
                    if (UPDATE_MONTH) {
                        monthView.initialMonth(originalSelectedDate);
                        UPDATE_MONTH = false;
                    }
                    return monthView;
                case 3:
                    if (UPDATE_YEAR) {
                        yearView.initialYear();
                        UPDATE_YEAR = false;
                    }
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
