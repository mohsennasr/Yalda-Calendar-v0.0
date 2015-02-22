package co.yalda.nasr_m.yaldacalendar.Month;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import co.yalda.nasr_m.yaldacalendar.Adapters.MonthGridViewAdapter;
import co.yalda.nasr_m.yaldacalendar.Calendars.PersianCalendar;
import co.yalda.nasr_m.yaldacalendar.Day.DayUC;
import co.yalda.nasr_m.yaldacalendar.MainActivity;
import co.yalda.nasr_m.yaldacalendar.R;
import co.yalda.nasr_m.yaldacalendar.Utilities.GetDeviceID;

/**
 * Created by Nasr_M on 2/21/2015.
 */
public class MonthView extends Fragment{

    private DayUC[] dayUC;                    //day user control object
    private View rootView;
    private Calendar monthCal = Calendar.getInstance();

    //Month View attributes
    private TextView monthHeader_tv;        //month name TextView
    private GridView monthGridView;         //month days gridView
    private MonthGridViewAdapter gridViewAdapter;   //month grid adapter
    private ArrayList<DayUC> dayUCList;     //DayUC Array list

    /*
    Fragment Classes have their own constructor method that we can't modify input parameter.
    So, for making Fragment Class with custom input value new method should be written to create
    class objects and set private attributes according to input values
     */
    public static MonthView newInstance(Calendar monthCal){
        MonthView monthView = new MonthView();
        monthView.monthCal.setTime(monthCal.getTime());
        return monthView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.month_view, container, false);

        monthHeader_tv = (TextView) rootView.findViewById(R.id.month_view_header);
        monthGridView = (GridView) rootView.findViewById(R.id.month_view_day_grid);

        if (dayUCList == null)
            dayUCList = new ArrayList<>();
        initialMonth();

        return rootView;
    }

    //initial attributes
    private void initialMonth(){
        PersianCalendar pCal = new PersianCalendar(monthCal);
        int maxDayMonth = pCal.getMaxDayOfMonth();
        pCal.persianSet(Calendar.DATE, 1);
        monthCal.setTime(pCal.getMiladiDate().getTime());
        int remainDay = pCal.persianPreMonthRemainingDay();
        monthCal.add(Calendar.DATE, -remainDay);
        dayUC = new DayUC[42];
        for (int i = 0; i < 42 ; i++) {
            dayUC[i] = DayUC.newInstance(monthCal, !(i<remainDay | i>(maxDayMonth+remainDay)) , MainActivity.viewMode.Month);
            dayUCList.add(dayUC[i]);
            monthCal.add(Calendar.DATE, 1);
        }

//        monthHeader_tv.setText(pCal.getPersianMonthName() + " " + String.valueOf(pCal.getiPersianYear()));

        String devId = (new GetDeviceID(getActivity())).getDEVICE_ID();
        monthHeader_tv.setText("Device ID (IMEI) is: " + devId);

//        gridViewAdapter = new MonthGridViewAdapter(getActivity(), dayUCList);
//        monthGridView.setAdapter(gridViewAdapter);
//        gridViewAdapter.notifyDataSetChanged();

//        String[] list = new String[]{"1", "2", "3"};
        ArrayList<String> arrlist = new ArrayList<>();
//        arrlist.addAll(Arrays.asList(list));

        String skey = "BAHAM";
        arrlist.add("Encryption/Decryption Key is: " + skey);


        DESKeySpec keySpec = null;
        try {
            keySpec = new DESKeySpec(skey.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Base64 encoder, decoder;

// ENCODE plainTextPassword String
            byte[] cleartext = devId.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encryptedPwd = cipher.doFinal(cleartext).toString();

// DECODE encryptedPwd String
            byte[] encrypedPwdBytes = encryptedPwd.getBytes();

            Cipher dcipher = Cipher.getInstance("DES");// cipher is not thread safe
            dcipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));

            arrlist.add("Encrypted Value is:" + encryptedPwd);
            arrlist.add("Decrypted Value is:" + plainTextPwdBytes.toString());

        } catch (InvalidKeyException | UnsupportedEncodingException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }


        gridViewAdapter = new MonthGridViewAdapter(getActivity(), arrlist);
        monthGridView.setAdapter(gridViewAdapter);
        gridViewAdapter.notifyDataSetChanged();
    }
}
