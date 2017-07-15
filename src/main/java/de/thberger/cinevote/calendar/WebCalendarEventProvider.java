package de.thberger.cinevote.calendar;

import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import de.thberger.cinevote.AppConfig;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class WebCalendarEventProvider extends BasicEventProvider {

    private final List<WebCalendar> webCalendars = new LinkedList<>();
    private AppConfig appConfig;

    @Autowired
    public WebCalendarEventProvider(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @PostConstruct
    public void updateAllCalendars() {
        for (AppConfig.WebCalendarConfig w : appConfig.getWebCalendars()) {
            importCalendar(w);
        }
    }

    private void importCalendar(AppConfig.WebCalendarConfig w) {
        try {
            WebCalendar webCalendar = readCalendar(w);
            webCalendars.add(webCalendar);
            webCalendar.getEvents().forEach(e -> addEvent(e, w.getStyle()));
        } catch (CalenderImportException e) {
            log.error(e.getMessage());
        }
    }

    private WebCalendar readCalendar(AppConfig.WebCalendarConfig webCalendarConfig) {
        try {
            return new WebCalendar(webCalendarConfig);
        } catch (IOException | ParserException e) {
            throw new CalenderImportException("Error importing calendar from URL " + webCalendarConfig, e);
        }
    }

    private void addEvent(CinemaEvent event, String styleName) {
        event.setStyleName(styleName);
        super.addEvent(event);
    }

    public void clearCalendars() {
        webCalendars.clear();
    }
}