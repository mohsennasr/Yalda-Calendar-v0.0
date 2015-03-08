//package co.yalda.nasr_m.yaldacalendar.Day;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import co.yalda.nasr_m.yaldacalendar.MainActivity;
//import co.yalda.nasr_m.yaldacalendar.R;
//
//import static co.yalda.nasr_m.yaldacalendar.MainActivity.context;
//
///**
// * Created by Nasr_M on 3/7/2015.
// */
//public class DaySwitch extends Fragment {
//
//    DayUC dayList, dayFull;
//    View rootView;
//    LinearLayout listLayout, fullLayout;
//    int CURRENT_VIEW = 1;
//    FrameLayout listFrame, fullFrame;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        dayList = DayUC.newInstance(MainActivity.originalSelectedDate, false, MainActivity.dayViewMode.DayList);
//        dayFull = DayUC.newInstance(MainActivity.originalSelectedDate, false, MainActivity.dayViewMode.DayFull);
//        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        rootView = mInflater.inflate(R.layout.day_switch, null);
//        listLayout = (LinearLayout) rootView.findViewById(R.id.day_list);
//        fullLayout = (LinearLayout) rootView.findViewById(R.id.day_full);
//        listLayout.addView(dayList.rootView);
//        fullLayout.addView(dayFull.rootView);
//        fullLayout.setVisibility(View.INVISIBLE);
////        listFrame = (FrameLayout) rootView.findViewById(R.id.day_list);
////        fullFrame = (FrameLayout) rootView.findViewById(R.id.day_full);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(listFrame.getId(), dayList);
////        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(fullFrame.getId(), dayFull);
////        fullFrame.setVisibility(View.INVISIBLE);
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
//
//    public void switchView(int view){
//        if (CURRENT_VIEW == 1 && view == 2){
//            listLayout.setVisibility(View.GONE);
//            fullLayout.setVisibility(View.VISIBLE);
//            CURRENT_VIEW = 2;
//        }else if (CURRENT_VIEW == 2 && view == 1){
//            fullLayout.setVisibility(View.GONE);
//            listLayout.setVisibility(View.VISIBLE);
//            CURRENT_VIEW = 1;
//        }
//    }
//}
