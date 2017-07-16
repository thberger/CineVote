package de.thberger.cinevote.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

import static de.thberger.cinevote.helpers.DateHelpers.getDayInWeeks;
import static de.thberger.cinevote.helpers.DateHelpers.getFirstDayOfWeek;

/**
 * @author thb
 */
@Getter
@AllArgsConstructor
public enum View {
    Next_Weeks("Nächste Wochen", getFirstDayOfWeek(), getDayInWeeks(3));

    private String title;
    private Date startDate;
    private Date endDate;


}
