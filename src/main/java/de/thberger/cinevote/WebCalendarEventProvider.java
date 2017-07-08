package de.thberger.cinevote;

import com.vaadin.ui.components.calendar.event.BasicEventProvider;

import java.util.LinkedList;
import java.util.List;

public class WebCalendarEventProvider extends BasicEventProvider {

    private List<WebCalendar> webCalendars;

    public WebCalendarEventProvider() {
        this.webCalendars = new LinkedList<>();
    }

    public void addWebCalendar(WebCalendar webCalendar, String styleName) {
        webCalendars.add(webCalendar);
        webCalendar.getEvents().forEach(e ->  addEvent(e, styleName));
    }

    public void clearCalendars() {
        webCalendars.clear();
    }

    public void addEvent(CinemaEvent event, String styleName) {
        event.setStyleName(styleName);
        super.addEvent(event);
    }
}