package co.yalda.nasr_m.yaldacalendar.Utilities;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Nasr_M on 2/22/2015.
 */
public class GetDeviceID {

    private String DEVICE_ID;

    public GetDeviceID(Context context) {
        DEVICE_ID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }
}
