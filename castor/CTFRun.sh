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

CLASSPATH=$CLASSPATH:./build/classes:./build/tests
CLASSPATH=`echo lib/*.jar | tr ' ' ':'`:$CLASSPATH

# Remove the output of the previous test
rm -rf ./build/tests/output/

# run the test
$JAVA -cp $CLASSPATH org.exolab.castor.tests.framework.CastorTestSuiteRunner $*



