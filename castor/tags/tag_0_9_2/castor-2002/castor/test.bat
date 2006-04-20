@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set OLDCP=%CLASSPATH%
set CLASSPATH=build\classes;build\tests;%CLASSPATH%
set cp=%CLASSPATH%
for %%i in (lib\*.jar) do call cp.bat %%i

%JAVA% -classpath %CP% Main -execute %1 %2 %3 %4 %5 %6
set CLASSPATH=%OLDCP%