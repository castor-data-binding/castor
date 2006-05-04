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

CLASSPATH=$LIB_D/ant-1.6.jar:$LIB_D/ant-1.6-launcher.jar:$LIB_D/ant-1.6-trax.jar:$LIB_D/xerces-J_1.4.0.jar:$BUILD_D/classes/:$CLASSPATH:$JAVA_HOME/lib/tools.jar:$LIB_D/commons-logging.jar

for i in `ls $LIB_D/*.jar`
   do 
       CLASSPATH=$CLASSPATH:$i
   done

$JAVA -classpath $CLASSPATH -Dant.home=lib org.apache.tools.ant.Main "$@" -buildfile $SRC_D/build.xml
