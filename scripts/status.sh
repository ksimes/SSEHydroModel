#!/bin/sh

PROCESS_ID=`ps -ef | grep "java -jar hydromodel.jar"  | grep -v "grep" | awk '{gsub(/^[ \t\r\n]+|[ \t\r\n]+$/, "", $2); printf $2}'`

if [ ! -z $PROCESS_ID ]
then
	echo "HydroModel is running with PID" $PROCESS_ID
	exit 0
else
	echo "HydroModel is NOT running!"
	exit 1
fi

