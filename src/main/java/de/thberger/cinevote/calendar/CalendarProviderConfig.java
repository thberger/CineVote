package de.thberger.cinevote.calendar;

import de.thberger.cinevote.AppConfig;
import lombok.AllArgsConstructor;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author thb
 */
@Configuration
@AllArgsConstructor
public class CalendarProviderConfig {

    private AppConfig appConfig;

    @Bean
    WebCalendarEventProvider webCalendarEventProvider() {
        WebCalendarEventProvider eventProvider = new WebCalendarEventProvider();
        for (AppConfig.WebCalendarConfig w : appConfig.getWebCalendars()) {
            WebCalendar webCalendar = readCalendar(w.getUrl());
            eventProvider.addWebCalendar(webCalendar, w.getStyle());
        }
        return eventProvider;
    }

    private WebCalendar readCalendar(String webcalUrl) {
        try {
            return new WebCalendar(webcalUrl);
        } catch (IOException | ParserException e) {
            e.printStackTrace();
            return null;
        }
    }

}
