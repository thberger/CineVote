package de.thberger.cinevote.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

import static de.thberger.cinevote.helpers.DateHelpers.getDayInFourWeeks;
import static de.thberger.cinevote.helpers.DateHelpers.getFirstDayOfWeek;

/**
 * @author thb
 */
@Getter
@AllArgsConstructor
public enum View {
    Month("Monatsansicht", getFirstDayOfWeek(), getDayInFourWeeks());

    private String title;
    private Date startDate;
    private Date endDate;


}
