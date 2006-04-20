@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set OLDCP=%CLASSPATH%
set CLASSPATH=.\build\classes;.\build\tests;%JAVA_HOME%\lib\tools.jar
set cp=%CLASSPATH%
for %%i in (lib\*.jar) do call cp.bat %%i
rmdir /q /s .\build\tests\output>nul
echo %CP%

if "%1"=="" goto noargs
%JAVA% -classpath %CP% org.exolab.castor.tests.framework.CastorTestSuiteRunner %1 %2 %3  %4 %5 %6 %7

goto end
:noargs
echo using default arguments [-text -verbose src/tests/MasterTestSuite -printStack]
%JAVA% -classpath %CP% org.exolab.castor.tests.framework.CastorTestSuiteRunner -text -verbose src/tests/MasterTestSuite -printStack

:end
set CLASSPATH=%OLDCP%
set OLDCP=

