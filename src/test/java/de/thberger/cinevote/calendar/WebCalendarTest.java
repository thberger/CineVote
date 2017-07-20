package de.thberger.cinevote.calendar;

import de.thberger.cinevote.AppConfig;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebCalendarTest {

    @Test
    public void createWithInvalidUrl() throws Exception {
        AppConfig.WebCalendarConfig config = mock(AppConfig.WebCalendarConfig.class);
        when(config.getUrl()).thenReturn("file://invalid");
        assertThatThrownBy(() -> new WebCalendar(config))
                .isInstanceOf(CalenderImportException.class);
    }

    @Test
    public void createEvents() throws Exception {
        AppConfig.WebCalendarConfig config = createTestCalendarConfig("/fhain.ics");
        WebCalendar webCalendar = new WebCalendar(config);
        List<CinemaEvent> events = webCalendar.createEvents();
        assertThat(events).hasSize(117);
    }

    private AppConfig.WebCalendarConfig createTestCalendarConfig(String file) {
        AppConfig.WebCalendarConfig config = new AppConfig.WebCalendarConfig();
        URL resource = getClass().getResource(file);
        config.setUrl(resource.toExternalForm());
        config.setEnabled(true);
        config.setName("Testcal");
        config.setShortName("Fhain");
        config.setVisible(true);
        return config;
    }

}