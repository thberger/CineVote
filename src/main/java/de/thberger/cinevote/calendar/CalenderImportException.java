package de.thberger.cinevote.calendar;


class CalenderImportException extends RuntimeException {
    CalenderImportException(String message, Exception cause) {
        super(message, cause);
    }
}
