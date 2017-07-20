package de.thberger.cinevote.calendar;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CinemaEventTest {
    @Test
    public void equals() throws Exception {
        WebCalendar webCalendar = mock(WebCalendar.class);
        Date start = getDate("2017-12-03T10:15:30.00Z");
        Date end = getDate("2017-12-03T15:15:30.00Z");
        CinemaEvent e1 = createEvent(webCalendar, start, end);
        CinemaEvent e2 = createEvent(webCalendar, start, end);
        assertThat(e1).isEqualTo(e2);

        Date end2 = getDate("2017-12-03T15:15:31.00Z");
        e2.setEnd(end2);
        assertThat(e1).isNotEqualTo(e2);
    }

    private CinemaEvent createEvent(WebCalendar webCalendar, Date startDate, Date endDate) {
        CinemaEvent event = new CinemaEvent();
        event.setCaption("Testevent");
        event.setVisible(true);
        event.setUrl("http://example.com");
        event.setLocation("Berlin");
        event.setDescription("Test event");
        event.setStart(startDate);
        event.setEnd(endDate);
        event.setAllDay(false);
        event.setStyleName("theme1");
        event.setParent(webCalendar);
        event.setTimeZone(ZoneId.systemDefault());
        return event;
    }

    private Date getDate(String sDate) {
        return Date.from(Instant.parse(sDate));
    }

}