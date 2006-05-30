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
BUILD_D=$CASTOR_HOME/build
LIB_D=$CASTOR_HOME/lib

CLASSPATH=$BUILD_D/classes:$BUILD_D/examples:$CLASSPATH
CLASSPATH=`echo $LIB_D/*.jar | tr ' ' ':'`:$CLASSPATH

if [ -z $1 ] ; then
  echo "Usage: example <pkg>";
  exit;
fi
$JAVA -cp $CLASSPATH $1.Test $2 $3 $4 $5 $6


