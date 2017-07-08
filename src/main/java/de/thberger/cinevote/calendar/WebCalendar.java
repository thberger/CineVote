package de.thberger.cinevote.calendar;

import de.thberger.cinevote.AppConfig;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.apache.commons.codec.binary.Base64;

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
 * Creates a ical4j {@link Calendar} instance from a webcal resource by
 * mapping all fields. The events can be fetched as stream of {@link CinemaEvent}s
 * which are compatible to Vaadin calendar.
 */
@Slf4j
class WebCalendar {

    private static final String DEFAULT_DATA_PATTERN = "yyyyMMdd'T'HHmmss";
    private final Calendar calendar;
    private final SimpleDateFormat dateFormat;

    WebCalendar(AppConfig.WebCalendarConfig config) throws IOException, ParserException {
        calendar = readRemoteCalendar(config);
        dateFormat = new SimpleDateFormat(DEFAULT_DATA_PATTERN);
    }

    private Calendar readRemoteCalendar(AppConfig.WebCalendarConfig config) throws IOException, ParserException {
        InputStreamReader in = getCalendarAsStream(config);
        BufferedReader reader = new BufferedReader(in);
        CalendarBuilder builder = new CalendarBuilder();
        return builder.build(reader);
    }

    private InputStreamReader getCalendarAsStream(AppConfig.WebCalendarConfig config) {
        try {
            URLConnection connection = getUrlConnection(config);
            return new InputStreamReader(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read remote calender with URL " + config.getUrl(), e);
        }
    }

    private URLConnection getUrlConnection(AppConfig.WebCalendarConfig config) throws IOException {
        URL url = new URL(config.getUrl().replace("webcal", "http"));
        URLConnection connection = url.openConnection();
        if (config.isAuthenticated()) {
            String userpass = config.getUsername() + ":" + config.getPassword();
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);
        }
        return connection;
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
        Date date = null;
        try {
            if (source != null) {
                date = dateFormat.parse(source);
            } else {
                log.warn("No date provided!");
            }
        } catch (ParseException e) {
            log.warn("Failed to parse date: " + source);
        }
        return date;
    }

    private String getValue(CalendarComponent comp, String name) {
        Property prop = comp.getProperty(name);
        return (prop != null) ? prop.getValue() : null;
    }

}
