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

sudo -H -u cinevote bash -c 'nohup java -jar -Duser.country=DE -Duser.language=de -Dfile.encoding=UTF-8 cinevote-*.jar &> cinecal.log&'