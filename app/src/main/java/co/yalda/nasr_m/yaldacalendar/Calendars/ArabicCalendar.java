package co.yalda.nasr_m.yaldacalendar.Calendars;

import java.util.Calendar;

import co.yalda.nasr_m.yaldacalendar.Converters.ArabicDateConverter;
import co.yalda.nasr_m.yaldacalendar.Converters.PersianUtil;

/**
 * Created by Nasr_M on 2/17/2015.
 */
public class ArabicCalendar{

    private Calendar baseMiladiCalendar = Calendar.getInstance();
    private int arabicYear, arabicMonth, arabicDate, arabicWeekDayNumber;
    private String arabicWeekDayName, arabicMonthName;
    private String[] weekDayNames = {"الاحد", "الاثنین", "الثلاثاء", "الاربعاء", "الخمیس",
            "الجمعه", "السبت"};
    private String[] monthNames = {"محرم", "صفر", "ربیع الاول",
            "ربیع الثانی", "جمادی الاول", "جمادی الثانی", "رجب",
            "شعبان", "رمضان", "شوال", "ذی القعده", "ذی الحجه"};

    public ArabicCalendar(Calendar baseMiladiCalendar) {
        this.baseMiladiCalendar = baseMiladiCalendar;
        calculate();
    }

    private void calculate(){
        Integer[] result = ArabicDateConverter.writeIslamicDate(baseMiladiCalendar, -1);
        arabicYear = result[0];
        arabicMonth = result[1];
        arabicDate = result[2];
        arabicMonthName = monthNames[arabicMonth];
        arabicWeekDayNumber = result[3];
        arabicWeekDayName = weekDayNames[arabicWeekDayNumber];
    }

    public void setBaseMiladiCalendar(Calendar baseMiladiCalendar) {
        this.baseMiladiCalendar.setTime(baseMiladiCalendar.getTime());
        calculate();
    }

    public Calendar getBaseMiladiCalendar() {
        return baseMiladiCalendar;
    }

    public int getArabicYear() {
        return arabicYear;
    }

    public int getArabicMonth() {
        return arabicMonth;
    }

    public int getArabicDate() {
        return arabicDate;
    }

    public String getArabicMonthName() {
        return arabicMonthName;
    }

    public int getArabicWeekDayNumber() {
        return arabicWeekDayNumber;
    }

    public String getArabicWeekDayName() {
        return arabicWeekDayName;
    }

    public String getArabicFullDate(){
        return (arabicWeekDayName + " " + PersianUtil.toArabic(arabicDate) + " " + arabicMonthName + " " + PersianUtil.toArabic(arabicYear));

    }
}
