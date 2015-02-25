package co.yalda.nasr_m.yaldacalendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomDrawer;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomViewPager;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;
import co.yalda.nasr_m.yaldacalendar.Year.YearView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener {

    public static String[] holidayList = new String[]{"01/01", "01/02", "01/03", "01/04"};
    public static calendarType mainCalendarType = calendarType.Solar;
    public static calendarType secondCalendarType = calendarType.Gregorian;
    public static calendarType thirdCalendarType = calendarType.Gregorian;
    public static Calendar originalSelectedDate = Calendar.getInstance();
    public static List<Byte> dayWeekHoliday;
    public static List<OCCVAC> holiday;
    public static Context context;
    public static int[] viewSize;


    private CustomViewPager viewPager;
    private ActionBar actionBar;
    private TabViewPagerAdapter tabPagerAdapter;
    private String[] tabNames = new String[]{"First", "Second", "Third", "Fourth"};
    private int currentTab = 0;
    private DayUC dayUCList, dayUCFull;
    private MonthView monthView;
    private YearView yearView;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_main);

        context = this;

//        if (savedInstanceState == null)
//            getSupportFragmentManager().beginTransaction()
//            .replace(R.id.container, DayListView.newInstance(this, Calendar.getInstance()))
//                    .commit();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (CustomDrawer) findViewById(R.id.drawer_layout));

        //setup view pager
        //assign viewpager
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);

        //declare page offset limit in view pager
        viewPager.setOffscreenPageLimit(4);

        //assign action bar
        actionBar = getSupportActionBar();

        //cofig actionBar to display home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt("Current_Selected_Tab");
            tabPagerAdapter.notifyDataSetChanged();
            actionBar.setSelectedNavigationItem(currentTab);
        }

        Point dim = new Point();
        getWindowManager().getDefaultDisplay().getSize(dim);
        viewSize = new int[2];
        viewSize[0] = dim.x;
        viewSize[1] = dim.y - getSupportActionBar().getHeight();

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            viewSize[1] -= actionBarHeight;
        }


        dayUCList = DayUC.newInstance(Calendar.getInstance(), false, dayViewMode.DayList);
        dayUCFull = DayUC.newInstance(Calendar.getInstance(), false, dayViewMode.DayFull);
        monthView = MonthView.newInstance(Calendar.getInstance(), dayViewMode.Month);
        yearView = YearView.newInstance(Calendar.getInstance());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container,
////                        PlaceHolderFragment.newInstance(position + 1)
//                        DayListView.newInstance(getApplicationContext(), Calendar.getInstance())
//                ).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.menu, menu);
//            restoreActionBar();
        return true;
//        }
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Current_Selected_Tab", viewPager.getCurrentItem());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        String swipeDirection = "";

        switch (viewPager.getCurrentItem()) {
            case 0:             //day list view
        }

//        if (viewPager.getCurrentItem() == 2 && event.getPointerCount() >= 2) {
//            SWIPE_ACTION = false;
//            if (progressDialog != null){
//                progressDialog.dismiss();
//                progressDialog = null;
//            }
//
//            if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
//                startPoint[0] = event.getX(0);
//                startPoint[1] = event.getY(0);
//                startPoint[2] = event.getX(1);
//                startPoint[3] = event.getY(1);
//            }else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
//                endPoint[0] = event.getX(0);
//                endPoint[1] = event.getY(0);
//                endPoint[2] = event.getX(1);
//                endPoint[3] = event.getY(1);
//
//                //start distance
//                distance[0] = (float) Math.sqrt(Math.pow ((startPoint[0] - startPoint[2]), 2)
//                        + Math.pow ((startPoint[1] - startPoint[3]), 2));
//
//                //end distance
//                distance[1] = (float) Math.sqrt(Math.pow ((endPoint[0] - endPoint[2]), 2)
//                        + Math.pow ((endPoint[1] - startPoint[3]), 2));
//
//                if (distance[0] < distance[1]) { //zoom in
//                    //change view to show year list
//                    yearCal.yearListView("IN");
//                }else if (distance[0] > distance[1]) { //zoom out
//                    //change view to show current year
//                    yearCal.yearListView("OUT");
//                }
//            }
//
////            SGD.onTouchEvent(event);
////            return true;
//        }
//        if (SWIPE_ACTION)
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    getWindowManager().getDefaultDisplay().getSize(point);
//                    if ((event.getX() > point.x - 40) || (event.getY() <= getActionBar().getHeight()) || drawerOpen) {
//                        IS_IN_VIEWPAGER_AREA = false;
//                        return super.dispatchTouchEvent(event);
//                    } else
//                        IS_IN_VIEWPAGER_AREA = true;
//                    startPoint[0] = event.getX();
//                    startPoint[1] = event.getY();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    if (!IS_IN_VIEWPAGER_AREA)
//                        return super.dispatchTouchEvent(event);
//                    endPoint[0] = event.getX();
//                    endPoint[1] = event.getY();
//                    float dx, dy;
//                    dx = Math.abs(endPoint[0] - startPoint[0]);
//                    dy = Math.abs(endPoint[1] - startPoint[1]);
//
//                    if ((dx >= dy) && (startPoint[0] >= endPoint[0])) {
//                        swipeDirection = "R2L";
//                    } else if ((dx > dy) && (startPoint[0] < endPoint[0])) {
//                        swipeDirection = "L2R";
//                    } else if ((dx < dy) && (startPoint[1] >= endPoint[1])) {
//                        swipeDirection = "D2U";
//                        if (progressDialog != null)
//                            progressDialog.dismiss();
//                    } else if ((dx < dy) && (startPoint[1] < endPoint[1])) {
//                        swipeDirection = "U2D";
//                        if (progressDialog != null)
//                            progressDialog.dismiss();
//                    }
//                    if (dx > 10) {
//                        startPoint[0] = endPoint[0] = startPoint[1] = endPoint[1] = 0;
//                        switch (customViewPager.getCurrentItem()) {
//                            case 0:     //Day Tab
//                                TOUCH_ACTION_MOVE = false;
//                                if (swipeDirection == "R2L") {
//                                    cal.add(Calendar.DATE, 1);
//                                    todayCal.updateDay(1);
//                                } else if (swipeDirection == "L2R") {
//                                    cal.add(Calendar.DATE, -1);
//                                    todayCal.updateDay(-1);
//                                }
//                                break;
//                            case 1:     //Month Tab
//                                TOUCH_ACTION_MOVE = false;
//                                if (swipeDirection == "R2L") {
//                                    cal.add(Calendar.MONTH, 1);
//                                    monthCal.updateThreadStart(1);
//                                } else if (swipeDirection == "L2R") {
//                                    cal.add(Calendar.MONTH, -1);
//                                    monthCal.updateThreadStart(-1);
//                                } else if (swipeDirection == "D2U") {
//                                    cal.add(Calendar.YEAR, 1);
//                                    monthCal.updateThreadStart(1);
//                                } else if (swipeDirection == "U2D") {
//                                    cal.add(Calendar.YEAR, -1);
//                                    monthCal.updateThreadStart(-1);
//                                }
//                                break;
//                            case 2:     //Year Tab
//                                if (!yearCal.YEAR_LIST) {
//                                    TOUCH_ACTION_MOVE = false;
//                                    if (swipeDirection == "R2L") {
//                                        cal.add(Calendar.YEAR, 1);
//                                        yearCal.updateYear(1);
//                                    } else if (swipeDirection == "L2R") {
//                                        cal.add(Calendar.YEAR, -1);
//                                        yearCal.updateYear(-1);
//                                    }
//                                }
//                                break;
//                        }
//                    }
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    if (customViewPager.getCurrentItem() == 2
//                            && Math.abs(event.getX() - startPoint[0]) > 10
//                            && Math.abs(event.getY() - startPoint[1]) < Math.abs(event.getY() - startPoint[0])
//                            && progressDialog == null
//                            && IS_IN_VIEWPAGER_AREA
//                            && SWIPE_ACTION
//                            && !yearCal.YEAR_LIST) {
//                        progressDialog = new ProgressDialog(this);
//                        progressDialog.setTitle("لطفاً منتظر بمانید...");
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
//                    }
//                    break;
//            }
//        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//            SWIPE_ACTION = true;
//        }

        return super.dispatchTouchEvent(ev);
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

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (yearView != null)
//            yearView.setColumns();
//    }

    public static enum simpleListViewMode {
        MonthWeekNumbers,
        YearWeekNumbers,
        MonthWeekDays,
        YearWeekDays
    }

    public static enum calendarType {
        Solar,
        Gregorian,
        Hejri
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
