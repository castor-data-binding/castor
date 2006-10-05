#!/bin/sh 

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

CLASSPATH=$CLASSPATH:../../build/classes:../../build/tests
CLASSPATH=`echo ../../lib/*.jar | tr ' ' ':'`:$CLASSPATH

rm -rf `find ./main/org/exolab/castor/tests/framework/testDescriptor/ -name "*.java"`

$JAVA -classpath $CLASSPATH org.exolab.castor.builder.SourceGenerator -i ./main/org/exolab/castor/tests/framework/TestDescriptor.xsd -dest ./main  -package org.exolab.castor.tests.framework.testDescriptor





