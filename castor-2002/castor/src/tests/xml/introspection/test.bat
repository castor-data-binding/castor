@echo off

REM Set the classpath  
set JAVA=%JAVA_HOME%\bin\java
set CLASSPATH=..\..\..\..\build\classes;..\..\..\..\build\tests;%CLASSPATH%
set cp=.;%CLASSPATH%
for %%i in (..\..\..\..\lib\*.jar) do call cp.bat %%i


rem java -cp "%CP%" junit.textui.TestRunner xml.InheritanceTest
rem java -cp "%CP%" junit.textui.TestRunner xml.TypeHandlingTest
rem java -cp "%CP%" junit.ui.TestRunner xml.xml2java.SGtypes1Test

rem java -cp "%CP%" junit.textui.TestRunner xml.xml2java.TimeInstantTest


javac -classpath "%CP%" *.java
rem java  -cp "%CP%" main
java -cp "%CP%" junit.ui.TestRunner IntroTest
set CLASSPATH=

pause