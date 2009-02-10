@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set OLDCP=%CLASSPATH%
set CLASSPATH=..\build\tests;%JAVA_HOME%\lib\tools.jar
set CLASSPATH=%CLASSPATH%;..\xmlctf\build\classes
set CLASSPATH=%CLASSPATH%;..\xmlctf-framework\build\classes
set CLASSPATH=%CLASSPATH%;..\build\classes
set CLASSPATH=%CLASSPATH%;..\codegen\build\classes;
set CLASSPATH=%CLASSPATH%;..\schema\build\classes;
set CLASSPATH=%CLASSPATH%;..\schema\target\classes;
set CLASSPATH=%CLASSPATH%;..\core\build\classes;
set CLASSPATH=%CLASSPATH%;..\diff\build\classes;
set cp=%CLASSPATH%

set CP=%CP%;"..\lib\commons-logging-1.1.jar"
set CP=%CP%;"..\lib\log4j-1.2.13.jar"
set CP=%CP%;"..\lib\junit_3.8.2.jar"
set CP=%CP%;"..\lib\jakarta-oro-2.0.5.jar"
set CP=%CP%;"..\lib\xerces-J_1.4.0.jar"

set CP=%CP%;"..\lib\velocity-1.5.jar"
set CP=%CP%;"..\lib\commons-collections-3.1.jar"
set CP=%CP%;"..\lib\commons-lang-2.1.jar"

echo *** Setting classpath to ...
echo %CP%

echo *** Cleaning test output directory
rmdir /q /s ..\xmlctf\build\tests\output>nul
echo *** Successfully cleaned test output directory

if "%1"=="" goto noargs
if "%1"=="regression" goto regression
if "%1"=="master" goto master

echo *** Using the JVM ...
echo "%JAVA%"

"%JAVA%" -classpath "%CP%" org.castor.xmlctf.CastorTestSuiteRunner %1 %2 %3 %4 %5 %6 %7

goto end

:noargs
:master
echo using default arguments [-text ../xmlctf/tests/MasterTestSuite]
"%JAVA%" -classpath "%CP%" org.castor.xmlctf.CastorTestSuiteRunner -text ../xmlctf/tests/MasterTestSuite
goto end

:master-verbose
echo using default verbose arguments [-text -verbose ../xmlctf/tests/MasterTestSuite]
"%JAVA%" -classpath "%CP%" org.castor.xmlctf.CastorTestSuiteRunner -text -verbose ../xmlctf/tests/MasterTestSuite
goto end

:regression
echo using default arguments [-text -verbose ../xmlctf/tests/RegressionTestSuite]
"%JAVA%" -classpath "%CP%" org.castor.xmlctf.CastorTestSuiteRunner -text ../xmlctf/tests/RegressionTestSuite
goto end

:regression-verbose
echo using default arguments [-text -verbose ../xmlctf/tests/RegressionTestSuite]
"%JAVA%" -classpath "%CP%" org.castor.xmlctf.CastorTestSuiteRunner -text -verbose ../xmlctf/tests/RegressionTestSuite
goto end

:end
set CLASSPATH=%OLDCP%
set OLDCP=
