#!/bin/sh
# $Id$

#
# If the user wants to clean up after running the test, do cleanup and exit
#
if [ "$1" = "clean" ] ; then
  rm -rf test *.class .castor.cdr invoice2.xml
  exit 0
fi

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

echo "Using CLASSPATH: " 
echo $CLASSPATH | tr ':' '\n'

#
# Generate classes
#
echo
echo Generating classes
$JAVA -cp $CLASSPATH org.exolab.castor.builder.SourceGeneratorMain -i invoice.xsd -f -binding-file bindingInvoice.xml

#
# Compile generated code
#
echo
echo About to compile generated source code
$JAVAC -classpath $CLASSPATH test/*.java test/*/*.java

#
# Compiling test class
#
echo
echo Compiling test class
$JAVAC -classpath $CLASSPATH InvoiceTest.java

#
# run the test
#
echo
echo about to run the test
$JAVA -cp $CLASSPATH InvoiceTest
