#! /bin/sh

# $Id: sourceGen 3636 2003-03-03 07:05:44Z kvisco $

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

CLASSPATH=`echo *.jar | tr ' ' ':'`:$CLASSPATH:.

$JAVA -cp $CLASSPATH org.exolab.castor.builder.SourceGeneratorMain $@
