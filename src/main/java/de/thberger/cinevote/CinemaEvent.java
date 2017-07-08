package de.thberger.cinevote;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import lombok.Data;

/**
 * @author thb
 */
@Data
public class CinemaEvent extends BasicEvent {

    String location;
    String url;
}
