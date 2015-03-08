//package co.yalda.nasr_m.yaldacalendar.Day;
//
//import android.app.ActionBar;
//import android.app.FragmentTransaction;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.util.Calendar;
//
//import co.yalda.nasr_m.yaldacalendar.Handler.CustomViewPager;
//import co.yalda.nasr_m.yaldacalendar.MainActivity;
//
///**
// * Created by Nasr_M on 3/7/2015.
// */
//public class Days extends Fragment implements ActionBar.TabListener{
//
//    View rootView;
//    CustomViewPager viewPager;
//    ActionBar actionBar;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        LayoutInflater mInflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        rootView = mInflater.inflate(R.layout.days_tabs, null);
////        viewPager = (CustomViewPager) rootView.findViewById(R.id.days_view_pager);
////        viewPager.setOffscreenPageLimit(2);
////        DaysViewPagerAdapter daysViewPagerAdapter = new DaysViewPagerAdapter(((FragmentActivity) MainActivity.context).getSupportFragmentManager());
////        viewPager.setAdapter(daysViewPagerAdapter);
////        actionBar = getActivity().getActionBar();
////        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
////        actionBar.addTab(actionBar.newTab().setText("روزانه 1").setTabListener(this));
////        actionBar.addTab(actionBar.newTab().setText("روزانه 2").setTabListener(this));
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return rootView;
//    }
//
//    @Override
//    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }
//
//    @Override
//    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }
//
//    public class DaysViewPagerAdapter extends FragmentPagerAdapter {
//
//        public DaysViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position){
//                case 0:
//                    return DayUC.newInstance(Calendar.getInstance(), false, MainActivity.dayViewMode.DayList);
//                case 1:
//                    return DayUC.newInstance(Calendar.getInstance(), false, MainActivity.dayViewMode.DayFull);
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }
//}
