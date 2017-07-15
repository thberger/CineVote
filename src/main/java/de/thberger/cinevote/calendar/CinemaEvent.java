package de.thberger.cinevote.calendar;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZoneId;

/**
 * Extends Vaadin's {@link BasicEvent} by two properties that are relevant
 * for the cinema shows.
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class CinemaEvent extends BasicEvent {

    private String location;
    private String url;
    private ZoneId timeZone;
}
