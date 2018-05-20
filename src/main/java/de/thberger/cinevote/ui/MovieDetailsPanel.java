package de.thberger.cinevote.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.thberger.cinevote.AppConfig;
import de.thberger.cinevote.calendar.CinemaEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author thb
 */
@SpringComponent
@SessionScope
class MovieDetailsPanel extends Panel {

    private DateTimeFormatter dateFormat;

    private TextField movieTitle;
    private TextField movieLocation;
    private TextArea movieDescription;
    private TextField movieStart;
    private TextField movieEnd;
    private Link movieUrl;

    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.dateFormat = DateTimeFormatter.ofPattern(appConfig.getDateFormat());
    }

    @PostConstruct
    public void init() {
        setCaption("Filminfos");
        setStyleName("detailPanel");
        setContent(createFieldsForm());
        setSizeFull();
    }

    private VerticalLayout createFieldsForm() {
        VerticalLayout v = new VerticalLayout();
        v.setSpacing(true);
        v.setSizeFull();
        v.setMargin(true);
        v.setWidth("100%");
        movieTitle = new TextField("Titel");
        movieTitle.setReadOnly(true);
        movieTitle.setIcon(VaadinIcons.FILM);

        movieLocation = new TextField("Kino");
        movieLocation.setReadOnly(true);
        movieLocation.setIcon(VaadinIcons.LOCATION_ARROW);

        movieStart = new TextField("Filmbeginn");
        movieStart.setReadOnly(true);
        movieStart.setIcon(VaadinIcons.CALENDAR);

        movieEnd = new TextField("Filmende");
        movieEnd.setReadOnly(true);
        movieEnd.setIcon(VaadinIcons.CALENDAR);

        movieDescription = new TextArea("Beschreibung");
        movieDescription.setRows(10);
        movieDescription.setReadOnly(true);
        movieDescription.setIcon(VaadinIcons.FILE_TEXT_O);

        movieUrl = new Link();
        movieUrl.setStyleName("movieUrl");
        v.addComponents(movieTitle, movieLocation, movieStart, movieEnd, movieDescription, movieUrl);
        return v;
    }

    void update(CinemaEvent cinemaEvent) {
        setReadOnlyField(movieTitle, cinemaEvent.getCaption());
        setReadOnlyField(movieLocation, cinemaEvent.getLocation());
        setReadOnlyField(movieDescription, getDescription(cinemaEvent));
        setReadOnlyField(movieStart, formatted(cinemaEvent.getStart()));
        setReadOnlyField(movieEnd, formatted(cinemaEvent.getEnd()));
        if (StringUtils.isNotBlank(cinemaEvent.getUrl())) {
            movieUrl.setVisible(true);
            movieUrl.setResource(new ExternalResource(cinemaEvent.getUrl()));
            movieUrl.setCaption(cinemaEvent.getUrl());
        } else {
            movieUrl.setVisible(false);
        }
    }

    private String getDescription(CinemaEvent cinemaEvent) {
        String caption = cinemaEvent.getCaption().toUpperCase() + " ";
        String description = Objects.toString(cinemaEvent.getDescription(), "");
        if (description.startsWith(caption)) {
            return description.replaceFirst(caption, "");
        } else {
            return description;
        }
    }

    private String formatted(ZonedDateTime date) {
        return dateFormat.format(date);
    }

    private void setReadOnlyField(AbstractTextField field, String newValue) {
        field.setReadOnly(false);
        field.setValue(Objects.toString(newValue, ""));
        field.setReadOnly(true);
    }
}
