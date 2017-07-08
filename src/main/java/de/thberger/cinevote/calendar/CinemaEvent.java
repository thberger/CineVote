package de.thberger.cinevote.calendar;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import lombok.Data;

/**
 * Extends Vaadin's {@link BasicEvent} by two properties that are relevant
 * for the cinema shows.
 */
@Data
public class CinemaEvent extends BasicEvent {

    private String location;
    private String url;
}
