
@echo off
REM Change the following line to set your JDK path
set JDK_BIN=c:\jdk1.3\bin

@echo Create the classpath
set CP=;
for %%i in (..\..\..\lib\*.jar) do call cp.bat %%i
set CP=%CP%;..\..\..\build\classes;%JDK_BIN%\lib\tools.jar


@echo Castor Test Cases
@echo.
@echo Generating classes...

@rem Java 2 style collection types
@rem java org.exolab.castor.builder.SourceGenerator -i invoice.xsd -f -types j2 -package test
@rem Java 1.1 collection types
java -cp %CP% org.exolab.castor.builder.SourceGenerator -i invoice.xsd -f -package test


@echo.
@echo About to compile generated source code...
@pause

%JDK_BIN%\javac -classpath %CP% test\*.java

@echo.
@echo Compiling test class...
@pause

%JDK_BIN%\javac -classpath %CP%;. *.java

@echo.
@echo Ready to run test...
@pause
%JDK_BIN%\java -cp %CP% InvoiceTest
