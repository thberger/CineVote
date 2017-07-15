package de.thberger.cinevote.helpers;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * @author thb
 */
public class DateHelpers {

    public static Date getDayInFourWeeks() {
        Calendar cal = Calendar.getInstance();
        cal.add(DAY_OF_WEEK, 27);
        return cal.getTime();
    }

    public static Date getDayInOneMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(MONTH, 1);
        return cal.getTime();
    }

    static Date getLastDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(DAY_OF_YEAR, 6);
        return cal.getTime();
    }

    public static Date getFirstDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return cal.getTime();
    }

    public static Date getNowPlusDays(int amount) {
        Calendar cal = Calendar.getInstance();
        cal.add(DAY_OF_WEEK, amount);
        return cal.getTime();
    }

}
