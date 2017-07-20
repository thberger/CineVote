package de.thberger.cinevote.calendar;

import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import de.thberger.cinevote.AppConfig;
import de.thberger.cinevote.ui.UserSession;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

@Service
@Slf4j
public class WebCalendarEventProvider extends BasicEventProvider {

    private final Map<String, WebCalendar> webCalendars = new HashMap<>();
    private AppConfig appConfig;

    @Autowired
    public WebCalendarEventProvider(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @PostConstruct
    public void importAllCalendars() {
        synchronized (webCalendars) {
            clearCalendars();
            for (AppConfig.WebCalendarConfig w : appConfig.getWebCalendars()) {
                importCalendar(w);
            }
        }
    }

    private void importCalendar(AppConfig.WebCalendarConfig w) {
        try {
            WebCalendar webCalendar = readCalendar(w);
            webCalendars.put(webCalendar.getConfig().getShortName(), webCalendar);
            webCalendar.createEvents().forEach(e -> addEvent(e, w.getStyle()));
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

    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        ArrayList<CalendarEvent> activeEvents = new ArrayList<>();

        for (CalendarEvent ev : eventList) {
            CinemaEvent event = (CinemaEvent) ev;
            if (event.isVisible()) {
                if (isInRange(ev, startDate, endDate)) {
                    activeEvents.add(ev);
                    checkForSelected(ev);
                }
            }
        }

        return activeEvents;
    }

    private void checkForSelected(CalendarEvent ev) {
        CinemaEvent ev1 = (CinemaEvent) ev;
        String style = (ev1.equals(UserSession.getSelectedEvent())) ? "selected" : "";
        String calStyle = ev1.getParent().getConfig().getStyle();
        ev1.setStyleName(calStyle + " " + style);
    }

    private boolean isInRange(CalendarEvent ev, Date startDate, Date endDate) {
        long from = startDate.getTime();
        long to = endDate.getTime();
        if (ev.getStart() != null && ev.getEnd() != null) {
            long f = ev.getStart().getTime();
            long t = ev.getEnd().getTime();
            return (f <= to && f >= from) || (t >= from && t <= to)
                    || (f <= from && t >= to);
        }
        return false;
    }

    public void clearCalendars() {
        webCalendars.clear();
    }

    public Collection<WebCalendar> getCalendars() {
        return webCalendars.values();
    }

    public void setVisibility(WebCalendar calendar, boolean visible) {
        updateEvents(calendar, e -> ((CinemaEvent) e).setVisible(visible));
        fireEventSetChange();
    }

    private void updateEvents(WebCalendar calendar, Consumer<CalendarEvent> calendarEventConsumer) {
        eventList.stream()
                .filter(e -> ((CinemaEvent) e).getParent().equals(calendar))
                .forEach(calendarEventConsumer);
    }

    public void update() {
        fireEventSetChange();
    }
}