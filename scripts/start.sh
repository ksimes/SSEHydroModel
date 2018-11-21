#!/bin/sh

PROCESS_ID=`ps -ef | grep "java -jar hydromodel.jar"  | grep -v "grep" | awk '{gsub(/^[ \t\r\n]+|[ \t\r\n]+$/, "", $2); printf $2}'`

if [ ! -z $PROCESS_ID ]
then
	echo "HydroModel is already running!"
else
	nohup java -jar hydromodel.jar &
fi


