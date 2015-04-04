package co.yalda.nasr_m.yaldacalendar.Calendars;

import java.util.Calendar;
import java.util.Date;

import co.yalda.nasr_m.yaldacalendar.Converters.JalaliCalendar;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;
import co.yalda.nasr_m.yaldacalendar.MainActivity;

import static co.yalda.nasr_m.yaldacalendar.Converters.JalaliCalendar.YearMonthDate;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class PersianCalendar extends Calendar{

    private Calendar miladiDate = getInstance();
    private JalaliCalendar converter = new JalaliCalendar();
    private int iPersianYear, iPersianMonth, iPersianDate, iPersianWeekDay, iPersianFirstDayOfWeek;
    private String persianYear, persianMonth, persianDay, persianFullDate;
    private String persianMonthName, persianDayName, sPersianFirstDayOfWeek;
    public static String[] persianMonthNames = new String[]{"فروردین", "اردیبهشت", "خرداد"
            , "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
    private String[] persianWeekDayNames = new String[]{"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "جمعه"};
    String[] miladiMonthName = {"January", "February", "March", "April", "May", "June", "July",                   // Miladi Month Names
                "August", "September", "October", "November", "December"};
    private int[] monthDays = new int[]{31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    private enum CalendarChanged{
        Jalali,
        Gregorian
    }

    public PersianCalendar(Calendar cal) {
        this.miladiDate.setTime(cal.getTime());
        calculateDate();
        iPersianFirstDayOfWeek = 7;
    }

    public PersianCalendar(Date date) {
        this.miladiDate.set(date.getYear(), date.getMonth(), date.getDate());
        calculateDate();
        iPersianFirstDayOfWeek = 7;
    }


    private void calculateDate() {
//        if (change == CalendarChanged.Jalali) {
//            miladiDate.setTime(converter.getGregorianDate(persianFullDate));
//        }else {
//        }

        persianFullDate = converter.getJalaliDate(miladiDate.getTime());
        persianFullDate = persianYear + "/" + persianMonth + "/" + persianDay;
        String[] convertedDate = persianFullDate.split("/");
        iPersianYear = Integer.valueOf(convertedDate[0]);
        iPersianMonth = Integer.valueOf(convertedDate[1]);      // 1-12
        iPersianDate = Integer.valueOf(convertedDate[2]);
        persianMonthName = persianMonthNames[iPersianMonth-1];
        iPersianWeekDay = miladiDate.get(DAY_OF_WEEK);
        persianDayName = persianWeekDayNames[iPersianWeekDay%7];
    }

    public int getMaxDayOfMonth() {
        if (iPersianMonth == 12 && isLeap(iPersianYear))
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

    public void setPersian(int field, int amount) {
        switch (field) {
            case YEAR:
                miladiDate.setTime(converter.getGregorianDate(amount + "/" + persianMonth + "/" + persianDay));
//                miladiDate.add(YEAR, amount - iPersianYear);
                calculateDate();
                break;
            case MONTH:
                miladiDate.setTime(converter.getGregorianDate(persianYear + "/" + amount + "/" + persianDay));
//                miladiDate.add(MONTH, amount - iPersianMonth + 1);
                calculateDate();
                break;
            case DATE:
                miladiDate.setTime(converter.getGregorianDate(persianYear + "/" + persianMonth + "/" + amount));
//                miladiDate.add(DATE, amount - iPersianDate);
                calculateDate();
                break;
        }
    }

    public static boolean isLeap(int year) {
        return ((Math.abs(year - 1395) % 4) == 0);
    }

    public int getiPersianYear() {
        return iPersianYear;
    }

    public int getiPersianMonth() {
        return iPersianMonth;
    }

    public int getiPersianDay() {
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
        return (persianDayName + " " + PersianUtil.toPersian(iPersianDate) + " " + persianMonthName + " " + PersianUtil.toPersian(iPersianYear));
    }

    public String getPersianDateIndex() {
        String date, month;
        date = iPersianDate < 10 ? "0" + String.valueOf(iPersianDate) : String.valueOf(iPersianDate);
        month = iPersianMonth < 10 ? "0" + String.valueOf(iPersianMonth) : String.valueOf(iPersianMonth);
        return month + "/" + date;
    }

    public void setPersian(int year, int month, int day) {
        miladiDate.setTime(converter.getGregorianDate(year + "/" + month + "/" + day));
        calculateDate();
    }

    public void addPersian(int field, int amount){
        if (amount == 0)
            return;
        switch (field){
            case YEAR:
                miladiDate.add(YEAR, amount);
                calculateDate();
                break;
            case MONTH:
                int dayCount = 0, currentYear = iPersianYear;
                if (amount > 0) {
                    YearMonthDate yearMonthDate = new YearMonthDate(iPersianYear, iPersianMonth-1, iPersianDate);
                    YearMonthDate converted = JalaliCalendar.jalaliToGregorian(yearMonthDate);

                    miladiDate.set(converted.getYear(), converted.getMonth(), converted.getDate());
                    calculateDate();
                }else{

                    YearMonthDate yearMonthDate = new YearMonthDate(iPersianYear, (iPersianMonth-1)%12, iPersianDate);
                    YearMonthDate converted = JalaliCalendar.jalaliToGregorian(yearMonthDate);
                    miladiDate.set(converted.getYear(), converted.getMonth(), converted.getDate());
                    calculateDate();
                }
                calculateDate();
                break;
            case DATE:
                miladiDate.add(DATE, amount);
                calculateDate();
                break;
        }
    }

    public int numberOfWeeksInMonth(){
        int dayCount = persianPreMonthRemainingDay() + getMaxDayOfMonth();
        return (int) Math.ceil(dayCount / 7.);
    }

    public int persianCompare(PersianCalendar pCal){
        if (iPersianYear > pCal.iPersianYear){
            return 1;
        }else if (iPersianYear < pCal.iPersianYear){
            return -1;
        }else if (iPersianMonth > pCal.iPersianMonth){
            return 1;
        }else if (iPersianMonth < pCal.iPersianMonth){
            return -1;
        }else if (iPersianDate > pCal.iPersianDate){
            return 1;
        }else if (iPersianDate < pCal.iPersianDate){
            return -1;
        }else {
            return 0;
        }
    }

    public String getDateString(MainActivity.calendarType calType, boolean full){
        switch (calType){
            case Solar:
                if (full){
                    return getPersianFullDate();
                }else {
                    return PersianUtil.toPersian(iPersianDate) + " " + persianMonthName + " " + PersianUtil.toPersian(iPersianYear);
                }
            case Gregorian:
                if (full){
                    return miladiDate.get(DAY_OF_WEEK) + " " + miladiDate.get(DATE) + " " +
                    miladiMonthName[miladiDate.get(MONTH)] + " " + miladiDate.get(YEAR);
                }else {
                    return miladiDate.get(DATE) + " " + miladiMonthName[miladiDate.get(MONTH)] + " " + miladiDate.get(YEAR);
                }
            case Hejri:
                ArabicCalendar arabCal = new ArabicCalendar(miladiDate);
                if (full){
                    return arabCal.getArabicFullDate();
                }else {
                    return PersianUtil.toArabic(arabCal.getArabicDate()) + " " + arabCal.getArabicMonthName()
                            + " " + PersianUtil.toArabic(arabCal.getArabicYear());
                }
        }
        return null;
    }


    @Override
    public void add(int field, int value) {
        miladiDate.add(field, value);
    }

    @Override
    public long getTimeInMillis() {
        return miladiDate.getTimeInMillis();
    }

    @Override
    protected void computeFields() {
    }

    @Override
    protected void computeTime() {

    }

    @Override
    public int getGreatestMinimum(int field) {
        return 0;
    }

    @Override
    public int getLeastMaximum(int field) {
        return 0;
    }

    @Override
    public int getMaximum(int field) {
        return 0;
    }

    @Override
    public int getMinimum(int field) {
        return 0;
    }

    @Override
    public void roll(int field, boolean increment) {

    }
}
