package co.yalda.nasr_m.yaldacalendar;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import co.yalda.nasr_m.yaldacalendar.Day.DayListView;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomDrawer;
import co.yalda.nasr_m.yaldacalendar.Handler.CustomViewPager;
import co.yalda.nasr_m.yaldacalendar.Month.MonthView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener {

    public static enum viewMode{
        Year,
        Month,
        DayHeader,
        DayFull,
        DaySimple,
        Week
    }

    public static enum calendarType{
        Solar,
        Gregorian,
        Hejri
    }

    public static calendarType mainCalendarType = calendarType.Solar;
    public static calendarType secondCalendarType = calendarType.Gregorian;
    public static calendarType thirdCalendarType = calendarType.Gregorian;
    public static Calendar originalSelectedDate;

    public static List<Byte> dayWeekHoliday;
    public static List<OCCVAC> holiday;

    private CustomViewPager viewPager;
    private ActionBar actionBar;
    private TabViewPagerAdapter tabPagerAdapter;
    private String[] tabNames = new String[]{"First", "Second", "Third"};
    private int currentTab = 0;


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
        viewPager.setOffscreenPageLimit(3);

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
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public class TabViewPagerAdapter extends FragmentPagerAdapter{

        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return DayListView.newInstance(getApplicationContext(), Calendar.getInstance());
                case 1:
                    return MonthView.newInstance(Calendar.getInstance());
                case 2:
//                    return YearView.newInstance(Calendar.getInstance());
                    return DayUC.newInstance(Calendar.getInstance(), true, viewMode.Month);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
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
}
