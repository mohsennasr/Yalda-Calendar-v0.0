package co.yalda.nasr_m.yaldacalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.yalda.nasr_m.yaldacalendar.Adapters.SimpleListAdapter;
import co.yalda.nasr_m.yaldacalendar.Converters.JalaliCalendar;

/**
 * Created by Nasr_M on 3/10/2015.
 */
public class Test extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test, container, false);

        ListView firstList = (ListView) rootView.findViewById(R.id.test_list_first);
        ListView secondList = (ListView) rootView.findViewById(R.id.test_list_second);
        ListView thirdList = (ListView) rootView.findViewById(R.id.test_list_third);

        ArrayList<String> firstArrayList = new ArrayList<>();
        ArrayList<String> secondArrayList = new ArrayList<>();
        ArrayList<String> thirdArrayList = new ArrayList<>();

        Calendar miladiCalendar = Calendar.getInstance();
        JalaliCalendar converter = new JalaliCalendar();
//        PersianCalendar persianCalendar = new PersianCalendar(miladiCalendar);
        String pYear = "1393", pMonth = "12", pDay = "19";
        String pDate = pYear + "/" + pMonth + "/" + pDay;

        for (int i=0; i<10; i++){
            firstArrayList.add(pDate);

            Date convertedDate = converter.getGregorianDate(pDate);
            miladiCalendar.setTime(convertedDate);
            secondArrayList.add(miladiCalendar.get(Calendar.YEAR) + "/" + miladiCalendar.get(Calendar.MONTH) + "/" + miladiCalendar.get(Calendar.DATE));

            miladiCalendar.add(Calendar.DATE, 10);
            pDate = converter.getJalaliDate(miladiCalendar.getTime());
            thirdArrayList.add(pDate);

//            firstArrayList.add(miladiCalendar.get(Calendar.YEAR) + "/" + miladiCalendar.get(Calendar.MONTH) + "/" + miladiCalendar.get(Calendar.DATE));

//            thirdArrayList.add(converter.getJalaliDate(miladiCalendar.getTime()));
//            miladiCalendar.setTime(convertedDate);
//            firstArrayList.add(miladiCalendar.get(Calendar.YEAR) + "/" + miladiCalendar.get(Calendar.MONTH) + "/" + miladiCalendar.get(Calendar.DATE));
//            miladiCalendar.add(Calendar.DATE, 10);
//            secondArrayList.add(converter.getJalaliDate(miladiCalendar.getTime()));

//            persianCalendar.setMiladiDate(miladiCalendar);
        }

        SimpleListAdapter miladiAdapter = new SimpleListAdapter(firstArrayList, MainActivity.context);
        SimpleListAdapter convertedAdapter = new SimpleListAdapter(secondArrayList, MainActivity.context);
        SimpleListAdapter shamsiAdapter = new SimpleListAdapter(thirdArrayList, MainActivity.context);

        firstList.setAdapter(miladiAdapter);
        miladiAdapter.notifyDataSetChanged();
        secondList.setAdapter(convertedAdapter);
        convertedAdapter.notifyDataSetChanged();
        thirdList.setAdapter(shamsiAdapter);
        shamsiAdapter.notifyDataSetChanged();

        return rootView;
    }
}
