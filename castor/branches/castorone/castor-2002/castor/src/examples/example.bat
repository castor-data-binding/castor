@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set cp=%CLASSPATH%
for %%i in (lib\*.jar) do call cp.bat %%i
set cp=%cp%:.

%JAVA% -classpath %CP% %1.Test %2 %3 %4 %5 %6


