#!/bin/sh

PROCESS_ID=`ps -ef | grep "java -jar hydromodel.jar"  | grep -v "grep" | awk '{gsub(/^[ \t\r\n]+|[ \t\r\n]+$/, "", $2); printf $2}'`

if [ ! -z $PROCESS_ID ]
then
	echo "HydroModel is running with PID" $PROCESS_ID
	kill $PROCESS_ID
	echo "HydroModel stopped"
else
	echo "HydroModel is NOT running!"
fi
