#!/bin/sh 

javac -classpath ~/cvs-work/castor/build/tests/:~/cvs-work/castor/dist/castor-0.9.1.jar:$CLASSPATH *.java
jar cvf ../Get-Method-Specification.jar *
