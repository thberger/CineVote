package de.thberger.cinevote.ui;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import de.thberger.cinevote.calendar.CinemaEvent;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author thb
 */
class MovieDetailsPanel extends Panel {

    private static final String GERMAN_DATE_TIME_FORMAT = "EEE, d. MMMMM '-' HH:mm 'Uhr'";
    private final SimpleDateFormat sf = new SimpleDateFormat(GERMAN_DATE_TIME_FORMAT);

    private TextField movieTitle;
    private TextField movieLocation;
    private TextArea movieDescription;
    private TextField movieStart;
    private TextField movieEnd;
    private Link movieUrl;

    MovieDetailsPanel() {
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
