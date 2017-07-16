package de.thberger.cinevote;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties( prefix = "cinevote")
@Getter @Setter
public class AppConfig {

    private List<WebCalendarConfig> webCalendars;
    private String dateFormat;
    private DayViewConfig dayView;

    @Getter @Setter
    public static class WebCalendarConfig {
        boolean enabled = true;
        boolean visible = true;
        String name;
        String shortName;
        String url;
        String style;
        boolean authenticated;
        String username;
        String password;
        String collection;
    }

    @Getter
    @Setter
    public static class DayViewConfig {
        int firstVisibleHourOfDay;
        int lastVisibleHourOfDay;
    }
}
