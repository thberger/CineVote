package de.thberger.cinevote.calendar;

import de.thberger.cinevote.AppConfig;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Content;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.property.DateProperty;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Creates a ical4j {@link Calendar} instance from a webcal resource by
 * mapping all fields. The events can be fetched as stream of {@link CinemaEvent}s
 * which are compatible to Vaadin calendar.
 */
@Slf4j
public class WebCalendar {

    private final Calendar iCalCalendar;

    private final AppConfig.WebCalendarConfig config;

    WebCalendar(AppConfig.WebCalendarConfig config) throws IOException, ParserException {
        this.config = config;
        iCalCalendar = readRemoteCalendar(config);
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
            throw new CalenderImportException("Cannot read remote calender with URL " + config.getUrl(), e);
        }
    }

    private URLConnection getUrlConnection(AppConfig.WebCalendarConfig config) throws IOException {
        URL url = new URL(config.getUrl().replace("webcal", "http"));
        URLConnection connection = url.openConnection();
        if (config.isAuthenticated()) {
            setBasicAuth(config, connection);
        }
        return connection;
    }

    private void setBasicAuth(AppConfig.WebCalendarConfig config, URLConnection connection) {
        String userPass = config.getUsername() + ":" + config.getPassword();
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
        connection.setRequestProperty("Authorization", basicAuth);
    }

    List<CinemaEvent> createEvents() {
        return iCalCalendar.getComponents().stream()
                .filter(c -> c.getName().equals("VEVENT"))
                .map(this::createEventFromCalendarComponent)
                .collect(Collectors.toList());
    }

    private CinemaEvent createEventFromCalendarComponent(CalendarComponent comp) {
        CinemaEvent calendarEvent = new CinemaEvent();
        try {
            calendarEvent.setCaption(getValue(comp, "SUMMARY"));
            calendarEvent.setLocation(getValue(comp, "LOCATION"));
            calendarEvent.setUrl(getValue(comp, "URL"));
            calendarEvent.setDescription(getValue(comp, "DESCRIPTION"));

            DateProperty eventStart = getDateProperty(comp, "DTSTART");
            ZonedDateTime start = ZonedDateTime.ofInstant(eventStart.getDate().toInstant(),
                    ZoneId.systemDefault());
            calendarEvent.setStart(start);
            calendarEvent.setTimeZone(getTimeZone(eventStart));

            DateProperty eventEnd = getDateProperty(comp, "DTEND");
            ZonedDateTime end = ZonedDateTime.ofInstant(eventEnd.getDate().toInstant(),
                    ZoneId.systemDefault());
            calendarEvent.setEnd(end);
            calendarEvent.setParent(this);
            calendarEvent.setVisible(getConfig().isVisible());

        } catch (Exception e) {
            log.error("Failed to create cinema event from event component", e);
        }
        return calendarEvent;
    }

    private ZoneId getTimeZone(DateProperty dateProperty) {
        Optional<TimeZone> eventZoneId = Optional.ofNullable(dateProperty.getTimeZone());
        return eventZoneId.map(t -> ZoneId.of(t.getID())).orElse(ZoneId.systemDefault());
    }

    private DateProperty getDateProperty(CalendarComponent comp, String name) {
        return (DateProperty) comp.getProperty(name);
    }

    private String getValue(CalendarComponent comp, String name) {
        Optional<Property> prop = Optional.ofNullable(comp.getProperty(name));
        return prop.map(Content::getValue).orElse(null);
    }

    public AppConfig.WebCalendarConfig getConfig() {
        return config;
    }

    public String getId() {
        return config.getShortName();
    }
}
