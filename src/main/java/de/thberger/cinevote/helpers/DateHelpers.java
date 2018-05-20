package de.thberger.cinevote.helpers;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;

/**
 * @author thb
 */
public class DateHelpers {

    public static Date getDayInFourWeeks() {
        Calendar cal = now();
        cal.add(DAY_OF_WEEK, 27);
        return cal.getTime();
    }

    public static ZonedDateTime getDayInWeeks(int numberOfWeeks) {
        ZonedDateTime dateTime = ZonedDateTime.now();
        dateTime.plusWeeks(numberOfWeeks);
        return dateTime;
    }

    public static Date getDayInOneMonth() {
        Calendar cal = now();
        cal.add(MONTH, 1);
        return cal.getTime();
    }

    public static ZonedDateTime getLastDayOfWeek() {
        ZonedDateTime now = ZonedDateTime.now();
        TemporalField fieldISO = WeekFields.of(Locale.GERMANY).dayOfWeek();
        return now.with(fieldISO, 6);
    }

    public static ZonedDateTime getFirstDayOfWeek() {
        ZonedDateTime now = ZonedDateTime.now();
        TemporalField fieldISO = WeekFields.of(Locale.GERMANY).dayOfWeek();
        return now.with(fieldISO, 1);
    }

    public static Date getNowPlusDays(int amount) {
        Calendar cal = now();
        cal.add(DAY_OF_WEEK, amount);
        return cal.getTime();
    }

    private static Calendar now() {
        return Calendar.getInstance();
    }

}
