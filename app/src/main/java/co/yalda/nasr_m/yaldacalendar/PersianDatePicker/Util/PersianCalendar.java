package co.yalda.nasr_m.yaldacalendar.PersianDatePicker.Util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * <strong> Persian(Shamsi) calendar </strong>
 * <p>
 * </p>
 * <p>
 * The calendar consists of 12 months, the first six of which are 31 days, the
 * next five 30 days, and the final month 29 days in a normal year and 30 days
 * in a leap year.
 * </p>
 * <p>
 * As one of the few calendars designed in the era of accurate positional
 * astronomy, the Persian calendar uses a very complex leap year structure which
 * makes it the most accurate solar calendar in use today. Years are grouped
 * into cycles which begin with four normal years after which every fourth
 * subsequent year in the cycle is a leap year. Cycles are grouped into grand
 * cycles of either 128 years (composed of cycles of 29, 33, 33, and 33 years)
 * or 132 years, containing cycles of of 29, 33, 33, and 37 years. A great grand
 * cycle is composed of 21 consecutive 128 year grand cycles and a final 132
 * grand cycle, for a total of 2820 years. The pattern of normal and leap years
 * which began in 1925 will not repeat until the year 4745!
 * </p>
 * </p> Each 2820 year great grand cycle contains 2137 normal years of 365 days
 * and 683 leap years of 366 days, with the average year length over the great
 * grand cycle of 365.24219852. So close is this to the actual solar tropical
 * year of 365.24219878 days that the Persian calendar accumulates an error of
 * one day only every 3.8 million years. As a purely solar calendar, months are
 * not synchronized with the phases of the Moon. </p>
 * <p>
 * </p>
 * <p/>
 * <p>
 * <strong>PersianCalendar</strong> by extending Default GregorianCalendar
 * provides capabilities such as:
 * </p>
 * <p>
 * </p>
 * <p/>
 * <li>you can set the date in Persian by setPersianDate(persianYear,
 * persianMonth, persianDay) and get the Gregorian date or vice versa</li>
 * <p>
 * </p>
 * <li>determine is the current date is Leap year in persian calendar or not by
 * IsPersianLeapYear()</li>
 * <p>
 * </p>
 * <li>getPersian short and long Date String getPersianShortDate() and
 * getPersianLongDate you also can set delimiter to assign delimiter of returned
 * dateString</li>
 * <p>
 * </p>
 * <li>Parse string based on assigned delimiter</li>
 * <p>
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 * <p>
 * <strong> Example </strong>
 * </p>
 * <p>
 * </p>
 * <p/>
 * <pre>
 * {@code
 *       PersianCalendar persianCal = new PersianCalendar();
 *       System.out.println(persianCal.getPersianShortDate());
 *
 *       persianCal.set(1982, Calendar.MAY, 22);
 *       System.out.println(persianCal.getPersianShortDate());
 *
 *       persianCal.setDelimiter(" , ");
 *       persianCal.parse("1361 , 03 , 01");
 *       System.out.println(persianCal.getPersianShortDate());
 *
 *       persianCal.setPersianDate(1361, 3, 1);
 *       System.out.println(persianCal.getPersianLongDate());
 *       System.out.println(persianCal.getTime());
 *
 *       persianCal.addPersianDate(Calendar.MONTH, 33);
 *       persianCal.addPersianDate(Calendar.YEAR, 5);
 *       persianCal.addPersianDate(Calendar.DATE, 50);
 *
 * }
 *
 * <pre>
 *
 * @author Morteza  contact: <a href="mailto:Mortezaadi@gmail.com">Mortezaadi@gmail.com</a>
 * @version 1.1
 */
public class PersianCalendar extends GregorianCalendar {

    private static final long serialVersionUID = 5541422440580682494L;

    private int persianYear;
    private int persianMonth;
    private int persianDay;
    private Calendar julianCalendar = getInstance();
    // use to seperate PersianDate's field and also Parse the DateString based
    // on this delimiter
    private String delimiter = "/";
    private int[] monthDays = new int[]{31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};


    /**
     * default constructor
     * <p/>
     * most of the time we don't care about TimeZone when we persisting Date or
     * doing some calculation on date. <strong> Default TimeZone was set to
     * "GMT" </strong> in order to make developer to work more convenient with
     * the library; however you can change the TimeZone as you do in
     * GregorianCalendar by calling setTimeZone()
     */
    public PersianCalendar(long millis) {
        julianCalendar.setTimeInMillis(millis);
        julianCalendar.setFirstDayOfWeek(SATURDAY);
    }

    /**
     * default constructor
     * <p/>
     * most of the time we don't care about TimeZone when we persisting Date or
     * doing some calculation on date. <strong> Default TimeZone was set to
     * "GMT" </strong> in order to make developer to work more convenient with
     * the library; however you can change the TimeZone as you do in
     * GregorianCalendar by calling setTimeZone()
     */
    public PersianCalendar() {
        julianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        julianCalendar.setFirstDayOfWeek(SATURDAY);
    }

    private long convertToMilis(long julianDate) {
        return PersianCalendarConstants.MILLIS_JULIAN_EPOCH + julianDate * PersianCalendarConstants.MILLIS_OF_A_DAY
                + PersianCalendarUtils.ceil(getTimeInMillis() - PersianCalendarConstants.MILLIS_JULIAN_EPOCH, PersianCalendarConstants.MILLIS_OF_A_DAY);
    }

    /**
     * Calculate persian date from current Date and populates the corresponding
     * fields(persianYear, persianMonth, persianDay)
     */
    protected void calculatePersianDate() {
        long julianDate = ((long) Math.floor((getTimeInMillis() - PersianCalendarConstants.MILLIS_JULIAN_EPOCH)) / PersianCalendarConstants.MILLIS_OF_A_DAY);
        long PersianRowDate = PersianCalendarUtils.julianToPersian(julianDate);
        long year = PersianRowDate >> 16;
        int month = (int) (PersianRowDate & 0xff00) >> 8;
        int day = (int) (PersianRowDate & 0xff);
        this.persianYear = (int) (year > 0 ? year : year - 1);
        this.persianMonth = month;
        this.persianDay = day;
    }

    /**
     * Determines if the given year is a leap year in persian calendar. Returns
     * true if the given year is a leap year.
     *
     * @return boolean
     */
    public boolean isPersianLeapYear() {
        //calculatePersianDate();
        return PersianCalendarUtils.isPersianLeapYear(this.persianYear);
    }

    /**
     * set the persian date it converts PersianDate to the Julian and assigned
     * equivalent milliseconds to the instance
     *
     * @param persianYear
     * @param persianMonth
     * @param persianDay
     */
    public void setPersianDate(int persianYear, int persianMonth, int persianDay) {
        this.persianYear = persianYear;
        this.persianMonth = persianMonth;
        this.persianDay = persianDay;
//        calculatePersianDate();
    }

    public int getPersianYear() {
        // calculatePersianDate();
        return this.persianYear;
    }

    /**
     * @return int persian month number
     */
    public int getPersianMonth() {
        // calculatePersianDate();
        return this.persianMonth + 1;
    }

    /**
     * @return String persian month name
     */
    public String getPersianMonthName() {
        // calculatePersianDate();
        return PersianCalendarConstants.persianMonthNames[this.persianMonth];
    }

    /**
     * @return int Persian day in month
     */
    public int getPersianDay() {
        // calculatePersianDate();
        return this.persianDay;
    }

    /**
     * @return String Name of the day in week
     */
    public String getPersianWeekDayName() {
        switch (get(DAY_OF_WEEK)) {
            case SATURDAY:
                return PersianCalendarConstants.persianWeekDays[0];
            case SUNDAY:
                return PersianCalendarConstants.persianWeekDays[1];
            case MONDAY:
                return PersianCalendarConstants.persianWeekDays[2];
            case TUESDAY:
                return PersianCalendarConstants.persianWeekDays[3];
            case WEDNESDAY:
                return PersianCalendarConstants.persianWeekDays[4];
            case THURSDAY:
                return PersianCalendarConstants.persianWeekDays[5];
            default:
                return PersianCalendarConstants.persianWeekDays[6];
        }

    }

    /**
     * @return String of Persian Date ex: شنبه 01 خرداد 1361
     */
    public String getPersianLongDate() {
        return getPersianWeekDayName() + "  " + this.persianDay + "  " + getPersianMonthName() + "  " + this.persianYear;
    }

    public String getPersianLongDateAndTime() {
        return getPersianLongDate() + " ساعت " + get(HOUR_OF_DAY) + ":" + get(MINUTE) + ":" + get(SECOND);
    }

    /**
     * @return String of persian date formatted by
     * 'YYYY[delimiter]mm[delimiter]dd' default delimiter is '/'
     */
    public String getPersianShortDate() {
        // calculatePersianDate();
        return "" + formatToMilitary(this.persianYear) + delimiter + formatToMilitary(getPersianMonth()) + delimiter + formatToMilitary(this.persianDay);
    }

    public String getPersianShortDateTime() {
        return "" + formatToMilitary(this.persianYear) + delimiter + formatToMilitary(getPersianMonth()) + delimiter + formatToMilitary(this.persianDay) + " " + formatToMilitary(this.get(HOUR_OF_DAY)) + ":" + formatToMilitary(get(MINUTE))
                + ":" + formatToMilitary(get(SECOND));
    }

    private String formatToMilitary(int i) {
        return (i < 9) ? "0" + i : String.valueOf(i);
    }

    /**
     * add specific amout of fields to the current date for now doesnt handle
     * before 1 farvardin hejri (before epoch)
     *
     * @param field
     * @param amount <pre>
     *                              Usage:
     *                              {@code
     *                              addPersianDate(Calendar.YEAR, 2);
     *                              addPersianDate(Calendar.MONTH, 3);
     *                              }
     *                             </pre>
     *
     *               u can also use Calendar.HOUR_OF_DAY,Calendar.MINUTE,
     *               Calendar.SECOND, Calendar.MILLISECOND etc
     */
    //
    public void addPersianDate(int field, int amount) {
        if (amount == 0) {
            return; // Do nothing!
        }

//        if (field < 0 || field >= ZONE_OFFSET) {
//            throw new IllegalArgumentException();
//        }

        if (field == YEAR) {
            setPersianDate(this.persianYear + amount, this.persianMonth, this.persianDay);
            return;
        } else if (field == MONTH) {
            setPersianDate(this.persianYear + ((amount < 0 ? (this.persianMonth + amount)+12 : (this.persianMonth + amount)) / 12), (amount < 0 ? (this.persianMonth + amount)+12 : (this.persianMonth + amount)) % 12, this.persianDay);
            return;
        } else if (field == DATE) {
            Calendar cal = getInstance();
            cal.setTimeInMillis(this.getTimeInMillis());
            cal.add(DATE, amount);
            this.setTimeInMillis(cal.getTimeInMillis());
            return;
        }
        add(field, amount);
        calculatePersianDate();
    }

    /**
     * <pre>
     *    use <code>{@link PersianDateParser}</code> to parse string
     *    and get the Persian Date.
     * </pre>
     *
     * @param dateString
     * @see PersianDateParser
     */
    public void parse(String dateString) {
        PersianCalendar p = new PersianDateParser(dateString, delimiter).getPersianDate();
        setPersianDate(p.getPersianYear(), p.getPersianMonth(), p.getPersianDay());
    }

    public String getDelimiter() {
        return delimiter;
    }

    /**
     * assign delimiter to use as a separator of date fields.
     *
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String toString() {
        String str = super.toString();
        return str.substring(0, str.length() - 1) + ",PersianDate=" + getPersianShortDate() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void set(int field, int value) {
        super.set(field, value);
        calculatePersianDate();
    }

    @Override
    public void setTimeInMillis(long millis) {
        super.setTimeInMillis(millis);
        calculatePersianDate();
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        super.setTimeZone(zone);
        calculatePersianDate();
    }

    public int getFirstWeekNumberOfMonth() {
        setPersianDate(persianYear, persianMonth, 1);
//        calculatePersianDate();
        int weekNum = getPersianWeekNumber();
        addPersianDate(DATE, persianDay - 1);
//        calculatePersianDate();
        return weekNum;
    }

    public int persianPreMonthRemainingDay() {
        addPersianDate(DATE, -(persianDay - 1));

        int firsDayOfMonth = get(DAY_OF_WEEK);
        int remainingDay = firsDayOfMonth% 7;

        addPersianDate(DATE, persianDay - 1);
        return remainingDay;
    }

    public int getPersianWeekNumber() {
        int firstWeekDays, daysCount = 0, weekNum;

//        if(iPersianMonth == 0)
//            daysCount = iPersianDate;
        if (persianMonth <= 6)
            daysCount += (persianMonth - 1) * 31 + persianDay - 1;
        else
            daysCount += 6 * 31 + (persianMonth - 7) * 30 + persianDay - 1;

        addPersianDate(DATE, -(daysCount));
        firstWeekDays = get(DAY_OF_WEEK)%7;
        daysCount = daysCount + firstWeekDays;
        weekNum = ((int) Math.floor(daysCount / 7));

        if (weekNum == 0 && daysCount >= 3)
            weekNum = 53;
        else if (weekNum == 0 && daysCount < 3)
            weekNum = 1;

        addPersianDate(DATE, daysCount);
        return weekNum;
    }

    public int getMaxDayOfMonth() {
        if (persianMonth == 11 && isPersianLeapYear())
            return 30;
        else
            return monthDays[persianMonth];
    }

    public void setPersian(int field, int amount){
        if (field < 0 || field >= ZONE_OFFSET) {
            throw new IllegalArgumentException();
        }
        switch (field){
            case DATE:
                persianDay = amount;
                break;
            case MONTH:
                persianMonth = amount;
                break;
            case YEAR:
                persianYear = amount;
                break;
        }
//        calculatePersianDate();
    }

    public String getPersianDateIndex() {
        String date, month;
        date = persianDay < 10 ? "0" + String.valueOf(persianDay) : String.valueOf(persianDay);
        month = (persianMonth+1) < 10 ? "0" + String.valueOf((persianMonth+1)) : String.valueOf((persianMonth+1));
        return month + "/" + date;
    }
}