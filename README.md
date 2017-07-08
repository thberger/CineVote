# CineCal

Calendar application to schedule cinema visits with your friends. 
It imports the calender events from multiple remote iCal streams.



## Build and Run
    mvn spring-boot:run
    
Then use the application by browsing http://localhost:9998
    
## Calendar Configuration

The application is configured via `src/main/resources/application.yml`.
You can configure the remote calendars to display as follows:

    cinevote:
      webCalendars:
        -
          url: webcal://piffl-medien.de/flk/kalender/kinokalender_friedrichshain.ics
          name: Freiluftkino Friedrichshain
          style: theme1
              
The `style` property refers to a CSS class to change the colors of
the event entries in the calendar. Have a look into `src/main/webapp/VAADIN/themes/vaadincalendar/vaadincalendar.scss`.
    
## FAQ

##### Why is this still Vaadin 7?

The Vaadin Calender component is not available in Vaadin 8 yet.