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

CLASSPATH=$CLASSPATH:$BUILD_D/classes:$BUILD_D/tests:$JAVA_HOME/lib/tools.jar
CLASSPATH=`echo $LIB_D/*.jar | tr ' ' ':'`:$CLASSPATH

# Remove the output of the previous test
rm -rf $BUILD_D/tests/output/

# No argument at all runs the master test suite
case "$1" in
    help)
        echo "Usage:"
        echo "   Run the master test suite with default arguments:"
        echo "       $0"
        echo "   Run a specific test or subtree of tests:"
        echo "       $0 [ normal CTF arguments ] ../src/tests/xml/path/to/test/tree"
        echo "   Run a test suite with optional extra arguments:"
        echo "       $0 master|regression|bugs|all  [ normal CTF arguments ]"
        echo ""
        echo "The normal CTF arguments are:"
        echo "   -verbose      - Display verbose status-of-test information"
        echo "   -printStack   - Print the stack dump of all exceptions that occur"
        echo "   -text         - Do not use the JUnit GUI"
        echo "   -seed <seed>  - Use the specified initial random seed"
        echo "   -help         - Print usage from the CTF suite program"
        echo

        exit 0
        ;;
    '')
        shift
        WHICHTESTS=../src/tests/xml/MasterTestSuite
        ;;
    master)
        shift
        WHICHTESTS=../src/tests/xml/MasterTestSuite
        ;;
    regression)
        shift
        WHICHTESTS=../src/tests/xml/RegressionTestSuite
        ;;
    bugs)
        shift
        WHICHTESTS=../src/tests/xml/KnownBugs
        ;;
    all)
        shift
        WHICHTESTS=../src/tests/xml/
        ;;
    *)
        WHICHTESTS=
        ;;
esac

# run the test
$JAVA -cp $CLASSPATH org.exolab.castor.tests.framework.CastorTestSuiteRunner $WHICHTESTS $*
