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

CLASSPATH=../build/classes:../build/tests:../codegen/build/classes
CLASSPATH=`echo ../lib/*.jar | tr ' ' ':'`:$CLASSPATH

rm -rf `find ./src/main/java/org/exolab/castor/tests/framework/testDescriptor/ -name "*.java"`

$JAVA -classpath $CLASSPATH org.exolab.castor.builder.SourceGeneratorMain -i ./src/main/resources/org/exolab/castor/tests/framework/TestDescriptor.xsd -dest ./src/main/java  -package org.exolab.castor.tests.framework.testDescriptor
