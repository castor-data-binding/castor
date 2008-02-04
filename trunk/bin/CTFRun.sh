#!/bin/sh

# $Id$
# 2007-10-01 Joachim, I align the shell script with the Java script
# even that the shell script is a bit more flexible I think it is better
# to synchronize the two scripts...
# 2008-01-31 Joachim: added schema/build/classes to classpath definition
# after schema had been extracted from main sources tree

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

OLD_CP=$CLASSPATH

if [ -z "$CASTOR_HOME" ] ; then
  DIRNAME=`dirname $0`
  CASTOR_HOME=`cd $DIRNAME/..; pwd`
fi

CLASSPATH=$CASTOR_HOME/build/tests
CLASSPATH=$CLASSPATH:$CASTOR_HOME/xmlctf/build/classes
CLASSPATH=$CLASSPATH:$CASTOR_HOME/xmlctf-framework/build/classes
CLASSPATH=$CLASSPATH:$CASTOR_HOME/build/classes
CLASSPATH=$CLASSPATH:$CASTOR_HOME/codegen/build/classes
CLASSPATH=$CLASSPATH:$CASTOR_HOME/schema/build/classes

CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/commons-logging-1.1.jar
CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/log4j-1.2.13.jar
CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/junit_3.8.2.jar
CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/jakarta-oro-2.0.5.jar
CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/xerces-J_1.4.0.jar

# libs required for code generation using velocity
CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/velocity-1.5.jar
CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/commons-collections-3.1.jar
CLASSPATH=$CLASSPATH:$CASTOR_HOME/lib/commons-lang-2.1.jar

CLASSPATH=$CLASSPATH:$JAVA_HOME/lib/tools.jar

echo "*** Setting classpath to ..."
echo $CLASSPATH

echo "*** Cleaning test output directory"
rm -rf $CASTOR_HOME/xmlctf/build/tests/output/
echo "*** Successfully cleaned test output directory"

echo "*** Using the JVM ..."
echo $JAVA

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
        WHICHTESTS=../xmlctf/tests/MasterTestSuite
        ;;
    master)
        shift
        WHICHTESTS=../xmlctf/tests/MasterTestSuite
        ;;
    regression)
        shift
        WHICHTESTS=../xmlctf/tests/RegressionTestSuite
        ;;
    bugs)
        shift
        WHICHTESTS=../xmlctf/tests/KnownBugs
        ;;
    all)
        shift
        WHICHTESTS=../xmlctf/tests/
        ;;
    *)
        WHICHTESTS=
        ;;
esac

# run the test
#$JAVA -Xms512M -Xmx512M -cp $CLASSPATH org.exolab.castor.tests.framework.CastorTestSuiteRunner $WHICHTESTS $*
$JAVA -cp $CLASSPATH org.castor.xmlctf.CastorTestSuiteRunner $WHICHTESTS $*

CLASSPATH=$OLD_CP
