package co.yalda.nasr_m.yaldacalendar.Converters;

import java.util.Calendar;

/**
 * Created by Nasr_M on 2/24/2015.
 */
public class ArabicDateConverter {
    static Double gmod(Double n, Double m) {
        return ((n % m) + m) % m;
    }

    static Integer[] kuwaiticalendar(boolean adjust, Calendar today) {

        int adj = 0;
        if (adjust) {
            adj = 0;
        } else {
            adj = 1;
        }

    /*if (adjust) {
        int adjustmili = 1000 * 60 * 60 * 24 * adj;
        long todaymili = today.getTimeInMillis() + adjustmili;
        today.setTimeInMillis(todaymili);
    }*/

        Integer tday = today.get(Calendar.DAY_OF_MONTH);
        Integer tmonth = today.get(Calendar.MONTH);
        Integer tyear = today.get(Calendar.YEAR);

        Double day = tday.doubleValue();
        Double month = tmonth.doubleValue();
        Double year = tyear.doubleValue();

        Double m = month + 1.;
        Double y = year;
        if (m < 3) {
            y -= 1;
            m += 12;
        }

        Double a = Math.floor(y / 100);
        Double b = 2 - a + Math.floor(a / 4.);

        if (y < 1583)
            b = 0.;
        if (y == 1582) {
            if (m > 10)
                b = -10.;
            if (m == 10) {
                b = 0.;
                if (day > 4)
                    b = -10.;
            }
        }

        Double jd = Math.floor(365.25 * (y + 4716.)) + Math.floor(30.6001 * (m + 1.)) + day
                + b - 1524.;
        b = 0.;
        if (jd > 2299160) {
            a = Math.floor((jd - 1867216.25) / 36524.25);
            b = 1. + a - Math.floor(a / 4.);
        }
        Double bb = jd + b + 1524;
        Double cc = Math.floor((bb - 122.1) / 365.25);
        Double dd = Math.floor(365.25 * cc);
        Double ee = Math.floor((bb - dd) / 30.6001);
        day = (bb - dd) - Math.floor(30.6001 * ee);
        month = ee - 1.;
        if (ee > 13.) {
            cc += 1.;
            month = ee - 13.;
        }
        year = cc - 4716.;

        Double wd = gmod(jd + 1, 7.0) + 1;

        Double iyear = 10631. / 30.;
        Double epochastro = 1948084.0;
        Double epochcivil = 1948085.0;

        Double shift1 = 8.01 / 60.;

        Double z = jd - epochastro;
        Double cyc = Math.floor(z / 10631.);
        z = z - 10631.0 * cyc;
        Double j = Math.floor((z - shift1) / iyear);
        Double iy = 30.0 * cyc + j;
        z = z - Math.floor(j * iyear + shift1);
        Double im = Math.floor((z + 28.5001) / 29.5);
        if (im == 13.0)
            im = 12.0;
        Double id = z - Math.floor(29.5001 * im - 29);

        Integer[] myRes = new Integer[8];


        myRes[0] = day.intValue(); // calculated day (CE)
        myRes[1] = month.intValue() - 1; // calculated month (CE)
        myRes[2] = year.intValue(); // calculated year (CE)
        myRes[3] = jd.intValue() - 1; // julian day number
        myRes[4] = wd.intValue() - 1; // weekday number
        myRes[5] = id.intValue(); // islamic date
        myRes[6] = im.intValue() - 1; // islamic month
        myRes[7] = iy.intValue(); // islamic year

        return myRes;
    }

    public static Integer[] writeIslamicDate(Calendar Date) {
        String[] wdNames = {"Ahad", "Ithnin", "Thulatha", "Arbaa", "Khams",
                "Jumuah", "Sabt"};
        String[] iMonthNames = {"محرم", "صفر", "ربیع الاول",
                "ربیع الثانی", "جمادی الاول", "جمادی الثانی", "رجب",
                "شعبان", "رمضان", "شوال", "ذی القعده", "ذی الحجه"};
        // This Value is used to give the correct day +- 1 day
        boolean dayTest = true;
        Integer[] iDate = kuwaiticalendar(dayTest, Date);
        Integer[] res = {iDate[7], iDate[6], iDate[5], iDate[4]};

        return res;
    }
}
