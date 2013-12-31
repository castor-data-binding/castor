@echo off
REM $Id: build.bat 8505 2010-01-07 03:34:08Z rjoachim $
set LIB_DIR=..\lib
set ANT_LIB_DIR=lib
set JAVA_HOME=C:\dev\Java\jdk1.5.0_22
REM Include Ant infrastructure on CLASSPATH
set CP="%ANT_LIB_DIR%\ant-1.7.1.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-launcher-1.7.1.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-trax-1.7.1.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-junit-1.7.1.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-nodeps-1.7.1.jar"
set CP=%CP%;"%ANT_LIB_DIR%\maven-ant-tasks-2.1.0.jar"
REM Include JUnit JAR
set CP=%CP%;"%LIB_DIR%\junit-4.5.jar"
REM Include compiled Castor artefacts
REM set "CP=%CP%;..\build\classes"
REM Include SUN's tools.jar (incl. the compiler), required by Ant for compilation
set CP=%CP%;"%JAVA_HOME%\lib\tools.jar"
REM Include various dependencies
REM Disable for for JDK 1.4 and above
set CP=%CP%;"%LIB_DIR%\xerces-J_1.4.0.jar"
REM Not sure why those would ever be needed
REM set CP=%CP%;"%LIB_DIR%\commons-logging-1.1.jar";"%LIB_DIR%\log4j-1.2.8.jar"
"%JAVA_HOME%\bin\java" -classpath %CP% -Dant.home=lib org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 -buildfile ..\src\build.xml

