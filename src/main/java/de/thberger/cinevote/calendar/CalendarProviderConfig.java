package de.thberger.cinevote.calendar;

import de.thberger.cinevote.AppConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author thb
 */
@Configuration
@AllArgsConstructor
@Slf4j
public class CalendarProviderConfig {

    private AppConfig appConfig;

    @Bean
    WebCalendarEventProvider webCalendarEventProvider() {
        WebCalendarEventProvider eventProvider = new WebCalendarEventProvider();
        for (AppConfig.WebCalendarConfig w : appConfig.getWebCalendars()) {
            WebCalendar webCalendar = readCalendar(w);
            eventProvider.addWebCalendar(webCalendar, w.getStyle());
        }
        return eventProvider;
    }

    private WebCalendar readCalendar(AppConfig.WebCalendarConfig webCalendarConfig) {
        try {
            return new WebCalendar(webCalendarConfig);
        } catch (IOException | ParserException e) {
            log.error("Error importing calendar from URL " + webCalendarConfig, e);
            return null;
        }
    }

}
