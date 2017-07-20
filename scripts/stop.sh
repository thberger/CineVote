#!/usr/bin/env bash

PID_FILE=run.pid
if [ -f ${PID_FILE} ]; then
   PID=`cat ${PID_FILE}`
   echo "Stopping process with PID $PID"
   sudo -E -u cinevote kill ${PID}
else
   echo "Cannot stop. $PID_FILE does not exist."
fi
