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

CLASSPATH=$CLASSPATH:$BUILD_D/classes:$BUILD_D/tests
CLASSPATH=`echo $LIB_D/*.jar | tr ' ' ':'`:$CLASSPATH

# Remove the output of the previous test
rm -rf $BUILD_D/tests/output/

# run the test
$JAVA -cp $CLASSPATH org.exolab.castor.tests.framework.CastorTestSuiteRunner $*



