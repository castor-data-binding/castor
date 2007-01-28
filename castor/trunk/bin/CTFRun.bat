@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set OLDCP=%CLASSPATH%
set CLASSPATH=..\build\classes;..\build\tests;..\xmlctf\build\classes;..\codegen\build\classes;%JAVA_HOME%\lib\tools.jar
set cp=%CLASSPATH%
for %%i in (..\lib\*.jar) do call cp.bat %%i
rmdir /q /s ..\build\tests\output>nul
echo %CP%

if "%1"=="" goto noargs
if "%1"=="regression" goto regression
if "%1"=="master" goto master

"%JAVA%" -classpath "%CP%" org.exolab.castor.tests.framework.CastorTestSuiteRunner %1 %2 %3 %4 %5 %6 %7

goto end

:noargs
:master
echo using default arguments [-text ../src/tests/xml/MasterTestSuite]
"%JAVA%" -classpath "%CP%" org.exolab.castor.tests.framework.CastorTestSuiteRunner -text ../xmlctf/tests/MasterTestSuite
goto end

:master-verbose
echo using default verbose arguments [-text -verbose ../src/tests/xml/MasterTestSuite]
"%JAVA%" -classpath "%CP%" org.exolab.castor.tests.framework.CastorTestSuiteRunner -text -verbose ../xmlctf/tests/MasterTestSuite
goto end

:regression
echo using default arguments [-text -verbose ../src/tests/xml/RegressionTestSuite]
"%JAVA%" -classpath "%CP%" org.exolab.castor.tests.framework.CastorTestSuiteRunner -text ../xmlctf/tests/RegressionTestSuite
goto end

:regression-verbose
echo using default arguments [-text -verbose ../src/tests/xml/RegressionTestSuite]
"%JAVA%" -classpath "%CP%" org.exolab.castor.tests.framework.CastorTestSuiteRunner -text -verbose ../xmlctf/tests/RegressionTestSuite
goto end

:end
set CLASSPATH=%OLDCP%
set OLDCP=
