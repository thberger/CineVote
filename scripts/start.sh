#!/usr/bin/env bash

####################################################################
# This is an opinionated start script:
#
# - Sets German locale (influences rendering of date strings)
# - Start the application as user 'cinevote'
# - Redirect all output to cinecal.log
#
# Usage: Place the script next to the jar file and run.
#
#####################################################################

START_OPTIONS="-Duser.country=DE -Duser.language=de -Dfile.encoding=UTF-8"
JAR_FILE=cinevote-*.jar
RUN_USER=cinevote
APP_LOGFILE=cinecal.log

sudo chmod 500 ${JAR_FILE}
sudo chattr +i ${JAR_FILE}
sudo -H -E -u ${RUN_USER} bash -c 'nohup java ${START_OPTIONS} -jar ${JAR_FILE} &> ${APP_LOGFILE}&'