package co.yalda.nasr_m.yaldacalendar.Converters;

/**
 * Created by Nasr_M on 2/24/2015.
 */
public class PersianUtil {

    private static final char[] FARSI = {'\u06f0', '\u06f1', '\u06f2', '\u06f3', '\u06f4', '\u06f5', '\u06f6', '\u06f7', '\u06f8', '\u06f9'};
    private static final char[] ARABIC = {'\u0660', '\u0661', '\u0662', '\u0663', '\u0664', '\u0665', '\u0666', '\u0667', '\u0668', '\u0669'};

    public static boolean isArabic(String lang) {
        return "ar".equals(lang) || "fa".equals(lang) || "ur".equals(lang);
    }

    /**
     * Convert digits in the specified string to arabic digits.
     */
    public static String convertDigits(String str) {
        if (str == null || str.length() == 0) return str;

        char[] s = new char[str.length()];
        for (int i = 0; i < s.length; i++)
            s[i] = toDigit(str.charAt(i));

        return new String(s);
    }

    public static String convertDigitstoArabic(String str) {
        if (str == null || str.length() == 0) return str;

        char[] s = new char[str.length()];
        for (int i = 0; i < s.length; i++)
            s[i] = toArabicDigit(str.charAt(i));

        return new String(s);
    }

    /**
     * Convert single digit in the specified string to arabic digit.
     */
    public static char toDigit(char ch) {
        int n = Character.getNumericValue((int) ch);
        return n >= 0 && n < 10 ? FARSI[n] : ch;
    }


    public static char toArabicDigit(char ch) {
        int n = Character.getNumericValue((int) ch);
        return n >= 0 && n < 10 ? ARABIC[n] : ch;
    }

    /**
     * Convert an int into arabic string.
     */
    public static String toString(int num) {
        return convertDigits(Integer.toString(num));
    }
}
