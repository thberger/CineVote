package de.thberger.cinevote.calendar;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import lombok.Data;

/**
 * @author thb
 */
@Data
public class CinemaEvent extends BasicEvent {

    private String location;
    private String url;
}
