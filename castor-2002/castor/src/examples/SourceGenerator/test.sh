#!/bin/sh
# $Id$

#
# Set up the environment
#
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
JAVAC=$JAVA_HOME/bin/javac

#
# Concatenate all the libraries in the lib directory and append
# it to the classpath environment variable
#
CLASSPATH=`echo ../../../lib/*.jar | tr ' ' ':'`
CLASSPATH=`echo ../../../dist/*.jar | tr ' ' ':'`:$CLASSPATH

CLASSPATH=$JAVA_HOME/lib/tools.jar:.:$CLASSPATH

#
# Generate classes
#
echo Castor classes
echo Generating classes
$JAVA -cp $CLASSPATH org.exolab.castor.builder.SourceGenerator -i invoice.xsd -f  -package test

#
# Compile generated code
#
echo 
echo About to compile generated source code
$JAVAC -classpath $CLASSPATH test/*.java

#
# Compiling test class
#
echo Compiling test class
$JAVAC -classpath $CLASSPATH InvoiceTest.java

#
# run the test
#
echo about to run the test
$JAVA -cp $CLASSPATH InvoiceTest