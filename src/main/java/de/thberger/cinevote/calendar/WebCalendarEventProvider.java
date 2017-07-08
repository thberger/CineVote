package de.thberger.cinevote.calendar;

import com.vaadin.ui.components.calendar.event.BasicEventProvider;

import java.util.LinkedList;
import java.util.List;

public class WebCalendarEventProvider extends BasicEventProvider {

    private List<WebCalendar> webCalendars;

    public WebCalendarEventProvider() {
        this.webCalendars = new LinkedList<>();
    }

    void addWebCalendar(WebCalendar webCalendar, String styleName) {
        webCalendars.add(webCalendar);
        webCalendar.getEvents().forEach(e ->  addEvent(e, styleName));
    }

    private void addEvent(CinemaEvent event, String styleName) {
        event.setStyleName(styleName);
        super.addEvent(event);
    }

    public void clearCalendars() {
        webCalendars.clear();
    }
}