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

DIRNAME=`dirname $0`
CASTOR_HOME=`cd $DIRNAME/..; pwd`
LIB_D=$CASTOR_HOME/lib
BUILD_D=$CASTOR_HOME/build
SRC_D=$CASTOR_HOME/src

CP=$JAVA_HOME/lib/tools.jar:$SRC_D/etc:$BUILD_D/classes/:$CLASSPATH

for i in `ls $LIB_D/*.jar`
   do 
       CP=$CP:$i
   done

$JAVA -classpath $CP org.exolab.castor.xml.schema.util.XMLInstance2Schema "$@"
