package de.thberger.cinevote.calendar;

import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author thb
 */
@Slf4j
class WebCalendar {

    private final Calendar calendar;
    private final SimpleDateFormat dateFormat;

    WebCalendar(String webcalUrl) throws IOException, ParserException {
        calendar = readRemoteCalendar(webcalUrl);
        dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    }

    private Calendar readRemoteCalendar(String webcalUrl) throws IOException, ParserException {
        InputStreamReader in = getCalendarAsStream(webcalUrl);
        BufferedReader reader = new BufferedReader(in);
        CalendarBuilder builder = new CalendarBuilder();
        return builder.build(reader);
    }

    private InputStreamReader getCalendarAsStream(String webcalUrl) {
        try {
            URL calendarURL = new URL(webcalUrl.replace("webcal", "http"));
            URLConnection connection = calendarURL.openConnection();
            return new InputStreamReader(connection.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid calendar URL", e);
        }
    }

    List<CinemaEvent> getEvents() {
        return calendar.getComponents().stream()
                .map(this::createEventFromCalendarComponent)
                .collect(Collectors.toList());
    }

    private CinemaEvent createEventFromCalendarComponent(CalendarComponent comp) {
        CinemaEvent calendarEvent = new CinemaEvent();
        calendarEvent.setCaption(getValue(comp, "SUMMARY"));
        calendarEvent.setLocation(getValue(comp, "LOCATION"));
        calendarEvent.setUrl(getValue(comp, "URL"));
        calendarEvent.setDescription(getValue(comp, "DESCRIPTION"));
        String dtstart = getValue(comp, "DTSTART");
        calendarEvent.setStart(parseDate(dtstart));
        String dtend = getValue(comp, "DTEND");
        calendarEvent.setEnd(parseDate(dtend));
        return calendarEvent;
    }

    private Date parseDate(String source) {
        try {
            if (source != null) {
                return dateFormat.parse(source);
            } else {
                log.warn("No date provided!");
                return null;
            }
        } catch (ParseException e) {
            log.warn("Failed to parse date: " + source);
            return null;
        }
    }

    private String getValue(CalendarComponent comp, String name) {
        Property prop = comp.getProperty(name);
        if (prop != null) {
            return prop.getValue();
        } else {
            return null;
        }
    }


}
