package de.thberger.cinevote.ui;

import com.vaadin.spring.annotation.SpringComponent;
import de.thberger.cinevote.AppConfig;
import de.thberger.cinevote.calendar.CinemaEvent;
import de.thberger.cinevote.calendar.WebCalendarEventProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.context.annotation.SessionScope;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import javax.annotation.PostConstruct;

/**
 * @author thb
 */
@SpringComponent
@SessionScope
@AllArgsConstructor
public class CineCalendar extends Calendar<CinemaEvent> {

    private WebCalendarEventProvider calendarEventProvider;

    private MovieDetailsPanel detailsPanel;

    private AppConfig appConfig;

    @PostConstruct
    public void init() {
        setDataProvider(calendarEventProvider);
        setCaptionAsHtml(true);
        setWidth("100%");
        setHeight("100%");
        withVisibleHours(appConfig.getDayView().getFirstVisibleHourOfDay(),
                appConfig.getDayView().getLastVisibleHourOfDay());
        setHandler(eventClickHandler());

    }

    void switchView(View view) {
        setStartDate(view.getStartDate());
        setEndDate(view.getEndDate());
    }

    private CalendarComponentEvents.ItemClickHandler eventClickHandler() {
        return (CalendarComponentEvents.ItemClickHandler) event -> {
            CinemaEvent calendarEvent = (CinemaEvent) event.getCalendarItem();
            UserSession.setSelectedEvent(calendarEvent);
            detailsPanel.update(calendarEvent);
            calendarEventProvider.update();
        };
    }
}
