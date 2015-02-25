package co.yalda.nasr_m.yaldacalendar.Calendars;

import java.util.Calendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class PersianCalendar {

    private Calendar miladiDate = getInstance();
    private int iPersianYear, iPersianMonth, iPersianDate, iPersianWeekDay, iPersianFirstDayOfWeek;
    private String persianMonthName, persianDayName, sPersianFirstDayOfWeek;
    private String[] persianMonthNames = new String[]{"فروردین", "اردیبهشت", "خرداد"
            , "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
    private String[] persianWeekDayNames = new String[]{"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "چمعه"};
    private int[] monthDays = new int[]{31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    public PersianCalendar(Calendar date) {
        this.miladiDate.setTime(date.getTime());
        calculateDate();
        iPersianFirstDayOfWeek = 7;
    }

    private void calculateDate() {
        String[] convertedDate = MiladiToSolarConverter.getCurrentShamsidate(miladiDate);
        iPersianYear = Integer.valueOf(convertedDate[0]);
        iPersianMonth = Integer.valueOf(convertedDate[1]);
        iPersianDate = Integer.valueOf(convertedDate[2]);
        persianMonthName = convertedDate[3];
        persianDayName = convertedDate[4];
        iPersianWeekDay = miladiDate.get(DAY_OF_WEEK);
    }

    public int getMaxDayOfMonth() {
        if (iPersianMonth == 11 && isLeap(iPersianYear))
            return 30;
        else
            return monthDays[iPersianMonth - 1];
    }

    public int getPersianWeekNumber() {
        int firstWeekDays, daysCount = 0, weekNum;

//        if(iPersianMonth == 0)
//            daysCount = iPersianDate;
        if (iPersianMonth <= 6)
            daysCount += (iPersianMonth - 1) * 31 + iPersianDate - 1;
        else
            daysCount += 6 * 31 + (iPersianMonth - 7) * 30 + iPersianDate - 1;

        miladiDate.add(DATE, -(daysCount));
        firstWeekDays = (miladiDate.get(DAY_OF_WEEK) - iPersianFirstDayOfWeek + 7) % 7;
        daysCount = daysCount + firstWeekDays;
        weekNum = ((int) Math.floor(daysCount / 7));

        if (weekNum == 0 && daysCount >= 3)
            weekNum = 53;
        else if (weekNum == 0 && daysCount < 3)
            weekNum = 1;

        miladiDate.add(DATE, daysCount);
        return weekNum;
    }

    public int getFirstWeekNumberOfMonth() {
        miladiDate.add(DATE, -(iPersianDate - 1));
        calculateDate();
        int weekNum = getPersianWeekNumber();
        miladiDate.add(DATE, iPersianDate - 1);
        calculateDate();
        return weekNum;
    }

    public int persianPreMonthRemainingDay() {
        miladiDate.add(DATE, -(iPersianDate - 1));

        int firsDayOfMonth = miladiDate.get(DAY_OF_WEEK);
        int remainingDay = (firsDayOfMonth - iPersianFirstDayOfWeek + 7) % 7;

        miladiDate.add(DATE, iPersianDate - 1);
        return remainingDay;
    }

    public void persianSet(int field, int amount) {
        switch (field) {
            case YEAR:
                miladiDate.add(YEAR, amount - iPersianYear);
                calculateDate();
                break;
            case MONTH:
                miladiDate.add(MONTH, amount - iPersianMonth + 1);
                calculateDate();
                break;
            case DATE:
                miladiDate.add(DATE, amount - iPersianDate);
                calculateDate();
                break;
        }
    }

    private boolean isLeap(int year) {
        return ((Math.abs(year - 1395) % 4) == 0);
    }

    public int getiPersianYear() {
        return iPersianYear;
    }

    public int getiPersianMonth() {
        return iPersianMonth;
    }

    public int getiPersianDate() {
        return iPersianDate;
    }

    public int getiPersianWeekDay() {
        return iPersianWeekDay;
    }

    public int getiPersianFirstDayOfWeek() {
        return iPersianFirstDayOfWeek;
    }

    public void setiPersianFirstDayOfWeek(int iPersianFirstDayOfWeek) {
        this.iPersianFirstDayOfWeek = iPersianFirstDayOfWeek;
    }

    public String getPersianMonthName() {
        return persianMonthName;
    }

    public String getPersianDayName() {
        return persianDayName;
    }

    public String getsPersianFirstDayOfWeek() {
        return sPersianFirstDayOfWeek;
    }

    public String[] getPersianMonthNames() {
        return persianMonthNames;
    }

    public String[] getPersianWeekDayNames() {
        return persianWeekDayNames;
    }

    public Calendar getMiladiDate() {
        return miladiDate;
    }

    public void setMiladiDate(Calendar miladiDate) {
        this.miladiDate.setTime(miladiDate.getTime());
        calculateDate();
    }

    public String getPersianFullDate() {
        return (persianDayName + " " + iPersianDate + " " + persianMonthName + " " + iPersianYear);
    }

    public String getPersianDateIndex() {
        String date, month;
        date = iPersianDate < 10 ? "0" + String.valueOf(iPersianDate) : String.valueOf(iPersianDate);
        month = iPersianMonth < 10 ? "0" + String.valueOf(iPersianMonth) : String.valueOf(iPersianMonth);
        return month + "/" + date;
    }
}
