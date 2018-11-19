#!/bin/sh

PROCESS_ID=`ps -ef | grep "java -jar hydromodel.jar"  | grep -v "grep" | awk '{print $2}'`

echo $PROCESS_ID
# nohup sudo java -jar catwatch.jar &
