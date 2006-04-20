@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set OLDCP=%CLASSPATH%
set CLASSPATH=..\build\tests;..\build\classes;%CLASSPATH%
set cp=%CLASSPATH%
for %%i in (..\lib\*.jar) do call cp.bat %%i
for %%i in (..\lib\tests\*.jar) do call cp.bat %%i

"%JAVA%" -classpath "%CP%" Main %1 %2 %3 %4 %5 %6
set CLASSPATH=%OLDCP%