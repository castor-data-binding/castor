@echo off
REM $Id$
set LIB_DIR=..\lib
set ANT_LIB_DIR=lib
REM Include Ant infrastructure on CLASSPATH
set CP="%ANT_LIB_DIR%\ant-1.6.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-1.6-launcher.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-1.6-trax.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-junit-1.6.jar"
set CP=%CP%;"%ANT_LIB_DIR%\ant-nodeps-1.6.jar"
REM Include JUnit JAR
set CP=%CP%;"%LIB_DIR%\junit_3.8.2.jar"
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

