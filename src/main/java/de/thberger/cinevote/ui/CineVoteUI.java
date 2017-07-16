package de.thberger.cinevote.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;


@SuppressWarnings("serial")
@Theme("vaadincalendar")
@SpringUI
public class CineVoteUI extends UI {

    private CineCalendar calendar;
    private MovieDetailsPanel detailPanel;
    private SelectionBar selectionBar;

    private Label caption;

    @Autowired
    public CineVoteUI(CineCalendar calendar, MovieDetailsPanel detailPanel, SelectionBar selectionBar) {
        this.calendar = calendar;
        this.detailPanel = detailPanel;
        this.selectionBar = selectionBar;
    }

    @Override
    protected void init(VaadinRequest request) {
        this.setLocale(Locale.GERMANY);
        setupUiComponents();
        setView(View.Next_Weeks);
    }

    private void setupUiComponents() {
        final VerticalLayout vLayout = new VerticalLayout();
        vLayout.setMargin(true);
        setContent(vLayout);

        vLayout.addComponent(createTopBar());
        vLayout.addComponent(createCalendarBar());
    }

    private HorizontalLayout createCalendarBar() {
        HorizontalLayout h = new HorizontalLayout();
        h.setSizeFull();
        h.setSpacing(true);
        VerticalLayout l = new VerticalLayout();
        l.addComponent(calendar);
        l.addComponent(selectionBar);
        h.addComponent(l);
        h.addComponent(detailPanel);
        h.setExpandRatio(l, 3.5f);
        h.setExpandRatio(detailPanel, 1);
        return h;
    }

    private HorizontalLayout createTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setStyleName("topBar");
        topBar.setSpacing(true);
        caption = new Label();
        caption.setStyleName("main-title");
        topBar.addComponent(caption);
        return topBar;
    }

    private void setView(View view) {
        setTitle(view);
        calendar.switchView(view);
    }

    private void setTitle(View view) {
        String title = "Programm Freiluftkinos - " + view.getTitle();
        caption.setValue(title);
        super.getPage().setTitle(title);
    }

}