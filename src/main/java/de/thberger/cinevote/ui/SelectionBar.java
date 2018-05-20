package de.thberger.cinevote.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import de.thberger.cinevote.AppConfig;
import de.thberger.cinevote.calendar.WebCalendar;
import de.thberger.cinevote.calendar.WebCalendarEventProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;

/**
 * @author thb
 */
@SpringComponent
@SessionScope
@AllArgsConstructor
@Slf4j
public class SelectionBar extends HorizontalLayout {

    private WebCalendarEventProvider eventProvider;

    private CineCalendar calendar;

    @PostConstruct
    private void init() {
        setSpacing(true);
        setStyleName("selectionBar");
        addComponent(new Label("Wochenansicht:"));
        addViewButtons();
        addComponent(new Label("Filtere Kinos:"));
        addBoxes();
    }

    private void addViewButtons() {
        Button thisWeek = createViewButton(View.This_Week);
        Button nextWeeks = createViewButton(View.Next_Weeks);
        addComponents(thisWeek, nextWeeks);
    }

    private Button createViewButton(final View view) {
        Button button = new Button(view.getTitle());
        button.addClickListener((Button.ClickListener) event -> calendar.switchView(view));
        return button;
    }

    private void addBoxes() {
        for (WebCalendar calendar : eventProvider.getCalendars()) {
            AppConfig.WebCalendarConfig calendarConfig = calendar.getConfig();
            CheckBox checkBox = new CheckBox(calendarConfig.getShortName());
            checkBox.setStyleName(calendar.getConfig().getStyle());
            checkBox.setValue(calendarConfig.isVisible());
            checkBox.addValueChangeListener(event -> {
                boolean visible = event.getValue();
                calendar.getConfig().setVisible(visible);
                eventProvider.setVisibility(calendar, visible);
            });
            addComponent(checkBox);
        }
    }
}
