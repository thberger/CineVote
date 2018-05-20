package de.thberger.cinevote.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

import static de.thberger.cinevote.helpers.DateHelpers.*;

/**
 * @author thb
 */
@Getter
@AllArgsConstructor
public enum View {
    Next_Weeks("Nächste Wochen", getFirstDayOfWeek(), getDayInWeeks(3)),
    This_Week("Diese Woche", getFirstDayOfWeek(), getLastDayOfWeek());

    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;


}
