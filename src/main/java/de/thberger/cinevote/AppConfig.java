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

    @Getter @Setter
    public static class WebCalendarConfig {
        boolean enabled;
        String name;
        String url;
        String style;
        boolean authenticated;
        String username;
        String password;
        String collection;
    }
}
