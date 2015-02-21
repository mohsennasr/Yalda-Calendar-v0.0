package co.yalda.nasr_m.yaldacalendar.Day;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Calendar;

/**
 * Created by Nasr_M on 2/16/2015.
 */
public class DayFullView extends Fragment{

    private DayUC dayUC;
    private View rootView;
    private RelativeLayout rootLayout;      //root layout of UC. UC will be linked to this layout
//    private EventClass eventList;    //day event list

    public static DayFullView newInstance(Calendar dayCal){
        DayFullView dayAgendaView = new DayFullView();
//        dayAgendaView.dayUC = new DayUC(dayCal);
        return dayAgendaView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void initialDay(/*LayoutInflater inflater, @Nullable ViewGroup container*/){

    }

    public void setData(){


    }
}
