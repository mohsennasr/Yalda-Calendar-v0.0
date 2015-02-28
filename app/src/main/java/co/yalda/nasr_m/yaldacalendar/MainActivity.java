package co.yalda.nasr_m.yaldacalendar;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.Calendar;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Adapters.ExpandableListAdapter;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.Handler.AddEvent;
import co.yalda.nasr_m.yaldacalendar.Handler.AddNote;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomDrawer;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomViewPager;
import co.yalda.nasr_m.yaldacalendar.Handler.GoToDate;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;
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
    public static List<Byte> dayWeekHoliday;
    public static List<OCCVAC> holiday;
    public static Context context;
    public static int[] viewSize;
    public static touchEvent touchAction;
    public static ProgressDialog progressDialog;
    private CustomViewPager viewPager;
    private CustomDrawer drawerLayout;
    private ActionBar actionBar;
    private TabViewPagerAdapter tabPagerAdapter;
    private String[] tabNames = new String[]{"First", "Second", "Third", "Fourth"};
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        viewPager.setOffscreenPageLimit(3);

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
                AddNote addNote = new AddNote();
                //TODO add note to DB and ListView
                return true;
            case R.id.ab_today_item:                //today button clicked
                originalSelectedDate = Calendar.getInstance();
                //TODO update calendar views
                return true;
            case R.id.ab_add_event_item:            //add event button clicked
                Intent eventActivity = new Intent(this, AddEvent.class);
                startActivityForResult(eventActivity, 0);
                return true;
            case R.id.ab_date_picker_item:          //date picker button clicked
                GoToDate goToDate = new GoToDate();
                //TODO get selected date and update dates
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
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
        if (viewPager.getCurrentItem() == 2 && event.getPointerCount() >= 2) {      // multiTouch gesture
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
                    //change view to show year list
                    touchAction = ZoomIn;
                } else if (distance[0] > distance[1]) { //zoom out
                    //change view to show current year
                    touchAction = ZoomOut;
                }
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
            switch (touchAction) {
                case Right2Left:
                    switch (viewPager.getCurrentItem()) {
                        case 0:         // DayList view Mode

                            break;
                        case 1:         // DayFull View Mode

                            break;
                        case 2:         // Month View Mode

                            break;
                        case 3:         // Year View Mode

                            break;
                    }
                    break;
                case Left2Right:

                    break;
                case Down2Up:

                    break;
                case Up2Down:

                    break;
                case ZoomIn:

                    break;
                case ZoomOut:

                    break;
            }
        }
        return super.dispatchTouchEvent(event);
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

    @Override
    public void onBackPressed() {
        if (drawerOpen)
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Current_Selected_Tab", viewPager.getCurrentItem());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String eventTitle, eventDetail;
                Long eventDate;
                int startHour, startMin, endHour, endMin;

                eventTitle = data.getStringExtra("Title");
                eventDetail = data.getStringExtra("Detail");
                eventDate = data.getLongExtra("Date", -1);
                startHour = data.getIntExtra("Start_Time_Hour", -1);
                startMin = data.getIntExtra("Start_Time_Minute", -1);
                endHour = data.getIntExtra("End_Time_Hour", -1);
                endMin = data.getIntExtra("End_Time_Minute", -1);

                //TODO set event
            }
        }
    }

    public static enum dayViewMode {
        Year,
        Month,
        DayHeader,
        DayFull,
        DayList,
        Week
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

    public class TabViewPagerAdapter extends FragmentPagerAdapter {

        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return dayUCList;
                case 1:
                    return dayUCFull;
                case 2:
                    return monthView;
                case 3:
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
