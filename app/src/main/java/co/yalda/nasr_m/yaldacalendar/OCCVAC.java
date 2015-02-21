package co.yalda.nasr_m.yaldacalendar;

/**
 * Created by Nasr_M on 2/18/2015.
 */
public class OCCVAC {

    private String date;
    private boolean isHoliday;
    private String subject;
    private String description;

    public OCCVAC(String date, boolean isHoliday, String subject, String description) {
        this.date = date;
        this.isHoliday = isHoliday;
        this.subject = subject;
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public boolean isHoliday() {
        return isHoliday;
    }
}
