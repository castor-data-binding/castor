@echo off
REM $Id$
set JAVA="%JAVA_HOME%\bin\java"
set "CP=..\lib\ant-1.6.jar;..\lib\ant-1.6-launcher.jar;..\build\classes;%JAVA_HOME%\lib\tools.jar;..\lib\xerces-J_1.4.0.jar;..\lib\commons-logging.jar;..\lib\log4j-1.2.8.jar"
%JAVA% -classpath %CP% -Dant.home=lib org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 -buildfile ..\src\build.xml

