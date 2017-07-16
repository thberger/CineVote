package de.thberger.cinevote.ui;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.thberger.cinevote.AppConfig;
import de.thberger.cinevote.calendar.CinemaEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author thb
 */
@SpringComponent
class MovieDetailsPanel extends Panel {

    private AppConfig appConfig;

    private SimpleDateFormat sf;

    private TextField movieTitle;
    private TextField movieLocation;
    private TextArea movieDescription;
    private TextField movieStart;
    private TextField movieEnd;
    private Link movieUrl;

    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @PostConstruct
    public void init() {
        setCaption("Filminfos");
        setStyleName("detailPanel");
        setContent(createFieldsForm());
        setSizeFull();
        sf = new SimpleDateFormat(appConfig.getDateFormat());
    }

    private VerticalLayout createFieldsForm() {
        VerticalLayout v = new VerticalLayout();
        v.setSpacing(true);
        v.setSizeFull();
        v.setMargin(true);
        v.setWidth("100%");
        movieTitle = new TextField("Titel");
        movieTitle.setReadOnly(true);
        movieLocation = new TextField("Kino");
        movieLocation.setReadOnly(true);
        movieStart = new TextField("Beginn");
        movieStart.setReadOnly(true);
        movieEnd = new TextField("Ende");
        movieEnd.setReadOnly(true);
        movieDescription = new TextArea("Beschreibung");
        movieDescription.setRows(10);
        movieDescription.setReadOnly(true);
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
        String description = Strings.nullToEmpty(cinemaEvent.getDescription());
        if (description.startsWith(caption)) {
            return description.replaceFirst(caption, "");
        } else {
            return description;
        }
    }

    private String formatted(Date date) {
        return sf.format(date);
    }

    private void setReadOnlyField(AbstractTextField field, String newValue) {
        field.setReadOnly(false);
        field.setValue(Strings.nullToEmpty(newValue));
        field.setReadOnly(true);
    }
}
