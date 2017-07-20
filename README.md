[![Build Status](https://travis-ci.org/thberger/CineVote.svg?branch=master)](https://travis-ci.org/thberger/CineVote)

# CineVote

Calendar application to schedule cinema visits with your friends. 
It imports the calender events from multiple remote iCal streams.

![Screenshot](docs/screenshot.png?raw=true "Screenshot")

## Prerequisites
- Install Java JDK 1.7+
- Install maven https://maven.apache.org/install.html
    

## Build and Run
    mvn spring-boot:run
    
Then use the application by browsing http://localhost:9998

## Deployment
    
Build the fat jar file:

    mvn package

This jar includes all libraries. This allows the app to be started with:

    java -jar cinevote*.jar
    
You can also use the sample script located in the `scripts/` directory.
    
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

### My IDE has compile errors

The application uses Project [Lombok](https://projectlombok.org/) to automatically add Getters, Setters, Equals and others methods
 to the byte code. Your IDE must be enabled to process the Lombok annotations.
 See [here](https://stackoverflow.com/questions/9424364/cant-compile-project-when-im-using-lombok-under-intellij-idea) for IDEA instructions.
 
For Maven you don't have to do anything. The project should compile out of the box.

### Why is this still Vaadin 7

The Vaadin Calender component is not available in Vaadin 8 yet.

### I can't find the voting function

Will be implemented soon. Pull requests are welcome.