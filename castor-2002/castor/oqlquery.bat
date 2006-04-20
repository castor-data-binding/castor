@echo off
REM $Id$
set JAVA=%JAVA_HOME%\bin\java
set OLDCP=%CLASSPATH%
set CLASSPATH=build\classes;build\tests;build\examples;%CLASSPATH%
set cp=%CLASSPATH%
for %%i in (lib\*.jar) do call cp.bat %%i

if not "%2"=="" goto run
echo Usage: oqlquery.bat DatabaseName DatabaseConfig
echo Example: oqlquery.bat test file:build/examples/jdo/database.xml
exit 1

run:
%JAVA% -classpath %CP% org.exolab.castor.tools.QueryAnalyser %1 %2
set CLASSPATH=%OLDCP%