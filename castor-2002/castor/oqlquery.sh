#! /bin/sh

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

CLASSPATH=$CLASSPATH:./build/classes:./build/tests:./build/examples
CLASSPATH=`echo lib/*.jar | tr ' ' ':'`:$CLASSPATH

if [ -z "$2" ] ; then
  echo "Usage: ./oqlquery.sh <DatabaseName> <DatabaseConfig>"
  echo "Example: ./oqlquery.sh test file:build/examples/jdo/database.xml"
  exit 1
fi

$JAVA -cp $CLASSPATH org.exolab.castor.gui.QueryAnalyser $1 $2