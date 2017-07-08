package de.thberger.cinevote;

import net.fortuna.ical4j.data.ParserException;

import java.io.IOException;

/**
 * @author thb
 */
public class Kinokalender {

    public WebCalendar kberg() {
        return readCalendar("webcal://piffl-medien.de/flk/kalender/kinokalender_friedrichshain.ics");
    }

    public WebCalendar fHain() {
        return readCalendar("webcal://piffl-medien.de/flk/kalender/kinokalender_kreuzberg.ics");
    }

    public WebCalendar rehberge() {
        return readCalendar("webcal://piffl-medien.de/flk/kalender/kinokalender_rehberge.ics");
    }

    private WebCalendar readCalendar(String webcalUrl) {
        try {
            return new WebCalendar(webcalUrl);
        } catch (IOException | ParserException e) {
            e.printStackTrace();
            return null;
        }
    }
}
