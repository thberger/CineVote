package de.thberger.cinevote.calendar;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZoneId;
import java.util.Objects;

/**
 * Extends Vaadin's {@link BasicEvent} by two properties that are relevant
 * for the cinema shows.
 */
@ToString
@Getter
@Setter
public class CinemaEvent extends BasicEvent {

    private String location;
    private String url;
    private ZoneId timeZone;
    private boolean visible;
    private WebCalendar parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CinemaEvent that = (CinemaEvent) o;
        return visible == that.visible &&
                Objects.equals(location, that.location) &&
                Objects.equals(url, that.url) &&
                Objects.equals(timeZone, that.timeZone) &&
                Objects.equals(parent, that.parent) &&
                Objects.equals(getStart(), that.getStart()) &&
                Objects.equals(getEnd(), that.getEnd()) &&
                Objects.equals(getCaption(), that.getCaption()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getStyleName(), that.getStyleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, url, timeZone, visible, parent);
    }
}
