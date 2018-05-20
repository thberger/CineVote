package de.thberger.cinevote.calendar;

import de.thberger.cinevote.AppConfig;
import de.thberger.cinevote.ui.UserSession;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.item.CalendarItem;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Consumer;

@Service
@Slf4j
public class WebCalendarEventProvider extends BasicItemProvider<CinemaEvent> {

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
        super.addItem(event);
    }

    @Override
    public List<CinemaEvent> getItems(ZonedDateTime startDate, ZonedDateTime endDate) {
        ArrayList<CinemaEvent> activeEvents = new ArrayList<>();
        for (CinemaEvent event : itemList) {
            if (event.isVisible()) {
                if (isInRange(event, startDate, endDate)) {
                    activeEvents.add(event);
                    checkForSelected(event);
                }
            }
        }
        return activeEvents;
    }

    private void checkForSelected(CinemaEvent ev) {
        String style = (ev.equals(UserSession.getSelectedEvent())) ? "selected" : "";
        String calStyle = ev.getParent().getConfig().getStyle();
        ev.setStyleName(calStyle + " " + style);
    }

    private boolean isInRange(CalendarItem ev, ZonedDateTime from, ZonedDateTime to) {
        if (ev.getStart() != null && ev.getEnd() != null) {
            ZonedDateTime f = ev.getStart();
            ZonedDateTime t = ev.getEnd();
            return (f.isBefore(to) && f.isAfter(from)) || (t.isAfter(from) && t.isBefore(to))
                    || (f.isBefore(from) && t.isAfter(to));
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
        updateEvents(calendar, e -> e.setVisible(visible));
        fireItemSetChanged();
    }

    private void updateEvents(WebCalendar calendar, Consumer<CinemaEvent> calendarEventConsumer) {
        itemList.stream()
                .filter(e -> e.getParent().equals(calendar))
                .forEach(calendarEventConsumer);
    }

    public void update() {
        fireItemSetChanged();
    }
}