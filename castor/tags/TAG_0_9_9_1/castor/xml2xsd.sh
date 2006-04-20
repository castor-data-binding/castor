#!/bin/sh

# $Id$

if [ -z "$JAVA_HOME" ] ; then
  JAVA=`which java`
  if [ -z "$JAVA" ] ; then
    echo "Cannot find JAVA. Please set your PATH."
    exit 1
  fi
  JAVA_BIN=`dirname $JAVA`
  JAVA_HOME=$JAVA_BIN/..
fi

JAVA=$JAVA_HOME/bin/java

CP=$JAVA_HOME/lib/tools.jar:src/etc:build/classes/:$CLASSPATH

for i in `ls ./lib/*.jar`
   do 
       CP=$CP:$i
   done

$JAVA -classpath $CP org.exolab.castor.xml.schema.util.XMLInstance2Schema "$@"
